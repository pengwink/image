<template>
  <div id="v-main">
    <div id="v-title">图片类型数据可视化</div>
    <!--为echarts准备一个具备大小的容器dom-->
    <div id="v-echart">
      <div id="echart"></div>
      <div class="button">
        <el-button type="primary" @click="downloadImage()">下载图表</el-button>
      </div>
    </div>
  </div>
</template>
<script>


export default {
  name: 'Visualdata',
  data() {
    return {
      charts: null,//图表对象
      token:JSON.parse(localStorage.getItem("user")).token, //token凭证
      resizeTimer: null,
      serveUrL: this.$serveUrL,
      screenWidth: '',
      ImageType: [       //pie表
        {
          value: 100,
          name: '动物'
        },
        {
          value: 11,
          name: '植物'
        },
      ],
    }
  },


  //调用
  mounted() {
    // window.onresize = () => {
    //   if (this.resizeTimer) {
    //     clearTimeout(this.resizeTimer)
    //   }
    //   this.resizeTimer = setTimeout(() => {
    //     this.charts.resize();
    //   }, 500)
    // };

    if (this.token == null) {
      this.$message({
        message: '登录已过期!',
        type: 'error'
      });
      this.$router.push({ name: 'login' });
    }
    this.selectImageCount();

  },
  methods: {
    // 下载图片
    downloadImage() {
      const link = document.createElement('a');
      link.download = "图片类型分布饼图";
      link.style.display = 'none';
      console.log(this.charts)
      link.href = this.charts.getDataURL({
        type: 'png',
        pixelRatio: 1.5,
        backgroundColor: '#fff'
      }); // 导出图表图片，返回一个 base64 的 URL
      document.body.appendChild(link);
      link.click();
      URL.revokeObjectURL(link.href); //释放URL对象
      document.body.removeChild(link);
    },
    //查询所有图片类型
    selectImageCount() {
      var _this = this;
      const formData = new FormData();
      this.axios({
        url: _this.serveUrL + "/type/typeAllPhoto",
        headers: {
          'token': this.token //设置token 其中K名要和后端协调好
        },
        method: "post",
        data: formData,
      }).then(function (resp) {
        console.log(resp)
        _this.ImageType = resp.data.data;
        _this.$nextTick(function () {
          _this.drawPie('echart');

        });

      });
    },

    drawPie(id) {
      this.charts = this.$echarts.init(document.getElementById(id))
      this.charts.setOption({
        title: {
          color: 'res',
          text: '图片类型分布饼图',
          left: '50%'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '数据来源',
            type: 'pie',
            radius: '60%',
            data: this.ImageType,
            left: '20%',

            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    },



  },

}
</script>
<style scoped>
#v-main {
  width: 100%;
  height: 90%;
  position: relative;
}

#v-title {
  margin-top: 1%;
  margin-bottom: 2%;
  text-align: center;
  font-size: 25px;
}

#v-echart {
  height: 90%;
  width: 100%;
  position: relative;
}

#echart {
  position: absolute;
  left: 3%;
  height: 100%;
  width: 80%;
  display: inline-block;
}

.button {
  position: absolute;
  display: inline-block;
  right: 5%;
}
</style>