<template>

  <body>
    <div class="container">
      <AppHeader />
      <div class="router-view">
        <router-view @load-more="loadMore"></router-view>
        <button v-if="!isEnd && !loading" class="load-more" @click="loadMore">더 보기</button>
        <div v-if="loading" class="loading-indicator">로딩 중...</div>
      </div>
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
  methods: {
    loadMore() {
      const homeComponent = this.$refs.home;
      if (homeComponent && homeComponent.loadMore) {
        homeComponent.loadMore();
      }
    }
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

.router-view {
  flex: 1;
}

.load-more {
  background-color: black;
  cursor: pointer;
  color: white;
  border: none;
  padding: 10px;
  margin: 20px auto;
  cursor: pointer;
  display: block;
}

.loading-indicator {
  text-align: center;
  margin: 20px auto;
}
</style>