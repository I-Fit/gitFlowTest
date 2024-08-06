<template>
  <main>
    <div class="find-id">
      <h1>아이디 찾기</h1>
      <span
        >고객님의 아이디는
        <p class="find-userid">{{ userId }}</p>
        입니다.</span
      >
      <button type="submit" class="login-go-btn" @click="goLogin">
        로그인하기
      </button>
    </div>
  </main>
</template>

<script>
import { useRouter, useRoute } from "vue-router";

export default {
  name: "FindIdComplete",

  setup() {
    const router = useRouter();
    const route = useRoute();
    const userId = route.params.userId;

    const goLogin = () => {
      router.push({ name: "Login" });
    };

    return {
      goLogin,
      userId,
    };
  },
};
</script>

<style scoped>
main {
  width: 100%;
  height: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.find-id {
  width: 400px;
  height: 300px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  border: 1px solid rgba(0, 0, 0, 0.164);
}

span {
  margin: 10px 0;
  font-size: 18px;
}

.find-userid {
  display: inline;
  color: orangered;
  font-weight: bold;
}

.login-go-btn {
  text-align: center;
  height: 40px;
  width: 200px;
  margin-top: 20px;
  background-color: #1a73e8;
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  font-size: 16px;
}
</style>
