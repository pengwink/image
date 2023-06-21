<template>
  <div class="mywork">
  <el-row type="flex" justify="center" ref="top_menu">
      <el-col :span="23">
        <!-- 选中菜单 -->
        <div id="check_menu" :style="displayCheckedMenu">
          <div class="check_menu_item"> <a @click="handleCheckAllChange(false)">
            <svg-icon icon-file-name="chacha" @click="checkedStatus=false"/>
          </a></div>
          <div class="check_menu_item check_menu_txt">已选择{{ checkedCount }}张图片</div>

          <div class="check_menu_feature">
            <a title="发布图片" @click="fabuPhoto()">
              <svg-icon icon-file-name="fabu" />
            </a>
            <a title="添加相册" @click="openAlbum()">
              <svg-icon icon-file-name="add" />
            </a>
            <a title="下载图片" @click="downloadImage()">
              <svg-icon icon-file-name="download" />
            </a>
            <a title="删除图片" @click="deleteImage()">
              <svg-icon icon-file-name="delete" />
            </a>
            <a title="全选图片" @click="handleCheckAllChange(!checkedStatus)">
              <svg-icon icon-file-name="all">
              </svg-icon>
            </a>
          </div>
        </div>
        <waterfall  class="mywork-div"  :gap="gap" :col="col" :width="itemWidth" :gutterWidth="gutterWidth" :data="imgs" @loadmore="loadmore"
      @scroll="scroll">
      <template>
        <div class="mywork-item" v-for="(item,index) in imgs" :key="index">
          <el-checkbox-group v-model="checkedImgIndex" @change="handleCheckedImgChange" class="check-box"
                             :style="displayCheckedMenu">
            <el-checkbox :label="index">
            </el-checkbox>
          </el-checkbox-group>
          <div class="mywork-img">
            <el-image :src="item.img" :preview-src-list="getSrcList(index)">
            </el-image>
            <p>{{item.name}}</p>
          </div>
          <div class="mywork-shadow">
            <div class="mywork-lc">
              <el-button type="text" @click="handleEdit(item)">修改</el-button>
            </div>

            <div class="mywork-det">
              <el-button type="text" @click="previewImag(index)">查看详情</el-button>
            </div>
            <div class="mywork-line">
              <div class="my-work-space"></div>
              <div v-if="my" class="mywork-btn">
                <el-button type="text" @click="deletepic(item.id)">删除</el-button>
              </div>
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
    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
        <el-form-item label="图片类型">
          <el-cascader
              class="formItem"
              v-model="form.typeId"
              :options="options"
              :show-all-levels="false"
              :props="{ checkStrictly: true }"
              clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload
              action="http://localhost:9090/file/upload"
              :show-file-list="false"
              list-type="picture-card"
              :on-success="handleImgSuccess"
              :before-upload="handleBefore"
          >
            <img v-if="form.img" :src="form.img" class="imgstyle">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="图片名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图片详情">
          <el-input v-model="form.content" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图片创建时间">
          <el-date-picker v-model="form.photoTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogYulan">
      <div >
        <el-image :src="previewImage"></el-image>
      </div>
        <p>{{this.img1.content}}</p>
    </el-dialog>
    <el-dialog :visible.sync="dialogVisible" width="70%" class="tj-dia">
      <div class="mywork-dia-img">
        <img :src="diaitem.position">
      </div>
      <div class="mywork-dia-text" v-text="diaitem.description"></div>
        <div class="tj-dia-like">
        <el-button icon="el-icon-gz-heart"></el-button>
          <span v-text="diaitem.like_num"></span>
        </div>
        <div class="mywork-tag">标签：{{diaitem.type_name}}</div>
          <div class="mywork-dia-tabs">
            <div class="mywork-hot" v-for="com in picdetail.comment" :key="com.cid">
              <div class="mywork-hot-tx">
                <img :src="com.from_head_image">
              </div>
              <div class="mywork-hot-name">{{com.from_username?com.from_username:'注册用户'}}</div>
              <div class="mywork-coms-com">{{com.content}}</div>
            </div>
          </div>
    </el-dialog>
    <!-- 添加相册表格 -->
    <el-dialog title="添加相册" :visible.sync="dialogTable" width="25%">
      <el-table ref="singleTable" :data="albumData" :show-header="false" highlight-current-row max-height="300px"
                @current-change="handleTableCurrentChange">
        <el-table-column property="albumImg" label="描述图片" width="200">
          <!-- 将图片添加到表格中 -->
          <template slot-scope="scope">
            <img :src="scope.row.albumImg" style="height:5em;width:12em" />
          </template>
        </el-table-column>
        <el-table-column property="name" label="相册名称">
        </el-table-column>
      </el-table>
      <el-button type="primary" size="small" style="margin-left: 45%; margin-top: 10px;" @click="addAlbum()">添加
      </el-button>
    </el-dialog>
    <!-- 发布作品 -->
    <el-dialog title="信息" :visible.sync="dialogTableVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">

        <el-form-item label="图片">
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
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt>
          </el-dialog>
        </el-form-item>
        <el-form-item label="标题">
          <el-input
              class="fb-width"
              :rows="3"
              v-model="formdata.name"
          ></el-input>
        </el-form-item>
        <el-form-item label="作品描述">
          <el-input
              class="fb-width"
              type="textarea"
              :rows="3"
              placeholder="说说你的拍摄经历...."
              v-model="formdata.descr"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="fabu()">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
export default {
  name: "mywork",
  data() {
    return {
      token:JSON.parse(localStorage.getItem("user")).token, //token凭证
      dialogYulan: false,
      dialogVisible: false,
      dialogTableVisible:false,
      diaitem: [],
      picdetail:[],
      activeName: "comments",
      col:5,
      gap:5,
      uid:this.$route.query.uid,
      my:this.$route.query.my,
      avatar:'http://188.131.192.194/head_images/5LSk0zVtyKDq9UciiWPab50dwjoNI2324KtwSyBD.jpeg',
      imgs:[],
      img1:[],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      previewImageUrL: [
        'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
      ],
      previewImage:"",
      dialogFormVisible: false,
      //添加到相册
      dialogTable: false,//菜单状态
      currentRowData: null, //当前行相册数据对象
      //相册数据
      albumData: [
        {
          name: '',
          albumImg: '',
          id: '',
        }
      ],
      formdata: {
        name:'',
        descr:'',
        img:'',
        typeName:'',
        height: '',
        width: ''
        // 值为-1时不使用相册
      },
      dialogImageUrl: "",
      elProgress: false,
      progressText: '正在上传 1%',
      fileList: [],
      form:[],
      tag: "",
      width:'',
      height:'',
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

      inputAlbum: '',
      options: [{
        value: 'zhinan',
        label: '指南',
        children: [{
          value: 'shejiyuanze',
          label: '设计原则',
        }],
      }],
      selectOption:"",
    };
  },
  mounted(){
    this.getmywork()
    this.selectType();
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
    /**
     * 查询用户
     */
    selectType() {
      var _this = this;
      const formData = new FormData();
      this.axios({
        url: this.$serveUrL + "/type/selectTypeDto",
        method: "get",
        headers: {
          'token': this.token, //设置token 其中K名要和后端协调好
        },
        data: formData
      }).then(function (resp) {
        if (resp.status == 200) {
          _this.options = resp.data.data
          console.log(resp)
        }
        else {
          1;
        }
      })
    },
    save() {
      if(this.form.typeId!=null){
        this.form.typeId = this.form.typeId[this.form.typeId.length-1]
      }
      console.log(this.form.typeId)
      this.request.post("/photo", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.getmywork()
        } else if(res.code === '600') {
          this.$message.error("无法找到该用户")
        }else if(res.code === '700') {
          this.$message.error("无法找到该相册")
        }else if(res.code === '800') {
          this.$message.error("无法找到该类型")
        }
        else {
          this.$message.error("保存失败")
        }
      })
    },
    // 添加到相册表格方法
    handleTableCurrentChange(val) {
      this.currentRowData = val;
    },
    previewImag(index){
      this.dialogYulan=!this.dialogYulan;
      this.previewImage = this.previewImageUrL[index]
      this.img1 = this.imgs[index]
      console.log(this.img1)
    },
    // 其中大图预览目前总是从第一张开始,简单处理下,就是构建一个以当前图片为起始的数组
    getSrcList(index) {

      return this.previewImageUrL.slice(index).concat(this.previewImageUrL.slice(0, index))
    },
    openAlbum() {
      this.selectAlbums();
      this.dialogTable = true;
    },
    addAlbum() {
      this.handleCheckIndexToId();
      var _this = this;
      const formData = new FormData();
      console.log(this.currentRowData)
      formData.append('imageId', this.checkedImgId);
      formData.append('albumId', this.currentRowData.id);
      this.axios({
        url: this.$serveUrL + "/album/addImageToAlbum",
        headers: {
          'token': this.token //设置token 其中K名要和后端协调好
        },
        method: "post",
        data: formData
      }).then(function (resp) {
        if (resp.data.status == "success") {
          _this.$message({
            message: '添加相册成功！',
            type: 'success'
          });
          _this.dialogTableVisible = false;
        }
        else {
          1;
        }
      })
    },
    selectAlbums() {
      var _this = this;
      const formData = new FormData();
      this.axios({
        url: this.$serveUrL + "/album/selectAlbumName",
        headers: {

          'token': this.token //设置token 其中K名要和后端协调好
        },
        method: "post",
        data: formData
      }).then(function (resp) {
        console.log(resp)
        if (resp.data.status == "success") {
          _this.albumData = resp.data.data;
        }
        else {
          1;
        }
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
      console.log("---------")
      console.log(this.checkedImgId)
      // 添加自定义参数，不传可删除
      // let tags=this.dynamicTags.toString();
      formData.append('name', this.formdata.name);   //标题
      formData.append('descr', this.formdata.descr)
      // formData.append('typeName', tags); //标签
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
    scroll() {
    },
    loadmore() {
    },
    fabuPhoto(){
      this.dialogTableVisible = true;
      this.handleCheckIndexToId();

    },
    getmywork(){
      const formData = new FormData();
      formData.append("currentPage", this.pageNum);
      formData.append("pageSize", this.pageSize);
      formData.append("userId", this.uid);
      let requestApi = '/photo/selectAll';
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
    deletepic(pid){

      this.request.delete("/photo/" + pid).then(res => {
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
    downloadImage() {
      var imgIndex = this.checkedImgIndex;
      console.log(imgIndex)
      for (let i = 0; i < imgIndex.length; i++) {
        let imgUrl = this.previewImageUrL[imgIndex[i]];
        let imgName = this.imgs[imgIndex[i]].name;
        this.downloadPicture(imgUrl, imgName);
      }
      this.handleCheckIndexToId();
      var _this = this;
      const formData = new FormData();
      formData.append('number', this.checkedImgId.length);
      this.axios({
        url: this.$serveUrL + "/record/recordDownload",
        headers: {
          'token': this.token //设置token 其中K名要和后端协调好
        },
        method: "post",
        data: formData
      }).then(function (resp) {
        if (resp.data.status == "success") {
          _this.$message({
            message: '图片下载成功！',
            type: 'success'
          });
          _this.dialogTableVisible = false;
        }
      })
    },
    downloadPicture(imgSrc, name) {
      console.log(imgSrc,name)
      var index = imgSrc.lastIndexOf(".");
      var ext = imgSrc.substring(index+1);
      // 根据url下载
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
    deleteImage() {
      this.$confirm('此操作将删除所选图片, 是否继续?', '删除图片', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.handleCheckIndexToId();
        let ids =  this.checkedImgId;
        var _this = this;
        const formData = new FormData();
        this.request.post("/photo/del/batch", ids).then(res => {
          if (res.code === '200') {
            this.$message.success("批量删除成功")
            _this.handleCheckAllChange(false);
            _this.getmywork();
          } else {
            this.$message.error("批量删除失败")
          }
        })
      })
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

    handleEdit(row) {
      console.log(row)
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if(this.$refs.img) {
          this.$refs.img.clearFiles();
        }
        if(this.$refs.file) {
          this.$refs.file.clearFiles();
        }
      })
    },
    handleImgSuccess(res,file){
      this.$forceUpdate();
      this.form.img = res
    },
    handleBefore(file){
      let acceptFileType = ['jpeg','jpg','png','gif']
      let type = file.name.split(".").slice(-1)[0].toLowerCase()
      if(!acceptFileType.includes(type)){
        setTimeout(() => {
          this.$notify({
            title: "温馨提示",
            message: "格式错误！",
            type: "error"
          })
        },50)
        return false
      }
      this.form.name = file.name.split('.')[0]
      return true;
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
.mywork-le{
  color: #fff;
  position: absolute;
  top: 75%;
  left: 15%;
}
.mywork-le span{
  color: #fff;
}
.mywork-det {
  color: #fff;
  position: absolute;
  top: 35%;
}
.mywork-det span {
  color: #fff;
}
.mywork-line{
  position: absolute;
  top: 75%;
}
.mywork-lc {
  top: 75%;
  left: 15%;
  color: #fff;
  font-size: 12px;
  display: inline-block;
}
.mywork-lc span{
  color: #fff;
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
.imgstyle {
  width: 146px;
  height: 146px;
  display: block;
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

