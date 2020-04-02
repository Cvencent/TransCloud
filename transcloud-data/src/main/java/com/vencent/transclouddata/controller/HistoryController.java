package com.vencent.transclouddata.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vencent.transclouddata.entity.History;
import com.vencent.transclouddata.service.HistoryService;
import com.vencent.transclouddata.utils.FileUtil;
import com.vencent.transclouddata.utils.TypeConver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    /**
     * 查询用户所有翻译记录
     *
     */
    @GetMapping("/all/{pageNum}/{count}")
    public @ResponseBody
    Page<History> findAll(@PathVariable("pageNum") int pageNum, @PathVariable("count") int count, @SessionAttribute("userID") String userID){
        Page<History> page = new Page<>(pageNum,count);
        return historyService.listByUserId(userID,page);
    }
    /**
     * 音乐播放
     */
    @GetMapping("/player/{id}")
    public void player(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
       History history =  historyService.getById(id);
        String path = history.getTransSpeech();
        if (path!=null) {
            String range = request.getHeader("Range");
            String[] rs = range.split("\\=");
            range = rs[1].split("\\-")[0];
            File file = new File(path);
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            long length = file.length();
            // 播放进度
            int count = 0;
            // 播放百分比
            int percent = (int)(length * 1);
            int irange = Integer.parseInt(range);
            length = length - irange;

            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", length + "");
            response.addHeader("Content-Range", "bytes " + range + "-" + length + "/" + length);
            response.addHeader("Content-Type", "audio/mpeg;charset=UTF-8");

            int len = 0;
            byte[] b = new byte[1024];
            while ((len = fis.read(b)) != -1) {
                os.write(b, 0, len);
                count += len;
                if(count >= percent){
                    break;
                }
            }
            fis.close();
            os.close();
        }

    }
    /**
     * 保存翻译记录
     */
    @PostMapping("/save")
    public String save(@RequestBody History history, @RequestParam byte[] outBytes) throws IOException {
//        HashMap hashMap = JSONObject.parseObject(map,HashMap.class);
//        JSONObject jSONObject = (JSONObject) hashMap.get("history");
//        History history = JSONObject.toJavaObject(jSONObject,History.class);
//        JSONArray jsonArray  = (JSONArray) hashMap.get("outBytes");
//        byte[] outBytes = JSONArray.toJSONBytes(jsonArray);
        String filePath =  FileUtil.GetFilePath();
        String name = history.getTransSpeech();
        //如果是pcm格式，就转化为wav
        if(name.substring(name.lastIndexOf('.')+1).equals("pcm")){

            //根据字节数组创建文件
            FileUtil.byte2File(outBytes,filePath.substring(1),name);
            //pcm文件
            File pcmFile = new File(filePath.substring(1)+File.separator+name);
            //wav文件
            String wavName = name.substring(0,name.lastIndexOf('.'))+".wav";
            File wavFile = new File(filePath.substring(1)+ File.separator+wavName);
            if(!wavFile.exists()){
                wavFile.createNewFile();
            }
            //进行转化并保存
            TypeConver.convert2Wav(pcmFile, wavFile, 16000, 1, 16);

            //删除pcm文件及文件夹
            FileUtil.deleteFile(pcmFile);
            //保存wav路径
            history.setTransSpeech(filePath.substring(1)+ File.separator+wavName);
            //如果是mp3
        }else if(name.substring(name.lastIndexOf('.')+1).equals("mp3")){
            //保存结果音频文件
            FileUtil.save(filePath,history.getTransSpeech(),outBytes);
            //保存mp3路径
            history.setTransSpeech(filePath.substring(1)+ File.separator+name);
        }else if(name.substring(name.lastIndexOf('.')+1).equals("wav")){
            //保存结果音频文件
            FileUtil.save(filePath,history.getTransSpeech(),outBytes);
            //保存mp3路径
            history.setTransSpeech(filePath.substring(1)+ File.separator+name);
        }

        history.setTransTime(LocalDateTime.now());
        historyService.save(history);
        return null;
    }
    /**
     * 删除翻译记录
     */
    @PostMapping("/delete")
    public @ResponseBody
    String delete(@RequestBody History history){

        if(historyService.removeById(history.getId())){
            String path = history.getTransSpeech();
            File file = new File(path.substring(0,path.lastIndexOf(File.separator)));
            FileUtil.deleteFile(file);
            return "删除成功";
        }
        return "删除失败";

    }
}
