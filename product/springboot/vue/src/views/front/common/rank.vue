<template>
    <div class="main">
      <div style="display: flex">
        <h1 style="margin-left:45%; height: 5%;">图片排行榜</h1>
        <el-select style="margin-left: 33%" v-model="agreeCollect" placeholder="请选择是否公开" @change="getTop">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" ></el-option>
        </el-select>
      </div>

      <el-row type="flex" justify="left">
        <div id="a">
          <el-col :span="8" v-for="(o,index) in images" :key="index" :offset="3">
            <div style="width: 100%;min-height: 200px;margin-top: 100px">
                <el-card class="card" >
                  <p v-if="index+1==1"  style="font-size: 30px;font-weight:bolder;color: red">{{index+1}}</p>
                  <p v-if="index+1==2" style="font-size: 30px;font-weight:bolder;color: #d5b105">{{index+1}}</p>
                  <p v-if="index+1==3" style="font-size: 30px;font-weight:bolder;color: yellowgreen">{{index+1}}</p>
                  <p v-if="index+1>3" style="font-size: 30px;font-weight:bolder;color: black">{{index+1}}</p>
                  <div class="gtuku-div">
                    <img style="margin:10px" :src="o.img" class="image">
                  </div>
                  <div style="padding: 14px;">
                    <span>{{o.name}}</span>
                    <br></br>
                    <p>作者：{{o.userNickname}}</p>
                    <div class="bottom clearfix">
<!--                      <p class="time">{{o.content}}</p>-->
                      <el-button type="text" class="button" @click="like(o.id)">点赞</el-button>
                      <el-button style="margin-left: 5px" type="text" class="button" @click="collectpic(o.id)">收藏</el-button>
                    </div>
                  </div>
                </el-card>
              </div>
          </el-col>
        </div>
      </el-row>
    </div>
</template>

<script>
export default {
    data() {
        return {
          uid: JSON.parse(localStorage.getItem("user")).id,
          token:JSON.parse(localStorage.getItem("user")).token, //token凭证
          images: {},
          previewImageUrL:"",
          limit:10,
          agreeCollect:0,
          options:[
            {
              value: 1,
              label: "点赞"
            },
            {
              value: 0,
              label: "收藏"
            }
          ],
            elProgress: false,
            progressText: '正在上传 1%',
            fileList: [],//图片列表
            dialogImageUrl: '',
            dialogVisible: false
        };
    },
    props: ['userprop'],
    // 监听父组件的值
    created() {

    },
    mounted() {
      this.getTop()
    },

    methods: {
      /**
       * 查询排行榜
       */
      getTop(){
        const formData = new FormData();
        formData.append("agreeCollect",this.agreeCollect)
        formData.append("limit", this.limit);
        let requestApi = '/photo/selectTop';
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
              this.images = res.data.data.images
              this.previewImageUrL = res.data.data.previewImageUrL;
              console.log(this.images)

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
    },

}
</script>
<style scoped>
.card{
  width: 90%;
  height: 80%;
  margin: 0;
  padding: 0;
}
.main {
  min-height: 541px;
  height: auto;
}

#title {
    text-align: center;
    height: 5%;
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  margin-top: 40%;
  float: right;
}

.image {
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
#a{
  /*盒子的大小为内容加上边框宽度*/
  width:100%;
  height:auto;
  margin-left: 20px;
  /*border: 1px dashed red; 宽度1像素 虚线 红色*/
  /*5px 实线 黄色*/
  border-width:5px;
  border-style:solid;
  border-color: #f9f9f9;
}
#b{
  width: 180px;
  height:180px;
  margin: 7px;
  display:inline-block
}
.el-col-8 {
   width: 30%;
}
 .el-col-offset-2 {
    /*margin-left: 5%;*/
 }
.gtuku-div {
  margin: 2px;
  margin-left: 10px;
  width: 200px;
  height: 200px;
  /*background-color: #ededef;*/
  /*position: relative;*/
  /*display: flex;*/
  overflow: hidden;
}
.gtuku-div img {
  /*position: relative;*/
  top: 0;
  width: 100%;
  height: 100%;
  vertical-align: middle;
  cursor: pointer;
  transition: all 0.6s;
  float: left;
}
.gtuku-div img:hover{
  transform: scale(1.2);
}
</style>
  