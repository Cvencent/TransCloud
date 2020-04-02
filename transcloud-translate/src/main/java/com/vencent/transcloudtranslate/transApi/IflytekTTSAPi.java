package com.vencent.transcloudtranslate.transApi;

import com.iflytek.cloud.speech.*;

import java.util.ArrayList;

/**
 * @description:
 * @author: vencent
 * @create 2020-03-22
 **/
public class IflytekTTSAPi {
    public byte[] textToSpeech(String text){
        SpeechUtility.createUtility( SpeechConstant.APPID +"=5e6c8fb3 ");
        //1.创建SpeechSynthesizer对象
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer( );
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速，范围0~100
        mTts.setParameter(SpeechConstant.PITCH, "50");//设置语调，范围0~100
        mTts.setParameter(SpeechConstant.VOLUME, "50");//设置音量，范围0~100
        //合成监听器
        SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener() {
            //progress为合成进度0~100
            public void onBufferProgress(int progress) {}
            //会话合成完成回调接口
            //uri为合成保存地址，error为错误信息，为null时表示合成会话成功
            public void onSynthesizeCompleted(String uri, SpeechError error) {}

            @Override
            public void onEvent(int eventType, int arg1, int arg2, int arg3, Object obj1, Object obj2) {
                if( SpeechEvent.EVENT_TTS_BUFFER == eventType ){
                    ArrayList<?> bufs = null;
                    if( obj1 instanceof ArrayList<?> ){
                        bufs = (ArrayList<?>) obj1;
                    }
                    if( null != bufs ){
                        for( final Object obj : bufs ){
                            if( obj instanceof byte[] ){
                                final byte[] buf = (byte[]) obj;
                            }
                        }//end of for
                    }//end of if bufs not null
                }//end of if tts buffer event
            }

        };
        //3.开始合成
        //设置合成音频保存位置（可自定义保存位置），默认保存在“./tts_test.pcm”
        mTts.synthesizeToUri(text, "tts_test.pcm",synthesizeToUriListener);
        return null;
    }

}
