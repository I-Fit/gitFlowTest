import { createStore } from "vuex";
import user from "@/store/modules/user";
import creategroup from "@/store/modules/creategroup";
import groupList from "@/store/modules/groupList";
import likegroup from "@/store/modules/likegroup";

const store = createStore({
  modules: {
    user,
    creategroup,
    groupList,
    likegroup,
  },
});

export default store;
