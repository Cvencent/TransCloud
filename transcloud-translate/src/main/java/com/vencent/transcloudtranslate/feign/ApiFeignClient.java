package com.vencent.transcloudtranslate.feign;

import com.vencent.transcloudtranslate.entity.History;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "data")
public interface ApiFeignClient {
    @PostMapping("/history/save")
    String save(History history, @RequestParam byte[] outBytes);
}
