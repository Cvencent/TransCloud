<!--  -->
<template>
<div>
  <el-table
    :data="tableData"
    style="width: 100%"
    max-height="1000"
    :row-class-name="tableRowClassName">
    <el-table-column v-if="false"
      fixed
      prop="id"
      label="ID"
      width="150">
    </el-table-column>
    <el-table-column v-if="false"
      prop="userId"
      label="用户ID"
      width="150">
    </el-table-column>
    <el-table-column 
      fixed
      prop="transSource"
      label="源内容"
      width="300">
    </el-table-column>
    <el-table-column
      prop="transTarget"
      label="目标内容"
      width="400">
    </el-table-column>
    <el-table-column v-if="false"
      prop="transSpeech"
      label="识别语音"
      width="400">
    </el-table-column>
    <el-table-column 
      prop="transTime"
      label="识别时间"
      width="170">
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="350">
      <template slot-scope="scope">
        <div class="audios" v-if="scope.row.id !== ''">
    <audio controls="controls" class="musicAudio">
    <source :src="musicSrc(scope.$index, scope.row)" type="audio/mpeg" id="Init"></audio>
    </div>
        <div class="delete">
        <el-button
          @click.native.prevent="deleteRow(scope.$index, tableData) "
          type="primary"
          icon="el-icon-delete">
        </el-button>
        </div>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination
  background
  layout="prev, pager, next"
  :page-size="pageSize"
  :total="totalNum"
  @current-change="page">
</el-pagination>
  </div>
</template>

<script>
  export default {
    inject:['reload'],
    methods: {
      //点击删除触发函数
      deleteRow(index, rows,tableData) {
        const _this = this;
        axios.post('http://localhost:8050/data/history/delete',this.tableData[index]).then(function(response){
          _this.reload();
          _this.$message(response.data);
        }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
        
      },
      //显示隔行色彩
    tableRowClassName({row, rowIndex}) {
        if (rowIndex === 1) {
          return 'warning-row';
        } else if (rowIndex === 3) {
          return 'success-row';
        }
        return '';
      },
      //分页函数
      page(currentPage){
        const _this = this
        axios.get('http://localhost:8050/data/history/all/'+currentPage+'/4').then(function(response){
        _this.tableData =  response.data.records;
        //_this.tableData.transTime.replace("T"," ");
          _this.totalNum = response.data.total+1;
          _this.pageSize = response.data.size;

        }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
      },
      //创建时间处理函数
      formatTime(){
        this.tableData.transTime.replace("T"," ")
      },
      //音乐路径处理函数
      musicSrc(index, rows){
        if(rows.id!==''){
          this.currentId = rows.id;
          return this.musicUrl+rows.id
        }
        return null;
      }
    },
    data(){
      return {
        pageSize:0,
        totalNum:4,
        musicUrl:'http://localhost:8050/data/history/player/',
        currentId:'',
        tableData: [{
          id: '',
          userId: '',
          transSource: '',
          transTarget: '',
          transSpeech: '',
          transTime:''
        }]
      }
    },
    created(){
        const _this = this
        axios.get('http://localhost:8050/data/history/all/1/4').then(function(response){
          _this.tableData =  response.data.records;
         // console.log(typeof(response.data.records))
          
          _this.totalNum = response.data.total+1;
          _this.pageSize = response.data.size;
        }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
      }

  }
</script>
<style>
  .musicAudio{
        width: 250px;
        height: 50px;
  }
  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
  .audios{
     position:relative;
      top:15px; 
      left:0px;
  }
  .delete{
     position:relative;
      top:-35px; 
      left:260px;
  }
</style>