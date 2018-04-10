package com.yiming.util;

public class Constant {
    public static final String USER = "USER_IN_SESSION";
    public static final String SERVER_URL="https://api.netease.im/sms/sendcode.action";
    
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    public static final String
    APP_KEY="a47ab9d20db8571215e78e6ae313c494";
    
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    public static final String APP_SECRET="fea5bc38245a";
    
    //随机数
    public static final String NONCE="123456";
    
    //短信模板ID
    public static final String TEMPLATEID="3952580";
    
    //验证码长度，范围4～10，默认为4
    public static final String CODELEN="6";
}