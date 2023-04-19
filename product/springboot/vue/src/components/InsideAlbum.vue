<template>
  <div id="main">
    <!-- 标题 -->
    <div :style="displayTopMenu" id="album_top">
      <a class="album-icon" title="返回首页" @click="$router.push({ path: 'home' });">
        <svg-icon icon-file-name="home" />
      </a>
      <span>{{ currentAlbumName }}</span>
    </div>
    <!-- 选中菜单 -->
    <div id="albumCheck_menu" :style="displayCheckedMenu">
      <div class="check_menu_item">
        <div> </div>
        <div class="check_menu_txt"><a style="vertical-align:middle;margin-right: 1.5em;"
            @click="handleCheckAllChange(false)">
            <svg-icon icon-file-name="chacha" />
          </a>已选择{{ checkedCount }}张图片</div>
      </div>
      <div class="check_menu_feature">
        <a title="全选图片" @click="handleCheckAllChange(!checkedStatus)">
          <svg-icon icon-file-name="all">
          </svg-icon>
        </a>
        <a title="移除相册" @click="removeImage()">
          <svg-icon icon-file-name="remove" />
        </a>
        <a title="下载图片" @click="downloadImage()">
          <svg-icon icon-file-name="download" />
        </a>
        <a title="修改封面" @click="setAlbumCover()">
          <svg-icon icon-file-name="set" />
        </a>
      </div>
    </div>
    <el-divider></el-divider>
    <!-- 图片区域 -->
    <div id="album-content" :style="empty1">
      <div id="album-image-content">
        <div v-for="(group, i) in images" :key="i" class="album-image-group">

          <div class="album-image-time">
            <el-checkbox :style="displayCheckedMenu" class="time-select"  @change="handleCheckPartChange(i,checkedPart)">
              </el-checkbox>
            <span class="time-txt"> {{ group.time }}</span>
          </div>
          <div class="image-item" :style="reactiveImage" v-for="(item, j) in group.image" :key="j" >
            <el-checkbox-group  v-model="checkedImgIndex" @change="handleCheckedImgChange" class="check-box" :style="displayCheckedMenu">
              <el-checkbox :label="i + ',' + j">
              </el-checkbox>
            </el-checkbox-group>
              <el-image :src="item.img" :preview-src-list="getSrcList(i, j)" class="image" lazy>
              <div slot="placeholder" class="image-slot" style="margin-top:3em">
                <svg-icon icon-file-name="loading" style="vertical-align:-5px">
                </svg-icon>加载中
              </div>
            </el-image>
          </div>
        </div>
      </div>
    </div>

    <div :style="empty2">
      <el-empty :image-size="200" description="暂无图片">
        <el-button type="primary" @click="$router.push({ name: 'upload' });">去上传</el-button>
      </el-empty>
    </div>

  </div>
</template>

<script>
export default {
  name: 'InsideAlbum',
  data() {
    return {
      token:JSON.parse(localStorage.getItem("user")).token,
      currentAlbumId: 0,
      currentAlbumName: '',
      isRouterAlive: true,
      serveUrL: this.$serveUrL,

      // 后端查询的图片数据
      images: [
        {
          time: '2021年8月31日星期一',
          image: [
            {
              imageId: '',
              img: 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
              name: '',
            },
            {
              imageId: '',
              img: 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
              name: '',
            }
          ]
        }
      ],
      previewImageUrL: [
        '',
      ],
      // 响应式布局图片大小css
      reactiveImage: {
        // margin: '0.3em',
        // width: '15em',
        // height: '20em',
      },

      //复选框
      checkAll: false,       //是否全选
      checkedImgIndex: [],   //图片索引数组
      checkedImgId: [],      //图片id数组
      isIndeterminate: false,

      //用户勾选图片，菜单的显示与隐藏
      checkedStatus: false,//是否全选
      checkedPart:true,//是否 部分选
      checkedCount: 0, //勾选数量
      displayTopMenu: 'true', //显示
      displayCheckedMenu: '',  //隐藏

      empty1: 'display:block',
      empty2: 'display:none',


      // 相册信息修改
      dialogFormVisible: false,
      form: {
        name: '',
        title: '',
        content: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      formLabelWidth: '100px'
    };
  },
  created() {

  },

  // 不是this.roueter!!!!!!!!!!!!!
  mounted() {

    this.currentAlbumId = this.$route.query.id;
    this.currentAlbumName = this.$route.query.name;
    this.selectAllImage();


  },
  methods: {

    selectAllImage() {
      var _this = this;
      const formData = new FormData();
      formData.append('albumId', this.currentAlbumId);
      this.axios({
        url: this.$serveUrL + '/album/selectAllImage',
        method: "post",
        data: formData
      }).then(function (resp) {
        if (resp.data.status == "success") {
          _this.images = resp.data.data.images;
          _this.previewImageUrL = resp.data.data.previewImageUrL;
          // console.log(resp.data.data)
          // for (var i = 0; i < _this.previewImageUrL.length; i++) {
          //   _this.previewImageUrL[i] = _this.serveUrL + _this.previewImageUrL[i]
          // }
          _this.isRouterAlive = !_this.isRouterAlive;
          //判断当前页面数据是否为空
          if (_this.images.length < 1) {
            _this.empty1 = 'display:none';
            _this.empty2 = 'display:block';
          }
          else {
            _this.empty1 = 'display:block';
            _this.empty2 = 'display:none';
          }
        }
      })

    },
    setAlbumCover() {
      this.handleCheckIndexToId();
      console.log(this.checkedImgId[0]);
      if (this.checkedImgIndex.length > 1) {

        this.$message({
          message: '您只能选择一张图片作为相册封面',
          type: 'warning'
        });
        return;
      }
      this.$confirm('是否将该图片设置为相册封面?', '设置相册封面', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {

        var _this = this;
        console.log(this.checkedImgId);
        const formData = new FormData();
        formData.append('albumId', this.currentAlbumId);
        formData.append('imageId', this.checkedImgId[0]);
        this.axios({
          url: this.$serveUrL + "/album/setAlbumCover",
          headers: {
            'token': this.token //设置token 其中K名要和后端协调好
          },
          method: "post",
          data: formData
        }).then(function (resp) {
          console.log(resp)
          if (resp.data.status == "success") {
            _this.$message({
              message: '相册封面设置成功！',
              type: 'success'
            });
            _this.handleCheckAllChange(false);
            _this.selectAllImage();
          }
          else {
            1;
          }
        })
      })
    },
    removeImage() {
      this.$confirm('此操作将移出相册集, 是否继续?', '移出相册', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.handleCheckIndexToId();
        var _this = this;
        console.log(this.checkedImgId);
        const formData = new FormData();
        formData.append('albumId', this.currentAlbumId);
        formData.append('imageId', this.checkedImgId);
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
            _this.$message({
              message: '图片移除成功！',
              type: 'success'
            });
            _this.handleCheckAllChange(false);
            _this.selectAllImage();
          }
          else {
            1;
          }
        })
      })
    },
    downloadImage() {
      var imgIndex = this.checkedImgIndex;
      var str = imgIndex[0].split(',');
      var count = 0
      for (let i = 0; i < str[0]; i++) {
        for (let j = 0; j < this.images[i].image.length; j++) {
          count++;
        }
      }
      count += parseInt(str[1]);//在previewImageUrL的位置,根据选中index排序完的顺序下载
      for (let i = 0; i < imgIndex.length; i++) {
        str = imgIndex[i].split(',');
        var imgUrl = this.previewImageUrL[count + i];
        var imgName = this.images[str[0]].image[str[1]].name;
        this.downloadPicture(imgUrl, imgName);
      }
      this.handleCheckIndexToId();
      var _this = this;
      const formData = new FormData();
      formData.append('number', this.checkedImgId.length);
      console.log(this.token)
      this.axios({
        url: this.$serveUrL + "/record/recordDownload",
        headers: {
          'token': this.token //设置token 其中K名要和后端协调好
        },
        method: "post",
        data: formData
      }).then(function (resp) {
        console.log(resp)
        if (resp.data.status == "success") {
          _this.$message({
            message: '图片下载成功！',
            type: 'success'
          });
          _this.dialogTableVisible = false;
        }
        else {
          1;
        }
      })
    },
    downloadPicture(imgSrc, name) {
      // 根据url下载
      var index = imgSrc.lastIndexOf(".");
      var ext = imgSrc.substring(index+1);
      console.log(imgSrc+"~~~~~~~~"+name)
      fetch(imgSrc).then((res) => {
        res.blob().then((blob) => {
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = name+"."+ext
          a.click()
          window.URL.revokeObjectURL(url)
        })
      })
    },

    // 其中大图预览目前总是从第一张开始,简单处理下,就是构建一个以当前图片为起始的数组
    getSrcList(x, y) {
      var count = 0;
      for (var i = 0; i < x; i++) {
        for (var j = 0; j < this.images[i].image.length; j++) {
          count++;
        }
      }

      return this.previewImageUrL.slice(count + y).concat(this.previewImageUrL.slice(0, count + y));
    },
    // 显示或隐藏顶部菜单和选中菜单
    SwitchDisplay(checkedCount) {
      if (checkedCount > 0) {
        // 获取顶部菜单高度，使选中菜单保持一致
        if (this.displayTopMenu != "display:none;" || this.displayTopMenu == '') {
          this.displayCheckedMenu = 'display:block;'
          this.displayTopMenu = 'display:none;'
        }
      }
      else {
        this.displayCheckedMenu = '';
        this.displayTopMenu = '';
      }
    },
    //部分选
    handleCheckPartChange(x,status){
      console.log(x,status);
      if(status){
        let len = this.images[x].image.length;
        console.log(len);
        this.checkedCount = len;
        this.checkedImgIndex = new Array(len);
        for(var i=0;i<len;i++){
          this.checkedImgIndex[i] = x+','+i;
        }
      }
      else {
        this.checkedImgIndex = [];
        this.checkedCount = 0;
      }
      this.isIndeterminate =false;
      this.checkedPart = !this.checkedPart;
      this.SwitchDisplay(this.checkedCount);

    },

    //全选框
    handleCheckAllChange(val) {
      if (val) {
        let len = this.images.length;
        var count = 0;
        for (var i = 0; i < len; i++) {
          for (var j = 0; j < this.images[i].image.length; j++) {
            count++;
          }
        }
        this.checkedCount = count;

        this.checkedImgIndex = new Array(count);
        count = 0;
        for (i = 0; i < len; i++) {
          for (j = 0; j < this.images[i].image.length; j++) {
            this.checkedImgIndex[count++] = i + ',' + j;
          }
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
    //根据选中的索引转化为图片的id
    handleCheckIndexToId() {
      let len = this.checkedImgIndex.length;
      let array = [];
      this.checkedImgId = new Array(len);
      for (let i = 0; i < len; i++) {
        let str = this.checkedImgIndex[i].split(',');
        array.push(this.images[str[0]].image[str[1]].imageId);
        console.log(array)

      }
      this.checkedImgId = array;
    },
    // 部分被选中,当勾选时，上方显示操作菜单
    handleCheckedImgChange(value) {
      this.checkedImgIndex.sort();

      this.checkedCount = value.length;
      this.SwitchDisplay(this.checkedCount);
      this.checkAll = this.checkedCount === this.images.length;
      this.isIndeterminate = this.checkedCount > 0 && this.checkedCount < this.images.length;
    },



  }

}


</script>

<style scoped>
/* 隐藏菜单 */
#albumCheck_menu {
  background-color: #3174ff;
  height: 9%;
  width: 100%;
  display: none;
  position: relative;
}

.check_menu_txt {
  font-family: MiSans, MIUI, Helvetica Neue, Helvetica, Arial, PingFang SC, Microsoft Yahei, Hiragino Sans GB, Heiti SC, WenQuanYi Micro Hei, sans-serif;
  color: #fff;
  font-size: 1.35em;
}

.check_menu_item {
  display: inline-block;
  position: absolute;
  top: 50%;
  left: 5%;
  transform: translate(-5%, -50%);
}

.check_menu_feature {
  right: 5%;
  position: absolute;
  top: 50%;
  transform: translate(-5%, -50%);
}

.check_menu_feature a {
  margin-right: 2em;
  font-size: 1.35em;
}

/* css */
#main {
  height: 100%;
  width: 100%;
  overflow-x: hidden;
}

#album_top {
  height: 9%;
  width: 100%;
  position: relative;
  text-align: center;
}

.album-icon {
  position: relative;
  float: left;
  left: 1%;
  top: 20%;
  width: 5%;
  height: 80%;
  font-size: 2em;
}

#album_top span {
  position: absolute;
  top: 22%;
  text-align: center;
  line-height: 1.5;
  font-size: 2em;
  color: rgba(34, 28, 28, 0.65);
  font-family: MiSans, serif;
}


/*--图片区域--*/
#album-content {
  width: 95%;
  margin-left: 5%;
  margin-top: 1%;
  height: 88%;
  overflow: scroll;
  overflow-x: hidden;
}

#album-image-content {
  width: 100%;
  height: 100%;

}

.album-image-group {
  margin-top:15px ;
}

.album-image-time {
  height:45px;
  position: relative;
  font-size: 1.5em;

  display: block;
}

/* .album-image-time:hover+.check-box {
  display: block;
} */

.time-select{
  display: none;
  position: absolute;
  top:5px;
}
.time-txt{
   position: absolute;
  top:5px;
  left:25px;
  display: block;
}


.image-item {
  margin-right: 10px;
  position: relative;
  display: flex;
  display: inline-block;
}
.image {
  height: 13em;
}
.check-box {
  width: 100%;
  margin: 5px;
  display: none;
  position: absolute;
}

.image-item:hover .check-box {
  display: block;
}


</style>


  <style>
#app {
  height: 100%;
  width: 100%;
}

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


.el-divider {
  margin: 0;
}
</style>

 <!-- <div :style="empty2">
      <el-empty :image-size="200" ></el-empty>
    </div> -->
    <!-- <el-button type="text" @click="dialogFormVisible = true" style="">
      修改相册信息
    </el-button>

    <el-dialog title="修改相册信息" :visible.sync="dialogFormVisible" width="600px">
      <el-form :model="form">
        <el-form-item label="相册名称:" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="相册标题:" :label-width="formLabelWidth">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="相册简介:" :label-width="formLabelWidth">
          <el-input v-model="form.content" autocomplete="off" type="textarea" placeholder="相册简介"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateAlbum">保 存</el-button>
      </div>
    </el-dialog> -->