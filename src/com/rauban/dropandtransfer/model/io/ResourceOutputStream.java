package com.rauban.dropandtransfer.model.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import com.rauban.dropandtransfer.model.io.policy.FileExistsPolicy;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.FileDropHeader;

public class ResourceOutputStream extends OutputStream {
	private FileExistsPolicy fep;
	private String baseDirectory;
	
	//States
	private static final int START_OF_RESOURCE = 0;
	private static final int HEADER_BODY = 1;
	private static final int RESOURCE = 2;
	private static final int SKIP = 3;
	private static final int END_OF_STREAM = -1;
	
	private int state;
	private File resource;
	private int headerRemaining;
	private ByteArrayOutputStream headerOutputStream;
	private long resourceRemaining;
	private OutputStream resourceOutputStream;
	
	private long toSkip;
	
	public <T extends FileExistsPolicy> ResourceOutputStream(Class<T> fep, String baseDirectory){
		try {
			this.fep =fep.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.baseDirectory = baseDirectory;
	}
	@Override
	public void write(int b) throws IOException {
		switch(state){
		case START_OF_RESOURCE:
			headerRemaining = b;
			headerOutputStream = new ByteArrayOutputStream(headerRemaining);
			incState();
			break;
		case HEADER_BODY:
			headerOutputStream.write(b);
			if(--headerRemaining == 0){
				FileDropHeader fdh = FileDropHeader.parseFrom(headerOutputStream.toByteArray());
				String fileName = fdh.getResourceName();
				while((resource = new File(baseDirectory + fileName)).exists()){
					if((fileName = fep.generateFileName(fdh.getResourceName())) == null ){
						break;
					}
				}
				resourceRemaining = fdh.getSize();
				if(resource.exists()){
					state = SKIP;
					toSkip = resourceRemaining;
					break;
				}else {
					resourceOutputStream = new FileOutputStream(resource);
					incState();
				}
			}
			break;
		case RESOURCE:
			resourceOutputStream.write(b);
			if(--resourceRemaining == 0){
				resourceOutputStream.flush();
				resourceOutputStream.close();
				incState();
			}
			break;
		case SKIP:
			if(--toSkip == 0){
				state = START_OF_RESOURCE;
			}
			break;
		default:
			throw new IOException("undefined State!");
		}
		
	}
	private void incState() {
		state = (state +1) %3;
	}

}
