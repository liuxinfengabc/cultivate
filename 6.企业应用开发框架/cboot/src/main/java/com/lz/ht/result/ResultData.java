package com.lz.ht.result;

 


public class ResultData<T> extends Result {

	private T data;
	 
	public ResultData(String msgCode, String message) {
		super(msgCode, message);
	}



	public ResultData() {
		 
	}



	public T getData() {
		return data;
	}



	public void setData(T data) {
		this.data = data;
	}

	 
	public static   ResultData   genSuccessResultData(Object data) {
		Result r1 = Result.genSuccessResult();
		ResultData resultData = new ResultData(r1.getMsgCode(),r1.getMessage());
		resultData.setData(data);
		return resultData;
	}
	

}
