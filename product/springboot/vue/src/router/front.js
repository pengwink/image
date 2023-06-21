import myindex from "../views/front/common/index.vue"
import fabu from '../views/front/common/fabu.vue'
import others from '../views/front/common/others.vue'
import upload from '../views/front/common/upload.vue'
import gallery from '../views/front/gallery/gallery.vue'
import search from '../views/front/gallery/search.vue'
import community from "../views/front/community/index.vue"
import mycommunity from '../views/front/community/myshequ.vue'
import recommend from '../views/front/community/tuijian.vue'
import mywork from '../views/front/community/mywork.vue'
import mylike from '../views/front/community/like.vue'
import collection from '../views/front/community/collection.vue'
import myalbum from '../views/front/community/album.vue'
import albumdetail from '../views/front/community/albumdetail.vue'
import user from '../views/front/user/user.vue'
import profile from '../views/front/user/profile.vue'
import account from '../views/front/user/account.vue'
import rank from '../views/front/common/rank.vue'
import uploadImage from '../views/front/common/uploadImage.vue'

const front=[
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
                path: '/ranking',name: '图片排行榜',component: rank,
            },
            {
                path: '/uploadImage',name: '上传图片',component:uploadImage
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
                    component: mylike,
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
                ]
            },

        ]
    },
]
export default front