<!--  -->
<template>
<div class= 'max'>
 <div class="box">
            <h2>语音翻译系统</h2>
 <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
  <el-form-item  label ="用户账号" placeholder="请输入用户账号" prop="userAccount">
    <el-input v-model="ruleForm.userAccount"></el-input>
  </el-form-item>
  <el-form-item  label ="密码" placeholder="请输入密码" prop="password">
    <el-input v-model="ruleForm.password" show-password></el-input>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="login('ruleForm')">登录</el-button>
    <el-button @click="dialog = true">注册</el-button>
  </el-form-item>
</el-form>
</div>
<el-drawer
  title="注册"
  :withHeader = "false"
  :visible.sync="dialog"
  direction="ltr"
  custom-class="demo-drawer"
  ref="drawer"
  >
  <div class="demo-drawer__content">
    <h1 style="text-align:center">注册</h1>
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" >
      <el-form-item label="用户账号" :label-width="formLabelWidth" placeholder="请输入用户账号" prop="userAccount">
        <el-input v-model="ruleForm.userAccount" autocomplete="off" style="width:300px;"></el-input>
      </el-form-item>
      <el-form-item label="用户名" :label-width="formLabelWidth" placeholder="请输入用户名" prop="username">
        <el-input v-model="ruleForm.username" autocomplete="off"  style="width:300px;"></el-input>
      </el-form-item>
      <el-form-item label="用户密码" :label-width="formLabelWidth" placeholder="请输入用户密码" prop="password" >
        <el-input v-model="ruleForm.password" autocomplete="off" style="width:300px;" show-password></el-input>
      </el-form-item>
      <el-form-item label="再次输入密码" :label-width="formLabelWidth" placeholder="请再次输入用户密码" prop="checkPass" >
        <el-input v-model="ruleForm.checkPass" autocomplete="off" style="width:300px;" show-password></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer">
      <el-button style="display:block;margin:0 auto" type="primary"  @click="regist('ruleForm')" >注册</el-button>
    </div>
  </div>
</el-drawer>
</div>
</template>

<style>
.max
{
width:98%;
height: 98%;
background:url("../assets/index3.jpg");
position: absolute;
background-size:cover;
}
.box
{
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%,-50%);
    width:400px;
 	padding: 40px;
    background:rgba(0,0,0,.8);
	box-sizing:border-box;
	box-shadow:0 15px 25px rgba(114, 121, 79, 0.5);
	border-radius: 10px;/*登录窗口边角圆滑*/
}
.box h2
{
	margin:0 0 30px;
	padding:0;
	color:#fff;
	text-align:center;
	}
	.box .inputBox
	{
		position:relative;
	}
.box .inputBox input
{
	width: 100%;
	padding: 10px 0;
    font-size: 16px;
	color: #fff;
	letter-spacing: 1px;
	margin-bottom:30px;/*输入框设置*/
	border:none;
	border-bottom:1px solid #fff;
	outline:none;
	background:transparent;
}
.box .inputBox label
{
    position: absolute;
    top: 0;
    left: 0;
    padding: 10px 0;
    font-size: 16px;
    color: #fff;
    pointer-events: none;
    transition: .5s;
}
.box .inputBox input:focus ~ label,
.box .inputBox input:valid ~ label
{
    top: -18px;
	left: 0;
	color: #03a9f4;
	font-size: 12px;
}
.box  input[type="submit"] 
{
	background: transparent;
	border: none;
	outline: none;
	color: #fff;
	background: #03a9f4;
	padding: 10px 20px;
	cursor: pointer;
	border-radius: 5px;
}

</style>
<script>
 export default {
    data() {
      return {
        dialog: false,
        ruleForm: {
        userAccount: '',
        password: '',
        checkPass: '',
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
            { required: true,message: '请输入密码', trigger: 'blur' }
        ],
        checkPass: [
            {  required: true,message: '请再次输入密码', trigger: 'blur' }
        ]
        },
      formLabelWidth: '120px',
      timer: null,
    };
    },
    methods: {
      //点击登录触发函数
      login(formName) {
        const _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            axios.post("http://localhost:8050/user/user/login",this.ruleForm).then(function(response){
                if(response.data !== 'success'){
                    _this.$message(response.data);
                }else{
                    _this.$router.push("/translatePage");
                }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      //点击注册触发函数
      regist(formName) {
        const _this = this;
        if(this.ruleForm.password !== this.ruleForm.checkPass){
          this.$message("两次输入密码不一致");
          return ;

        }
        this.$refs[formName].validate((valid) => {
          if (valid) {
            axios.post("http://localhost:8050/user/user/register",this.ruleForm).then(function(response){
                  _this.$message(response.data)
                  _this.dialog = false;
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
        
      }
  
    }
  }
</script>
