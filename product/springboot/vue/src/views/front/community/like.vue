<template>
  <div class="mywork">
    <el-row type="flex" justify="center" ref="top_menu">
      <el-col :span="23">
        <waterfall  class="mywork-div"  :gap="gap" :col="col" :width="itemWidth" :gutterWidth="gutterWidth" :data="imgs" @loadmore="loadmore"
                    @scroll="scroll">
          <template>
            <div class="mywork-item" v-for="(item,index) in imgs" :key="index">
              <div class="mywork-img">
                <el-image :src="item.img">
                </el-image>
              </div>
              <div class="mywork-shadow">
                <div class="mywork-det">
                  <el-button type="text" @click="show(item);getpicdetail(item.id)">查看详情</el-button>
                </div>
                <div class="mywork-line">
                  <div class="my-work-space"></div>
                  <div v-if="my" class="mywork-btn"><el-button type="text" @click="deletepic(item.id)">删除</el-button></div>
                </div>
              </div>
            </div>
          </template>
        </waterfall>
        <!-- 分页条 -->
        <div style="padding: 20px 0">
          <el-pagination
              :current-page.sync="pageNum"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :page-sizes="[5, 10, 20,30,40]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination>
        </div>
      </el-col>


    </el-row>

    <el-dialog :visible.sync="dialogVisible" width="50%" class="tj-dia">
      <div style="width: 100%;min-height: 200px;margin-top: 100px">
          <div id="a">
            <img id="b" v-for="item in picdetail" :src="item.img"/>
          </div>
      </div>
      <div class="mywork-dia-text" v-text="diaitem.descr"></div>
      <div class="tj-dia-like">
        <el-button icon="el-icon-gz-heart"></el-button>
        <span v-text="diaitem.like_num"></span>
      </div>
      <div class="mywork-tag">标签：{{diaitem.type_name}}</div>
      <div class="tj-dia-comment">
<!--        <el-input class="tj-dia-input" v-model="comment" placeholder="请输入内容"></el-input>-->
<!--        <el-button class="tj-dia-btn" type="primary" @click="addcom(diaitem.pid,diaitem.uid,comment)">评 论</el-button>-->
      </div>
      <div class="mywork-dia-tabs">

        <div class="mywork-hot" v-for="com in picdetail" :key="com.cid">
          <div class="mywork-hot-tx">
            <img :src="com.img">
          </div>
          <div class="mywork-hot-name">{{com.userNickname?com.userNickname:'注册用户'}}</div>
          <div class="mywork-coms-com">{{com.content}}</div>
        </div>
      </div>
    </el-dialog>


  </div>

</template>

<script>
export default {
  name: "like",
  data() {
    return {
      token:JSON.parse(localStorage.getItem("user")).token, //token凭证
      dialogVisible: false,
      diaitem: [],
      picdetail:[],
      activeName: "comments",
      col:5,
      gap:5,
      uid:this.$route.query.uid,
      my:this.$route.query.my,
      avatar:'http://188.131.192.194/head_images/5LSk0zVtyKDq9UciiWPab50dwjoNI2324KtwSyBD.jpeg',
      imgs:[],
      total: 0,
      pageNum: 1,
      pageSize: 10,

      //复选框
      checkAll: false,       //是否全选
      checkedImgIndex: [],   //图片索引数组
      checkedImgId: [],      //图片id数组
      isIndeterminate: false,

      //用户勾选图片，菜单的显示与隐藏
      checkedStatus: false,
      checkedCount: 0, //勾选数量
      displayTopMenu: 'true', //显示
      displayCheckedMenu: '',  //隐藏
    };
  },
  mounted(){
    this.getmywork()
  },
  computed: {
    itemWidth() {
      return 138 * 0.5 * (document.documentElement.clientWidth /400);
    },
    gutterWidth() {
      return 9 * 0.5 * (document.documentElement.clientWidth /500);
    }
  },
  methods: {
    scroll() {
    },
    loadmore() {
    },
    getmywork(){
      const formData = new FormData();
      formData.append("currentPage", this.pageNum);
      formData.append("pageSize", this.pageSize);
      formData.append("userId", this.uid);
      let requestApi = '/posts/selectAl';
      this.axios({
        url: this.$serveUrL + requestApi,
        headers: {
          'token': this.token //设置token 其中K名要和后端协调好
        },
        method: "post",
        data: formData
      })
          .then(res=>{
            console.log(res)
            if(res.data.data.total!=null){
              this.total = res.data.data.total
            }
            // this.imgs = Object.assign(res.data.records);
            this.$set(this, 'imgs',Object.assign(res.data.data.posts));
            console.log(this.imgs)
          })
    },
    getpicdetail(pid){
      console.log(pid)
      this.request.get("/posts/selectAlll",{
        params:{
          uid:this.uid,
          id:pid
        }
      }).then(res=>{
        this.picdetail=Object.assign(res.data);
        console.log(this.picdetail);
      })
    },
    deletepic(pid){
      this.request.delete("/posts/" + pid).then(res => {
        if (res.code === '200') {
          this.$message({
            message: "删除成功",
            type: "success",
            customClass: "zIndex"
          });
          this.getmywork()
        } else {
          this.$message({
            message: "删除失败",
            type: "danger",
            customClass: "zIndex"
          });
        }
      })
    },
    show(item) {
      this.dialogVisible = true;
      this.diaitem = item;
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.getmywork()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getmywork()
    },
    //根据选中的索引转化为图片的id
    handleCheckIndexToId() {
      let len = this.checkedImgIndex.length;
      this.checkedImgId = new Array(len);
      for (let i = 0; i < len; i++) {
        this.checkedImgId[i] = this.imgs[this.checkedImgIndex[i]].id;
      }
    },
    // 部分被选中,当勾选时，上方显示操作菜单
    handleCheckedImgChange(value) {
      this.checkedCount = value.length;
      this.SwitchDisplay(this.checkedCount);
      this.checkAll = this.checkedCount === this.imgs.length;
      this.isIndeterminate = this.checkedCount > 0 && this.checkedCount < this.imgs.length;
    },
    // 显示或隐藏顶部菜单和选中菜单
    SwitchDisplay(checkedCount) {
      if (checkedCount > 0) {
        // 获取顶部菜单高度，使选中菜单保持一致
        // let h = this.$refs.top_menu.$el["offsetHeight"];
        let h = 50;
        if (this.displayTopMenu != "display:none;" || this.displayTopMenu == '') {
          this.displayCheckedMenu = 'display:block;height:' + h + 'px;';
          this.displayTopMenu = 'display:none;'
        }
      }
      else {
        this.displayCheckedMenu = '';
        this.displayTopMenu = '';
      }
    },
    //全选框
    handleCheckAllChange(val) {
      if (val) {
        let len = this.imgs.length;
        this.checkedCount = len;
        this.checkedImgIndex = new Array(len);
        for (var i = 0; i < len; i++) {
          this.checkedImgIndex[i] = i;
        }
      }
      else {
        this.checkedImgIndex = [];
        this.checkedCount = 0;
      }
      this.isIndeterminate = false;
      this.checkedStatus = val;
      this.SwitchDisplay(this.checkedCount);
    },
  }
};
</script>
<style >
.mywork {
  min-height: 300px;
  height: auto;
  display: block;
  flex-wrap: wrap;
  width:auto;
  margin: 0 auto;
}
.mywork-div {
  margin: 20px auto;
  margin-left:3%;
}
.mywork-item{
  text-align: center;
  margin-bottom: 20px;
  position: relative;
}
.mywork-img img{
  width: 90%;
  height: 100%;
}

.mywork-shadow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0);
  opacity: 0;
  transition: all 0.2s;
}
.mywork-shadow:hover {
  background: rgba(0, 0, 0, 0.4);
  opacity: 1;
}
.mywork-det {
  color: #fff;
  position: absolute;
  top: 35%;
  left: 40%;
}
.mywork-det span {
  color: #fff;
}
.mywork-line{
  position: absolute;
  top: 75%;
  width: 100%;
}
.mywork-lc {
  color: #fff;
  font-size: 12px;
  display: inline-block;
}
.my-work-space{
  display: inline-block;
  width: 50%;
}
.mywork-btn {
  display: inline-block;
}
.mywork-btn span {
  color: #fff;
  font-size: 12px;
}

.el-dialog__body {
  padding-top: 0;
}
.mywork-dia-img {
  margin: 10px auto;
  width: auto;
  height: auto;
  text-align: center;
}
.mywork-dia-img img{
  width: 100%;
  height: 100%;
}
.mywork-dia-text {
  margin: 0 0 0 20px;
  display: inline-block;
  font-size: 1rem;
  color: #444;
}
.mywork-dia-tabs {
  margin-top: 40px;
  background-color: #fafafa;
  overflow: hidden;
}
.mywork-hot {
  border-bottom: 1px solid #e7e7e7;
  padding: 10px 0 10px 10px;
  font-size: 12px;
}
.mywork-hot-tx {
  display: inline-block;
  width: 30px;
  height: 30px;
  text-align: center;
}
.mywork-hot-tx img {
  width: 100%;
  height: 100%;
  vertical-align: middle;
}
.mywork-hot-name {
  display: inline-block;
  margin: 0 10px;
  color: #7594b3;
}
.mywork-coms-com {
  display: inline-block;
}
.mywork-tag{
  margin-top: 5px;
  margin-left: 10px;
}

#check_menu {
  background-color: #3174ff;
  height: 8%;
  width: 100%;
  z-index: 10;
  display: none;
  position: relative;
}

.check_menu_txt {
  font-family: MiSans, MIUI, Helvetica Neue, Helvetica, Arial, PingFang SC, Microsoft Yahei, Hiragino Sans GB, Heiti SC, WenQuanYi Micro Hei, sans-serif;
  color: #fff;
  font-size: 1.35em;
}

.check_menu_item {
  margin-left: 2em;
  margin-top: 1em;
  display: inline-block;

}

.check_menu_feature {
  float: right;
  margin-top: 1em;
  margin-right: 2.8em;
}

.check_menu_feature a {
  margin-right: 2em;
  font-size: 1.35em;
}

/*#image-content {*/
/*  margin-left: 1.6em;*/
/*  margin-top: 2em;*/
/*  width: 100%;*/
/*  height: 82%;*/
/*  overflow: scroll;*/
/*  overflow-x: hidden;*/
/*}*/

/*.image-item {*/
/*  margin: 15px 15px;*/
/*  width: 12em;*/
/*  height: 12em;*/
/*  position: relative;*/
/*  display: inline-block;*/
/*}*/

.check-box {
  display: block;
}

/*.image {*/
/*  position: absolute;*/
/*  width: 100%;*/
/*  height: 100%;*/
/*}*/

.check-box {
  margin-left: 10px;
  /*display: none;*/
  position: absolute;
}
#a{
  /*盒子的大小为内容加上边框宽度*/
  width:auto;
  height:auto;
  margin-left: 50px;
  /*border: 1px dashed red; 宽度1像素 虚线 红色*/
  /*5px 实线 黄色*/
  border-width:5px;
  border-style:solid;
  border-color:darkgrey;
}
#b{
  width: 180px;
  height:180px;
  border: 1px dashed black;
  margin: 7px;
  display:inline-block
}
</style>
<style>
.el-checkbox__inner {
  background-color: white;
  opacity: 0.9;
  border: #3174ff 1px solid;
  border-radius: 100px;
  width: 1.4em;
  height: 1.4em;
}

.el-checkbox__inner::after {
  height: 0.68em;
  left: 0.42em;
  width: 0.29em;
}

/* 增大点击区域，提高用户体验 */
.el-checkbox__inner::before {
  content: '';
  position: absolute;
  top: -0.625em;
  right: -2.5em;
  bottom: -1.875em;
  left: -0.75em;
}

/* 相册菜单 */
.el-menu--popup {
  max-height: 32em;
  overflow: auto;
  overflow-x: hidden;
}
</style>

