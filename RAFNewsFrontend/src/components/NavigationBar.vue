<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <a class="navbar-brand" href="#">RAF News</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">

            <router-link :to="{ name: 'News' }" tag="a" class="nav-link"
                         :class="{ active: this.$router.currentRoute.name === 'News' }">News
            </router-link>

          </li>
          <li class="nav-item">

            <router-link :to="{ name: 'PopularNews' }" tag="a" class="nav-link"
                         :class="{ active: this.$router.currentRoute.name === 'PopularNews' }">Most Popular News
            </router-link>

          </li>
          <li class="nav-item">
            <b-dropdown text="Categories" variant="secondary" class="e-auto mb-2 mb-lg-0 ctgdrop">
              <b-dropdown-item href="#" v-for="category in categoryList" :key="category.name"
                               @click="findInCategory(category.id)">{{
                  category.name
                }}
              </b-dropdown-item>
            </b-dropdown>
          </li>
          <li class="nav-item ccli" v-if="canLogout">
            <b-dropdown text="Content-Creator" variant="primary" class="e-auto mb-2 mb-lg-0 ctgdrop">
              <b-dropdown-item @click="goToCategories">
                Categories
              </b-dropdown-item>
              <b-dropdown-item @click="goToCreatorNews">
                News
              </b-dropdown-item>
            </b-dropdown>
          </li>
          <li class="nav-item ccli" v-if="canLogout && adminCheck">
            <b-dropdown text="Admin" variant="success" class="e-auto mb-2 mb-lg-0 ctgdrop">
              <b-dropdown-item @click="goToUsers()">
                Users
              </b-dropdown-item>
            </b-dropdown>
          </li>
          <li>
            <form class="form-inline my-2 my-lg-0" @submit.prevent="searchNews">
              <input v-model="searchQuery" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
              <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>

          </li>
        </ul>
        <ul class="navbar-nav ms-auto" v-if="canLogout">
          <li class="nav-item">
            <p class="customlink">Hello, {{ user }}</p>
          </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
          <router-link v-if="!canLogout" :to="{name: 'Login'}" tag="a" class="nav-link"
                       :class="{active: this.$router.currentRoute.name === 'Login'}">Log In
          </router-link>
          <div v-else>
            <button class="btn btn-outline-success" @click="logOut">Log Out</button>
          </div>
        </form>
      </div>
    </nav>
  </div>
</template>
<script>
import router from '@/router';

export default {
  name: "Navigation_Bar",
  props: {
    jwttoken: ""
  },
  watch: {
    jwttoken(val) {
      this.token = val;
      if (this.token != null) {
        let payload = this.token.split(".")[1];
        let u = JSON.parse(atob(payload));
        let username = JSON.stringify(u.name);
        let type = JSON.stringify(u.type);
        if (!type.includes(' ')) {
          this.admin = 1;
        } else {
          this.admin = 0
        }
        this.user = username.replaceAll("\"", "");
      }
      this.$forceUpdate();
    }
  },
  async created() {
    this.isJwtSet = localStorage.getItem("token");
  },
  data() {
    return {
      selectedCategory: null,
      categoryList: [],
      token: "",
      isJwtSet: null,
      user: "",
      admin: 0,
      searchQuery: ""
    };
  },
  mounted() {
    let a = localStorage.getItem("token");
    fetch("http://127.0.0.1:8090/api/categories")
        .then(response => {
          return response.json();
        }).then(ctgs => {
      this.categoryList = ctgs;
      this.searchQuery = this.$route.params.query;
    });
  },
  methods: {
    searchNews() {
      if (this.searchQuery.trim() !== "") {
        const newRoute = {
          path: this.buildSearchUrl(this.searchQuery, this.currentPage, this.perPage),
        };

        if (this.$route.fullPath !== this.$router.resolve(newRoute).href) {
          this.$router.push(newRoute);
          window.location.reload();
        }
      }
    },
    buildSearchUrl(searchQuery, page, perPage) {
      let url = `/search/${searchQuery}`;
      if(page!=='') {url += `?page=${page}&perPage=${perPage}`;}
      return url;
    },

    logOut() {
      localStorage.removeItem("token");
      this.token = null;
      this.$forceUpdate();
      this.$router.push({name: "Login"});
    },
    findInCategory(id) {
      try {
        this.$router.push("/category/" + id);
      }finally {
        window.location.reload();
      }

    },
    goToUsers() {
      console.log("users");
      this.$router.push({name: "Users"});
    },
    goToCategories() {
      this.$router.push({name: 'Categories'});
    },
    goToCreatorNews() {
      this.$router.push({name: 'CreatorNews'});
    }
  },
  computed: {
    canLogout() {
      return this.token !== "";
    },
    adminCheck() {
      return this.admin === 1;
    }

  },
  components: {router}
}
</script>
<style scoped>
.ctgdrop {
  margin-top: 3px;
  height: 35px;
}

.customlink {
  color: #198754;
  margin-top: 14px;
  margin-right: 12px;
}

.ccli {
  padding-left: 8px;
}

</style>