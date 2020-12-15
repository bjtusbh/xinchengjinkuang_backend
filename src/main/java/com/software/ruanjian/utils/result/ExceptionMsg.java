package com.software.ruanjian.utils.result;
//2开头操作成功
//3开头权限验证失败
//4开头操作失败(服务端问题)
//5开头操作失败(客户端问题)
public enum ExceptionMsg {

    //实现响应的枚举类
    SUCCESS("20001", "操作成功"),
    UnAuthentication("30002","认证失败"),
    UnAuthorization("30003","授权失败"),
    LoginFail("30001","登陆失败"),
    FAILED("50001","操作失败"),
    ParamError("50002", "参数错误！"),
    FileEmpty("50003","上传文件为空"),
    LimitPictureSize("50004","图片大小必须小于2M"),
    LimitPictureType("50005","图片格式必须为'jpg'、'png'、'jpge'、'gif'、'bmp'")
    ;
    private ExceptionMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }


}


