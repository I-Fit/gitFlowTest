import { createStore } from "vuex";
import user from "@/store/modules/user";
import creategroup from "@/store/modules/creategroup";
import groupList from "@/store/modules/groupList";

const store = createStore({
  modules: {
    user,
    creategroup,
    groupList,
  },
});

export default store;
