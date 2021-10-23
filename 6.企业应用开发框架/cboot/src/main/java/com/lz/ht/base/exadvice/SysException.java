package com.lz.ht.base.exadvice;

public class SysException extends RuntimeException{

    private String code;
    private String msg;

    public SysException(String code, String msg){
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
