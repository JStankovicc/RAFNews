<template>
  <div id="app">
    <Navigation_Bar :jwttoken="jwttoken"/>
    <div class="container">
      <router-view/>
    </div>
    <div id="footer">
      <FooterC/>
    </div>
  </div>
</template>

<script>
import Navigation_Bar from '../src/components/NavigationBar.vue';
import FooterC from '../src/components/Footer.vue';

export default {
  components: {Navigation_Bar, FooterC},
  data() {
    return {
      jwttoken: ''
    }
  },
  mounted() {
    let a = localStorage.getItem('token');
    if (a === null) {
      this.jwttoken = '';
    } else {
      this.jwttoken = a;
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: justify;
  color: #2c3e50;
  background-color: #f2f4f5;
  position: relative;
  min-height: 100vh;
  /* background-color: #FFFDFA; */
}

#footer {
  bottom: 0;
  position: absolute;
  /* margin-top: auto; */
  width: 100%;
  height: 2.5rem;
}

.container {
  padding-bottom: 2.5rem;
}
</style>
