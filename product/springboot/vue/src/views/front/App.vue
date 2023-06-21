<template>
  <div id="app">
    <el-container>
      <el-header class="appheader">
        <myheader></myheader>
        </el-header>
      <el-main>
        <router-view @refreshUser="getUser" ></router-view>
        </el-main>
      <el-footer>
        <myfooter></myfooter>
        </el-footer>
    </el-container>
  </div>
</template>

<script>
import myheader from "../front/common/header.vue";
import myfooter from "../front/common/footer.vue";

export default {
  name: "app",
  components: {
    myheader,
    myfooter,
  },
  data(){
    return{
    }
    
  },
  methods: {
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // 从后台获取User数据
        this.request.get("/user/username/" + username).then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
        })
      }
    }
  }
};
</script>

<style scoped>

</style>
<style>
body {
  margin: 0;
}
.el-header,.el-main,.el-footer{
  padding: 0 !important;
  height: auto !important;

}
.appheader{
  margin-bottom: 49px;
  z-index: 2000;
}
.zIndex{
  z-index: 9999 !important;
}
.el-dialog {
  display: flex;
  flex-direction: column;
  margin: 0 !important;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.el-dialog .el-dialog__body {
  flex: 1;
  overflow: auto;
}
</style>
