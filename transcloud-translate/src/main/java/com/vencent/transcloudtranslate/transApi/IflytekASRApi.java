package com.vencent.transcloudtranslate.transApi;

import com.vencent.transcloudtranslate.utils.WebIATWS;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import static com.vencent.transcloudtranslate.utils.WebIATWS.getAuthUrl;

public class IflytekASRApi {

    public String getResult(byte[] file) throws Exception {
        String hostUrl = "https://iat-api.xfyun.cn/v2/iat";
        String apiKey = "508c9a6827d11c8e9ffbbefbcb9b590a";
        String apiSecret = "3441e1f072ddf8a6e546db098a25f764";
        String appid = "5e6c8fb3";
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

    /*
       单例模式
       双重校验锁
        */
        private IflytekASRApi(){};
        private volatile static IflytekASRApi iflytekASRApi;
        public static IflytekASRApi getIflytekASRApi(){
            if(iflytekASRApi == null){
                synchronized (IflytekASRApi.class){
                    if (iflytekASRApi == null){
                        iflytekASRApi = new IflytekASRApi();
                    }
                }
            }
            return iflytekASRApi;

        }


}
