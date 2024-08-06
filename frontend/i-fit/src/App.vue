<template>
  <body>
    <div class="container">
      <AppHeader />
        <router-view></router-view>
      <AppFooter />
    </div>
  </body>
</template>

<script>
import AppHeader from "./components/layout/AppHeader.vue";
import AppFooter from "./components/layout/AppFooter.vue";

export default {
  components: {
    AppHeader,
    AppFooter,
  },    
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: Arial, Helvetica, sans-serif;
}

body {
  display: flex;
  justify-content: center;
}

.container {
  width: 1536px;
  display: grid;
  grid-template-rows: 150px 1fr 150px;
}
</style>
