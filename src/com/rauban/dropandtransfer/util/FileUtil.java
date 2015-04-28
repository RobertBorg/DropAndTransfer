package com.rauban.dropandtransfer.util;

import java.io.File;
import java.util.List;

import com.rauban.dropandtransfer.model.protocol.FileTransfer.ResourceHeader;
import com.rauban.dropandtransfer.model.protocol.FileTransfer.TransferOffer;

public class FileUtil {
	public static void iterateResources(TransferOffer.Builder b, File[] currentFiles, String absoluteBase) {
		for(File current: currentFiles) {
			ResourceHeader.Builder bf = b.addResourcesBuilder();
			bf.setResourceName(current.getAbsolutePath().substring(absoluteBase.length()+1));
			if(current.isDirectory()) {
				bf.setIsDir(true);
				bf.setSize(current.length());
				iterateResources(b, current.listFiles(), absoluteBase);
			} else {
				bf.setIsDir(false);
				bf.setSize(current.length());
			}
		}

	}
}
