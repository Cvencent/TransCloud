<template>
<div>
    <div>
    <MRecorder @handleStop="handelEndRecord"  class="recorder"/>
    </div>
    <div class="sourceAudio">
        <audio controls="controls" v-if="sourceflag">
            <source :src="msource" v-if="sourceflag" type="audio/mpeg" >
        </audio>
    </div>
    <div class= "transbutton">
  <el-button type="primary" icon="el-icon-search" @click="translate()" :loading="loading">{{ loading ? '翻译中..' : '翻译' }}</el-button>
</div>
<div class= "resetbutton">
  <el-button type="primary" icon="el-icon-refresh-right" @click="reset()" >重置</el-button>
</div>
<div class="sourcecard" v-if = card_flag>
  <el-card class="box-card">
  <div slot="header" class="clearfix" >
  <span>源内容</span>
  </div>
       {{source_text}}
</el-card>
</div>
<div class="card" v-if = card_flag>
  <el-card class="box-card">
  <div slot="header" class="clearfix" >
  <span>结果</span>
  </div>
       {{trans_text}}
</el-card>
</div >
<audio controls="controls" controlsList="nodownload" v-if="musicflag">
  <source v-if="musicflag" :src="musicUrl" type="audio/mpeg" id="Init" ></audio>
</div>
</template>

<script>
import MRecorder from '@/components/MRecorder';
export default {
  inject:['reload'],
components: {MRecorder},
data() {
return {
    msource:'',
    //显示卡片flag
    card_flag:false,
    source_text:'',
    trans_text:'',
    //是否加载中
    loading:false,
    //源音乐条flag
    sourceflag:false,
    file:null,
    //目标音乐条flag
    musicflag:false,
    musicUrl:"http://localhost:8050/data/history/player/",
    musicName:''
};
},
methods: {
  //点击录音触发函数
handelEndRecord(param){
    this.msource = param.url
    this.sourceflag = true;
    this.file = param.mblob;
},
//点击翻译触发函数
translate(){
       const _this = this;
       if(this.file === null){
           this.$message("请先录音");
           return;
       }
        this.loading = true;
        let formData = new window.FormData();
       formData.append("file",this.file);
       axios.post("http://localhost:8050/translate/translate/transBlob",formData).then(function(response){
           if(response.data === ''){
               _this.$message("未能正确识别语音");
               _this.loading = false;
               return;
           }
           //处理显示内容
         _this.source_text = response.data.transSource;
         _this.trans_text = response.data.transTarget;
         _this.trans_id = response.data.id;
         //截取出文件名
         var index =  response.data.transSpeech.lastIndexOf('\\');
        _this.musicName =  response.data.transSpeech.substring(index+1);
              _this.card_flag = true;
              _this.loading = false;
              _this.musicUrl = _this.musicUrl+_this.trans_id;
              _this.musicflag = true;
              _this.$nextTick(function () {
                    // 此时已经渲染完成
                })
       }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
     },
      reset(){
       this.loading = false;
       this.card_flag = false;
       this.musicflag = false;
       //this.reload();
     }
}
}

</script>
<style>
.audioSource{
            width: 200px;
            height: 200px;
            position: absolute;
            right: 200px;
            bottom: 200px;
}
.resetbutton{
    width:100px;
    position:relative;
    top:120px; 
    left:300px;
    text-align: center;
}
.sourceAudio{
     position:relative;
      top:120px; 
      left:0px;
  }
.recorder{
     position:relative;
      top:60px; 
      left:-500px;
  }

.transbutton {
    width:100px;
    position:relative;
    top:160px; 
    left:480px;
    text-align: center;
}
.sourcecard {
    position:relative;
    top:-340px; 
    left:700px;
}
.card {
    position:relative;
    top:-140px; 
    left:700px;
}

.text {
    font-size: 14px;
  }

  .item {
    padding: 18px 0;
  }

  .box-card {
    width: 480px;
  }
</style>