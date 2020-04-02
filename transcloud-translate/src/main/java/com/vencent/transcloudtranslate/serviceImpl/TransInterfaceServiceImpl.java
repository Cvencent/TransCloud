package com.vencent.transcloudtranslate.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vencent.transcloudtranslate.entity.History;
import com.vencent.transcloudtranslate.entity.TransInterface;
import com.vencent.transcloudtranslate.feign.ApiFeignClient;
import com.vencent.transcloudtranslate.mapper.TransInterfaceMapper;
import com.vencent.transcloudtranslate.service.TransInterfaceService;
import com.vencent.transcloudtranslate.transApi.BaiduApi;
import com.vencent.transcloudtranslate.transApi.HuaweiAPi;
import com.vencent.transcloudtranslate.transApi.IflytekAPi;
import com.vencent.transcloudtranslate.transApi.TencentApi;
import com.vencent.transcloudtranslate.utils.FileUtil;
import com.vencent.transcloudtranslate.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
@Service
@Transactional
public class TransInterfaceServiceImpl extends ServiceImpl<TransInterfaceMapper,TransInterface> implements TransInterfaceService {

    @Autowired
    private ApiFeignClient apiFeignClient;

    @Override
    public Page<TransInterface> listByUserId(String userId, Page page){
        QueryWrapper<TransInterface> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
       return this.baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public TransInterface selectByUserIdAndCurrent(String userId) {
        QueryWrapper<TransInterface> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("current_interface","1");
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public History translate(String userId, byte[] bytes, String fileName) throws Exception {
        TransInterface transInterface =  selectByUserIdAndCurrent(userId);
        String transSource = null;
        String transTarget = null;
        byte[] outBytes = new byte[0];
        String filePath =  FileUtil.GetFilePath();
        //用来存放历史记录
        History history = new History();
        String type =  transInterface.getInterfaceType();
        switch (type){
            case "百度":
                BaiduApi baiduApi = BaiduApi.getBaiduApi(transInterface);
                //ASR
                transSource = baiduApi.getResult(bytes, "wav");
                List<TransInterface> listForBaidu =  listByType("腾讯");
                TransInterface transInterfaceTencentForBaidu = null;

                if(listForBaidu!= null){
                    transInterfaceTencentForBaidu = listForBaidu.get(0);
                }
                TencentApi tencentApiForBaidu = TencentApi.getTencentApi(transInterfaceTencentForBaidu);
                //MT
                transTarget = tencentApiForBaidu.textTrans(transSource);
                //TTs
                outBytes = baiduApi.getSpeech(transTarget);
                //百度默认mp3
                history.setTransSpeech(fileName+".mp3");
                break;
            case "腾讯":
                TencentApi tencentApi =  TencentApi.getTencentApi(transInterface);
                //ASR
                transSource = tencentApi.getResult(bytes);
                //MT
                transTarget =  tencentApi.textTrans(transSource);
                //TTS
                outBytes =  tencentApi.sendStringRequest(transTarget);
                //腾讯默认pcm
                history.setTransSpeech(fileName+".pcm");
                break;
            case "讯飞":
                List<TransInterface> listforIfly =  listByType("腾讯");
                TransInterface transInterfaceTencentForIlfy = null;

                if(listforIfly!= null){
                    transInterfaceTencentForIlfy = listforIfly.get(0);
                }
                IflytekAPi iflytekAPi =  IflytekAPi.getIflytekAPi(transInterface);
                //ASR
                transSource = iflytekAPi.getResult(bytes);
                TencentApi tencentApiForIfly = TencentApi.getTencentApi(transInterfaceTencentForIlfy);
                //MT
                transTarget = tencentApiForIfly.textTrans(transSource);
                //TTS
                outBytes = iflytekAPi.textToSpeech(transTarget,filePath.substring(1)+ File.separator+fileName+".pcm");
                //讯飞默认为pcm
                history.setTransSpeech(fileName+".pcm");
                break;
            case "华为":
                HuaweiAPi huaweiAPi=  HuaweiAPi.getHuaweiAPi(transInterface);
                //ASR
                transSource =  huaweiAPi.getResult(bytes);
                List<TransInterface> listForHuawei =  listByType("腾讯");
                TransInterface transInterfaceTencentForHuawei = null;

                if(listForHuawei!= null){
                    transInterfaceTencentForHuawei = listForHuawei.get(0);
                }
                TencentApi tencentApiForHuawei = TencentApi.getTencentApi(transInterfaceTencentForHuawei);
                //MT
                transTarget = tencentApiForHuawei.textTrans(transSource);
                //TTS
                outBytes = huaweiAPi.textToSpeech(transTarget,filePath.substring(1)+ File.separator+fileName+".wav");
                //华为默认为wav
                history.setTransSpeech(fileName+".wav");
                break;
        }
        //存放历史记录
        history.setId(UUIDUtil.getUUID());
        history.setTransSource(transSource);
        history.setTransTarget(transTarget);
        history.setUserId(userId);
//        HashMap<String ,Object> map = new HashMap<>();
//        map.put("outBytes", JSONObject.toJSON(outBytes));
//        map.put("history",JSONObject.toJSON(history));
//        String hashMap = JSONObject.toJSONString(map);
        apiFeignClient.save(history,outBytes);
        return history;
    }

    @Override
    public Boolean UpdateTwoByCurrentInterface(String changeOne, String changeTwo) {
        TransInterface transInterfaceOne =  this.baseMapper.selectById(changeOne);
        if(transInterfaceOne.getCurrentInterface().equals("0")){
            transInterfaceOne.setCurrentInterface("1");
        }else{
            transInterfaceOne.setCurrentInterface("0");
        }
        boolean a =  updateById(transInterfaceOne);
        if(!changeTwo.equals("null")){
           TransInterface transInterfaceTwo =  this.baseMapper.selectById(changeTwo);
           transInterfaceTwo.setCurrentInterface("0");
          boolean b = updateById(transInterfaceTwo);
           return a&b;
        }
        return a;
    }

    @Override
    public List<TransInterface> listByType(String type) {
        QueryWrapper<TransInterface> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interface_type",type);
        return  list(queryWrapper);
    }

}
