import { createApp } from "vue";
import App from "@/App.vue";
import router from "@/router";
import store from "@/store";
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import VueCookies from "vue-cookies";

const app = createApp(App);

app.use(router);
app.use(store);
app.component('VueDatePicker', VueDatePicker);
app.use(VueCookies);

store.dispatch('isLogged/restoreToken');

app.mount("#app");
app.$cookies.config("7d");