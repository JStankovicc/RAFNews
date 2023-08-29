<template>
  <div class="news">
    <br>
    <h1>Most popular news</h1><br>

    <ul id="itemList">
      <li v-for="news in newsList" :key="news.id">
        <div class="newsInfoDiv" @click="goToNews(news.id)">
          <h3><b>{{ news.title }}</b></h3>
          <p> ( {{ news.categoryName }} )</p>
          <p>{{ news.createdAt }}</p>
          <p>{{ news.content | shortText }}</p>
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

    <h2>Most Reacted To</h2>
    <ul>
      <li v-for="popularNews in mostPopularNews" :key="popularNews.id">
        <div class="newsInfoDiv">
          <h3 @click="goToNews(popularNews.id)"><b>{{ popularNews.title }}</b></h3>
          <p> ( {{ popularNews.categoryName }} )</p>
          <p>{{ popularNews.createdAt }}</p>
          <p>{{ popularNews.content | shortText }}</p>
        </div>
      </li>
    </ul>
  </div>
</template>
<script>
export default ({
  data() {
    return {
      newsList: [],
      newsCategoryList: [],
      currentPage: 1,
      perPage: 2,
      totalRows: 0,
      mostPopularNews: [],

    }
  },
  mounted() {
    this.fetchNewsCount();
    this.fetchNews();
    this.fetchMostPopularNews();

  },
  filters: {
    shortText(value) {
      if (value.length <= 80) {
        return value;
      } else {
        return value.slice(0, 80) + '...';
      }
    }
  },
  methods: {
    fetchMostPopularNews() {
      this.$axios
          .get('/api/news/mostreactedto')
          .then((response) => {
            this.mostPopularNews = response.data;
          })
          .catch((error) => {
            console.error('Error fetching most popular news:', error);
          });
    },
    fetchNewsCount() {
      this.$axios
          .get('/api/news/count')
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
          .get(`/api/news/byViews?page=${this.currentPage}&perPage=${this.perPage}`)
          .then((response) => {
            this.newsList = response.data;
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
      console.log('KLIKNUTO NA ' + id);
      this.$router.push('/news/' + id);
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