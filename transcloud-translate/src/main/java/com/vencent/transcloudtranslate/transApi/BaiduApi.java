package com.vencent.transcloudtranslate.transApi;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.vencent.transcloudtranslate.entity.TransInterface;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description:
 * @author: vencent
 * @create 2020-03-22
 **/
public class BaiduApi {
    public String APP_ID ;
    public String API_KEY ;
    public String SECRET_KEY ;
    AipSpeech client = null;
    public String getResult(byte[] file, String format) throws JSONException {

        // 调用接口
        JSONObject res = client.asr(file, format, 16000, null);
        if(res.get("result") == null){
            return null;
        }
        String result = res.get("result").toString();
        //提取识别内容
        return result.substring(2,result.length()-2);
    }

    public byte[] getSpeech(String text) throws JSONException {
        // 调用接口
        TtsResponse res = client.synthesis(text, "zh", 1, null);
        byte[] data = res.getData();

        /*JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, "output.mp3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }*/
        return data;
    }
    /*
        单例模式
        双重校验锁
         */
    private volatile static BaiduApi baiduApi;
    private BaiduApi(TransInterface transInterface){
        APP_ID = transInterface.getAppId();
        API_KEY = transInterface.getSecretId();
        SECRET_KEY = transInterface.getSecretKey();
        client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(20000);
        client.setSocketTimeoutInMillis(60000);
    }
    public static BaiduApi getBaiduApi(TransInterface transInterface){
        if (baiduApi == null){
            synchronized(BaiduApi.class){
                if (baiduApi == null){
                    baiduApi = new BaiduApi(transInterface);
                }
            }
        }
        return baiduApi;
    }
}
