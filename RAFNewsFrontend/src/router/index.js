import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Login from '../views/Login.vue'
import News from '../views/News.vue'
import SingleNews from '../views/SingleNews.vue'
import PopularNews from '../views/PopularNews.vue'
import NewsInCategory from '../views/NewsInCategory.vue'
import NewsWithTag from '../views/NewsWithTag.vue'
import AddNews from '../views/AddNews.vue'
import AddUser from '../views/AddUser.vue'
import Users from '../views/Users.vue'
import EditUser from '../views/EditUser.vue'
import Categories from '../views/Categories.vue'
import EditCategory from '../views/EditCategory.vue'
import AddCategory from '../views/AddCategory.vue'
import NewsForCreator from '../views/NewsForCreator.vue'
import EditNews from '../views/EditNews.vue'
import SearchView from '../views/SearchView.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: News,
        meta: {
            auth: false
        }
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {
            auth: false
        }
    },
    {
        path: '/news',
        name: 'News',
        component: News,
        meta: {
            auth: false
        }
    },
    {
        path: '/news/:id',
        name: 'Single News',
        component: SingleNews,
        meta: {
            auth: false
        }
    },
    {
        path: '/popularnews',
        name: 'PopularNews',
        component: PopularNews,
        meta: {
            auth: false
        }
    },
    {
        path: '/category/:id',
        name: 'Category',
        component: NewsInCategory,
        meta: {
            auth: false
        }
    },
    {
        path: '/news/withtag/:id',
        name: 'NewsWithTag',
        component: NewsWithTag,
        meta: {
            auth: false
        }
    },
    {
        path: '/addnews',
        name: 'AddNews',
        component: AddNews,
        meta: {
            auth: true
        }
    },
    {
        path: '/adduser',
        name: 'AddUser',
        component: AddUser,
        meta: {
            auth: true,
            admin: true
        }
    },
    {
        path: '/users',
        name: 'Users',
        component: Users,
        meta: {
            auth: true,
            admin: true
        }
    },
    {
        path: '/edituser/:id',
        name: 'EditUser',
        component: EditUser,
        meta: {
            auth: true,
            admin: true
        }
    },
    {
        path: '/categories',
        name: 'Categories',
        component: Categories,
        meta: {
            auth: true
        }
    },
    {
        path: '/categories/edit/:id',
        name: 'EditCategory',
        component: EditCategory,
        meta: {
            auth: true
        }
    },
    {
        path: '/addcategory',
        name: 'AddCategory',
        component: AddCategory,
        meta: {
            auth: true
        }
    },
    {
        path: '/creatornews',
        name: 'CreatorNews',
        component: NewsForCreator,
        meta: {
            auth: true
        }
    },
    {
        path: '/editnews/:id',
        name: 'EditNews',
        component: EditNews,
        meta: {
            auth: true
        }
    },
    {
        path: "/search/:query",
        name: "SearchResults",
        component: SearchView,
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    if (to.meta.auth) {
        const jwt = localStorage.getItem('token');
        if (!jwt) {
            next({name: 'Login'});
            return;
        }

        const payload = JSON.parse(atob(jwt.split('.')[1]));

        const type = payload.type;
        if (to.meta.admin) {
            if (type !== "admin") {
                alert("Only admin can work with users");
                return;
            }
        }
    }

    next();
});

export default router
