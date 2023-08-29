<template>
  <div class="news">
    <br>
    <h1>Latest news from <b>{{ category }}</b></h1><br>

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

  </div>
</template>
<script>
export default ({
  data() {
    return {
      newsList: [],
      perPage: 2,
      currentPage: 1,
      totalRows: 0,
      category: ''
    }
  },
  mounted() {
    this.fetchNewsCount();
    this.fetchNews();


      this.$axios.get('/api/categories/find/' + this.$route.params.id).then((response => {
        let c = response.data;
        this.category = c.name;
      }))

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
          .get('/api/news/count/category/' + this.$route.params.id)
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
          .get(`/api/news/category/${this.$route.params.id}?page=${this.currentPage}&perPage=${this.perPage}`)
          .then((response) => {
            this.newsList = response.data;
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    onPageChange(newPage) {
      console.log('Clicked on page:', newPage);
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

.pcontent {
  max-width: 100%;
  word-wrap: break-word;
}

ul {
  list-style-type: none;
}
</style>