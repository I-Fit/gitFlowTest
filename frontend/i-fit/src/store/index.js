import { createStore } from "vuex";
import user from "@/store/modules/user";
import creategroup from "./modules/creategroup";

const store = createStore({
  modules: {
    user,
    creategroup,
  },
});

export default store;
