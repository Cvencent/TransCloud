package com.vencent.transcloudtranslate.transApi;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;
/*
腾讯机器翻译接口调用
 */
public class TencentMTAPi {
    private final static String SECRET_ID = "AKIDLbgdSayTO8JqGZogYKzoGfzjZhndFTmu";
    private final static String SECRET_KEY = "CGD8sQvIVYeex6869364tbEi5b8Zr10t";
    /*
    机器翻译方法
    return String
     */
    public String textTrans(String sourceText) throws TencentCloudSDKException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        ClientProfile clientProfile = new ClientProfile();
        //创建机器翻译客户端
        TmtClient tmtClient = new TmtClient(cred, "ap-guangzhou", clientProfile);
        TextTranslateRequest textTranslateRequest = new TextTranslateRequest();
        //设置翻译内容
        textTranslateRequest.setSourceText(sourceText);
        //设置翻译源语言
        textTranslateRequest.setSource("zh");
        //设置翻译目标语言
        textTranslateRequest.setTarget("en");
        //设置项目ID
        textTranslateRequest.setProjectId(0L);

        TextTranslateResponse textTranslateResponse = tmtClient.TextTranslate(textTranslateRequest);

        return textTranslateResponse.getTargetText();
    }
    /*
      单例模式
      双重校验锁
       */
    private volatile static TencentMTAPi tencentMTAPi;
    private TencentMTAPi(){}
    public static TencentMTAPi getTencentMTApi(){
        if (tencentMTAPi == null){
            synchronized(TencentASRApi.class){
                if (tencentMTAPi == null){
                    tencentMTAPi = new TencentMTAPi();
                }
            }
        }
        return tencentMTAPi;
    }
}
