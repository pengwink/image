<template>
  <div class="collection">
    <el-row type="flex" justify="center">
      <el-col :span="23">
        <waterfall class="collec-div" :gap="gap" :col="col" :width="itemWidth" :gutterWidth="gutterWidth" :data="imgs" @loadmore="loadmore"
      @scroll="scroll">
      <template>
        <div class="collec-item" v-for="img in imgs" :key="img.id">
          <div class="collec-img">
            <img :src="img.img">
          </div>
          <div class="collec-shadow">
          <div class="collec-det">
            <el-button type="text" @click="show(img)">查看详情</el-button>
          </div>
          <div class="collec-line">
            <div class="collec-btn"><el-button type="text" @click="deletecollection(img.collectId)">取消收藏</el-button></div>
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

    <el-dialog :visible.sync="dialogVisible" width="70%">
      <div class="colec-dia-cont">
        <img :src="diaitem.img">
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "collection",
  data() {
    return {
      token:JSON.parse(localStorage.getItem("user")).token, //token凭证
      dialogVisible: false,
      diaitem: [],
      activeName: "comments",
      comment: "",
      uid:this.$route.query.uid, 
      col:5,
      gap:5,
      avatar:'http://188.131.192.194/head_images/5LSk0zVtyKDq9UciiWPab50dwjoNI2324KtwSyBD.jpeg',
      imgs:[],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      previewImageUrL: [
        'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
      ],
      previewImage:"",
    };
  },
  created(){
    this.getmycollection()
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
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.getmycollection()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getmycollection()
    },
    scroll(scrollData) {
    },
    loadmore(index) {
    },
    getmycollection(){
      const formData = new FormData();
      formData.append("currentPage", this.pageNum);
      formData.append("pageSize", this.pageSize);
      formData.append("userId", this.uid);
      let requestApi = '/photo/selectCollectImage';
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
            this.total = res.data.data.totalCount
            // this.imgs = Object.assign(res.data.records);
            this.$set(this, 'imgs',Object.assign(res.data.data.images));
            this.previewImageUrL = res.data.data.previewImageUrL;
          })
    },
    deletecollection(pid){
      this.request.delete("/collect/" + pid)
          .then(res => {
            console.log(res)
        if (res.code==200) {
          this.$message({
              message: "取消收藏成功",
              type: "success",
              customClass: "zIndex"
            });
          this.getmycollection()
        }else{
          this.$message({
              message: "取消收藏失败",
              type: "danger",
              customClass: "zIndex"
            });
        }
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
.collection {
  min-height: 300px;
  height: auto;
  width: auto;
  margin: 0 auto;
}
.collec-div {
  margin: 20px auto;
  margin-left:3%;
}
.collec-item{
  text-align: center;
  margin-bottom: 20px;
  position: relative;
}
.collec-img img{
  width: 90%;
  height: 100%;
}

.collec-shadow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0);
  opacity: 0;
  transition: all 0.2s;
}
.collec-shadow:hover {
  background: rgba(0, 0, 0, 0.4);
  opacity: 1;
}
.collec-det {
  color: #fff;
  position: absolute;
  top: 35%;
  left: 40%;
}
.collec-det span {
  color: #fff;
}
.collec-line{
  position: absolute;
  top: 75%;
  width: 100%;
}
.collec-lc {
  color: #fff;
  font-size: 12px;
  display: inline-block;
}
.my-work-space{
  display: inline-block;
  width: 50%;
}
.collec-btn {
  display: inline-block;
}
.collec-btn span {
  color: #fff;
  font-size: 12px;
}

.el-dialog__body {
  padding-top: 0;
}

.colec-dia-cont {
  margin: 20px auto;
  width: auto;
  height: auto;
  text-align: center;
}
.colec-dia-cont img{
  width: 100%;
  height: 100%;
}
</style>


