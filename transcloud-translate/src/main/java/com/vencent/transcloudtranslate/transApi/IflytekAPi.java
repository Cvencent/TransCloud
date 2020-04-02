package com.vencent.transcloudtranslate.transApi;

import com.iflytek.cloud.speech.*;
import com.vencent.transcloudtranslate.entity.TransInterface;
import com.vencent.transcloudtranslate.utils.FileUtil;
import com.vencent.transcloudtranslate.utils.HttpUtil;
import com.vencent.transcloudtranslate.utils.WebIATWS;
import com.vencent.transcloudtranslate.utils.WebITS;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.vencent.transcloudtranslate.utils.WebIATWS.getAuthUrl;

/**
 * @description:
 * @author: vencent
 * @create 2020-03-31
 **/
public class IflytekAPi {
    public String appid ;
    public String apiKey ;
    public String apiSecret ;


    /**
     * ASR
     * @param file
     * @return
     * @throws Exception
     */
    public String getResult(byte[] file) throws Exception {
        String hostUrl = "https://iat-api.xfyun.cn/v2/iat";
        String result = null;
        // 构建鉴权url
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        //将url中的 schema http://和https://分别替换为ws:// 和 wss://
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        //构造request请求
        Request request = new Request.Builder().url(url).build();
        WebIATWS webIATWS =  new WebIATWS(apiKey,apiSecret,appid);
        webIATWS.setFile(file);
        WebSocket webSocket = client.newWebSocket(request, webIATWS);

        while(result == null){
            //Thread.currentThread().wait(600);
            for(int j = 0;j<100;j++){

            }
            result = webIATWS.getStrResult();
        }
        //成功识别
        return result;
    }

    /**
     * MT
     * @param sourceText
     * @return
     * @throws Exception
     */
    public String textTrans(String sourceText) throws Exception {
        String FROM = "cn";
        String TO = "en";
        String WebITS_URL = "https://itrans.xfyun.cn/v2/its";
        String body = WebITS.buildHttpBody(appid,FROM,TO,sourceText);
        Map<String, String> header = WebITS.buildHttpHeader(body,WebITS_URL,apiKey,apiSecret);
        Map<String, Object> resultMap = HttpUtil.doPost2(WebITS_URL, header, body);
        String resultStr = null;
        if (resultMap != null) {
            resultStr = resultMap.get("body").toString();
            System.out.println("【ITS WebAPI 接口调用结果】\n" + resultStr);
        } else {
            System.out.println("调用失败！请根据错误信息检查代码，接口文档：https://www.xfyun.cn/doc/nlp/xftrans/API.html");
        }
        return resultStr;
    }

    /**
     * LLS
     * @param text
     * @return
     */
    public byte[] textToSpeech(String text,String path) throws IOException {
        SpeechUtility.createUtility( SpeechConstant.APPID +"="+appid);
        String dirPath = path.substring(0,path.lastIndexOf(File.separator));
        String fileName = path.substring(path.lastIndexOf(File.separator)+1);
        //1.创建SpeechSynthesizer对象
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer();
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速，范围0~100
        mTts.setParameter(SpeechConstant.PITCH, "50");//设置语调，范围0~100
        mTts.setParameter(SpeechConstant.VOLUME, "50");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ASR_AUDIO_PATH,path);


        //合成监听器
        SynthesizeToUriListener synthesizeToUriListener = XunfeiLib.getSynthesize();
        //3.开始合成
        //设置合成音频保存位置（可自定义保存位置），默认保存在“./tts_test.pcm”
        mTts.synthesizeToUri(text, path,synthesizeToUriListener);
        while(!XunfeiLib.checkDone(path)){
            for(int i = 0;i<100;i++){

            }

        }
        byte[] result = FileUtil.file2Byte(path);
        //删除pcm

        File dir = new File(dirPath);
        FileUtil.deleteFile(dir);
        return result;
    }
    private volatile static IflytekAPi iflytekAPi;
    private IflytekAPi(TransInterface transInterface){
        appid = transInterface.getAppId();
        apiKey = transInterface.getSecretId();
        apiSecret = transInterface.getSecretKey();
    }
    public static IflytekAPi getIflytekAPi(TransInterface transInterface){
        if (iflytekAPi == null){
            synchronized(IflytekAPi.class){
                if (iflytekAPi == null){
                    iflytekAPi = new IflytekAPi(transInterface);
                }
            }
        }
        return iflytekAPi;
    }
}
