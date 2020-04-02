<!--  -->
<template>
<div>
  <div>
    <el-upload
  class="upload"
  drag
  action="http://localhost:8050/translate/translate/upload"
  multiple list-type= "['wav','pcm']">
  <i class="el-icon-upload"></i>
  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
  <div class="el-upload__tip" slot="tip">只能上传pcm/wav文件，且时长不超过60秒</div>
</el-upload>
</div>

<div class= "transbutton">
  <el-button type="primary" icon="el-icon-search" @click="translate()" :loading="loading">{{ loading ? '翻译中..' : '翻译' }}</el-button>
</div>
<div class= "resetbutton">
  <el-button type="primary" icon="el-icon-refresh-right" @click="reset()" >重置</el-button>
</div>
<div class="sourceCard" v-if = card_flag>
  <el-card class="box-card">
  <div slot="header" class="clearfix" >
  <span>结果</span>
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
</div>
<audio controls="controls"  v-if="musicflag">
  <source v-if="musicflag" :src="musicUrl" type="audio/mpeg" id="Init" ></audio>
</div>
</template>

<script>

export default {
  inject:['reload'],
   data(){
     return {
       //是否加载中
       loading:false,
       source_text:'',
       trans_text:'',
       //显示卡片flag
       card_flag :false,
       trans_id:'',
       //显示音乐条flag
       musicflag:false,
       musicUrl:"http://localhost:8050/data/history/player/",
       musicName:''
     }
   },
   methods:{
     //点击翻译触发函数
     translate(){
       const _this = this;
       _this.loading = true;
         axios.get("http://localhost:8050/translate/translate/currentInterface").then(function(response){
           const that = this;
         if(response.data !== 'success'){
           _this.$message(response.data);
            _this.loading = false;
            return;
         }
         //判断已开启翻译接口后
           axios.get("http://localhost:8050/translate/translate/trans").then(function(response){
            if(response.data === ''){
                _this.$message("未上传文件或未能正确识别语音");
                _this.loading = false;
                return;
            };
              //处理显示内容
              _this.trans_text = response.data.transTarget;
              _this.source_text = response.data.transSource;
              _this.trans_id = response.data.id;
              var index =  response.data.transSpeech.lastIndexOf('\\');
              _this.musicName =  response.data.transSpeech.substring(index+1);
              _this.card_flag = true;
              _this.loading = false;
              _this.musicUrl = _this.musicUrl+_this.trans_id;
              _this.musicflag = true;
              _this.$nextTick(function () {
                    // 此时已经渲染完成
                });
        }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
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
.resetbutton{
    width:100px;
    position:relative;
    top:120px; 
    left:300px;
    text-align: center;
}
  .transbutton {
    width:100px;
    position:relative;
    top:160px; 
    left:500px;
    text-align: center;
}
.sourceCard{
   position:relative;
    top:-360px; 
    left:700px;
}
.card {
    position:relative;
    top:-200px; 
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
