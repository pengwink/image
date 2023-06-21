<template>
  <el-container class="fb">
    <el-main class="fb-main">
      <el-upload  class="fb-upload"
                  action="#" ref="upload"
                  list-type="picture-card"
                  :on-preview="handlePictureCardPreview"
                  :before-upload="onBeforeUpload"
                  :on-remove="handleRemove"
                  :on-change="handleChange"
                  :auto-upload="false"
                  :multiple="true"
                  :limit="1"
                  accept="image/jpeg,image/gif,image/png,image/jpg"
                  :on-exceed="maxnum"
                  :file-list="fileList">
        <i class="el-icon-plus">
          <span style="font-size:20px;  vertical-align:1px;">选择图片</span>
        </i>
        <div class="el-upload__tip" slot="tip">
          最大支持20MB的JPEG格式照片
          <br>不建议加画框和水印签名
        </div>
      </el-upload>
      <el-button type="primary" @click="showPhoto" style="margin-left: 80px">选取图片</el-button>
      <el-dialog :visible.sync="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt>
      </el-dialog>
    </el-main>
    <el-aside width="30%" class="fb-aside">
      <h5 class="fb-title">标题</h5>
      <el-input
          class="fb-width"
          :rows="3"
          v-model="formdata.name"
      ></el-input>
      <h5 class="fb-title">作品描述</h5>
      <el-input
        class="fb-width"
        type="textarea"
        :rows="3"
        placeholder="说说你的拍摄经历...."
        v-model="formdata.descr"
      ></el-input>
      <h5 class="fb-title">标签</h5>
      <span class="fb-title fb-tj">推荐:</span>
      <ul class="fb-ul fb-ulf">
        <li
          v-for="(item,i) of categories"
          :key="item.id"
          v-if="i<9"
          v-text="item.category"
          @click="handleInputConfirm(item.category)"
        ></li>
      </ul>
      <ul class="fb-ul">
        <li
          v-for="(item,i) of categories"
          :key="item.id"
          v-if="i>=9"
          v-text="item.category"
          @click="handleInputConfirm(item.category)"
        ></li>
      </ul>
      <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">{{tag}}</el-tag>
      <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="mini"
        @keyup.enter.native="handleInputConfirm(inputValue)"></el-input>
      <el-button class="button-new-tag" size="small" @click="showInput" v-if="dynamicTags.length<5">+</el-button>
      <ul class="fb-ult">
        <li class="fb-li-left">(输入标签内容回车即可添加标签)</li>
        <li class="fb-li-right">{{dynamicTags.length}}/5</li>
      </ul>
      <el-button class="fb-btn" @click="fabu()" plain>发&nbsp;&nbsp;&nbsp;布</el-button>
    </el-aside>
<!--    弹出选择图片-->
  <el-dialog :visible.sync="dialog" width="50%" class="tj-dia">
    <div style="width: 100%;min-height: 200px;margin-top: 100px">
      <el-button class="buttonEL" @click="selectPhotos" type="primary" plain>确定</el-button>
      <div id="c">
        <div id="d" class="mywork-item" v-for="(item,index) in imgs" :key="index">
          <el-checkbox-group v-model="checkedImgIndex" @change="handleCheckedImgChange" class="check-box"
                             :style="displayCheckedMenu">
            <el-checkbox :label="index">
            </el-checkbox>
          </el-checkbox-group>
          <img style="width: 150px;height: 150px;margin-right: 10px;" :src="item.img" />
        </div>
      </div>
    </div>
  </el-dialog>

    </el-container>

</template>

<script>
export default {
  name: "fabu",
  data() {
    return {
      uid:JSON.parse(localStorage.getItem("user")).id,
      token:JSON.parse(localStorage.getItem("user")).token, //token凭证
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      dialogImageUrl: "",
      dialogVisible: false,
      dialog: false,
      formdata: {
        name:'',
        descr:'',
        img:'',
        typeName:'',
        height: '',
        width: ''
        // 值为-1时不使用相册
      },
      elProgress: false,
      progressText: '正在上传 1%',
      fileList: [],
      tag: "",
      width:'',
      height:'',
      categories: [
        {
          id:'0',
          category:'旅行'
        }
      ],
      //复选框
      checkAll: false,       //是否全选
      checkedImgIndex: [],   //图片索引数组
      checkedImgId: [],      //图片id数组
      isIndeterminate: false,
      imgs:[],
      //用户勾选图片，菜单的显示与隐藏
      checkedStatus: false,
      checkedCount: 0, //勾选数量
      displayTopMenu: 'true', //显示
      displayCheckedMenu: '',  //隐藏
    };
  },
  created(){
    this.getCategories()
  },
  methods: {
    /**
     * 获取标签
     */
    getCategories(){
      this.request.get("/categories", {
      }).then(res => {
        this.categories=res.data
        console.log(this.categories)
      })
    },
    /**
     * 对图片进行校验
     * @param file
     * @returns {boolean}
     */
    onBeforeUpload(file)
    {
      const isIMAGE = file.type === 'image/jpeg'||'image/gif'||'image/png'||'image/jpg';
      const isLt100M = file.size / 1024 / 1024 < 100;

      if (!isIMAGE) {
        this.$message.error('上传文件只能是图片格式!');
      }
      if (!isLt100M) {
        this.$message.error('上传文件大小不能超过 100MB!');
      }


      return isIMAGE && isLt100M;
    },
    /**
     * 移除图片
     * @param file
     * @param fileList
     */
    handleRemove(file, fileList) {
      this.fileList = fileList;
      console.log(file, fileList);
    },
    /**
     * 绑定图片
     * @param file
     * @param fileList
     */
    handleChange(file, fileList) {
      this.fileList = fileList;
      file;
      var reader = new FileReader();
      reader.onload = function (event) {
        var txt = event.target.result;
        var img = document.createElement("img");
        img.src = txt;
        img.onload = function () {
          this.width=img.width
          this.height=img.height
        }
      };
      reader.readAsDataURL(file.raw);
    },
    /**
     * 预览图片
     * @param file
     */
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    /**
     * 限制最大图片
     */
    maxnum() {
      this.$message({
              message: "最多只能上传1张图片",
              type: "warning",
              customClass: "zIndex"
      });
    },
    handleClose(tag) {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      },
    showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },
    handleInputConfirm(inputValue) {
        let flag=false;
        for (let i = 0; i < this.dynamicTags.length; i++) {
          if(inputValue==this.dynamicTags[i]){
            flag=true;
          }
        }
        if(flag){
          this.inputVisible = false;
          this.inputValue = '';
        }else if(inputValue){
          this.dynamicTags.push(inputValue);
          this.inputVisible = false;
          this.inputValue = '';
        }
      },
    fabu(){
      // console.log(this.$refs.upload.fileList)
      console.log(this.checkedImgId)
      if (this.fileList.length == 0) {
        return this.$message.warning('请选取文件后再上传')
      }
      var _this = this;
      // 下面的代码将创建一个空的FormData对象:
      var formData = new FormData();

      // 你可以使用FormData.append来添加键/值对到表单里面；
      this.fileList.forEach((file) => {
        formData.append('file', file.raw);
      })
      // 添加自定义参数，不传可删除
      let tags=this.dynamicTags.toString();
      formData.append('name', this.formdata.name);   //标题
      formData.append('descr', this.formdata.descr)
      formData.append('typeName', tags); //标签
      formData.append('imageId', this.checkedImgId);
      formData.append('weight', this.width)
      formData.append('height', this.height)
      this.elProgress = true;
      console.log()
      this.axios({
        url: this.$serveUrL + "/posts/upload",
        headers: {
          'token': this.token, //设置token 其中K名要和后端协调好
          'Content-Type': 'multipart/form-data'
        },
        method: "post",
        data: formData,
        onUploadProgress: function (progressEvent) {
          if (progressEvent.lengthComputable) {
            let progressPercent = parseInt((progressEvent.loaded / progressEvent.total) * 100)

            if (progressPercent == 100) {
              progressPercent--;
            }
            _this.progressText = '正在发布 ' + progressPercent + '%';
          }
        },
      }).then(res=>{
        if (res.data.status == "success") {
          this.$message({
              message: "发布成功",
              type: "success",
              customClass: "zIndex"
            });
          this.description='';
          this.dynamicTags=[];
          this.fileList=[];
          this.width='';
          this.height='';
        }else{
          this.$message({
              message: "发布失败",
              type: "danger",
              customClass: "zIndex"
            });
        }
      })
    },
    showPhoto(){
      this.dialog = true;
      this.getmyphoto()
    },
    // 获取当前用户下的所有图片
    getmyphoto(){
      this.request.get("/photo/selectPhoto",{
        params:{
          id:this.uid
        }
      }).then(res=>{
        console.log(res)
            this.$set(this, 'imgs',Object.assign(res.data) );
            console.log(this.imgs)
          })
    },
    // 提交当前选择的图片
    selectPhotos(){
      this.handleCheckIndexToId();
      console.log(this.checkedImgId)
      this.dialog= false;
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
        let h = 25;
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

<style>

.el-tag{
  margin-left: 10px !important;
}
  .button-new-tag {
    margin-left: 10px;
    height: 30px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }

.fb {
  min-height: 500px;
  height: auto;
}
.fb-main {
  background-color: #f9f9f9;
}
.fb-aside {
  background-color: #f5f5f5;
  border-left: #ffffff solid 1px;
}
.fb-upload {
  margin: 35px;
}
.el-upload--picture-card {
  background-color: #ededef !important;
  border: #bbbbbb solid 1px !important;
}
.fb-title {
  margin-left: 10px;

}
.fb-width {
  text-align: center;
}
.fb-width input,
textarea {
  width: 350px !important;
  margin: 0 auto;
}
.fb-tj {
  color: #85888a;
  font-size: 13px;
  display: inline;
}
.fb-ulf {
  display: inline;
  padding: 0;
}
.fb-ul {
  margin: 5px 0 10px 0;
}
.fb-ul li {
  list-style: none;
  display: inline;
  margin: 0 10px 0 10px;
  color: #101010;
  font-size: 14px;
  cursor: pointer;
}
.fb-ult {
  list-style: none;
  margin: 0;
  padding: 0;
}
.fb-ult li {
  display: inline;
  font-size: 13px;
  color: #85888a;
}
.fb-li-left {
  margin-left: 8%;
}
.fb-li-right {
  margin-left: 30%;
}
.fb-btn {
  clear: both;
  width: 60%;
  display: block !important;
  margin:  150px auto 0 auto !important;
  background-color: #d1b200 !important;
  color: #ffffff !important;
  font-size: 16px !important;
}
.mywork-item{
  display: flex;
  float: left;
}
.dialogEL{
  overflow-y:scroll;
  /*max-height: 600px;*/
  width: 100%;
  /*align-items: center;*/
  /*justify-content: center;*/
}
.eImage{
  /*width:100%;*/
  /*height: 100%;*/
  /*float: left;*/
}
.check-box {
  /*display: none;*/
  left: 5px;
  top: 10px;
  position: relative;
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

/* 相册菜单 */
.el-menu--popup {
  max-height: 32em;
  overflow: auto;
  overflow-x: hidden;
}
#c{
  /*盒子的大小为内容加上边框宽度*/
  margin-top: -100px;
  /*width:550px;*/
  max-height:600px;
  overflow: auto;
  margin-left: 80px;
  /*border: 1px dashed red; 宽度1像素 虚线 红色*/
  /*5px 实线 黄色*/
  border-width:5px;
  border-style:solid;
  border-color:darkgrey;
}
#d{
  width: 150px;
  height:150px;
  border: 1px dashed black;
  margin: 7px;
  display:inline-block
}
.buttonEL{
  float: right;
}
</style>

