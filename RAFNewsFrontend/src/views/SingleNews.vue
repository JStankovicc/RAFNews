<template>
  <div>
    <NewsC :news="news"/>
  </div>
</template>
<script>
import NewsC from '../components/NewsC.vue';

export default ({
  name: "SingleNews",
  comments: NewsC,
  data() {
    return {
      news: {}
    };
  },
  mounted() {
    let id = this.$route.params.id;
    this.$axios.get("api/news/find/" + id).then((response => {
      this.news = response.data;
      console.log(this.news);
    }));
  },
  methods: {},
  components: {NewsC}
})
</script>
<style scoped>
</style>