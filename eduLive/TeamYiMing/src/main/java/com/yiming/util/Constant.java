package com.yiming.util;

public class Constant {
    public static final String USER = "USER_IN_SESSION";
    public static final String SERVER_URL="https://api.netease.im/sms/sendcode.action";
    public static final String IMGPATH = "IMG_PATH_IN_SESSION"; // 直播间封面资源路径
    public static final String SOURCEPATH = "SOURCE_PATH_IN_SESSION"; // 直播间教学资源路径
    
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    public static final String
    APP_KEY="d1b79ae1bac22da25836d13ec5e7dfb1";
    
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    public static final String APP_SECRET="aa4e73064d82";
    
    //随机数
    public static final String NONCE="123456";
    
    //短信模板ID
    public static final String TEMPLATEID="4002774";
    
    //验证码长度，范围4～10，默认为4
    public static final String CODELEN="6";
}