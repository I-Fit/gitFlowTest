import { createApp } from "vue";
import App from "@/App.vue";
import router from "@/router";
import store from "@/store";
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import vuetify from '@/utils/vuetify'; // Vuetify 플러그인 설정

const app = createApp(App);

app.use(router);
app.use(store);
app.component('VueDatePicker', VueDatePicker);
app.use(vuetify);

app.mount("#app");