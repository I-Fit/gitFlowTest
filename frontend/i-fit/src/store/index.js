import { createStore } from "vuex";
import user from "@/store/modules/user";
import isLogged from "./modules/isLogged";

const store = createStore({
  modules: {
    user,
    isLogged,
  },
});

export default store;
