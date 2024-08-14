<template>
  <!-- <LoggedHeader v-if="isLoggedIn" /> -->
  <NonLoggedHeader />
</template>

<script>
import NonLoggedHeader from "./NonLoggedHeader.vue";
// import LoggedHeader from "./LoggedHeader.vue";
import { computed } from "vue";
import { useStore } from "vuex";

export default {
  components: {
    // LoggedHeader,
    NonLoggedHeader,
  },

  setup() {
    const store = useStore();

    const isLoggedIn = computed(() => store.getters.Loggedin);

    return {
      isLoggedIn,
    }
  }
};
</script>

<style></style>
