package com.vencent.transcloudtranslate.transApi;
import com.huawei.sis.bean.AuthInfo;
import com.huawei.sis.bean.SisConfig;
import com.huawei.sis.bean.SisConstant;
import com.huawei.sis.bean.request.AsrShortRequest;
import com.huawei.sis.bean.response.AsrShortResponse;
import com.huawei.sis.client.AsrClient;
import com.huawei.sis.exception.SisException;
import java.util.Base64;


/**
 *
 * 华为短语音识别
 *
 */
public class HuaweiASRApi {
    private String ak = "CIUAW8LH1HL4JQA5YY1U";
    private String sk = "ZitdgC31snkjevSwmKizzCwIRbIyNOPxJrzMTYaS";
    private String region = "cn-north-1";       // 区域，如cn-north-1

    //使用上传文件字节数组，不使用本地文件
    // private String path = "";         // 本地音频路径，如D:/test.wav
    // private String obsUrl = "";       // obs音频链接，音频传送支持本地文件或者obs链接


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
            AuthInfo authInfo = new AuthInfo(ak, sk, region);
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
        return "{code:"+response.getStatusCode()+",message:"+response.getStatusMsg()+",result"+response.getResult()+"}";
    }

    /*
      单例模式
      双重校验锁
       */
    private volatile static HuaweiASRApi huaweiASRApi;
    private HuaweiASRApi(){}
    public static HuaweiASRApi getHuaweiASRApi(){
        if (huaweiASRApi == null){
            synchronized(HuaweiASRApi.class){
                if(huaweiASRApi == null){
                    huaweiASRApi = new HuaweiASRApi();
                }
            }
        }
        return huaweiASRApi;
    }
}