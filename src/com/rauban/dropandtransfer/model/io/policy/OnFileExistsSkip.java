package com.rauban.dropandtransfer.model.io.policy;

public class OnFileExistsSkip extends FileExistsPolicy {

	@Override
	public String generateFileName(String baseFileName) {
		return null;
	}
	
}
