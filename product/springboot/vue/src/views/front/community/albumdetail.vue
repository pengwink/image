<template>
  <div class="detail-body">
    <div class="aldetail">
      <el-row type="flex" justify="center">
        <el-col :span="23">
<!--          <div class="aldetail-head" v-if="my">-->
<!--            <input type="file" @change="changeimg($event)" ref="imgInput" style="display:none;">-->
<!--            <el-button v-if="myPhoto" size="mini" class="aldetail-head-btn" @click="uploadimg">上传图片</el-button>-->
<!--          </div>-->
          <waterfall
            class="aldetail-div"
            :col="col"
            :width="itemWidth"
            :gutterWidth="gutterWidth"
            :data="imgs"
            @loadmore="loadmore"
            @scroll="scroll"
          >
            <template>
              <div class="aldetail-item" v-for="img in imgs" :key="img.imageId">
                <div class="aldetail-img">
                  <img :src="img.img">
                </div>
                <div class="aldetail-shadow">
                  <div class="aldetail-det">
                    <el-button type="text" @click="show(img)">查看详情</el-button>
                  </div>
                  <div v-if="my" class="aldetail-line">
                    <div class="aldetail-btn">
                      <el-button type="text" @click="removeImage(img.imageId,img.albumId)">删除</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </waterfall>
        </el-col>
      </el-row>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="70%">
      <div class="colec-dia-cont">
        <img :src="diaitem.img">
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "albumdetail",
  data() {
    return {
      token:JSON.parse(localStorage.getItem("user")).token,
      myPhoto:this.$route.query.uid==JSON.parse(localStorage.getItem("user")).id?true:false,
      dialogVisible: false,
      diaitem: [],
      activeName: "comments",
      gid: this.$route.query.gid,
      uid: this.$route.query.uid,
      my: this.$route.query.my,
      col: 5,
      imgs: []
    };
  },
  created() {
    this.getalbumdetail();
  },
  computed: {
    itemWidth() {
      return 138 * 0.5 * (document.documentElement.clientWidth / 310);
    },
    gutterWidth() {
      return 9 * 0.5 * (document.documentElement.clientWidth / 400);
    }
  },
  methods: {
    changeimg(e) {
      var file = e.target.files[0];
      var image = new FormData();
      image.append("file", file);
      this.$http.post("/api/upload", image).then(res => {
        if (res.body.message == "上传成功") {
          this.galleryUpload(res.body.image, res.body.weight, res.body.height);
        }
      });
    },
    uploadimg() {
      this.$refs.imgInput.click();
    },
    galleryUpload(pos, width, height) {
      this.$http
        .post(
          "/api/galleryUpload",
          {
            uid: this.uid,
            gid: this.gid,
            position: pos,
            weight: width,
            height: height,
            description: ""
          },
          { emulateJSON: true }
        )
        .then(res => {
          if (res.body.message == "添加成功") {
            this.$message({
              message: "添加成功",
              type: "success",
              customClass: "zIndex"
            });
            this.getalbumdetail();
          } else {
            this.$message({
              message: "添加失败",
              type: "success",
              customClass: "zIndex"
            });
          }
        });
    },
    scroll() {},
    loadmore() {},
    getalbumdetail() {
      this.request
        .post("/photo/albumPhoto",this.gid )
        .then(res => {
          this.imgs = Object.assign(res.data);
          console.log(this.imgs)
        });
    },
    removeImage(imageId,albumId) {
      this.$confirm('此操作将移出相册集, 是否继续?', '移出相册', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const formData = new FormData();
        formData.append('albumId', albumId);
        formData.append('imageId', imageId);
        this.axios({
          url: this.$serveUrL + "/album/removeImageFromAlbum",
          headers: {
            'token': this.token //设置token 其中K名要和后端协调好
          },
          method: "post",
          data: formData
        }).then(function (resp) {
          console.log(resp)
          if (resp.data.code == "200") {
            this.$message({
              message: '图片移除成功！',
              type: 'success'
            });
            this.getalbumdetail();
          }
          else {
            1;
          }
        })
      })
    },
    show(item) {
      this.dialogVisible = true;
      this.diaitem = item;
    }
  }
};
</script>

<style>
.detail-body {
  min-height: 300px;
  height: auto;
  display: flex;
  width: auto;
  margin: 0 auto;
}
.aldetail-head {
  display: block;
  margin: 10px auto;
  float: right;
  position: relative;
  left: -0.8%;
}
.aldetail-head-btn {
  margin-top: 8px;
  border: #d1b200 solid 1px !important;
}
.aldetail-head-btn span {
  color: #d1b200 !important;
}
.aldetail {
  height: auto;
  width: auto;
  margin: 0 auto;
}
.aldetail-div {
  margin: 20px auto;
}
.aldetail-item {
  text-align: center;
  margin-bottom: 20px;
  position: relative;
}
.aldetail-img img {
  width: 100%;
  height: 100%;
}

.aldetail-shadow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0);
  opacity: 0;
  transition: all 0.2s;
}
.aldetail-shadow:hover {
  background: rgba(0, 0, 0, 0.4);
  opacity: 1;
}
.aldetail-det {
  color: #fff;
  position: absolute;
  top: 35%;
  left: 40%;
}
.aldetail-det span {
  color: #fff;
}
.aldetail-line {
  position: absolute;
  top: 75%;
  width: 100%;
}
.aldetail-lc {
  color: #fff;
  font-size: 12px;
  display: inline-block;
}
.my-work-space {
  display: inline-block;
  width: 50%;
}
.aldetail-btn {
  display: inline-block;
}
.aldetail-btn span {
  color: #fff;
  font-size: 12px;
}

.el-dialog__body {
  padding-top: 0;
}
.el-tabs__content {
  padding: 0 !important;
}

.detail-dia-cont {
  margin: 0 auto;
  width: auto;
  height: auto;
  text-align: center;
}
</style>