import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'
import request from "@/utils/request";
import axios from 'axios'
import VueAxios from 'vue-axios'
import './assets/icons/index'
import * as echarts from "echarts";//引入echarts
import Waterfall from 'vue-waterfall2'
// main.js全局注册
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// use
Vue.use(mavonEditor)

Vue.prototype.$echarts = echarts//挂载到vue实例，方便全局使用
Vue.use(Waterfall)
Vue.use(VueAxios, axios)

Vue.config.productionTip = false

Vue.use(ElementUI, { size: "mini" });

Vue.prototype.request=request
Vue.prototype.$serveUrL = 'http://localhost:9090'
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

// axios.interceptors.request.use( config => {
//   // console.log("config:", config);
//   config.headers.Authorization = window.sessionStorage.getItem("Token");
//   return config;
// });

