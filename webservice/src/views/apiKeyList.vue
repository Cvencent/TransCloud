<!--  -->
<template>
<div>
  <el-table
    :data="tableData"
    style="width: 100%"
    max-height="1000"
    stripe
  >
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
      prop="interfaceType"
      label="接口类型"
      width="130">
    </el-table-column>
    <el-table-column
      prop="appId"
      label="产品ID"
      width="150">
    </el-table-column>
    <el-table-column
      prop="region"
      label="接口区域"
      width="180">
    </el-table-column>
    <el-table-column
      prop="secretId"
      label="用户Key"
      width="260">
    </el-table-column>
    <el-table-column
      prop="secretKey"
      label="用户密钥"
      width="330">
    </el-table-column>
    
     <el-table-column label="操作">
      <template slot-scope="scope">
        <el-tooltip :content="scope.row.currentInterface=== '1' ? '开启':'关闭'" placement="top">
        <el-switch
        inactive-value='0'
        active-value='1'
          v-model = scope.row.currentInterface
          @change="getValue(scope.$index, scope.row)"
          active-color="#13ce66"
          inactive-color="#ff4949">
        </el-switch>
        </el-tooltip>
        <el-button
          size="mini"
          @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
        <el-button
          size="mini"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
 <div>
<el-drawer
  title="修改密钥"
  :withHeader = "false"
  :visible.sync="dialog"
  direction="rtl"
  custom-class="demo-drawer"
  ref="drawer"
  >
  <div class="demo-drawer__content">
    <h1 style="text-align:center">修改密钥</h1>
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" >
      <el-form-item label="接口类型" :label-width="formLabelWidth" prop="interfaceType">
         <el-select v-model="ruleForm.interfaceType" placeholder="请选择接口类型">
        <el-option label="百度" value="百度"></el-option>
        <el-option label="腾讯" value="腾讯"></el-option>
        <el-option label="华为" value="华为"></el-option>
        <el-option label="讯飞" value="讯飞"></el-option>
      </el-select>
      </el-form-item>
      <el-form-item label="翻译key" :label-width="formLabelWidth" placeholder="请输入翻译key" prop="secretId" >
        <el-input v-model="ruleForm.secretId" autocomplete="off" style="width:300px;"></el-input>
      </el-form-item>
      <el-form-item label="翻译密钥" :label-width="formLabelWidth" placeholder="请输入翻译密钥" prop="secretKey">
        <el-input v-model="ruleForm.secretKey" autocomplete="off"  style="width:300px;"></el-input>
      </el-form-item>
      <el-form-item label="产品ID" :label-width="formLabelWidth" placeholder="请输入产品ID">
        <el-input v-model="ruleForm.appId" autocomplete="off" style="width:300px;" placeholder="如果对应接口没有该项则无需填写"></el-input>
      </el-form-item>
      <el-form-item label="接口区域" prop="region" :label-width="formLabelWidth" >
    <el-input v-model="ruleForm.region" placeholder="如果对应接口没有该项则无需填写" style="width:300px;"></el-input>
    </el-form-item>
    </el-form>
    <div class="demo-drawer__footer">
      <el-button style="display:block;margin:0 auto" type="primary"  @click="settingNow('ruleForm')" >修改</el-button>
    </div>
  </div>
</el-drawer>
        </div>
  </div>
</template>

<script>
  export default {
    inject:['reload'],
    methods: {
      //点击修改触发函数
      handleEdit(index, row) {
        this.ruleForm=  row;  
        this.dialog = true;
      },
      //点击删除触发函数
      handleDelete(index, row) {
        const _this = this;
        axios.post('http://localhost:8050/translate/translate/delete',row).then(function(response){
          _this.reload();
          _this.$message(response.data);
        }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
      },
      //抽屉内点击修改触发函数
      settingNow(formName){
        const _this = this;
        axios.post('http://localhost:8050/translate/translate/update',this.ruleForm).then(function(response){
          _this.dialog = false;
          _this.reload();
          _this.$message(response.data);
        }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });

      },
      //分页函数
      page(currentPage){
        const _this = this
        axios.get('http://localhost:8050/translate/translate/all/'+currentPage+'/10').then(function(response){
        _this.tableData =  response.data.records;
          _this.totalNum = response.data.total;
          _this.pageSize = response.data.size;

        }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
      },
      //开关触发函数
      getValue(index,row){
        const _this = this;
        var changeOne = row.id;
        var changeTwo = 'null';
        //如果是要开启
        if(row.currentInterface === '1'){
          for(var i = 0;i<_this.totalNum;i++){
            if(_this.tableData[i].currentInterface === '1'&&_this.tableData[i].id !== changeOne){
             changeTwo = _this.tableData[i].id;
            }
        }
      }
      axios.post('http://localhost:8050/translate/translate/changeState/'+changeOne+"/"+changeTwo).then(function(response){
        _this.reload();
        _this.$message(response.data);

      }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
    }
    },
    data(){
      return {
        pageSize:0,
        totalNum:5,
        dialog: false,
        formLabelWidth: '120px',
        tableData: [{
          id: '',
          userId: '',
          interfaceType: '',
          appId: '',
          region: '',
          secretId:'',
          secretKey:'',
          currentInterface:'',
        }],
        ruleForm: {
          id: '',
          userId: '',
          interfaceType: '',
          appId: '',
          region: '',
          secretId:'',
          secretKey:'',
          currentInterface:'',
        },
          rules: {
          secretId: [
            { required: true, message: '请输入翻译key', trigger: 'blur' }
          ],
          secretKey: [
            { required: true, message: '请输入翻译密钥', trigger: 'blur' }
          ],
          appId: [
            { required: false, message: '请选择时间', trigger: 'blur' }
          ],
          interfaceType: [
            { required: true, message: '请选择接口类型', trigger: 'change' }
          ]
        }
      }
    },
    created(){
        const _this = this
        axios.get('http://localhost:8050/translate/translate/all/1/10').then(function(response){
          _this.tableData =  response.data.records;
          _this.totalNum = response.data.total;
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
  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
</style>