<template>
  <v-header/>
  <v-footer/>
</template>
<script>
import vHeader from './components/v-header.vue'
import vFooter from './components/v-footer.vue'

export default {
  components: { vHeader, vFooter }
}
</script>

<style>
body{
  padding: 0;
  margin: 0;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
 }

</style>
