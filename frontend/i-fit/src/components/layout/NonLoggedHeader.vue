<template>
  <header>
    <div class="icon-table">
      <div class="icon-img">
        <img src="../../assets/image/icon.png" alt="아이콘 이미지" />
      </div>
      <div class="icon-text">I-Fit</div>
    </div>
    <div class="text-btn">
      <a href="#" @click="home">홈</a>
      <a href="#" @click="creategroup">모임 생성하기</a>
      <a href="#" @click="post">오운완 챌린지</a>
      <a href="#" @click="membership">멤버십</a>
      <a href="#" @click="mypage">마이페이지</a>
      <div class="login-btn">
        <div class="join" @click="createAccount">회원가입</div>
        <div class="login" @click="goLogin">로그인</div>
      </div>
    </div>
  </header>
</template>

<script>
import { useRouter } from "vue-router";

export default {
  name: "NonLogged",
  setup() {
    const router = useRouter();

    const home = () => {
      router.push({ name: "Home" });
    };

    const goLogin = () => {
      router.push({ name: "Login" });
    };

    const createAccount = () => {
      router.push({ name: "Account" });
    };

    const post = () => {
      router.push({ name: "MainBoard" });
    };

    const membership = () => {
      router.push({ name: "Membership" });
    };

    const mypage = () => {
      router.push({ name: "Mypage" });
    };

    const creategroup = () => {
      router.push({ name: "CreateGroup" });
    };

    return {
      home,
      goLogin,
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
  font-size: 18px;
}

a {
  text-decoration: none;
  color: #5d5a88;
}
a:nth-child(1) {
  margin-left: 150px;
}

.login-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
}

.join {
  border: 0.5px solid rgba(0, 0, 0, 0.103);
  padding: 12px;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
}

.login {
  margin: 10px;
  padding: 12px;
  border: 0.5px solid rgba(0, 0, 0, 0.103);
  border-radius: 10px;
  background-color: #1a73e8;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
}
</style>
