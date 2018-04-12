package com.yiming.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.yiming.entity.User;

public class SendVerificationCode {
    private String phoneNum;

    public SendVerificationCode(String phoneNum) {
        super();
        this.phoneNum = phoneNum;
    }

    public String sendVerificationCode() throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Constant.SERVER_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        String checkSum = CheckSumBuilder.getCheckSum(Constant.APP_SECRET, Constant.NONCE, curTime);

        // 设置请求的header
        httpPost.addHeader("AppKey", Constant.APP_KEY);
        httpPost.addHeader("Nonce", Constant.NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        
        nvps.add(new BasicNameValuePair("templateid", Constant.TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", phoneNum));
        nvps.add(new BasicNameValuePair("codeLen", Constant.CODELEN));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpclient.execute(httpPost);
        /*
         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
         * 2.具体的code有问题的可以参考官网的Code状态表
         */
        //System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
        
        String result = EntityUtils.toString(response.getEntity());
        String[] object = result.split(",");
        if(!object[0].split(":")[1].equals("200")){
            return object[0].split(":")[1];
        }else {
            String pattern = "\\d+";
            return object[2].split(":")[1].split("\"")[1];
        }
    }
}
