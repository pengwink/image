import myindex from "../views/front/common/index.vue"
import fabu from '../views/front/common/fabu.vue'
import others from '../views/front/common/others.vue'
import upload from '../views/front/common/upload.vue'
import gallery from '../views/front/gallery/gallery.vue'
import search from '../views/front/gallery/search.vue'
import community from "../views/front/community/index.vue"
import mycommunity from '../views/front/community/myshequ.vue'
import recommend from '../views/front/community/tuijian.vue'
import myfollow from '../views/front/community/guanzhu.vue'
import mywork from '../views/front/community/mywork.vue'
import mylike from '../views/front/community/like.vue'
import collection from '../views/front/community/collection.vue'
import myalbum from '../views/front/community/album.vue'
import follows from '../views/front/community/follows.vue'
import fans from '../views/front/community/fans.vue'
import albumdetail from '../views/front/community/albumdetail.vue'
import user from '../views/front/user/user.vue'
import profile from '../views/front/user/profile.vue'
import account from '../views/front/user/account.vue'
import setting from '../views/front/user/setting.vue'
const front=[
    {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Front'),
    children: [
        {
            path: 'home',
            name: 'FrontHome',
            component: () => import('../views/front/Home.vue')
        },
        {
            path: 'password',
            name: 'Password',
            component: () => import('../views/front/Password.vue')
        },
        {
            path: 'person',
            name: 'Person',
            component: () => import('../views/front/Person.vue')
        }
    ]
},
    {
        path: '/App',
        component: () => import('../views/front/App.vue'),
        redirect: "/index",
        children: [
            {
                path: '/index',name: '首页',component: myindex,
            },
            {
                path: '/publish',name: '发布',component: fabu,
            },
            {
                path: '/others',name: '其他',component: others,
            },
            {
                path: '/upload',name: '上传',component: upload,
            },
            {
                path: '/gallery',name: '图库',component: gallery
            },
            {
                path:'/gallery/search',name: '搜索',component:search
            },
            {
                path:'/community/others',
                component:others,
                children:[{
                    path: '/',
                    component: mywork,
                },
                    {
                        path: 'work',
                        component: mywork,
                    },
                    {
                        path: 'like',
                        component: mylike,
                    },
                    {
                        path: 'collection',
                        component: collection,
                    },
                    {
                        path: 'album',
                        component: myalbum
                    },
                    {
                        path: 'album/detail',
                        component: albumdetail
                    }
                ]
            },
            {
                path: '/community',
                component: community,
                children: [{
                    path: '/',
                    component: recommend
                },
                    {
                        path: 'recommend',
                        component: recommend
                    },
                    {
                        path: 'myfollow',
                        component: myfollow
                    },
                    {
                        path: 'mycommunity',
                        component: mycommunity,
                        children: [{
                            path: '/',
                            component: mywork,
                        },
                            {
                                path: 'mywork',
                                component: mywork,
                            },
                            {
                                path: 'mylike',
                                component: mylike,
                            },
                            {
                                path: 'mycollection',
                                component: collection,
                            },
                            {
                                path: 'myalbum',
                                name:'myalbum',
                                component: myalbum
                            },
                            {
                                path: 'myalbum/detail',
                                component: albumdetail
                            },
                            {
                                path: 'follows',
                                component: follows,
                            },
                            {
                                path: 'fans',
                                component: fans,
                            }
                        ]
                    },

                ]
            },
            {
                path:'/user',
                component: user,
                children:[
                    {
                        path:'profile',
                        name:'profile',
                        component:profile
                    },
                    {
                        path:'account',
                        name:'account',
                        component:account
                    },
                    {
                        path:'setting',
                        name:'setting',
                        component:setting
                    }
                ]
            },

        ]
    },
//     {
//         path: '/',
//         component: () => import('../views/front/App'),
//     },
//     {
//         path: '/front/common/index',
//         component: myindex,
//     },
//     {
//         path: '/upload',
//         component: upload,
//     },
//     {
//         path: '/gallery',
//         component: gallery
//     },
//     {
//         path:'/gallery/search',
//         component:search
//     },
//     {
//         path:'/community/others',
//         component:others,
//         children:[{
//             path: '/',
//             component: mywork,
//         },
//             {
//                 path: 'work',
//                 component: mywork,
//             },
//             {
//                 path: 'like',
//                 component: mylike,
//             },
//             {
//                 path: 'collection',
//                 component: collection,
//             },
//             {
//                 path: 'album',
//                 component: myalbum
//             },
//             {
//                 path: 'album/detail',
//                 component: albumdetail
//             }
//         ]
//     },
//     {
//         path: '/community',
//         component: community,
//         children: [{
//             path: '/',
//             component: recommend
//         },
//             {
//                 path: 'recommend',
//                 component: recommend
//             },
//             {
//                 path: 'myfollow',
//                 component: myfollow
//             },
//             {
//                 path: 'mycommunity',
//                 component: mycommunity,
//                 children: [{
//                     path: '/',
//                     component: mywork,
//                 },
//                     {
//                         path: 'mywork',
//                         component: mywork,
//                     },
//                     {
//                         path: 'mylike',
//                         component: mylike,
//                     },
//                     {
//                         path: 'mycollection',
//                         component: collection,
//                     },
//                     {
//                         path: 'myalbum',
//                         name:'myalbum',
//                         component: myalbum
//                     },
//                     {
//                         path: 'myalbum/detail',
//                         component: albumdetail
//                     },
//                     {
//                         path: 'follows',
//                         component: follows,
//                     },
//                     {
//                         path: 'fans',
//                         component: fans,
//                     }
//                 ]
//             },
//
//         ]
//     },
//     {
//         path: '/publish',
//         component: fabu
//     },
//     {
//         path: '/notice',
//         component: notice,
//         children:[
//             {
//                 path:'/',
//                 component:inform
//             },
//             {
//                 path:'message',
//                 name:'message',
//                 component:message
//             },
//             {
//                 path:'inform',
//                 name:'inform',
//                 component:inform
//             }
//         ]
//     },
//     {
//         path:'/user',
//         component: user,
//         children:[
//             {
//                 path:'profile',
//                 name:'profile',
//                 component:profile
//             },
//             {
//                 path:'account',
//                 name:'account',
//                 component:account
//             },
//             {
//                 path:'setting',
//                 name:'setting',
//                 component:setting
//             }
//         ]
//     }
]
export default front