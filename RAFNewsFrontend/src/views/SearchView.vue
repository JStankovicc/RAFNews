<template>
  <div class="news">
    <br>
    <h1>Search: {{searchQuery}}</h1><br>

    <ul id="itemList">
      <li v-for="news in newsList" :key="news.id">
        <div class="newsInfoDiv">
          <h3 @click="goToNews(news.id)"><b>{{ news.title }}</b></h3>
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

  </div>
</template>

<script>
export default {
  name: "SearchResults",
  mounted() {
    this.searchQuery = this.$route.params.query;
    this.fetchNewsCount();
    this.fetchNews();
  },
  data() {
    return {
      searchQuery: "",
      newsList: [],
      currentPage: 1,
      perPage: 2,
      totalRows: 0,
    };
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
    fetchNewsCount() {
      this.$axios
          .get('/api/news/search/count/' + this.searchQuery)
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
          .get(`/api/news/search/${this.searchQuery}?page=${this.currentPage}&perPage=${this.perPage}`)
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
      this.$router.push('/news/' + id);
    }
  },
  computed: {
    rows() {
      return this.totalRows;
    }
  }
};
</script>