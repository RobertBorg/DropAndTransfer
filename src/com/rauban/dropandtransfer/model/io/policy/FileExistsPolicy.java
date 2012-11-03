package com.rauban.dropandtransfer.model.io.policy;

public abstract class FileExistsPolicy {
	public abstract String generateFileName(String baseFileName);
}
