import { createStore } from "vuex";
import isLogged from "./modules/isLogged";

const store = createStore({
  modules: {
    isLogged,
  },
});

export default store;
