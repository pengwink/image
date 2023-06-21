<template>
    <div class="main">
      <div id="title">
        <h1>上传图片</h1>
      </div>
      <div class="main-content">
        <div class="form">
          <el-form :model="formdata" ref="formdata">
            <el-form-item label="上传相册">
              <el-select v-model="selectModel" placeholder="不创建相册，可不选择" ref="formSelect" class="formItem">
                <el-option v-for="(item, index) in albums" :key="index" :label="item.name"
                           :value="index">
                  <span>{{ item.name }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="图片类型">
              <el-cascader
                  class="formItem"
                  v-model="selectOption"
                  :options="options"
                  :show-all-levels="false"
                  :props="{ checkStrictly: true }"
                  clearable>
              </el-cascader>
            </el-form-item>
            <el-form-item label="图片名称">
              <el-input v-model="formdata.imgname" class="formItem"></el-input>
            </el-form-item>
            <el-form-item label="图片详情">
              <el-input v-model="formdata.imgSite" class="formItem" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
        </div>

        <div class="images" v-loading="elProgress" :element-loading-text="progressText"
             element-loading-spinner="el-icon-loading" element-loading-background="rgba(255, 255, 255, 0.9)">
          <el-upload action="#" ref="upload" list-type="picture-card" :on-preview="handlePictureCardPreview"
                     :on-remove="handleRemove" :on-change="handleChange" :auto-upload="false" :multiple="true"
                     accept="image/jpeg,image/gif,image/png,image/jpg"
                     :file-list="fileList">
            <i class="el-icon-plus">
              <span style="font-size:20px;  vertical-align:1px;">选择图片</span>
            </i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </div>
        <div class="u-button-main">
          <div class="u-button">
            <div class="u-button-son1">
              <el-button type="primary" @click="SubmitImage">全部上传</el-button>
              <div class="u-button-son2">
                <el-button type="danger">取消上传</el-button>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
          uid: JSON.parse(localStorage.getItem("user")).id,
          token:JSON.parse(localStorage.getItem("user")).token, //token凭证
          albums: [],
            inputAlbum: '',
            formdata: {
                // 值为-1时不使用相册
                album: {
                    name: '',
                    id: '-1'
                },//相册名字
                imgname: '',//拍摄地点
                imgSite: '',//拍摄地点
                imgtype: [],//照片类型
                imgdate: '',//拍摄时间
            },
          options: [{
            value: 'zhinan',
            label: '指南',
            children: [{
              value: 'shejiyuanze',
              label: '设计原则',
            }],
            }],
            selectOption:"",
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
        this.selectAlbums();
        this.selectTypes();
    },
    computed: {
        selectModel: {
            get() {
                return this.formdata.album.name;
            },
            set(albumIndex) {
                this.formdata.album.id = this.albums[albumIndex].id;
                this.formdata.album.name = this.albums[albumIndex].name;
            }
        }
    },
    methods: {
      /**
       * 查询用户相册
       */
        selectAlbums() {
            var _this = this;
            const formData = new FormData();
        this.request.post('/album/selectAlbumUser',this.uid)
            .then(res=>{
              this.albums=res.data;
            })
        },
      /**
       * 查询用户
       */
        selectTypes() {
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
        SubmitImage() {
          if(this.selectOption!=null){
            this.selectOption = this.selectOption[this.selectOption.length-1]
          }
            if (this.fileList.length == 0) {
                return this.$message.warning('请选取文件后再上传')
            }
            var _this = this;
            var formData = new FormData();
            this.fileList.forEach((file) => {
                formData.append('file', file.raw);
            })
          this.elProgress = true;
            // 添加自定义参数，不传可删除
            formData.append('albumId', this.formdata.album.id);   //相册id
            formData.append('albumName', this.formdata.album.name); //相册名字
            formData.append('imgName', this.formdata.imgname);//图片名称
            formData.append('content', this.formdata.imgSite); //拍摄地点
            formData.append('imgTypeId', this.selectOption);//图片类型
            formData.append('imgType', this.formdata.imgtype);//图片类型
            formData.append('imgDate', this.formdata.imgdate); //拍摄时间
            this.axios({
                url: this.$serveUrL + "/photo/upload",
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
                        _this.progressText = '正在上传 ' + progressPercent + '%';
                    }
                },
            }).then(function (resp) {
                 _this.elProgress = false;
                if (resp.data.status == "success") {
                    _this.$message({
                        type: 'success',
                        message: '上传图片成功!',
                    });
                    _this.dialogImageUrl = ""
                }
                else {
                    _this.$message({
                        type: 'fail',
                        message: '上传图片失败!'
                    });
                }

            })

        },
        createAlbum() {
            //将input内容赋给select，并关闭下拉菜单
            this.formdata.album.name = this.inputAlbum;

            this.formdata.album.id = 0;

            this.$refs.formSelect.blur();
        },
        handleRemove(file, fileList) {
            this.fileList = fileList;
            console.log(file, fileList);
        },
        handleChange(file, fileList) {
          const isIMAGE = file.type === 'image/jpeg'||'image/gif'||'image/png'||'image/jpg';
          const isLt100M = file.size / 1024 / 1024 < 100;
          if (!isIMAGE) {
            this.$message.error('上传文件只能是图片格式!');
          }
          if (!isLt100M) {
            this.$message.error('上传文件大小不能超过 100MB!');
          }
          const items = new Set(fileList);
          this.fileList = Array.from(items)
            this.formdata.imgname =this.formdata.imgname +file.name.split('.')[0]+ ','
          return isIMAGE && isLt100M;
        },
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        }
    },

}
</script>
<style scoped>
.main {
  min-height: 541px;
  height: auto;
}

#title {
    text-align: center;
    height: 5%;
}

#title h1 {
    margin-top: 20px;
    font-size: 20px;
}

.main-content {
    margin-left: 30%;
    margin-right: auto;
    width: 60%;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh; /* 视口高度 */
}

.form {
    width: 28%;
    height: 70%;
    margin-left: 2%;
}

.formItem {
    width: 55%;
}

.images {
    position: absolute;
    left: 40%;
    top: 18%;
    right: 0;
    bottom: 0;
    /* margin-left:10% ; */
    /* left,top,right,bottom都为0，充满真个页面 */
    overflow-y: auto;
    overflow-x: hidden;
    height: 80%;
    width: 72%;
    /* border: 2px solid #5cb6ff; */
}

.u-button-main {
    width: 100%;
    height: 10%;
}

.u-button {

    width: 100%;
    height: 100%;
    margin-top: 35%;
}

.u-button-son1 {
    display: inline;
    /*margin-top: 90%;*/
    margin-left: -10%;

}

.u-button-son2 {
    margin-left: 5%;
    display: inline;
}
</style>
  