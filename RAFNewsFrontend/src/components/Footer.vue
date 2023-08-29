<template>

  <footer class="footer">
    <div class="text-center p-3 text-light">
      © 2023 Projekat Web Programiranje
      <br>
      <a class="text-light" href="http://192.168.1.30:8080/">RAF News</a>
      <br>
      <p class="text-gray" id="infoP">Jovan Stankovic</p>
    </div>

  </footer>

</template>

<script>
export default {
  name: 'Footer',
  data() {
    return {}
  }
}
</script>
<style>
.footer {
  background-color: #0b111a;
  width: 100%;
  bottom: 0;
}

#infoP {
  color: #73728a;
}
</style>