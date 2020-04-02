package com.vencent.transcloudtranslate.transApi;

import com.baidu.aip.speech.AipSpeech;
import org.json.JSONException;
import org.json.JSONObject;

/*
baidu短语音翻译接口
 */
public class BaiduASRApi {
    //设置APPID/AK/SK
    public static final String APP_ID = "18619485";
    public static final String API_KEY = "oc0pCLNC3UYqOhzCXHpwqNLE";
    public static final String SECRET_KEY = "iH9Dv0V8MdpBniSuOwaa04TGkEMgaHqT";
    public String getResult(byte[] file, String format) throws JSONException {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        // 调用接口
        JSONObject res = client.asr(file, format, 16000, null);

        String result = res.get("result").toString();
        //提取识别内容
        return result.substring(2,result.length()-2);
        }

        /*
        单例模式
        双重校验锁
         */
        private volatile static BaiduASRApi baiduASRApi;
        private BaiduASRApi(){}
        public static BaiduASRApi getBaiduASRApi(){
            if (baiduASRApi == null){
                synchronized(BaiduASRApi.class){
                    if (baiduASRApi == null){
                        baiduASRApi = new BaiduASRApi();
                    }
                }
            }
            return baiduASRApi;
        }
    }
