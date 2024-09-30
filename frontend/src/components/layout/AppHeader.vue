<template>
  <LoggedHeader v-if="loggedIn" />
  <NonLoggedHeader v-else />
</template>

<script>
import NonLoggedHeader from "./NonLoggedHeader.vue";
import LoggedHeader from "./LoggedHeader.vue";
import { computed } from "vue";
import { useStore } from "vuex";

export default {
  components: {
    LoggedHeader,
    NonLoggedHeader,
  },

  setup() {
    const store = useStore();

    const loggedIn = computed(() => store.getters['isLogged/loggedIn']);

    return {
      loggedIn,
    }
  }
};
</script>

<style></style>
