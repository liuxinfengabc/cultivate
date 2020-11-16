package com.lz.ht.result;

public class SimditorResult {
	
	private boolean success ;
	
	private String file_path;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public static SimditorResult genSuccess(String file_path) {
		SimditorResult simditorResult = new SimditorResult();
		simditorResult.setSuccess(true);
		simditorResult.setFile_path(file_path);
		return simditorResult;
	}
	
	public static SimditorResult genFail() {
		SimditorResult simditorResult = new SimditorResult();
		simditorResult.setSuccess(false); 
		return simditorResult;
	}
	
}
