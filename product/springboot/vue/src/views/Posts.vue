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
      <!-- <el-upload action="http://localhost:9090/posts/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="info" round class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="info" round @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button> -->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="标题"></el-table-column>
      <el-table-column prop="descr" label="描述"></el-table-column>
      <el-table-column label="图片">
        <template slot-scope="scope">
          <el-image style="width: 100px; height: 100px" :src="scope.row.img" :preview-src-list="[scope.row.img]">

          </el-image>
        </template></el-table-column>
      <el-table-column prop="time" label="时间"></el-table-column>
<!--      <el-table-column prop="userId" label="发帖人"></el-table-column>-->
      <el-table-column prop="hideRadio" label="是否显示">
          <template slot-scope="scope">
            <el-tag type="primary" v-if="scope.row.hideRadio === 1">显示</el-tag>
            <el-tag  type="warning" v-if="scope.row.hideRadio === 0">隐藏</el-tag>
          </template>
      </el-table-column>
<!--      <el-table-column prop="photoId" label="图片id"></el-table-column>-->
      <el-table-column prop="isPass" label="审核通过">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.isPass === 1">通过</el-tag>
            <el-tag  type="danger" v-if="scope.row.isPass === 0">不通过</el-tag>
          </template>
      </el-table-column>

      <el-table-column label="操作"  width="300" align="center">
        <template slot-scope="scope">
          <el-button type="warning" round @click="show(scope.row)" >查看内容 <i class="el-icon-edit"></i></el-button>
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
        <el-form-item label="标题">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.descr" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="封面">
          <el-upload action="http://localhost:9090/file/upload" ref="img" :on-success="handleImgUploadSuccess">
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker v-model="form.time" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="发帖人">
          <el-input v-model="form.userId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="是否显示">
          <el-switch
              v-model="form.hideRadio"
              active-color="#13ce66"
              inactive-color="#D1D0CE"
              :active-value=1
              :inactive-value=0>
          </el-switch>
        </el-form-item>
<!--        <el-form-item label="图片id">-->
<!--          <el-input v-model="form.photoId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="审核通过">
          <el-switch
              v-model="form.isPass"
              active-color="#13ce66"
              inactive-color="#D1D0CE"
              :active-value=1
              :inactive-value=0>
          </el-switch>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogVisible">
      <div id="a">
        <div class="mywork-item" v-for="(item,index) in picdetail" :key="index">
          <div class="mywork-img">
            <el-image id="b" :src="item.img" fit="cover"></el-image>
          </div>
          <div class="mywork-shadow">
            <div class="mywork-det">
              <el-button type="text" @click="show(item);getpicdetail(item.pid)">查看详情</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
  <!--弹出对话框-->

</template>

<script>
export default {
  name: "Posts",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      uid: JSON.parse(localStorage.getItem("user")).id,
      dialogVisible:false,
      diaitem:{},
      picdetail:{},
    }
  },
  created() {
    this.load()
  },
  methods: {
    getpicdetail(pid) {
      this.request.get("/posts/selectAlll", {
        params: {
          id: pid,
          uid: this.uid
        }
      }).then(res => {
        this.picdetail = Object.assign(res.data);
        console.log(res);
      })
    },
    show(item) {
      if (this.uid) {
        this.dialogVisible = true;
        this.diaitem = item;
        console.log(item)
        this.getpicdetail(item.id)
      } else {
        this.$message({
          message: "您还未登录",
          type: "warning",
          customClass: "zIndex"
        })
      }
    },
      load() {
      this.request.get("/posts/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/posts", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
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
      this.request.delete("/posts/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/posts/del/batch", ids).then(res => {
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
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
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
      window.open("http://localhost:9090/posts/export")
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
.el-dialog {
  position: absolute;
  margin: 0 auto 50px;
  background: #FFF;
  border-radius: 2px;
  box-shadow: 0 1px 3px rgba(0,0,0,.3);
  box-sizing: border-box;
  width: 50%;
}
</style>
