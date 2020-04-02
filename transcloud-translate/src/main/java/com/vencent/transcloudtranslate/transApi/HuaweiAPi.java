package com.vencent.transcloudtranslate.transApi;

import com.huawei.sis.bean.SisConfig;
import com.huawei.sis.bean.SisConstant;
import com.huawei.sis.bean.request.AsrShortRequest;
import com.huawei.sis.bean.request.TtsRequest;
import com.huawei.sis.bean.response.AsrShortResponse;
import com.huawei.sis.bean.response.TtsResponse;
import com.huawei.sis.client.AsrClient;
import com.huawei.sis.client.TtsClient;
import com.huawei.sis.exception.SisException;
import com.vencent.transcloudtranslate.entity.TransInterface;
import com.vencent.transcloudtranslate.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Base64;

/**
 * @description:
 * @author: vencent
 * @create 2020-03-31
 **/
public class HuaweiAPi {
    String ak = "";
    String sk = "";
    String region = "";  // 区域，如cn-north-1
    String appId = "";
    private final Logger logger = LoggerFactory.getLogger(HuaweiAPi.class);

    /**
     * 定义config，所有参数可选，设置超时时间。
     * @return SisConfig
     */
    private SisConfig getConfig() {
        SisConfig config = new SisConfig();
        // 设置连接超时，默认5000ms
        config.setConnectionTimeout(SisConstant.DEFAULT_CONNECTION_TIMEOUT);
        // 设置请求超时，默认1000ms
        config.setRequestTimeout(SisConstant.DEFAULT_CONNECTION_REQUEST_TIMEOUT);
        // 设置socket超时，默认5000ms
        config.setSocketTimeout(SisConstant.DEFAULT_SOCKET_TIMEOUT);
        // 设置代理, 一定要确保代理可用才启动此设置。 代理初始化也可用不加密的代理，new ProxyHostInfo(host, port);
        // ProxyHostInfo proxy = new ProxyHostInfo(host, port, username, password);
        // config.setProxy(proxy);
        return config;
    }
    /**
     * 短语音识别。
     */
    public String getResult(byte[] file){
        AsrShortResponse response = null;
        try {
            // 1. 初始化AsrClient
            // 定义authInfo，根据ak，sk，region。注意这里只有三个初始化参数，不要填projectId。
            com.huawei.sis.bean.AuthInfo authInfo = new com.huawei.sis.bean.AuthInfo(ak, sk, region);
            // 设置config，主要与超时有关
            SisConfig config = getConfig();
            // 根据authInfo和config，构造AsrClient
            AsrClient asr = new AsrClient(authInfo, config);

            // 2. 配置请求
            AsrShortRequest request = new AsrShortRequest();
            //Base64转码
            String fileBase64Str =  Base64.getEncoder().encodeToString(file);

            request.setData(fileBase64Str);
            // 也可以通过obsUrl传入待识别音频。若两者都设置，默认使用传入的data
            // request.setObsUrl(obsUrl);
            // 设置采样率，目前支持8k，16k。默认是8k。详见api文档
            request.setSampleRate("16k");
            // 设置文件的类型，支持wav、mp3、wma、amr、ac3、ogg、aac等。 可不填赋值空串。
            request.setEncodeType("");
            // 3. 发送请求，获取响应
            response = asr.getAsrShortResponse(request);
        } catch (SisException e) {
            e.printStackTrace();
            System.out.println("error_code:" + e.getErrorCode() + "\nerror_msg" + e.getErrorMsg());
        }
        return response.getResult();
    }



    /**
     * TTS
     * @param targetText
     * @return
     */
    public byte[] textToSpeech(String targetText,String path){
        TtsResponse response = null;
        byte[] outbytes = null;

        try {
            // 1. 初始化TtsClient
            // 定义authInfo，根据ak，sk，ttsRegion。注意初始化参数不需要填写projectId。
            com.huawei.sis.bean.AuthInfo authInfo = new com.huawei.sis.bean.AuthInfo(ak, sk, region);
            // 定义config，所有参数可选，设置超时时间。
            SisConfig config = getConfig();
            // 根据authInfo和config，构造TtsClient
            TtsClient tts = new TtsClient(authInfo, config);

            // 2. 配置请求
            TtsRequest request = new TtsRequest(targetText);
            // 设置参数，所有参数均可选，如果要保存合成音频文件，需要在request设置
            request.setSaved(true);
            request.setSavePath(path);
            setParameter(request);
            // 3. 发送请求，获取响应
            response = tts.getTtsResponse(request);
            if(!response.isSaved()){
                for(int i = 0;i<100;i++){

                }
            }
            outbytes = FileUtil.file2Byte(path);
            //删除pcm
            String dirPath = path.substring(0,path.lastIndexOf(File.separator));
            File dir = new File(dirPath);
            FileUtil.deleteFile(dir);
        } catch (SisException e) {
            e.printStackTrace();
            logger.error("error_code:" + e.getErrorCode() + "\nerror_msg" + e.getErrorMsg());
            System.out.println("error_code:" + e.getErrorCode() + "\nerror_msg" + e.getErrorMsg());
        }
        return outbytes;
    }
    /**
     * 用于语音合成参数设置，例如发声人、音高、语速、音量、采样率、连接超时。所有参数均可以不设置，采用默认。
     *
     * @param request 语音合成请求
     */
    private void setParameter(TtsRequest request) {

        // 发声人，支持xiaoyu，xiaoqi，xiaoyan
        request.setVoiceName(TtsRequest.DEFAULT_VOICE_NAME);
        // 音高，[-500, 500], 默认0
        request.setPitchRate(TtsRequest.DEFAULT_PITCH_RATE);
        // 语速，[-500, 500]，默认0
        request.setSpeechSpeed(TtsRequest.DEFAULT_SPEECH_SPEED);
        // 音量，[-20,20]，默认0
        request.setVolume(TtsRequest.DEFAULT_VOLUME);
        // 当前支持8k和16k
        request.setSampleRate(TtsRequest.DEFAULT_SAMPLE_RATE);

    }
    private volatile static HuaweiAPi huaweiAPi;
    private HuaweiAPi(TransInterface transInterface){
        ak = transInterface.getSecretId();
        sk = transInterface.getSecretKey();
        region = transInterface.getRegion();
        appId = transInterface.getAppId();
    }
    public static HuaweiAPi getHuaweiAPi(TransInterface transInterface){
        if (huaweiAPi == null){
            synchronized(HuaweiAPi.class){
                if (huaweiAPi == null){
                    huaweiAPi = new HuaweiAPi(transInterface);
                }
            }
        }
        return huaweiAPi;
    }
}
