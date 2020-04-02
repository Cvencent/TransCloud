package com.vencent.transcloudtranslate.transApi;

import com.tencent.cloud.asr.realtime.sdk.config.AsrBaseConfig;
import com.tencent.cloud.asr.tts.sdk.config.TtsConfig;
import com.tencent.cloud.asr.tts.sdk.http.synchronize.TtsSynSender;
import com.tencent.cloud.asr.tts.sdk.model.TtsResponse;
import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionRequest;
import com.tencentcloudapi.asr.v20190614.models.SentenceRecognitionResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;
import com.vencent.transcloudtranslate.entity.TransInterface;

import java.io.IOException;
import java.util.Base64;

/**
 * @description:
 * @author: vencent
 * @create 2020-03-22
 **/
public class TencentApi {
     String SECRET_ID = "";
     String SECRET_KEY = "";
     String APP_ID = "";
    Credential cred = null;
    public String getResult(byte[] file){
        String result = null;
        try{
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
               // req.setProjectId(Long.getLong(APP_ID));

            //子服务类型。2： 一句话识别。
            req.setSubServiceType(2L);

            req.setUsrAudioKey("session-123");
            req.setVoiceFormat("wav");
            //引擎模型类型。
            req.setEngSerViceType("16k");
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
    机器翻译方法
    return String
     */
    public String textTrans(String sourceText) throws TencentCloudSDKException {
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
     ** 从字节数组读取语音数据，发送请求。
     */
    public byte[] sendStringRequest(String text) throws IOException {
        // Required
        AsrBaseConfig.appId = APP_ID;
        AsrBaseConfig.secretId = SECRET_ID;
        AsrBaseConfig.secretKey = SECRET_KEY;

        // optional，根据自身需求设置配置值， 不配则使用默认值。
        TtsConfig.VOLUME = 5; // 音量大小, 范围[0，10]，默认为0，表示正常音量。
        // TtsConfig.REQUEST_ENCODE = RequestEncode.UTF_8; // 传入的文字所采用的编码，默认为utf-8
        // TtsConfig.SPEED = 0; // 语速，范围[-2，2]. -2: 0.6倍; -1: 0.8倍; 0:1.0倍（默认）; 1: 1.2倍; 2: 1.5倍 。其他值：1.0 倍。
        // TtsConfig.VOICE_TYPE = 0; // 音色： 0：亲和女声（默认） 1：亲和男声 2：成熟男声 4：温暖女声 5：情感女声 6：情感男声
        // TtsConfig.SAMPLE_RATE = 16000;// 音频采样率： 16000：16k（默认）; 8000：8k
        // TtsConfig.PRIMARY_LANGUAGE = 1;// 主语言类型： 1-中文（默认） 2-英文
        // TtsConfig.CODEC = CodeC.PCM; // 无需修改。暂未支持Opus方式。

        TtsSynSender ttsSynSender = new TtsSynSender(); // 创建之后可重复使用
        TtsResponse response = ttsSynSender.request(text, "session-id-123");
        if(response == null){
            return null;
        }
        byte[] responseBytes = response.getResponseBytes();
        // TtsResponse response2 = ttsSynSender.sendRequest(text);

        // int bitNum = TtsConfig.SAMPLE_RATE == 16000 ? 16 : 8;

       //  PlayMusic.playMusic(responseBytes,TtsConfig.SAMPLE_RATE,bitNum,1);

        return responseBytes;




    }
    /*
       单例模式
       双重校验锁
        */
    private volatile static TencentApi tencentApi;
    private TencentApi(TransInterface transInterface){
        APP_ID = transInterface.getAppId();
        SECRET_ID = transInterface.getSecretId();
        SECRET_KEY = transInterface.getSecretKey();
        cred = new Credential(SECRET_ID, SECRET_KEY);
    }
    public static TencentApi getTencentApi(TransInterface transInterface){
        if (tencentApi == null){
            synchronized(TencentApi.class){
                if (tencentApi == null){
                    tencentApi = new TencentApi(transInterface);
                }
            }
        }
        return tencentApi;
    }
}
