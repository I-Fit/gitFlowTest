<template>
  <header>
    <div class="icon-img">
      <img src="../../assets/images/IFIT.png" alt="아이콘 이미지" @click="home" />
    </div>
    <div class="text-btn">
      <!-- <a href="#" @click="home">홈</a> -->
      <a href="#" @click="creategroup">모임 생성</a>
      <a href="#" @click="post">게시판</a>
      <a href="#" @click="membership">멤버십</a>
      <a href="#" @click="mypage">마이페이지</a>

      <div class="register-and-login">
        <img class="sign-up-icon" src="@/assets/images/sign-up-icon.png" @click="createAccount" />
        <div class="sign-up" @click="createAccount">회원가입</div>
        <img class="sign-in-icon" src="@/assets/images/sign-in-icon.png" @click="goSignIn" />
        <div class="sign-in" @click="goSignIn">로그인</div>
      </div>
    </div>
  </header>
</template>

<script>
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { computed } from "vue";

export default {
  name: "NonLogged",
  setup() {
    const router = useRouter();
    const store = useStore();

    const isLoggedIn = computed(() => store.getters['isLogged/loggedIn']);

    const home = () => {
      router.push({ name: "Home" });
    };

    const goSignIn = () => {
      router.push({ name: "SignIn" });
    };

    const createAccount = () => {
      router.push({ name: "SignUp" });
    };

    const post = () => {
      router.push({ name: "Board" });
    };

    const membership = () => {
      router.push({ name: "Membership" });
    };

    const mypage = () => {
      if (isLoggedIn.value) {
        router.push({ name: "Mypage" });
      } else {
        alert("로그인 후 이용해주세요.");
        router.push({ name: "SignIn" });
      }
    };

    const creategroup = () => {
      if (isLoggedIn.value) {
        router.push({ name: "AddNewGroup" });
      } else {
        alert("로그인 후 이용해주세요.");
        router.push({ name: "SignIn" });
      }
    };

    return {
      home,
      goSignIn,
      createAccount,
      post,
      membership,
      mypage,
      creategroup,
    };
  },
};
</script>

<style scoped>
header {
  display: grid;
  grid-template-columns: 384px 1fr;
  height: 150px;
  margin-top: 30px;
}

.icon-img {
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
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
  font-weight: bolder;
  font-size: 20px;
}
 
a {
  text-decoration: none;
  color: #5d5a88;
  font-weight: bolder;
}

a:nth-child(1) {
  margin-left: 280px;
}

.register-and-login {
  display: flex;
  margin-right: 20px;
  align-items: center;
}

.sign-up-icon {
  width: 20px;
  height: 20px;
  margin-right: -3px;
  cursor: pointer;
}

.sign-up {
  padding: 12px;
  border-radius: 25px;
  font-size: 13px;
  cursor: pointer;
  margin-right: 10px;
  text-align: center;
}

.sign-in-icon {
  width: 20px;
  height: 20px;
  margin-right: -17px;
  cursor: pointer;
}

.sign-in {
  margin: 10px;
  padding: 12px;
  border-radius: 25px;
  font-size: 13px;
  cursor: pointer;
  text-align: center;
}
</style>
