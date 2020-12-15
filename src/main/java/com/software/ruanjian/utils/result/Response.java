package com.software.ruanjian.utils.result;

public class Response {

    //实现返回对象实体

    /** 返回信息码*/
    private String code ="20000";
    /** 返回信息内容*/
    private String msg ="操作成功";

    public Response() {
    }

    public Response(ExceptionMsg msg){
        this.code =msg.getCode();
        this.msg =msg.getMsg();
    }

    public Response(String rspCode) {
        this.code = rspCode;
        this.msg = "";
    }

    public Response(String rspCode, String rspMsg) {
        this.code = rspCode;
        this.msg = rspMsg;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "rspCode='" + code + '\'' +
                ", rspMsg='" + msg + '\'' +
                '}';
    }
}





