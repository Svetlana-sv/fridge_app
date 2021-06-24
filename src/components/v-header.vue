<template>
  <div id="nav">
    <router-link to="/">О проекте</router-link> <span class="devider">|</span>
    <router-link to="/app">Веб-версия проекта (Демонстрационная)</router-link>
  </div>
  <router-view />
</template>

<script>
export default {}
</script>

<style>
#nav {
  padding: 30px;
  display: flex;
  justify-content: center;
  gap: 10px;
}
#nav a {
  font-weight: bold;
  color: #2c3e50;
}
#nav a.router-link-exact-active {
  color: #42b983;
}
@media (max-width: 550px) {
    #nav{
        flex-direction: column;
    }
    .devider{
        display: none;
    }
}
</style>
