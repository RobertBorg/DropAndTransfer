package com.rauban.dropandtransfer.model.io.policy.file_exists;

import com.rauban.dropandtransfer.model.io.policy.FileExistsPolicy;

public class OnFileExistsSkip extends FileExistsPolicy {

	@Override
	public String generateFileName(String baseFileName) {
		return null;
	}
	
}
