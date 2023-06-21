import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";
import front from './front'


Vue.use(VueRouter)

const routes=[
  {
    path:'/',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      {path: 'home',name: '首页',component:()=>import('../views/Home.vue')},
      {path: 'user',name: '用户管理',component:()=>import('../views/User.vue')},
      {path: 'album',name: '相册管理',component:()=>import('../views/Album.vue')},
      {path: 'type',name: '类型管理',component:()=>import('../views/Type.vue')},
      {path: 'photo',name: '图片管理',component:()=>import('../views/Photo.vue')},
      {path: 'comment',name: '图片管理',component:()=>import('../views/Comment.vue')},
      {path: 'person',name: '个人信息',component:()=>import('../views/Person.vue')},
      {path: 'password',name: '修改密码',component:()=>import('../views/Password.vue')},
      {path: 'file',name: '文件管理',component:()=>import('../views/File.vue')},
      {path: 'upload',name: '上传管理',component:()=>import('../views/upload.vue')},
      {path: 'album1',name: '查看相册',component:()=>import('../views/Album1.vue')},
      {path: 'InsideAlbum',name: '查看',component:()=>import('../components/InsideAlbum.vue')},
      {path: 'record',name: '操作记录',component:()=>import('../views/Record.vue')},
      {path: 'VisualdataType',name: '数据可视化',component:()=>import('../views/VisualdataType.vue')},
      {path: 'Posts',name: '发布审核',component:()=>import('../views/Posts.vue')},

    ],
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
    ...front
]

const router = new VueRouter({
  mode: 'history',
  routes
})



// 重置路由
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    routes
  })
}
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
  store.commit("setPath")
  next()
})

export default router
// const routes = [
//   {
//     path: '/login',
//     name: 'Login',
//     component: () => import('../views/Login.vue')
//   },
//   {
//     path: '/register',
//     name: 'Register',
//     component: () => import('../views/Register.vue')
//   },
//   {
//     path: '/404',
//     name: '404',
//     component: () => import('../views/404.vue')
//   },
//   {
//     path: '/front',
//     name: 'Front',
//     component: () => import('../views/front/Front'),
//     children: [
//       {
//         path: 'home',
//         name: 'FrontHome',
//         component: () => import('../views/front/Home.vue')
//       },
//       {
//         path: 'password',
//         name: 'Password',
//         component: () => import('../views/front/Password.vue')
//       },
//       {
//         path: 'person',
//         name: 'Person',
//         component: () => import('../views/front/Person.vue')
//       }
//     ]
//   }
// ]
//

// router.beforeEach((to, from, next) => {
//   localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
//   store.commit("setPath")
//   if (!to.matched.length) {
//     const menus = localStorage.getItem("menus")
//     if (!menus) {
//       next("/login")
//     } else {
//       next("/404")
//     }
//   } else {
//     next()
//   }
// })

//
// // 注意：刷新页面会导致页面路由重置
// export const setRoutes = () => {
//   const storeMenus = localStorage.getItem("menus");
//   if (storeMenus) {
//     // 拼装动态路由
//     const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
//         { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
//         { path: 'password', name: '修改密码', component: () => import('../views/Password.vue')},
//       ] }
//     const menus = JSON.parse(storeMenus)
//     menus.forEach(item => {
//       if (item.path) {  // 当且仅当path不为空的时候才去设置路由
//         let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
//         manageRoute.children.push(itemMenu)
//       } else if(item.children.length) {
//         item.children.forEach(item => {
//           if (item.path) {
//             let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
//             manageRoute.children.push(itemMenu)
//           }
//         })
//       }
//     })
//
//     // 获取当前的路由对象名称数组
//     const currentRouteNames = router.getRoutes().map(v => v.name)
//     if (!currentRouteNames.includes('Manage')) {
//       // 动态添加到现在的路由对象中去
//       router.addRoute(manageRoute)
//     }
//   }
// }
//
// // 每次刷新页面都要重新设置路由，否则路由就会被重置
// setRoutes()
//
