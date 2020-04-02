package com.vencent.transcloudtranslate.utils;

import java.util.UUID;

/**
 * @description: 生成UUID
 * @author: vencent
 * @create 2020-03-24
 **/
public class UUIDUtil {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
