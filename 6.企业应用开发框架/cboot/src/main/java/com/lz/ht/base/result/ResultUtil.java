package com.lz.ht.base.result;

public class ResultUtil {

    /**返回成功 */
    public static Result success(){
        Result result = new Result();
         
        result.setCode("0");//成功
        result.setMsg("成功");//提示语
        return result;
    }

    /**返回成功 */
    public static Result success(Object object){
        Result result = new Result();
        result.setCode("0");//成功
        result.setMsg("成功");//提示语
        result.setDate(object);//返回内容
        return result;
    }

    /**返回失败 */
    public static Result error(){
        Result result = new Result();
        result.setCode("1");//失败
        result.setMsg("失败");//提示语
        return result;
    }

    /**返回失败 */
    public static Result error(String code, String msg){
        Result result = new Result();
        result.setCode(code);//失败
        result.setMsg(msg);//提示语
        return result;
    }

}