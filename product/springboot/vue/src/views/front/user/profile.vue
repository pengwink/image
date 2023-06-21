<template>
  <div class="pr">
    <h2 class="pr-title">基本信息</h2>
    <el-container>
      <el-main class="pr-title">
        <h5>用户名</h5>
        <el-input v-model="user.nickname" class="pr-input"></el-input>
        <h5 class="pr-title">手机号码</h5>
        <el-input v-model="user.phone" :disabled="true" class="pr-input"></el-input>
        <h5 class="pr-title">Email地址</h5>
        <el-input v-model="user.email" :disabled="true" class="pr-input"></el-input>
        <el-button class="pr-btn" @click="save">保&nbsp;存</el-button>
      </el-main>
      <el-aside width="300px" class="pr-aside">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/file/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="user.avatarUrl" :src="user.avatarUrl" class="pr-tx">
<!--          <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
        </el-upload>
        <br>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
import VDistpicker from 'v-distpicker';
export default {
  name: "profile",
  components:{
      VDistpicker
  },
   data() {
      return {
        user: {},
        uid: JSON.parse(localStorage.getItem("user")).id,
        picture: {},
        username:'',
        sex:0,
        options:[
        {
          value: 0,
          label: "保密"
        },
        {
          value: 1,
          label: "男"
        },
        {
          value:2,
          label:'女'
        }
      ],
        birth:'',
        desc:'',
        province:'',
        city:'',
        headimg:'',
        multiple:false,
        avatar:'',
        avatarurl:'',
        background:''
      }
   },
   created(){
     this.getUser()
   },
   methods: {
     getUser() {
       let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
       if (username) {
         // 从后台获取User数据
         this.request.get("/user/username/" + username).then(res => {
           // 重新赋值后台的最新User数据
           this.user = res.data
           console.log(this.user)
         })
       }
     },
     handleAvatarSuccess(res) {
       this.user.avatarUrl = res
     },
     save() {
       this.request.post("/user", this.user).then(res => {
         if (res.code === '200') {
           this.$message.success("保存成功")

           // 触发父级更新User的方法
           this.$emit("refreshUser")

           // 更新浏览器存储的用户信息
           this.getUser().then(res => {
             res.token = JSON.parse(localStorage.getItem("user")).token
             localStorage.setItem("user", JSON.stringify(res))
           })

         } else {
           this.$message.error("保存失败")
         }
       })
     },
     changehead(e){
       console.log(e)
      var file = e.target.files[0]
      var reader = new FileReader()
      var that = this
      reader.readAsDataURL(file)
      reader.onload = function(e) {
      that.user.avatar = this.result
      }
     },
   }
};
</script>

<style>
.pr {
  background-color: #fff;
  border: #bbbbbb solid 1px;
  width: 80%;
  margin: 30px auto;
}
.pr-aside {
  text-align: center;
}
.pr-tx {
  border-radius: 50%;
  width: 150px;
  height: 150px;
}
.pr-title{
    margin-left: 15px;
}
.pr-input input{
    width: 70%;
}
.pr-btn{
    width: 15%;
    margin: 50px 0 50px 0 !important;
    background-color: #D1B200 !important;
    color: #fff !important;
}
.pr-txbtn{
    margin-top: 20px !important;
    border: #C99A05 solid 1px !important;
    background-color: #fff;
}
.pr-txbtn button{
    border-radius: 5% !important;
}
.pr-txbtn span{
    color:#C99A05;
}
</style>
