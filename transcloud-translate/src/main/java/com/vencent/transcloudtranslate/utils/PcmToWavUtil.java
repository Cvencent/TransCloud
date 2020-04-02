package com.vencent.transcloudtranslate.utils;

import java.io.*;

public class PcmToWavUtil {

    public static byte[] convert2Wav(byte[] pcmFile, int sampleRate, int channels, int bitNum) {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        byte[] result = new byte[0];
        byte[] data = new byte[1024];

        try {
            long byteRate = sampleRate * channels * bitNum / 8;
            in = new ByteArrayInputStream(pcmFile);
            out = new ByteArrayOutputStream();
            
            long totalAudioLen = pcmFile.length;
            long totalDataLen = totalAudioLen + 36L;
            writeWaveFileHeader(out, totalAudioLen, totalDataLen, sampleRate, channels, byteRate);
            int length;
            while((length = in.read(data)) > 0) {
                out.write(data, 0, length);
            }
            result = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return result;
    }



    /*
    生成wav头信息
     */
    private static void writeWaveFileHeader(OutputStream out, long totalAudioLen, long totalDataLen, int sampleRate, int channels, long byteRate) throws IOException {
        byte[] header = new byte[]{82, 73, 70, 70, (byte)((int)(totalDataLen & 255L)), (byte)((int)(totalDataLen >> 8 & 255L)), (byte)((int)(totalDataLen >> 16 & 255L)), (byte)((int)(totalDataLen >> 24 & 255L)), 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0, 0, 1, 0, (byte)channels, 0, (byte)(sampleRate & 255), (byte)(sampleRate >> 8 & 255), (byte)(sampleRate >> 16 & 255), (byte)(sampleRate >> 24 & 255), (byte)((int)(byteRate & 255L)), (byte)((int)(byteRate >> 8 & 255L)), (byte)((int)(byteRate >> 16 & 255L)), (byte)((int)(byteRate >> 24 & 255L)), (byte)(channels * 16 / 8), 0, 16, 0, 100, 97, 116, 97, (byte)((int)(totalAudioLen & 255L)), (byte)((int)(totalAudioLen >> 8 & 255L)), (byte)((int)(totalAudioLen >> 16 & 255L)), (byte)((int)(totalAudioLen >> 24 & 255L))};
        
        out.write(header, 0, 44);
    }
}
