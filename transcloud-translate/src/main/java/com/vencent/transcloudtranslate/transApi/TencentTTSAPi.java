package com.vencent.transcloudtranslate.transApi;

import com.tencent.cloud.asr.realtime.sdk.config.AsrBaseConfig;
import com.tencent.cloud.asr.tts.sdk.config.TtsConfig;
import com.tencent.cloud.asr.tts.sdk.http.synchronize.TtsSynSender;
import com.tencent.cloud.asr.tts.sdk.model.TtsResponse;
import com.vencent.transcloudtranslate.utils.PlayMusic;

/*
腾讯语音合成接口
 */
public class TencentTTSAPi {
    /*
     ** 从字节数组读取语音数据，发送请求。
     */
    public byte[] sendStringRequest(String text) {
        TtsSynSender ttsSynSender = new TtsSynSender(); // 创建之后可重复使用
        TtsResponse response = ttsSynSender.request(text, "session-id-123");
        if(response == null){
            return null;
        }
        byte[] responseBytes = response.getResponseBytes();
        // TtsResponse response2 = ttsSynSender.sendRequest(text);
        int bitNum = TtsConfig.SAMPLE_RATE == 16000 ? 16 : 8;
        PlayMusic.playMusic(responseBytes,TtsConfig.SAMPLE_RATE,bitNum,1);
       // PcmToWavUtil.convert2Wav(responseBytes,TtsConfig.SAMPLE_RATE,1,bitNum);
        return response.getResponseBytes();

    }


    /*
     单例模式
     双重校验锁
      */
    private volatile static TencentTTSAPi tencentTTSAPi;
    private TencentTTSAPi(){
        // Required
        AsrBaseConfig.appId = "1252800122";
        AsrBaseConfig.secretId = "AKIDLbgdSayTO8JqGZogYKzoGfzjZhndFTmu";
        AsrBaseConfig.secretKey = "CGD8sQvIVYeex6869364tbEi5b8Zr10t";

        // optional，根据自身需求设置配置值， 不配则使用默认值。
        TtsConfig.VOLUME = 5; // 音量大小, 范围[0，10]，默认为0，表示正常音量。
        // TtsConfig.REQUEST_ENCODE = RequestEncode.UTF_8; // 传入的文字所采用的编码，默认为utf-8
        // TtsConfig.SPEED = 0; // 语速，范围[-2，2]. -2: 0.6倍; -1: 0.8倍; 0:1.0倍（默认）; 1: 1.2倍; 2: 1.5倍 。其他值：1.0 倍。
        // TtsConfig.VOICE_TYPE = 0; // 音色： 0：亲和女声（默认） 1：亲和男声 2：成熟男声 4：温暖女声 5：情感女声 6：情感男声
        // TtsConfig.SAMPLE_RATE = 16000;// 音频采样率： 16000：16k（默认）; 8000：8k
        // TtsConfig.PRIMARY_LANGUAGE = 1;// 主语言类型： 1-中文（默认） 2-英文
        // TtsConfig.CODEC = CodeC.PCM; // 无需修改。暂未支持Opus方式。
    }
    public static TencentTTSAPi getTencentTTSAPi(){
        if (tencentTTSAPi == null){
            synchronized(TencentASRApi.class){
                if (tencentTTSAPi == null){
                    tencentTTSAPi = new TencentTTSAPi();
                }
            }
        }
        return tencentTTSAPi;
    }
}

