package com.vencent.transcloudtranslate.transApi;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @description:
 * @author: vencent
 * @create 2020-03-22
 **/
public class BaiduTTSApi {
    //设置APPID/AK/SK
    public static final String APP_ID = "18619485";
    public static final String API_KEY = "oc0pCLNC3UYqOhzCXHpwqNLE";
    public static final String SECRET_KEY = "iH9Dv0V8MdpBniSuOwaa04TGkEMgaHqT";

    public byte[] getSpeech(String text) throws JSONException {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        // 调用接口
        TtsResponse res = client.synthesis(text, "zh", 1, null);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, "output.mp3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }
        return null;
    }

}
