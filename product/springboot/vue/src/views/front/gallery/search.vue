<template>
  <div class="search">
    <div class="search-row">
    <el-row type="flex" justify="center" style="height:50px;">
      <el-col :span="11" class="search-col">
        <el-input placeholder="请输入内容" v-model="input">
          <el-button slot="append" style="fontSize:20px;" icon="el-icon-search" @click="getdata(input)"></el-button>
        </el-input>
      </el-col>
    </el-row>
    </div>
    <div class="search-result">
      <div
        class="search-result-div"
        v-for="(img) in this.imgs"
        :key="img.id"
        :style="{width:100,height:100}"
      >
<!--        :style="{width:img.weight*200/img.height+'px',flexGrow:img.weight*200/img.height}"-->
<!--        <i :style="{paddingBottom:img.height/img.weight*100+'%'}"></i>-->
        <el-image :src="img.img" class="el-image1" lazy>
          <div slot="placeholder" class="image-slot" style="margin-top:3em">
            <svg-icon icon-file-name="loading" style="vertical-align:-5px">
            </svg-icon>加载中
          </div>
        </el-image>
      </div>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="70%">
        <div class="dia-cont">
          <img :src="diaitem.img">
        </div>
        <el-button type="text" @click="docollect(diaitem.id)">收藏</el-button>
      </el-dialog>
  </div>
</template>
<script>
export default {
  name: "search",
  data() {
    return {
      uid:localStorage.getItem('uid'),
      input:this.$route.query.keywords,
      dialogVisible: false,
      diaitem:[],
      imgs: []
    };
  },
  created(){
    this.getdata(this.input)
  },
  methods: {
    getdata(keywords){
      this.request.post('/photo/selectPhotoName',keywords)
      .then(res=>{
        this.imgs=Object.assign(res.data);
        let query = this.$router.history.current.query;
        // let path = this.$router.history.current.path;
        //对象的拷贝
        let newQuery = JSON.parse(JSON.stringify(query));
        newQuery.keywords = keywords;
        // this.$router.push({ path, query: newQuery });
        console.log(this.imgs)
      })
    },
    showdia(item) {
      this.dialogVisible = true;
      this.diaitem = item;
    },
    docollect(pid){
      this.$http.post('/api/pictureCollect',{uid:this.uid,pid:pid},{emulateJSON:true})
      .then(res=>{
        if(res.body.message=="收藏成功"){
          this.$message({
              message: "收藏成功",
              type: "success",
              customClass: "zIndex"
            });
        }else{
          this.$message({
              message: "您已经收藏",
              type: "danger",
              customClass: "zIndex"
            });
        }
      })
    },
  }
};
</script>
<style>
.search {
  min-height: 550px;
  height: auto;
  background-color: #f5f5f5;
}
.search-row {
  background-color: #fff;
  height: 60px;
  position: fixed;
  top: 47px;
  width: 100%;
  z-index: 9999;
}
.search-col {
  position: absolute;
  margin-top: 10px;
}
.search-result {
  min-height: 300px;
  height: auto;
  display: flex;
  flex-wrap: wrap;
  width: 90%;
  margin: 0 auto;
  padding-top: 80px;
}
.search-result::after {
  content: "";
  flex-grow: 999999999;
}

.search-result-div {
  margin: 2px;
  margin-left: 10px;
  width: 200px;
  height: 200px;
  /*background-color: #ededef;*/
  /*position: relative;*/
  /*display: flex;*/
  overflow: hidden;
  float: left;
}

.search-result-div i {
  display: block;
}

.search-result-div img {
  top: 0;
  width: 100%;
  height: 100%;
  vertical-align: middle;
  cursor: pointer;
  transition: all 0.6s;
  float: left;
}
.search-result-div img:hover {
  transform: scale(1.2);
}
</style>

