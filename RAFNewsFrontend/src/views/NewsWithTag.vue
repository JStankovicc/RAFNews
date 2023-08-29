<template>
  <div class="news">
    <br>
    <h1>Latest news with the tag: {{ tag }}</h1><br>

    <ul id="itemList">
      <li v-for="news in newsList" :key="news.id">
        <div class="newsInfoDiv" @click="goToNews(news.id)">
          <h3><b>{{ news.title }}</b></h3>
          <p> ( {{ news.categoryName }} )</p>
          <p>{{ news.createdAt }}</p>
          <p class="pcontent">{{ news.content | shortText }}</p>
        </div>
      </li>
    </ul>
    <b-pagination
        v-model="currentPage"
        :total-rows="totalRows"
        :per-page="perPage"
        aria-controls="itemList"
        align="center"
        @input="onPageChange"
        :limit="5"
    >
    </b-pagination>

  </div>
</template>
<script>
export default ({
  data() {
    return {
      newsList: [],
      newsCategoryList: [],
      tag: '',
      perPage: 2,
      currentPage: 1,
      totalRows: 0,
    }
  },
  mounted() {
    this.fetchNewsCount();
    this.fetchNews();
  },
  filters: {
    shortText(value) {
      if (value.length <= 317) {
        return value;
      } else {
        return value.slice(0, 317) + '...';
      }
    }
  },
  methods: {
    fetchNewsCount() {
      this.$axios
          .get('/api/news/count/tag/' + this.$route.params.id)
          .then((response) => {
            const totalCount = response.data;
            this.totalRows = totalCount;
          })
          .catch((error) => {
            console.error('Error fetching news count:', error);
          });
    },
    fetchNews() {
      this.$axios
          .get(`/api/news/tag/${this.$route.params.id}?page=${this.currentPage}&perPage=${this.perPage}`)
          .then((response) => {
            this.newsList = response.data;
            this.tag = this.newsList[0].searchedTag;
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    onPageChange(newPage) {
      this.currentPage = newPage;
      this.fetchNews();
    },
    goToNews(id) {
      this.$router.push('/news/' + id);
      window.location.reload();
    }
  },
  computed: {
    rows() {
      return this.totalRows;
    }
  }
})
</script>
<style scoped>
.news {
  text-align: center;
}

.table {
  width: 80%;
  height: auto;
}

.newsInfoDiv {
  margin-top: 8px;
  margin-bottom: 12px;
  border: 2px solid black;
  width: 100%;
  padding-left: 24px;
  padding-right: 24px;
  padding-bottom: 12px;
  margin-bottom: 8px;
  cursor: pointer;
}

ul {
  list-style-type: none;
}
</style>