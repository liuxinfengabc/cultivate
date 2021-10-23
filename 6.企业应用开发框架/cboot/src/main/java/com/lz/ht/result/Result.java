package com.lz.ht.result;

import lombok.Data;

@Data
public class Result {
    private String msgCode;
    private String message;

    public static final Result SUCCESS = new Result("10000","SUCCESS");

    public static final Result NO_TOKEN = new Result("9999","NO_TOKEN");

    
    
    public Result(String msgCode, String message) {
        this.msgCode = msgCode;
        this.message = message;
    }

    public static Result  genSuccessResult(){
        return SUCCESS;
    }

    public static Result  genNoTokenResult(){
        return NO_TOKEN;
    }


    public static Result  genResult(String msgCode,String message){
    	return  new Result(  msgCode,   message); 
    }

	public Result() {
		 
	}


}
