<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
<!--      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>-->
<!--      <el-input style="width: 200px" placeholder="请输入" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button class="ml-5" icon="el-icon-search" circle @click="load"></el-button>
      <el-button icon="el-icon-refresh-right" circle @click="reset"></el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button  type="primary" round  @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="确定批量删除吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" round slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <!-- <el-upload action="http://localhost:9090/photo/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="info" round class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="info" round @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> -->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="albumName" label="相册名称"></el-table-column>
      <el-table-column prop="userNickname" label="所属用户"></el-table-column>
      <el-table-column prop="typeName" label="类型"></el-table-column>
      <el-table-column prop="name" label="图片名称"></el-table-column>
      <el-table-column label="图片">
        <template slot-scope="scope">
          <el-image style="width: 100px; height: 100px" :src="scope.row.img" :preview-src-list="[scope.row.img]">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="图片详情"></el-table-column>
      <el-table-column prop="photoTime" label="图片创建时间" sortable></el-table-column>
      <el-table-column prop="photoRight" label="图片权限">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.photoRight === 1">可访问</el-tag>
          <el-tag  type="warning" v-if="scope.row.photoRight === 0">不可访问</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="photoStatue" label="图片状态">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.photoStatue === 1">启用</el-tag>
          <el-tag  type="warning" v-if="scope.row.photoStatue === 0">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isStandard" label="是否符合分类规范">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.isStandard === 1">符合</el-tag>
          <el-tag  type="warning" v-if="scope.row.isStandard === 0">不符合</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isAgree" label="点赞量" sortable></el-table-column>
      <el-table-column prop="isCollect" label="收藏量" sortable></el-table-column>
      <el-table-column label="操作"  width="180" align="center">
        <template slot-scope="scope">
          <el-button type="success" round @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" round slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="120px" size="small" style="width: 80%; margin: 0 auto">
        <el-form-item label="相册名称">
          <el-input v-model="form.albumName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属用户">
          <el-input v-model="form.userNickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="form.typeName" autocomplete="off"></el-input>
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
<!--          <el-dialog :visible.sync="dialogVisible">-->
<!--            <img width="100%" :src="dialogImageUrl" alt="">-->
<!--          </el-dialog>-->
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
        <el-form-item label="图片权限">
<!--          <el-input v-model="form.photoRight" autocomplete="off"></el-input>-->
          <el-switch
              v-model="form.photoRight"
              active-color="#13ce66"
              inactive-color="#D1D0CE"
              :active-value=1
              :inactive-value=0>
          </el-switch>
        </el-form-item>
        <el-form-item label="图片状态">
          <el-switch
              v-model="form.photoStatue"
              autocomplete="off"
              active-color="#13ce66"
              inactive-color="#D1D0CE"
              :active-value=1
              :inactive-value=0>
          </el-switch>
        </el-form-item>
<!--        <el-form-item label="是否符合分类规范">-->
<!--          <el-input v-model="form.isStandard" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Photo",
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      autoUpload: false,
      file :"",
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/photo/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        console.log(res.data.records)
      })
    },
    save() {
          this.request.post("/photo", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
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
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
      })
    },
    handleEdit(row) {
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
    del(id) {
      this.request.delete("/photo/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/photo/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open("http://localhost:9090/photo/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
.imgstyle {
  width: 146px;
  height: 146px;
  display: block;
}
</style>
