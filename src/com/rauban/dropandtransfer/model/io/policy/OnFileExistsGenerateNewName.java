package com.rauban.dropandtransfer.model.io.policy;
/**
 * not thread safe!
 * @author RauBan
 *
 */
public class OnFileExistsGenerateNewName extends FileExistsPolicy {
	private String lastProcessed;
	private int count;
	@Override
	public String generateFileName(String baseFileName) {
		handleCounter(baseFileName);
		int offsetDot = baseFileName.indexOf('.'); 
		if(offsetDot == -1){
			return String.format("%s%d", baseFileName,count);
		} else {
			return handleBaseNameWithExtention(baseFileName, offsetDot);
		}
	}
	private String handleBaseNameWithExtention(String baseFileName,
			int offsetDot) {
		String part1 = baseFileName.substring(0, offsetDot-1);
		String part2 = baseFileName.substring(offsetDot);
		return String.format("%s%d%s", part1,count,part2);
	}
	private void handleCounter(String baseFileName) {
		if(baseFileName.equals(lastProcessed)){
			count++;
		} else {
			count = 0;
		}
	}

}
