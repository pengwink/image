<template>
    <div id="main" :style="mainStyle">
        <!-- 隐藏菜单 -->
        <div id="check_menu" :style="displayCheckedMenu">
            <div class="check_menu_item"> <a @click="handleCheckAllChange(false)">
                    <svg-icon icon-file-name="chacha" />
                </a></div>
            <div class="check_menu_item check_menu_txt">已选择{{ checkedCount }}张相册</div>
            <div class="check_menu_feature">
                <a title="全选图片" @click="handleCheckAllChange(!checkedStatus)">
                    <svg-icon icon-file-name="all">
                    </svg-icon>
                </a>
                <a title="删除相册" @click="deleteAlbums()">
                    <svg-icon icon-file-name="delete" />
                </a>
            </div>
        </div>
        <!-- 标题 -->
        <div id="title" :style="displayTopMenu">
            <h1>相册集</h1>
        </div>
        <div>

            <el-row class="album">
                <!--添加相册(个体)  -->
                <el-col class="album-el-col" :span="3" :offset="1">
                    <a @click="dialogFormVisible=true">
                        <el-card :body-style="{ padding: '10px', }" shadow="hover">
                            <div style="text-align:center;height:10em;">
                                <svg-icon icon-file-name="addAlbum" style="font-size:3em;margin-top: 1em;" />
                            </div>
                            <div class="album-el-txt">
                                <div style="text-align:center;font-size:21px;height:42px;"><a>新建相册</a>
                                </div>
                            </div>
                        </el-card>
                    </a>
                </el-col>

                <!-- 相册内容(循环) -->
                <el-col class="album-el-col" v-for="(item, index) in albumData" :key="index" :span="3" :offset="1">
                    <a>
                        <!-- 相册外边距 -->
                        <el-card :body-style="{ padding: '10px', }" shadow="hover" class="card">
                            <el-checkbox-group v-model="checkedImgIndex" @change="handleCheckedImgChange"
                                class="check-box" :style="displayCheckedMenu">
                                <el-checkbox :label="index">
                                </el-checkbox>
                            </el-checkbox-group>
                            <img class="album-el-img" :src=item.albumImg @click="openAblum(index)">
                            <div class="album-el-txt">
                                <span>{{ item.name }}</span>
                                <div>图片数量:<a> {{ item.imageNumber }}张</a>
                                </div>
                            </div>
                        </el-card>
                    </a>
                </el-col>

            </el-row>
        </div>
      <div>
        <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
          <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
            <el-form-item label="所属用户">
              <el-input v-model="form.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="相册名称">
              <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="相册状态">
              <el-switch
                  v-model="form.statue"
                  active-color="#13ce66"
                  inactive-color="#D1D0CE"
                  :active-value=1
                  :inactive-value=0>
              </el-switch>
            </el-form-item>
            <el-form-item label="相册权限">
              <el-switch
                  v-model="form.righte"
                  active-color="#13ce66"
                  inactive-color="#D1D0CE"
                  :active-value=1
                  :inactive-value=0>
              </el-switch>
            </el-form-item>
            <el-form-item label="相册创建时间">
              <el-date-picker v-model="form.creatTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
            </el-form-item>
            <el-form-item label="相册封面">
              <el-upload action="http://localhost:9090/file/upload" ref="img" :on-success="handleImgSuccess">
                <el-button size="small" type="primary" >点击上传</el-button>
              </el-upload>
            </el-form-item>

          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </div>

</template>
<script>
export default {
    data() {
        return {
          token:JSON.parse(localStorage.getItem("user")).token,
          dialogFormVisible: false,
          form: {},
          mainStyle: {
                height: '500px',
            },
            name: '好吃的汉堡',
            ImageNumber: 100,
            resizeTimer: null,
            // 服务器url
            serveUrL: this.$serveUrL,
            // 相册json数据
            albumData: [
                {
                    name: '',
                    albumImg: 'https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png',
                    id: '',
                    imageNumber: 100,
                }
            ],

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
    mounted() {

        this.selectAlbums();
        let h = window.document.body.clientHeight;
        this.mainStyle.height = (h * 0.75) + 'px';
        window.onresize = () => {
            if (this.resizeTimer) {
                clearTimeout(this.resizeTimer)
            }
            this.resizeTimer = setTimeout(() => {
                let h = window.document.body.clientHeight;
                this.mainStyle.height = (h * 0.75) + 'px';
            }, 500)
        };
    },
    methods: {
        //打开相册
        openAblum(id) {
            let name = this.albumData[id].name;
            this.currentAlbumId = this.albumData[id].id;
            this.$message({
                type: 'success',
                message: '打开成功 '
            });
            this.$router.push({ path: '/InsideAlbum', query: { id: this.currentAlbumId, name: name } });
        },
        // 查询所有相册
        selectAlbums() {
            var _this = this;
            const formData = new FormData();
          this.request.post("/album/selectAllAlbumName", {
            params: {
              formData
            }
          }).then(function (resp) {
            console.log(resp.data)
              if (resp.data!=null) {
                  _this.albumData = resp.data;
                  console.log(resp.data)
              }
              else {
                  1;
              }
          })
        },
        //新建相册
        buildAblum() {
            this.$prompt('请输入新的相册名', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
            }).then(({ value }) => {
                this.handleCheckIndexToId();
                var _this = this;
                const formData = new FormData();
                formData.append('name', value);
                this.axios({
                    url: this.$serveUrL + "/album/addAlbum",
                  headers: {
                    'token': this.token //设置token 其中K名要和后端协调好
                  },
                    method: "post",
                    data: formData

                }).then(function (resp) {
                    if (resp.data.status == "success") {
                        _this.dialogTableVisible = false;
                        _this.$message({
                            type: 'success',
                            message: '你的相册名字是: ' + value
                        });
                        _this.load()
                    }
                    else {
                        1;
                    }
                })

            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '取消新建相册'
                });
            });
        },
        save() {
          this.request.post("/album", this.form).then(res => {

            if (res.code === '200') {
              this.$message.success("保存成功")
              this.dialogFormVisible = false
              this.selectAlbums();
            } else if (res.code === '600') {
              this.$message.error("无法找到该用户")
            } else {
              this.$message.error("保存失败")
            }
          })
        },
      handleImgSuccess(res){
        this.form.albumImg = res
      },
        // 删除相册
        deleteAlbums() {
            this.$confirm('此操作将删除所选相册, 是否继续?', '删除相册集', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                console.log(1)
                this.handleCheckIndexToId();
                var _this = this;
                const formData = new FormData();
                // formData.append('ids', this.checkedImgId);
              let ids = this.checkedImgId
              console.log(ids)
              this.request.post("/album/del/batch", ids)
                    .then(res=> {
                      console.log(res)
                    if (res.code==200) {
                        _this.$message({
                            message: '相册删除成功！',
                            type: 'success'
                        });
                        _this.handleCheckAllChange(false);
                        _this.selectAlbums();
                    }
                    else {
                        1;
                    }
                })
            })
        },
        SwitchDisplay(checkedCount) {
            if (checkedCount > 0) {
                // 获取顶部菜单高度，使选中菜单保持一致
                if (this.displayTopMenu != "display:none;" || this.displayTopMenu == '') {
                    this.displayCheckedMenu = 'display:block'
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
                let len = this.albumData.length;
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
        //根据选中的索引转化为图片的id
        handleCheckIndexToId() {
            let len = this.checkedImgIndex.length;
            this.checkedImgId = new Array(len);
            for (let i = 0; i < len; i++) {
                this.checkedImgId[i] = this.albumData[this.checkedImgIndex[i]].id;
            }
        },
        // 部分被选中,当勾选时，上方显示操作菜单
        handleCheckedImgChange(value) {
            console.log(value.length)
            this.checkedCount = value.length;
            this.SwitchDisplay(this.checkedCount);
            this.checkAll = this.checkedCount === this.albumData.length;
            this.isIndeterminate = this.checkedCount > 0 && this.checkedCount < this.albumData.length;
        },
    },
}
</script>
<style scoped>
#main {
    width: 100%;
    overflow-y: auto;
    overflow-x: hidden;
}

#title {
    height: 55px;
    text-align: center;
    font-size: 2em;
    color: transparent;

}

#check_menu {
    background-color: #3174ff;
    height: 55px;
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


.check-box {
    margin: 5px;
    display: none;
    position: absolute;
}

/* 整体相册 */
.album {
    margin-left: 2em;
}

.album-el-col {
    margin: 0.9em;
    width: 12em;
}

.album-el-img {
    /* 消去label */
    position: relative;
    width: 100%;
    height: 10em;
}

.album-el-txt {
    margin-top: 0.8em
}

.card:hover .check-box {
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

