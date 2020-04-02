<!--  -->
<template>
    <div id="index">
        <el-header style="text-align: right; font-size: 12px">
            <span class="CSS5-left" >语音翻译系统</span>
                <span style="margin-right: 20px;font-size: 20px">{{name}}</span>
                <i class="el-icon-setting" style="margin-right: 20px;font-size: 20px" @click="setting()"></i>
                <i class="el-icon-close" style="margin-right: 10px;font-size: 20px" @click="exit()"></i>

        </el-header>
        <el-container style="height: 680px; border: 1px solid #eee">
            <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
            <el-menu router :default-openeds="['0','1','2']">
                <template v-for="(item,index) in $router.options.routes">
                <el-submenu  v-if="(index<3)" :index = "index+''" :key="index+''"  >
                    <template slot="title"><i class="el-icon-menu"></i>{{item.name}}</template>
                    <el-menu-item v-for="(item2,index2) in item.children" :index = "item2.path" :class="$route.path==item2.path?'is-active':''" :key="index2+''">{{item2.name}}</el-menu-item>
                </el-submenu>
                </template>
            </el-menu>
            </el-aside>  
        <el-container>
            <el-main>
                <router-view></router-view>
            </el-main>
        </el-container>
        </el-container>
        <div>
<el-drawer
  title="修改个人信息"
  :withHeader = "false"
  :visible.sync="dialog"
  direction="rtl"
  custom-class="demo-drawer"
  ref="drawer"
  >
  <div class="demo-drawer__content">
    <h1 style="text-align:center">修改个人信息</h1>
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" >
      <el-form-item label="用户账号" :label-width="formLabelWidth" placeholder="请输入用户账号" prop="userAccount">
        <el-input :disabled="true" v-model="ruleForm.userAccount" autocomplete="off" style="width:300px;" ></el-input>
      </el-form-item>
      <el-form-item label="用户名" :label-width="formLabelWidth" placeholder="请输入用户名" prop="username">
        <el-input v-model="ruleForm.username" autocomplete="off"  style="width:300px;"></el-input>
      </el-form-item>

      <el-form-item label="请输入新密码" :label-width="formLabelWidth" placeholder="请输入新密码" prop="password" >
        <el-input v-model="ruleForm.password" autocomplete="off" style="width:300px;" show-password></el-input>
      </el-form-item>
      <el-form-item label="请输入原密码" :label-width="formLabelWidth" placeholder="请输入原密码" prop="Oldpass" >
        <el-input v-model="ruleForm.Oldpass" autocomplete="off" style="width:300px;" show-password></el-input>
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
    data(){
        return {
        name:'',
        dialog: false,
        ruleForm: {
        id:'',
        userAccount: '',
        oldPassword:'',
        password: '',
        Oldpass: '',
        username:'',
        },
        rules: {
        userAccount: [
            { required: true, message: '请输入用户账号', trigger: 'blur' }
        ],
        username: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
            { required: true,message: '请输入新密码', trigger: 'blur' }
        ],
        Oldpass: [
            {  required: true,message: '请输入原密码', trigger: 'blur' }
        ]
        },
      formLabelWidth: '120px',
      timer: null,
        }
    },
    methods:{
        //抽屉内点击修改触发函数
        settingNow(formName) {
        const _this = this;
        
        this.$refs[formName].validate((valid) => {
          if (valid) {
            //原密码校验
            if(_this.$md5(_this.ruleForm.Oldpass) !== _this.oldPassword){
              _this.$message("原密码错误");
              return ;
            }
            axios.post("http://localhost:8050/user/user/update",this.ruleForm).then(function(response){
                  _this.$message(response.data)
                  _this.dialog = false;
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
      //点击右上方退出触发函数
      exit(){
            const _this = this;
          axios.get("http://localhost:8050/user/user/exit").then(function(response){
              _this.$router.push("/login");

          }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
      },
      //点击右上方设置触发函数
      setting(){
          const _this = this;
          axios.get("http://localhost:8050/user/user/select").then(function(response){
            _this.ruleForm.userAccount =  response.data.userAccount;
            _this.ruleForm.username = response.data.username;
            _this.oldPassword = response.data.password;
            _this.ruleForm.id = response.data.id;
            _this.dialog = true;

          }).catch(function (error) {
          if(error.response.status === 401){
            _this.$router.push("/login");
            _this.$message("请重新登录")
          }
  });
      }
    },
    created(){
      const _this = this;
       axios.get("http://localhost:8050/user/user/getUserName").then(function(response){
               _this.name = response.data;
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
    .el-header {
        background-color: rgb(101, 146, 206);
        color: #333;
        line-height: 60px;
    }
    .el-aside {
        color: #333;
    }
    .CSS5-left{  float:left; border:0px solid rgb(101, 146, 206);font-size:xx-large;}

</style>