package com.vencent.transclouddata.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @description:
 * @author: vencent
 * @create 2020-04-01
 **/
public class TypeConver {

    /**
     * PCM转WAV
     * @param inPcmFile
     * @param outWavFile
     * @param sampleRate
     * @param channels
     * @param bitNum
     */
    public static void convert2Wav(File inPcmFile, File outWavFile, int sampleRate, int channels, int bitNum) {
        FileInputStream in = null;
        FileOutputStream out = null;
        byte[] data = new byte[1024];

        try {
            long byteRate = (long)(sampleRate * channels * bitNum / 8);
            in = new FileInputStream(inPcmFile);
            out = new FileOutputStream(outWavFile, false);
            long totalAudioLen = in.getChannel().size();
            long totalDataLen = totalAudioLen + 36L;
            writeWaveFileHeader(out, totalAudioLen, totalDataLen, sampleRate, channels, byteRate);
            boolean var14 = false;

            int length;
            while((length = in.read(data)) > 0) {
                out.write(data, 0, length);
            }
        } catch (Exception var27) {
            var27.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var26) {
                    var26.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException var25) {
                    var25.printStackTrace();
                }
            }

        }

    }

    private static void writeWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, int sampleRate, int channels, long byteRate) throws IOException {
        byte[] header = new byte[]{82, 73, 70, 70, (byte)((int)(totalDataLen & 255L)), (byte)((int)(totalDataLen >> 8 & 255L)), (byte)((int)(totalDataLen >> 16 & 255L)), (byte)((int)(totalDataLen >> 24 & 255L)), 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0, 0, 1, 0, (byte)channels, 0, (byte)(sampleRate & 255), (byte)(sampleRate >> 8 & 255), (byte)(sampleRate >> 16 & 255), (byte)(sampleRate >> 24 & 255), (byte)((int)(byteRate & 255L)), (byte)((int)(byteRate >> 8 & 255L)), (byte)((int)(byteRate >> 16 & 255L)), (byte)((int)(byteRate >> 24 & 255L)), (byte)(channels * 16 / 8), 0, 16, 0, 100, 97, 116, 97, (byte)((int)(totalAudioLen & 255L)), (byte)((int)(totalAudioLen >> 8 & 255L)), (byte)((int)(totalAudioLen >> 16 & 255L)), (byte)((int)(totalAudioLen >> 24 & 255L))};
        out.write(header, 0, 44);
    }
}
