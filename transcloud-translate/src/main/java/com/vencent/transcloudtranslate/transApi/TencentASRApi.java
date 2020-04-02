package com.vencent.transcloudtranslate.transApi;

import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskRequest;
import com.tencentcloudapi.asr.v20190614.models.CreateRecTaskResponse;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusRequest;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

import java.util.Base64;

public class TencentASRApi {

    private final static String SECRET_ID = "AKIDLbgdSayTO8JqGZogYKzoGfzjZhndFTmu";
    private final static String SECRET_KEY = "CGD8sQvIVYeex6869364tbEi5b8Zr10t";
    public String startTranslate(byte[] file){
        String result = null;
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            // 实例化要请求产品的client对象
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
            AsrClient client = new AsrClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象
            CreateRecTaskRequest req = new CreateRecTaskRequest();
            //腾讯翻译要求Base64编码
            Base64.Encoder en = Base64.getEncoder();
            String string = en.encodeToString(file);

            //设置类型
            req.setEngineModelType("16k_zh");
            //设置声道
            req.setChannelNum(1L);

            req.setResTextFormat(0l);
            req.setSourceType(1l);
            req.setDataLen((long) string.length());
            req.setData(string);
            // 通过client对象调用想要访问的接口，需要传入请求对象
            CreateRecTaskResponse resp = client.CreateRecTask(req);
            // 输出json格式的字符串回包
            result = CreateRecTaskResponse.toJsonString(resp.getData());
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return result;


    }

    public DescribeTaskStatusResponse getResult(String params){
        String result = null;
        DescribeTaskStatusResponse resp = null;
        try {

            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            AsrClient client = new AsrClient(cred, "ap-guangzhou", clientProfile);

            DescribeTaskStatusRequest req = DescribeTaskStatusRequest.fromJsonString(params, DescribeTaskStatusRequest.class);

            resp = client.DescribeTaskStatus(req);
            
            

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        
        return resp;
    }

    /*
       单例模式
       双重校验锁
        */
    private volatile static TencentASRApi tencentASRApi;
    private TencentASRApi(){}
    public static TencentASRApi getTencentASRApi(){
        if (tencentASRApi == null){
            synchronized(TencentASRApi.class){
                if (tencentASRApi == null){
                    tencentASRApi = new TencentASRApi();
                }
            }
        }
        return tencentASRApi;
    }
}
