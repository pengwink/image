<template>
  <div class="al">
    <!--我的相册-->
    <el-row class="al-row" v-if="myalbum">
     <el-col :span="5" class="al-col" v-for="item in datalist" :key="item.id">
       <div class="al-two" v-if="item.imageNumber==0">
          <div class="al-one" :style="{backgroundImage:'url('+item.albumImg+')'}" @click="detail(item.id)"></div>
        </div>
        <div class="al-two" v-else-if="item.imageNumber==1">
          <div class="al-one" :style="{backgroundImage:'url('+item.image[0].img+')'}" @click="detail(item.id)"></div>
        </div>
        <div class="al-two" v-else-if="item.imageNumber>1" :style="{backgroundImage:'url('+item.image[0].img+')'}">
          <div class="al-one" :style="{backgroundImage:'url('+item.albumImg+')'}" @click="detail(item.id)"></div>
        </div>
        <ul class="al-ul">
          <li class="al-name">{{item.gallery_name}}</li>
          <li class="alli-btn">
            <el-button class="al-btn" icon="el-icon-edit" @click="handleEdit(item)"></el-button></li>
          <li class="al-num">{{item.imageNumber}}张</li>
          <li class="al-open" v-if="item.righte == 1">公开</li>
          <li class="al-open" v-else>私密</li>
          <p>{{item.name}}</p>
        </ul>

       <el-dialog :visible.sync="dialogFormVisible">
         <el-form class="album-dia">
           <el-form-item label="相册名称" :label-width="formLabelWidth1">
             <el-input v-model="form.name" autocomplete="off"></el-input>
           </el-form-item>
           <el-form-item label="相册封面" :label-width="formLabelWidth1">
             <el-upload action="http://localhost:9090/file/upload" ref="img" :on-success="handleImgSuccess">
               <el-button size="small" type="primary" >点击上传</el-button>
             </el-upload>
           </el-form-item>
           <el-form-item label="是否公开" :label-width="formLabelWidth2">
             <el-select v-model="form.righte" placeholder="请选择是否公开">
               <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
             </el-select>
           </el-form-item>
           <el-form-item label="相册创建时间" :label-width="formLabelWidth1">
             <el-date-picker v-model="form.creatTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>
           </el-form-item>
         </el-form>
         <div slot="footer" class="dialog-footer">
           <el-button @click="dialogFormVisible = false">取 消</el-button>
           <el-popconfirm
               class="ml-5"
               confirm-button-text='确定'
               cancel-button-text='取消'
               icon="el-icon-info"
               icon-color="red"
               title="确定删除吗？"
               @confirm="del(item.id)"
           >
             <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
           </el-popconfirm>
           <el-button type="primary" @click="save">确 定</el-button>
         </div>
       </el-dialog>

      </el-col>
    </el-row>
      <!--其他用户的相册-->
      <el-row class="al-row" v-else>
      <el-col :span="5" class="al-col" v-for="item in datalist" v-if="item.righte==1" :key="item.id">
        <div class="al-two" v-if="item.imageNumber==0">
          <div class="al-one" :style="{backgroundImage:'url('+item.albumImg+')'}" @click="detail(item.id)"></div>
        </div>
        <div class="al-two" v-else-if="item.imageNumber==1">
          <div class="al-one" :style="{backgroundImage:'url('+item.image[0].img+')'}" @click="detail(item.id)"></div>
        </div>
        <div class="al-two" v-else-if="item.imageNumber>1" :style="{backgroundImage:'url('+item.image[0].img+')'}">
          <div class="al-one" :style="{backgroundImage:'url('+item.albumImg+')'}" @click="detail(item.id)"></div>
        </div>
        <ul class="al-ul">
          <li class="al-name">{{item.gallery_name}}</li>
          <li class="al-num">{{item.imageNumber}}张</li>
        </ul>
      </el-col>
    </el-row>

  </div>
</template>
<script>
export default {
  name: "album",
  data() {
    return {
      form:{},
      style: "",
      bgurl:'http://188.131.192.194/head_images/Wmyn4BVtK5ZyeZvpTOd5SfIbAYJWVt6lvQdPvhpl.gif',
      comName: this.$route.path,
      uid:this.$route.query.uid,
      datalist:[],
      dialogFormVisible:false,
      albumname:'',
      albuminfo:'',
      options:[
        {
          value: 1,
          label: "公开"
        },
        {
          value: 0,
          label: "私密"
        }
      ],
      status:'0',
      gid:'',
      formLabelWidth1: "100px",
      formLabelWidth2: "100px",
      myalbum:this.$route.query.uid==JSON.parse(localStorage.getItem("user")).id?true:false,
    };
  },
  created(){
      this.getalbum()
  },
  methods: {
    handleImgSuccess(res){
      this.form.albumImg = res
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/album/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.getalbum()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
     getalbum(){
      this.request.post('/album/selectAlbumUser',this.uid)
      .then(res=>{
        this.datalist=res.data;
        console.log(this.datalist);

      })
    },
    changeal(gid,name,status,desc){
      this.gid=gid;
      this.changealbum=true;
      this.albumname=name;
      this.status=status;
      this.albuminfo=desc;
    },
    save() {
      this.form.userId = this.uid
      this.request.post("/album", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.getalbum()
        }else if(res.code === '600') {
          this.$message.error("无法找到该用户")
        }
        else {
          this.$message.error("保存失败")
        }
      })
    },
    deletealbum(gid){
      this.$http.post('/api/galleryDelete',{gid:gid,uid:this.uid},{emulateJSON:true})
      .then(res=>{
        if (res.body.message=="删除成功") {
          this.$message({
              message: "删除成功",
              type: "success",
              customClass: "zIndex"
            });
          this.getalbum()
          this.changealbum=false;
        }else{
          this.$message({
              message: "删除失败",
              type: "danger",
              customClass: "zIndex"
            });
        }
      })
    },
    detail(gid) {
      if (/others/gi.test(this.comName)) {
        this.$router.push({ path: "/community/others/album/detail",query:{my:false,gid:gid,uid:this.uid}});
      } else {
        this.$router.push({ path: "/community/mycommunity/myalbum/detail" ,query:{my:true,gid:gid,uid:this.uid}});
      }
    }
  }
};
</script>
<style>
.al {
  margin-top: 10px;
}
.al-row {
  margin-left: 10px;
}
.al-col {
  width: 300px;
  height: 240px;
  margin: 10px 10px 10px 10px;
}
.al-one {
  width: 280px;
  height: 200px;
  background: no-repeat;
  background-size: cover;
  position: relative;
  left: 15px;
  top: 10px;
  cursor: pointer;
}
.al-two {
  width: 280px;
  height: 200px;
  background: no-repeat;
  background-size: cover;
}
.al-ul {
  margin: 15px 0 0 0;
  padding: 0;
}
.al-ul li {
  list-style: none;
  display: inline-block;
}
.al-name {
  float: left;
  font-size: 15px;
  color: #000;
}
.al-num {
  font-size: 5px;
  color: #85888a;
  float: right;
}
.al-open{
  font-size: 5px;
  color: #85888a;
  margin-left: 10px;
}
.alli-btn{
  margin-left: 10px;
}
.al-btn {
  margin: 0 !important;
  padding: 0 !important;
  width: 100% !important;
  height: 100% !important;
}
.album-dia{
  margin-top: 30px;
}
</style>
