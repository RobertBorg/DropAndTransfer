package com.rauban.dropandtransfer.model.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.rauban.dropandtransfer.model.protocol.FileTransfer.FileDropHeader;
import com.rauban.dropandtransfer.view.listener.ResourceInputStreamListener;
import com.rauban.speaker_listener_pattern.speaker.AudienceHolder;
import com.rauban.speaker_listener_pattern.speaker.Speaker;

public class ResourceInputStream extends InputStream implements Speaker<ResourceInputStreamListener>, ResourceInputStreamListener {
	private Iterator<String> resourceIterator;
	
	//states
	private static final int START_OF_RESOURCE = 0;
	private static final int HEADER_BODY = 1;
	private static final int RESOURCE = 2;
	private static final int END_OF_STREAM = -1;
	
	//current state
	private int state;
	private File currentResourceFile;
	private InputStream headerInputStream;
	private InputStream resourceInputStream;

	private boolean stopAfterCurrentFile;

	private AudienceHolder audience;
	public ResourceInputStream(String... resourcePaths) {
		resourceIterator = Arrays.asList(resourcePaths).iterator();
	}

	@Override
	public int read() throws IOException {
		switch(state){
		case START_OF_RESOURCE:
			if(stopAfterCurrentFile){
				state = END_OF_STREAM;
				return -1;
			}
			processStartOfResource();
			return headerInputStream.available();
		case HEADER_BODY:
			return doCheckStateForward(headerInputStream.read());
		case RESOURCE:
			return doCheckStateForward(resourceInputStream.read());
		}
		return -1;
		
	}

	private void processStartOfResource() throws FileNotFoundException,
			IOException {
		if(!resourceIterator.hasNext()){
			state = END_OF_STREAM;
		}
		currentResourceFile = new File(resourceIterator.next());
		FileDropHeader fdh = FileDropHeader.newBuilder().setResourceName(currentResourceFile.getName()).setIsDir(currentResourceFile.isDirectory()).setSize(currentResourceFile.length()).build();
		if(!currentResourceFile.isDirectory()){
			headerInputStream = new ByteArrayInputStream(fdh.toByteArray()); //XXX creates need for garbage collection, reuse same buffer?
			resourceInputStream = new FileInputStream(currentResourceFile); //XXX no support for directories
		} else {
			throw new IOException("Directories are currently not supported"); //XXX replace with appropriate signal
		}
		onNewOutboundResource(fdh.getResourceName(), fdh.getSize(), fdh.getIsDir());
		incState();
	}

	private int doCheckStateForward(int rtn) throws IOException {
		if(rtn == -1){
			incState();
			rtn = read();
		}
		return rtn;
	}

	private void incState() {
		state = (state + 1) % 4;
		switch (state) {
		case START_OF_RESOURCE:
			onResourceTransferEnded(); //XXX close resource handle!?
			break;
		}
	}

	public void stopAfterCurrentFile(boolean b) {
		stopAfterCurrentFile = b;
	}

	public void stopNow() {
		state = -2; //XXX not thread safe!
		
	}

	@Override
	public void addListener(ResourceInputStreamListener arg0) {
		audience.addToAudience(arg0, ResourceInputStreamListener.class);
		
	}

	@Override
	public void onNewOutboundResource(String resource, long size,
			boolean isDirectory) {
		List<ResourceInputStreamListener> risll = audience.getAudience(ResourceInputStreamListener.class);
		for(ResourceInputStreamListener risl:risll ){
			risl.onNewOutboundResource(resource, size, isDirectory);
		}
	}

	@Override
	public void onResourceTransferEnded() {
		List<ResourceInputStreamListener> risll = audience.getAudience(ResourceInputStreamListener.class);
		for(ResourceInputStreamListener risl:risll ){
			risl.onResourceTransferEnded();
		}		
	}

}
