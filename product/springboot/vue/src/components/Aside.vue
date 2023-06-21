<template>
  <el-menu :defaultOpeneds="openeds" style="min-height: 100%; overflow-x: hidden"
           background-color=#20124d
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px;">
      <b style="color: white; margin-left: 5px" v-show="logoTextShow">图片后台管理系统</b>
    </div>
    <el-menu-item index="/home">
      <i class="el-icon-house"></i>
      <span slot="title">主页</span>
    </el-menu-item>
    <el-menu-item index="/user">
      <i class="el-icon-s-custom"></i>
      <span slot="title">用户管理</span>
    </el-menu-item>
    <el-submenu index="2">
        <template slot="title">
        <i class="el-icon-folder-opened"></i>
        <span slot="title">服务管理</span>
        </template>
      <el-menu-item index="/album">
        <i class="el-icon-folder-opened"></i>
        <span slot="title">相册管理</span>
      </el-menu-item>
      <el-menu-item index="/type">
        <i class="el-icon-folder-opened"></i>
        <span slot="title">类型管理</span>
      </el-menu-item>
      <el-menu-item index="/photo">
        <i class="el-icon-folder-opened"></i>
        <span slot="title">图片管理</span>
      </el-menu-item>
      <el-menu-item index="/Posts">
        <i class="el-icon-folder-opened"></i>
        <span slot="title">发布审核</span>
      </el-menu-item>
      <el-menu-item index="/Comment">
        <i class="el-icon-folder-opened"></i>
        <span slot="title">评论管理</span>
      </el-menu-item>
<!--      <el-menu-item index="/upload">-->
<!--        <i class="el-icon-folder-opened"></i>-->
<!--        <span slot="title">上传管理</span>-->
<!--      </el-menu-item>-->
      <el-menu-item index="/album1">
        <i class="el-icon-folder-opened"></i>
        <span slot="title">查看相册</span>
      </el-menu-item>
    </el-submenu>
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>
      <el-menu-item index="/file">
        <i class="el-icon-folder"></i>
        <span slot="title">文件管理</span>
      </el-menu-item>
      <el-menu-item index="/VisualdataType">
        <i class="el-icon-folder"></i>
        <span slot="title">数据可视</span>
      </el-menu-item>
      <el-menu-item index="/record">
        <i class="el-icon-folder-opened"></i>
        <span slot="title">操作记录</span>
      </el-menu-item>
    </el-submenu>

<!--    <div v-for="item in menus" :key="item.id">-->
<!--      <div v-if="item.path">-->
<!--        <el-menu-item :index="item.path">-->
<!--          <i :class="item.icon"></i>-->
<!--          <span slot="title">{{ item.name }}</span>-->
<!--        </el-menu-item>-->
<!--      </div>-->
<!--      <div v-else>-->
<!--        <el-submenu :index="item.id + ''">-->
<!--          <template slot="title">-->
<!--            <i :class="item.icon"></i>-->
<!--            <span slot="title">{{ item.name }}</span>-->
<!--          </template>-->
<!--          <div  v-for="subItem in item.children" :key="subItem.id">-->
<!--            <el-menu-item :index="subItem.path">-->
<!--              <i :class="subItem.icon"></i>-->
<!--              <span slot="title">{{ subItem.name }}</span>-->
<!--            </el-menu-item>-->
<!--          </div>-->
<!--        </el-submenu>-->
<!--      </div>-->
<!--    </div>-->
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  data() {
    return {
      // menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      openeds: ['1','2','3','4','5','6','7'], // opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
    }
  }
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: rgb(38, 52, 69) !important;
}
.el-menu-item:hover {
  background-color: rgb(38, 52, 69) !important;
}

.el-submenu__title:hover {
  background-color: rgb(38, 52, 69) !important;
}
/*解决收缩菜单文字不消失问题*/
.el-menu--collapse span {
  visibility: hidden;
}
</style>
