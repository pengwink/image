<template>
  <div class="wrapper">
    <div style="height: 60px; line-height: 60px; font-size: 20px; padding-left: 50px; color: white;
      background-color: rgba(0,0,0,0.2)">图片管理系统</div>
    <div class="regist-form" >
      <div style="margin: 20px 0; text-align: center; font-size: 30px;color: #fff"><b>注 册</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item  prop="phone">
          <el-input
              placeholder="请输入手机号码"
              v-model="user.phone"
              size="medium"
          ></el-input>
        </el-form-item>
        <el-form-item  prop="email">
          <el-input
              placeholder="请输入邮箱地址"
              v-model="user.email"
              size="medium"
          ></el-input>
        </el-form-item>
        <el-form-item prop="username">
          <el-input placeholder="请输入账号" size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="success" plain size="small"  autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
          <el-button type="success" plain size="small"  autocomplete="off" @click="login">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
let validateEmail = (rule, value, callback) => {
  if (!value) {
    return callback(new Error("邮箱不能为空！"));
  }
  else {
    const reg=/^[1-9][0-9]{4,}@qq.com$/
    if(reg.test(value)){
      callback();
    }else {
      return callback(new Error("邮箱格式不正确！"));
    }
  }
};
var checkPhone = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('手机号不能为空'));
  } else {
    //验证手机号
    const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
    //验证区号
    const phoneReg = /^\d{3}-\d{8}|\d{4}-\d{7}$/
    console.log(reg.test(value));
    if (reg.test(value)||phoneReg.test(value)) {
      callback();
    } else {
      return callback(new Error('请输入正确的联系电话'));
    }
  }
}
export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        email: [{ required: true, validator: validateEmail, trigger: "blur" }],
        phone: [{ required: true, validator: checkPhone, trigger: "blur" }],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          console.log(this.user)
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("注册成功")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper{
  background-image: url(../assets/bg.jpg);
  height: 100%;
  width: 100%;
  background-color: #2d3a4b;
  position: fixed;
  background-size: 100%;
}
.regist-form {
  position: relative;
  width: 520px;
  top: 20%;
  max-width: 100%;
  padding: 35px 50px 10px;
  margin: 0 auto;
  background-color: #2d3a4b;
  border-radius: 10px;
  opacity: 0.8;
}

</style>
