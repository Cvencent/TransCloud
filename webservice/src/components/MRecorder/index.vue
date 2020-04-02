<template>
  <div class="wrapper">
    <el-row>
      <el-col>
        <icon :name="recording?'ly_s1':'ly_s0'" :class="['mrecorder', {anirecorder: recording } ]" @click.native="handleClick"/>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <span class="tip">{{ tiptext }}</span>
      </el-col>
    </el-row>
  </div>
</template>
 
<script>
import { MediaStreamRecorder } from '@/external/MediaStreamRecorder.js'
import Icon from 'vue-svg-icon/Icon.vue'
export default {
    components: {
    Icon
  },
  name: 'MRecorder',
  props: {
    timelimit: {
      default: 60, // 默认为1min
      type: Number
    },
    pushurl: {
      default: '', // 上传到服务器的路径
      type: String
    }
  },
  data() {
    return {
      mcounter: 0, // 累积时间
      recording: false, // 标记是否在录音
      intervaltimerid: '', // 间隔时间定时器编号
      tiptext: '点击录音', // 提示文字
      mediaRecorder:'', // 录音笔
      mediaConstraints: {
        audio: true
      }
    }
  },
  methods: {
    // 处理点击
    handleClick() {
      var that = this
      this.recording = !this.recording
      // 如果开始录音
      if (this.recording === true) {
        this.mcounter = 0
        this.tiptext = '录音中 ' + this.mcounter + 's'
        this.captureRecord()
        this.intervaltimerid = setInterval(() => {
          // 开始累积
          that.mcounter = that.mcounter + 1
          this.tiptext = '录音中 ' + that.mcounter + 's'
        }, 1000)
      } else { // 如果结束录音
        this.tiptext = '点击录音'
        this.mediaRecorder.stop()
        this.mediaRecorder.stream.stop()
        
      }
    },
    // 错误处理方法
    onMediaError(e) {
      console.log('阿偶~您的浏览器貌似不支持录音哦...', e)
      clearInterval(this.intervaltimerid)
      this.tiptext = '点击录音'
      this.recording = false
      this.$message.error('您的浏览器暂不支持录音功能')
    },
    // 成功
    onMediaSuccess(stream) {
      this.mediaRecorder = new MediaStreamRecorder(stream)
      // 获取音频流
      this.mediaRecorder.stream = stream
      this.mediaRecorder.mimeType = 'audio/wav'
      var _that = this
      this.mediaRecorder.ondataavailable = function(blob) {
        clearInterval(_that.intervaltimerid)
        const url = URL.createObjectURL(blob)
        _that.$emit('handleStop', {
          url: url,
          mblob: blob
        })
      }
      // 定义间隔
      this.mediaRecorder.start(this.timelimit * 1000)
    },
    // 开始记录方法
    captureRecord() {
      navigator.mediaDevices
        .getUserMedia(this.mediaConstraints)
        .then(this.onMediaSuccess)
        .catch(this.onMediaError)
    }
  }
}
</script>
<style scoped>
.wrapper {
    text-align: center;
}
 
.mrecorder {
    width:80px;
    height:80px;
    font-size:80px;
}
 
.anirecorder {
    position: relative;
    animation: mymove 5s infinite;
    -webkit-animation: mymove 5s infinite;
    animation-direction:alternate;
    animation-timing-function: ease-in-out;
    /*safari & chrome*/
    -webkit-animation-direction: alternate;
    /* -webkit-transition-timing-function: ease-in-out; */
}
 
 @keyframes mymove
{
    0%{
    transform: scale(1);  /*开始为原始大小*/
    }
    25%{
        transform: scale(1.1); /*放大1.1倍*/
    }
    50%{
        transform: scale(0.9);
    }
    75%{
        transform: scale(1.1);
    }
 
}
 
@-webkit-keyframes mymove /*Safari and Chrome*/
{
    0%{
    transform: scale(1);  /*开始为原始大小*/
    }
    25%{
        transform: scale(1.1); /*放大1.1倍*/
    }
    50%{
        transform: scale(0.9);
    }
    75%{
        transform: scale(1.1);
    }
}

</style>
