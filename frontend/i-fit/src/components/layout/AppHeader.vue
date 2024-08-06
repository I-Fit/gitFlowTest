<template>
  <!-- <LoggedHeader v-if="isLoggedIn" /> -->
  <NonLoggedHeader />
</template>

<script>
import NonLoggedHeader from "./NonLoggedHeader.vue";
// import LoggedHeader from "./LoggedHeader.vue";
// import { ref, onMounted } from "vue";
// import axios from "axios";

export default {
  components: {
    // LoggedHeader,
    NonLoggedHeader,
  },

  // setup() {
  //   const isLoggedIn = ref(false);

  //   const checkLoginStatus = async () => {
  //     try {
  //       const response = await axios.get(""); // 서버에
  //       isLoggedIn.value = response.data.isLoggedIn;
  //     } catch (error) {
  //       console.error("로그인 상태 확인 실패", error);
  //     }
  //   };

  //   onMounted(checkLoginStatus);

  //   return {
  //     isLoggedIn,
  //   }
  // }
};
</script>

<style></style>
