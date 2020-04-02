package com.vencent.transcloudtranslate.utils;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlayMusic {

    public static void playMusic(byte[] pcmFile,int sampleRate,int bitNum,int channel){
        SourceDataLine auline = null;
        InputStream fis = null;
        fis = new ByteArrayInputStream(pcmFile);
        AudioFormat.Encoding encoding =  new AudioFormat.Encoding("PCM_SIGNED");
        //编码格式，采样率，每个样本的位数，声道，帧长（字节），帧数，是否按big-endian字节顺序存储
        AudioFormat format = new AudioFormat(encoding,sampleRate, bitNum, channel, 2, 8000 ,false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        auline.start();
        byte[] b = new byte[1024];
        try {
            while(fis.read(b)>0) {
                auline.write(b, 0, b.length);
                System.out.println(1);
            }
            auline.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
