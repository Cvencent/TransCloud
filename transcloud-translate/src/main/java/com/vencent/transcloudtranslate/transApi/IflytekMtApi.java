package com.vencent.transcloudtranslate.transApi;

import com.vencent.transcloudtranslate.utils.HttpUtil;
import com.vencent.transcloudtranslate.utils.WebITS;

import java.util.Map;

/**
 * @description:
 * @author: vencent
 * @create 2020-03-22
 **/
public class IflytekMtApi {

    // OTS webapi 接口地址
    private static final String WebITS_URL = "https://itrans.xfyun.cn/v2/its";
    // 应用ID（到控制台获取）
    private static final String APPID = "5e6c8fb3";
    // 接口APIKey（到控制台机器翻译服务页面获取）
    private static final String API_KEY = "508c9a6827d11c8e9ffbbefbcb9b590a";
    // 接口APISercet（到控制台机器翻译服务页面获取）
    private static final String API_SECRET = "3441e1f072ddf8a6e546db098a25f764";
    // 语种列表参数值请参照接口文档：https://doc.xfyun.cn/rest_api/机器翻译.html
    // 源语种
    private static final String FROM = "cn";
    // 目标语种
    private static final String TO = "en";

    public String textTrans(String sourceText) throws Exception {

        String body = WebITS.buildHttpBody(APPID,FROM,TO,sourceText);
        Map<String, String> header = WebITS.buildHttpHeader(body,WebITS_URL,API_KEY,API_SECRET);
        Map<String, Object> resultMap = HttpUtil.doPost2(WebITS_URL, header, body);
        String resultStr = null;
        if (resultMap != null) {
            resultStr = resultMap.get("body").toString();
            System.out.println("【ITS WebAPI 接口调用结果】\n" + resultStr);
           /* //以下仅用于调试
            Gson json = new Gson();
            WebITS.ResponseData resultData = json.fromJson(resultStr, WebITS.ResponseData.class);
            int code = resultData.getCode();*/
        } else {
            System.out.println("调用失败！请根据错误信息检查代码，接口文档：https://www.xfyun.cn/doc/nlp/xftrans/API.html");
        }
        return resultStr;
    }

}
