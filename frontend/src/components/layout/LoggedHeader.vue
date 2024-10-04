<template>
  <header>
    <div class="icon-table">
      <div id="icon-img">
        <img src="../../assets/images/IFIT.png" alt="I-FIT-icon" @click="home" />
      </div>
      <!-- <div class="icon-text">I-Fit</div> -->
    </div>
    <div class="text-btn">
      <a href="#" @click="creategroup">모임 생성</a>
      <a href="#" @click="post">게시판</a>
      <a href="#" @click="membership">멤버십</a>
      <a href="#" @click="mypage">마이페이지</a>

      <div class="sign-in-btn">
        <img :src="userImage" alt="사용자 이미지" />
        <p class="sign-in-user-name">{{ userName }}</p>
        <span>님, 환영합니다!</span>
      </div>
    </div>
  </header>
</template>

<script>
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { computed } from "vue";

export default {
  name: "Logged",

  setup() {
    const router = useRouter();
    const store = useStore();

    const userName = computed(() => store.getters['isLogged/userName']);
    const userImage = computed(() => store.getters['isLogged/userImage']);

    const home = () => {
      router.push({ name: "Home" });
    };
    const creategroup = () => {
      router.push({ name: "AddNewGroup" });
    };
    const post = () => {
      router.push({ name: "Board" });
    };
    const membership = () => {
      router.push({ name: "Membership" });
    };
    const mypage = () => {
      router.push({ name: "Mypage" });
    };

    return {
      home,
      creategroup,
      post,
      membership,
      mypage,
      userName,
      userImage,
    }
  }
};
</script>

<style scoped>
header {
  display: grid;
  grid-template-columns: 384px 1fr;
}

.icon-table {
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-text {
  font-size: 48px;
  font-weight: bold;
  color: #5d5a88;
  margin-left: 20px;
}

.text-btn {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  font-weight: bold;
}

a {
  text-decoration: none;
  color: #5d5a88;
}

a:nth-child(1) {
  margin-left: 150px;
}

.sign-in-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
}

.sign-in-btn img {
  width: 45px;
  height: 40px;
  margin-right: 5px;
}

.sign-in-user-name {
  font-weight: bold;
}
</style>
