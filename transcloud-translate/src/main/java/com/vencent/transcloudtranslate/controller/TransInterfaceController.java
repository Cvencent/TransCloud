package com.vencent.transcloudtranslate.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusRequest;
import com.tencentcloudapi.asr.v20190614.models.DescribeTaskStatusResponse;
import com.vencent.transcloudtranslate.entity.History;
import com.vencent.transcloudtranslate.entity.TransInterface;
import com.vencent.transcloudtranslate.feign.ApiFeignClient;
import com.vencent.transcloudtranslate.service.TransInterfaceService;
import com.vencent.transcloudtranslate.transApi.*;
import com.vencent.transcloudtranslate.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/translate")
public class TransInterfaceController {
    @Autowired
    private TransInterfaceService transInterfaceService;
    @Autowired
    private ApiFeignClient apiFeignClient;
    private byte[] bytes;
    private String fileName;
    public static Logger logger = LoggerFactory.getLogger(TransInterfaceController.class);

    /**
     * 上传文件
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload")
    public String uploadTranslate(@RequestParam MultipartFile file){
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            logger.error("上传文件转换出错");
        }
        fileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().length()-4);
        return "access";
    }

    /**
     * 进行翻译（上传文件方式）
     * @param userID
     * @return
     */
    @RequestMapping(value = "/trans", method = RequestMethod.GET)
    public History getTranslate(@SessionAttribute("userID") String userID){
        if(bytes == null){
            return null;
        }
        History history = null;
        try{
            history =  transInterfaceService.translate(userID,bytes,fileName);
        }catch (Exception  e) {
            logger.error(e + "");
        }
        //上传文件置空
        bytes = null;
        return history;
    }

    /**
     * 翻译（录音方式）
     * @param userID
     * @param file
     * @return
     */
    @PostMapping(value = "/transBlob")
    public History translateBlob(@SessionAttribute("userID") String userID,@RequestParam MultipartFile file){
        byte[] bytesBlob = null;
        String blobFileName = file.getOriginalFilename().substring(0,file.getOriginalFilename().length()-4);
        History history = null;
        try{
            bytesBlob = file.getBytes();
            history =  transInterfaceService.translate(userID,bytesBlob,blobFileName);
        } catch (Exception  e) {
            logger.error(e + "");
        }
        return history;
    }
//    /*
//     百度翻译
//     */
//    @RequestMapping(value = "/baiduTrans", method = RequestMethod.POST)
//    public @ResponseBody
//    String getBaiduTranslate(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException, TencentCloudSDKException, JSONException {
//        byte[] bytes = file.getBytes();
//        BaiduApi baiduApi =  BaiduApi.getBaiduApi();
//        String result =  baiduApi.getResult(bytes,"pcm");
//        TencentApi tencentApi =  TencentApi.getTencentApi();
//        String target =  tencentApi.textTrans(result);
//        baiduApi.getSpeech(target);
//        return result;
//    }

    /*
    腾讯长语音翻译
     */
    @RequestMapping(value = "/tencentTrans", method = RequestMethod.POST)
    public String getTencentTranslate(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
//        File fileToSave = new File(file.getOriginalFilename());
//        FileCopyUtils.copy(file.getBytes(), fileToSave);
//        File newFile = new File(file.getOriginalFilename());
//        Transfer.Mp3ToPcm(newFile);
        TencentASRApi tencentASRApi = TencentASRApi.getTencentASRApi();
        String taskId= tencentASRApi.startTranslate(bytes);
        DescribeTaskStatusResponse resp = tencentASRApi.getResult(taskId);
        while(resp.getData().getStatus() == 1L||resp.getData().getStatus() == 0L){
            Thread.currentThread().sleep(2000);
            resp = tencentASRApi.getResult(taskId);
        }
        String result = DescribeTaskStatusRequest.toJsonString(resp);
        return result;
    }
    /*
    科大讯飞翻译
     */
    @RequestMapping(value = "/iflytekTrans", method = RequestMethod.POST)
    public String getIflytekTranslate(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        IflytekASRApi iflytekASRApi = IflytekASRApi.getIflytekASRApi();
        String result = iflytekASRApi.getResult(bytes);
        IflytekMtApi iflytekMtApi = new IflytekMtApi();
        String target = iflytekMtApi.textTrans(result);
        IflytekTTSAPi iflytekTTSAPi = new IflytekTTSAPi();
        iflytekTTSAPi.textToSpeech(target);
        return result;
    }
    /*
    华为短语音翻译
     */
    @RequestMapping(value = "/huaweiTrans", method = RequestMethod.POST)
    public String getHuaweiTranslate(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        HuaweiASRApi huaweiASRApi = HuaweiASRApi.getHuaweiASRApi();
        String result = huaweiASRApi.getResult(bytes);
        return result;
    }

    /**
     * 获取所有记录
     * @param pageNum
     * @param count
     * @param userID
     * @return
     */
    @GetMapping("/all/{pageNum}/{count}")
    public Page<TransInterface> getAll(@PathVariable("pageNum") int pageNum,@PathVariable("count") int count ,@SessionAttribute("userID") String userID){
        Page<TransInterface> page = new Page<>(pageNum,count);
        return  transInterfaceService.listByUserId(userID,page);
    }

    /**
     * 添加翻译密钥
     * @param transInterface
     * @param userID
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody(required = true) TransInterface transInterface,@SessionAttribute("userID") String userID){
        transInterface.setId(UUIDUtil.getUUID());
        transInterface.setUserId(userID);
        transInterfaceService.save(transInterface);
        return "success";
    }

    /**
     * 改变当前使用的翻译密钥
     * @param changeOne
     * @param changeTwo
     * @return
     */
    @PostMapping("/changeState/{changeOne}/{changeTwo}")
    public String changeState(@PathVariable("changeOne") String changeOne,@PathVariable("changeTwo") String changeTwo){
        if(transInterfaceService.UpdateTwoByCurrentInterface(changeOne,changeTwo)){
            return "修改成功";
        }
        return "修改失败";
    }

    /**
     * 删除翻译密钥
     * @param transInterface
     * @return
     */
    @PostMapping("/delete")
    public String delete(@RequestBody TransInterface transInterface){
        if(transInterfaceService.removeById(transInterface.getId())){
            return "删除成功";
        }
        return "删除失败";
    }

    /**
     * 修改翻译密钥
     * @param transInterface
     * @return
     */
    @PostMapping("/update")
    public String update(@RequestBody TransInterface transInterface){
        if(transInterfaceService.updateById(transInterface)){
            return "修改成功";
        }
        return "修改失败";
    }

    /**
     * 查询是否存在正在使用的翻译接口
     * @param userID
     * @return
     */
    @GetMapping("/currentInterface")
    public String HasCurrentInterface(@SessionAttribute String userID){
        if(transInterfaceService.selectByUserIdAndCurrent(userID) != null){
            return "success";
        }
        return "请先开启翻译接口";
    }

}
