package com.vencent.transcloudtranslate.transApi;

import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionRequest;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;

import java.util.Base64;

public class TencentShortASRApi {
    private final static String SECRET_ID = "AKIDLbgdSayTO8JqGZogYKzoGfzjZhndFTmu";
    private final static String SECRET_KEY = "CGD8sQvIVYeex6869364tbEi5b8Zr10t";
    public String getResult(byte[] file){
        String result = null;
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            // 实例化要请求产品的client对象
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
            AsrClient client = new AsrClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象
            SentenceRecognitionRequest req = new SentenceRecognitionRequest();
            //腾讯翻译要求Base64编码
            Base64.Encoder en = Base64.getEncoder();
            String string = en.encodeToString(file);

            //腾讯云项目 ID，可填 0，总长度不超过 1024 字节
            req.setProjectId(0L);
            //子服务类型。2： 一句话识别。
            req.setSubServiceType(2L);

            req.setUsrAudioKey("112233");
            req.setVoiceFormat("wav");
            //引擎模型类型。
            req.setEngSerViceType("16k_zh");
            //语音数据来源。0：语音 URL；1：语音数据（post body）
            req.setSourceType(1l);


            req.setDataLen((long) string.length());
            req.setData(string);
            // 通过client对象调用想要访问的接口，需要传入请求对象
            SentenceRecognitionResponse resp = client.SentenceRecognition(req);
            // 输出json格式的字符串回包
            result = resp.getResult();
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return result;


    }
    /*
    单例模式
     */
    private volatile static TencentShortASRApi tencentShortTransApi;
    private TencentShortASRApi(){}
    public static TencentShortASRApi getTencentShortTranApi(){
        if(tencentShortTransApi == null){
            synchronized(TencentShortASRApi.class){
                if(tencentShortTransApi == null){
                    tencentShortTransApi = new TencentShortASRApi();
                }
            }

        }
        return tencentShortTransApi;
    }
}
