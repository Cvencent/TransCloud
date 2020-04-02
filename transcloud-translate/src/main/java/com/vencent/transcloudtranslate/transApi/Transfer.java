package com.vencent.transcloudtranslate.transApi;


import javax.sound.sampled.*;

import java.io.*;

public class Transfer {

    /*public static byte[] Mp3ToPcm(File file){
        AudioInputStream mp3audioStream = null;
        byte[] bytes = null;
        try {
            MpegAudioFileReader mp = new MpegAudioFileReader();
            //获取原始音频流
            mp3audioStream =  mp.getAudioInputStream(file);

            //获取原始音频属性
            AudioFormat audioFormat = mp3audioStream.getFormat();
            //构建新的音频属性
            AudioFormat newFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16,
                    audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);

            //构建转化后的音频输入流
            AudioInputStream newAudioInputStream = AudioSystem.getAudioInputStream(newFormat, mp3audioStream);

            //生成转化后文件
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //生成pcm文件
            AudioSystem.write(newAudioInputStream, AudioFileFormat.Type.WAVE, byteArrayOutputStream);
            bytes =  byteArrayOutputStream.toByteArray();
            //关闭流
            byteArrayOutputStream.close();

        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return bytes;
    }*/
    public static void Mp3ToPcm(File mp3) throws Exception{
        File pcm = new File("111.pcm");
        //原MP3文件转AudioInputStream
        AudioInputStream mp3audioStream = AudioSystem.getAudioInputStream(mp3);
        //将AudioInputStream MP3文件 转换为PCM AudioInputStream
        AudioInputStream pcmaudioStream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, mp3audioStream);
        //准备转换的流输出到OutputStream
        OutputStream os = new FileOutputStream(pcm);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead=pcmaudioStream.read(buffer, 0, 8192))!=-1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        pcmaudioStream.close();
    }
}
