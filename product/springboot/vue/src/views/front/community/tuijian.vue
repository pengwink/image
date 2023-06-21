<template>
  <div class="tj">
    <el-row type="flex" justify="center">
      <el-col :span="23">
        <waterfall class="tj-div" :col="col" :width="itemWidth" :gutterWidth="gutterWidth" :data="imgs"
                   @loadmore="loadmore"
                   @scroll="scroll">
          <template>
            <div class="cell-item" v-for="img in imgs" :key="img.id">
              <img :src="img.img" class="tj-img" @click="show(img)">
              <div class="item-body">
                <div class="tj-desc">{{ img.descr }}</div>
                <el-row type="flex" class="tj-footer">
                  <el-col :xs="4" :sm="4" :md="4" :lg="4" :xl="4" class="tj-tx">
                    <img :src="img.headImage" @click="others(img.userId)">
                  </el-col>
                  <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6" class="tj-name">
                    <span @click="others(img.userId)">{{ img.username ? img.username : '注册用户' }}</span>
                  </el-col>
                </el-row>
                <div class="tj-desc">
                  <el-tag v-for="category in img.pcategories">{{ category.category }}</el-tag>
                </div>
              </div>
            </div>
          </template>
        </waterfall>
      </el-col>
    </el-row>
    <!--弹出对话框-->
    <el-dialog :visible.sync="dialogVisible">
      <div class="tj-diahead">
        <div class="tj-diahead-tx">
          <img :src="useritem.avatarUrl" @click="others(diaitem.uid)">
        </div>
        <div class="tj-diahead-name" @click="others(diaitem.uid)">
          {{ useritem.username ? useritem.username : '注册用户' }}
        </div>
        <!--        <el-button size="medium" class="tj-diahead-btn" type="success" @click="follow(diaitem.uid)">关注</el-button>-->
      </div>
      <div id="a">
        <div class="mywork-item" v-for="(item,index) in picdetail" :key="index">
          <div class="mywork-img">
            <el-image id="b" :src="item.img" fit="cover"></el-image>
          </div>
          <div class="mywork-shadow">
            <div class="mywork-det">
            </div>
            <div class="mywork-line">
              <div class="mywork-lc">
                <el-button v-if="item.isAgree==1" icon="el-icon-circle-plus" @click="like(item.id)"></el-button>
                <el-button v-else icon="el-icon-circle-plus-outline" @click="like(item.id)"></el-button>
              </div>
              <div class="my-work-space"></div>
              <div class="mywork-btn">
                <el-button v-if="item.isCollect == 1" icon="el-icon-star-on" @click="collectpic(item.id)"></el-button>
                <el-button v-else icon="el-icon-star-off" @click="collectpic(item.id)"></el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="tj-dia-text" v-text="diaitem.description"></div>
      <div class="tj-dia-tabs">
        <div class="tj-dia-comment">
          <el-input class="tj-dia-input" v-model="commentForm.content" placeholder="请输入内容"></el-input>
          <el-button class="tj-dia-btn" type="primary" @click="addcom(diaitem.id)">评 论
          </el-button>
        </div>
        <!--        评论列表-->
        <div class="tj-hot" v-for="com in comments" :key="com.id">
          <div>
            <div class="tj-hot-tx">
              <img :src="com.avatarUrl">
            </div>
            <div class="tj-hot-name">{{ com.nickname ? com.nickname : '注册用户' }}</div>
            <div class="tj-coms-com">{{ com.content }}</div>
            <div style="display: flex;margin-top: 10px;">
              <div style="width: 200px;">
                <i class="el-icon-time"><span>{{ com.time }}</span></i>
              </div>
              <div style="text-align: right;flex: 1">
                <el-button style="margin-left:5px" type="text" @click="handleReply(com.id)">回复</el-button>
                <el-button style="color:red" type="text" @click="del(com.id)" v-if="uid===com.userId">删除</el-button>
              </div>
            </div>
          </div>
          <!--          回复列表-->
          <div v-if="com.children.length" style="padding-left: 50px">
            <div v-for="subItem in com.children" :key="subItem.id" style="background-color: #F2F2F2;padding:5px 10px">
              <div>
                <b>@{{ subItem.pnickname }}</b>
              </div>
              <div class="tj-hot-tx">
                <img :src="subItem.avatarUrl">
              </div>
              <b style="margin-left: 5px;;margin-right: 10px">{{ subItem.nickname }}</b>
              <div class="tj-coms-com">{{ subItem.content }}</div>
              <div style="display: flex;margin-top: 5px;">
                <div style="width: 200px;">
                  <i class="el-icon-time"><span>{{ subItem.time }}</span></i>
                </div>
                <div style="text-align: right;flex: 1">
                  <el-button style="margin-left:5px" type="text" @click="handleReply(subItem.id)">回复</el-button>
                  <el-button style="color:red" type="text" @click="del(subItem.id)" v-if="uid===subItem.userId">删除
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <el-dialog title="回复" :visible.sync="vis" width="50%">
      <el-form label-width="80px" size="small">
        <el-form-item label="回复内容">
          <el-input type="textarea" v-model="commentForm.contentReply" autocomplete="off">

          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="vis = false">取 消</el-button>
        <el-button type="primary" @click="addcom(diaitem.id);vis = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>
<script>

export default {
  name: "tuijian",
  components: {},
  data() {
    return {
      uid: JSON.parse(localStorage.getItem("user")).id,
      token: JSON.parse(localStorage.getItem("user")).token, //token凭证
      isAgree:0,
      isCollect:0,
      dialogVisible: false,
      comments:[],
      commentForm:{},
      col: 4,
      avatar: 'http://188.131.192.194/head_images/5LSk0zVtyKDq9UciiWPab50dwjoNI2324KtwSyBD.jpeg',
      imgs: [],
      diaitem: [],
      picdetail: [],
      useritem: [],
      likeFlag: true,
      collectFlag: true,
      vis:false
    };
  },
  created() {
    this.getData();
  },
  computed: {
    itemWidth() {
      return 138 * 0.5 * (document.documentElement.clientWidth / 320);
    },
    gutterWidth() {
      return 9 * 0.5 * (document.documentElement.clientWidth / 300);
    }
  },
  methods: {
    del(id){
      this.request.delete("/comment/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.loadComment(this.diaitem.id)
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleReply(pid){
      this.commentForm={pid:pid}
      this.vis=true
    },
    loadComment(pid){
      this.request.get("/comment/tree/"+pid).then(res => {
        this.comments = res.data;
        console.log(res);
      })
    },
    others(uid) {
      this.$router.push({path: "/community/others", query: {my: false, uid: uid}})
    },
    scroll() {
    },
    loadmore() {
    },
    getData() {
      this.request.get("/posts/selectAll")
          .then(res => {
            this.$set(this, 'imgs', Object.assign(res.data));
          })
    },
    getuserinfo(uid) {
      this.request.get("/user/" + uid)
          .then(res => {
            this.useritem = Object.assign(res.data);
            console.log(this.useritem)
            this.user = res.data
            this.username = res.data.nickname;
            this.txurl = res.data.avatarUrl;
          })

    },
    getpicdetail(pid) {
      this.request.get("/posts/selectAlll", {
        params: {
          id: pid,
          uid: this.uid
        }
      }).then(res => {
        this.picdetail = Object.assign(res.data);
        console.log(res);
      })
      this.loadComment(pid)

    },
    addcom(pid) {
      if (!this.uid){
        this.$message.warning("请登录后操作")
      }
      if (this.commentForm.contentReply){
        this.commentForm.content= this.commentForm.contentReply
      }
      this.commentForm.postsId = pid;
      this.commentForm.userId = this.uid
      this.request.post("/comment", this.commentForm)
          .then(res => {
            if (res.code == '200') {
              this.commentForm = {}
              this.$message.success("评论成功")
              this.getpicdetail(pid)
            } else {
              this.$message.error(res.msg)
            }
          })
    },
    like(pid) {
      this.dialogVisible = false;
      console.log(pid)
      var formData = new FormData();
      formData.append('uid', this.uid)
      formData.append('pid', pid)
      this.axios({
        url: this.$serveUrL + "/agree/save",
        headers: {
          'token': this.token, //设置token 其中K名要和后端协调好
          'Content-Type': 'multipart/form-data'
        },
        method: "post",
        data: formData,
      }).then(res => {
        console.log(res)

        // this.show(img)
        // this.isAgree = this.isAgree ==1?0:1
            if (res.data.code == '取消点赞') {
              this.$message({
                message: "取消点赞",
                type: "success",
                customClass: "zIndex"
              })
            } else if (res.data.code == 200) {
                this.$message({
                  message: "点赞成功",
                  type: "success",
                  customClass: "zIndex"
                })
              }
              else {
                this.$message({
                message: "点赞失败",
                type: "danger",
                customClass: "zIndex"
              })
            }
          })
    },
    collectpic(pid) {
      this.dialogVisible = false;
      var formData = new FormData();
      formData.append('uid', this.uid)
      formData.append('pid', pid)
      this.axios({
        url: this.$serveUrL + "/collect/save",
        headers: {
          'token': this.token, //设置token 其中K名要和后端协调好
          'Content-Type': 'multipart/form-data'
        },
        method: "post",
        data: formData,
      }).then(res => {
        // this.getpicdetail(pid)
        // this.isCollect = this.isCollect ==1?0:1
        console.log(res);
        if (res.data.code == 200) {
          this.$message({
            message: "收藏成功",
            type: "success",
            customClass: "zIndex"
          })
        }else if(res.data.code= "取消点赞"){
          this.$message({
            message: "取消收藏",
            type: "success",
            customClass: "zIndex"
          })
        }
        else {
          this.$message({
            message: "收藏失败",
            type: "danger",
            customClass: "zIndex"
          })
        }
      })
    },
    show(item) {
      if (this.uid) {
        this.dialogVisible = true;
        this.diaitem = item;
        console.log(item)
        this.getuserinfo(item.userId)
        this.getpicdetail(item.id)
      } else {
        this.$message({
          message: "您还未登录",
          type: "warning",
          customClass: "zIndex"
        })
      }

    }
  }
};
</script>
<style scope>
.tj {
  background-color: #ededef;
  width: 100%;
  min-height: 500px;
  height: auto;
  overflow: hidden;
}

.tj-div {
  margin: 20px auto;
}

.cell-item {
  text-align: center;
  background: #fff;
  border: #bbbbbb solid 1px;
  margin-bottom: 20px;
}

.tj-img {
  margin-top: 0;
  width: 100%;
  height: 80%;
  cursor: pointer;
}

.tj-desc {
  margin: 5px 0 5px 10px;
  text-align: left;
  font-size: 14px;
  height: auto;
  word-wrap: break-word;
}

.tj-footer {
  border-top: 1px solid #F2F2F2;
}

.tj-tx {
  width: 35px;
  height: 35px;
  margin: 10px 10px 10px 20px;
  vertical-align: middle;
}

.tj-tx img {
  width: auto;
  height: 100%;
  border-radius: 50%;
  vertical-align: middle;
  cursor: pointer;
}

.tj-name {
  color: #9E7E6B;
  font-size: 14px;
  display: table-cell;
  vertical-align: middle;
  line-height: 55px;
  cursor: pointer;
}

.tj-card-foot-btn button {
  padding: 0;
  border: none;
  margin-top: 25%;
}

.el-dialog__body {
  padding-top: 0;
}

.tj-diahead {
  height: 50px;
  margin-bottom: 20px;
  /*display: flex;*/
  /*!*justify-content: space-between;*!*/
  /*!*align-items: center;*!*/
  /*float: left;*/
  /*padding: 0;*/
  /*margin: 0;*/
  /*align-items: center;*/
  /*justify-content: center;*/
  /*!*width: 120px;*!*/
}

.tj-diahead-tx {
  width: 50px;
  height: 50px;
  display: inline-block;
  text-align: center;
}

.tj-diahead-tx img {
  /*width: 100%;*/
  height: 100%;
  vertical-align: middle;
  cursor: pointer;
}

.imag {
  /*border: #bbbbbb solid 1px;*/
  width: 200px;
  height: 200px;
  /*object-fit: cover;*/
  margin-right: 20px;
}

.tj-diahead-name {
  width: auto;
  margin: auto 30px;
  font-size: 20px;
  text-align: center;
  display: inline-block;
  cursor: pointer;
}

.tj-diahead-btn {
  display: inline-block;
}

.tj-dia-cont {
  display: flex;
  /*justify-content: space-between;*/
  /*align-items: center;*/
  /*float: left;*/
  overflow-y: scroll;
  padding: 0;
  margin: 0;
  align-items: center;
  /*justify-content: center;*/
}

/*.tj-dia-cont img{*/
/*  width: 100%;*/
/*  height: 100%;*/
/*}*/
.tj-dia-text {
  margin: 10px 0 0 0px;
  display: inline-block;
  font-family: "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", tahoma, arial,
  simsun, "宋体";
  font-size: 1rem;
  color: #444;
}

.tj-dia-tabs {
  margin-top: 40px;
  background-color: #fafafa;
  overflow: hidden;
}

.tj-dia-like {
  font-size: 15px;
  text-align: center;
  height: 25px;
  width: 70px;
  margin-top: 10px;
  float: right;
}

.tj-dia-like button {
  border: none;
  padding: 0;
}

.tj-dia-like span {
  line-height: 15px;
  margin-left: 10px;
  display: inline-block;
  position: relative;
  top: -29%;
}

.tj-hot {
  border-bottom: 1px solid #e7e7e7;
  padding: 10px 0 10px 10px;
  font-size: 12px;
}

.tj-hot-tx {
  display: inline-block;
  width: 30px;
  height: 30px;
  text-align: center;
}

.tj-hot-tx img {
  width: 100%;
  height: 100%;
  vertical-align: middle;
}

.tj-hot-name {
  display: inline-block;
  margin: 0 10px;
  color: #7594b3;
}

.tj-coms-com {
  display: inline-block;
}

.tj-dia-comment {
  margin: 10px auto;
  width: 300px;
}

.tj-dia-input {
  display: inline-block;
  width: 200px;
}

.tj-dia-btn {
  display: inline-block;
  margin-left: 10px;
}


#a {
  /*盒子的大小为内容加上边框宽度*/
  position: relative;
  display: inline-block;
  width: auto;
  height: auto;
  margin-left: 50px;
  margin-right: 50px;
  /*border: 1px dashed red; 宽度1像素 虚线 红色*/
  /*5px 实线 黄色*/
  border-width: 5px;
  border-style: solid;
  border-color: darkgrey;
}

#b {
  position: relative;
  width: 180px;
  height: 180px;
  border: 1px dashed black;
  margin: 7px;
  display: inline-block
}

.like-button {
  position: absolute;
  top: 10px;
  right: 10px;
}

.text-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
}

.text-overlay::before {
  content: "";
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.mywork-item {
  margin-top: 20px;
  /*text-align: center;*/
  position: relative;
}

.mywork-img img {
  width: 90%;
  height: 100%;
}

.mywork-shadow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(220, 220, 220, 0);
  opacity: 0;
  transition: all 0.2s;
}

.mywork-shadow:hover {
  background: rgba(0, 0, 0, 0.1);
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

.mywork-line {
  position: absolute;
  top: 75%;
  width: 100%;
}

.mywork-lc {
  font-size: 12px;
  display: inline-block;
}

.my-work-space {
  display: inline-block;
  width: 50%;
}

.mywork-btn {
  display: inline-block;
}

.mywork-btn span {
  font-size: 12px;
}

/* 使顶部进行吸顶 */
.top {
  position: sticky;
  position: -webkit-sticky;
  top: 0px;
}
/* 表单大小设置 */
.el-dialog {
  position: absolute;
  margin: 0 auto !important;
  height: 80%;
  overflow: hidden;

}

.el-dialog__body {
  position: absolute;
  left: 0;
  top: 54px;
  bottom: 70px;
  right: 0;
  padding: 0;
  z-index: 1;
  overflow: hidden;
  overflow-y: auto;
}
/**表单 确定和取消 按钮的位置 */
.el-dialog__footer {
  position: absolute;
  /*left: 0;*/
  /*right: 0;*/
  bottom: 0;
}
/* 表单输入框的大小 */
.el-input {
  width: 50%;
}

/* 弹出框滚动条 */
/* 设置滚动条的样式 */
/**解决了滚动条之间发生错位的现象 */
::-webkit-scrollbar {
  width: 10px !important;
  height: 10px !important;
  border-radius: 5px;
}
::-webkit-scrollbar-thumb {
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.2);
  /* 滚动条的颜色 */
  background-color: #e4e4e4;
}



</style>

