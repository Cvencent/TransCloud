<!--  -->
<template>
  <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="接口类型" prop="interfaceType">
      <el-select v-model="ruleForm.interfaceType" placeholder="请选择接口类型">
        <el-option label="百度" value="百度"></el-option>
        <el-option label="腾讯" value="腾讯"></el-option>
        <el-option label="华为" value="华为"></el-option>
        <el-option label="讯飞" value="讯飞"></el-option>
      </el-select>
  </el-form-item>
  <el-form-item label="翻译key" prop="secretId">
    <el-input v-model="ruleForm.secretId"></el-input>
  </el-form-item>
  <el-form-item label="翻译密钥" prop="secretKey">
    <el-input v-model="ruleForm.secretKey"></el-input>
  </el-form-item>
  <el-form-item label="产品ID" prop="appId" >
    <el-input v-model="ruleForm.appId" placeholder="如果对应接口没有该项则无需填写"></el-input>
  </el-form-item>
  <el-form-item label="接口区域" prop="region"  >
    <el-input v-model="ruleForm.region" placeholder="如果对应接口没有该项则无需填写"></el-input>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
    <el-button @click="resetForm('ruleForm')">重置</el-button>
  </el-form-item>
</el-form>
</template>

<script>
  export default {
    data() {
      return {
        ruleForm: {
          id: '',
          userId: '',
          interfaceType: '',
          appId: '',
          region: '',
          secretId:'',
          secretKey:'',
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
      };
    },
    methods: {
      //点击设置触发函数
      submitForm(formName) {
        const _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
           axios.post("http://localhost:8050/translate/translate/add",this.ruleForm).then(function(response){
             if(response.data === "success"){
               _this.$message("设置成功");

               //跳转页面
               _this.$router.push("/apiKeyList");
             }
              

           }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      //重置函数
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>