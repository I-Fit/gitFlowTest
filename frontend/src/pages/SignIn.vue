<template>
  <main>
    <div class="sign-in-container">
      <form action="#" class="signup-form">
        <h1>로그인</h1>
        <div class="signup-id input-block">
          <label class="signup-label" for="id">아이디<span class="signup-id-find" @click="FindId">아이디 찾기</span></label>
          <div class="signup-field">
            <input type="text" id="id" name="id" placeholder="아이디를 입력하세요." class="signup-field-input"
              v-model="formData.id" />
          </div>
        </div>
        <div class="signup-pw input-block">
          <label class="signup-label" for="id">비밀번호<span class="signup-pw-find" @click="FindPw">비밀번호 재설정</span></label>
          <div class="signup-field">
            <input type="password" id="pw" name="id" placeholder="비밀번호를 입력하세요." class="signup-field-input"
              v-model="formData.password" />
          </div>
        </div>
        <button type="submit" class="signup-sign-in-btn" @click="Complete">
          로그인
        </button>
        <div class="signup-social">
          <button type="submit" class="signup-social-btn" @click="signInGoogle">
            <img src="../assets/images/google.png" alt="구글 로그인" class="signup-social-images" />
          </button>
          <button type="submit" class="signup-social-btn" @click="signInNaver">
            <img src="../assets/images/naver-icon.png" alt="네이버 로그인" class="signup-social-images" />
          </button>
          <button type="submit" class="signup-social-btn" @click="signInKakao">
            <img src="../assets/images/kakao-icon.png" alt="카카오 로그인" class="signup-social-images" />
          </button>
        </div>
        <p class="signup-account">
          아직 회원이 아니신가요?<span class="signup-join" @click="createAccount">회원가입</span>
        </p>
      </form>
    </div>
  </main>
</template>

<script>
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { ref } from "vue";
import { toRaw } from "vue";

export default {
  name: "SignIn",

  setup() {
    const router = useRouter();
    const store = useStore();

    const formData = ref({
      id: "",
      password: "",
    });

    console.log(toRaw(formData));
    const loading = ref(false); // 로딩 상태

    // 로그인 성공 시 메인으로 이동
    const Complete = async () => {
      if (!formData.value.id || !formData.value.password) {
        alert("아이디와 비밀번호를 입력해주세요.");
        return;
      }

      loading.value = true;   // 요청 시작 시 로딩 상태 설정

      try {
        await store.dispatch('isLogged/signin', {
          id: formData.value.id,
          password: formData.value.password,
        });

        if (store.getters['isLogged/Loggedin']) {
          alert("로그인 성공");
          router.push({ name: "Home" });
        } else {
          alert("아이디 또는 비밀번호가 틀립니다.");
        }
      } catch (error) {
        console.error("로그인 요청 중 오류 발생", error);
        alert("서버와의 요청에 실패");
      } finally {
        loading.value = false;    // 요청 완료 후 로딩 상태 해제
      }
    };

    // 회원가입 페이지로 이동
    const createAccount = () => {
      router.push({ name: "SignUp" });
    };

    // 아이디 찾기로 이동
    const FindId = () => {
      router.push({ name: "FindId" });
    };

    // 비밀번호로 이동
    const FindPw = () => {
      router.push({ name: "FindPassword" });
    };

    return {
      Complete,
      createAccount,
      FindId,
      FindPw,
      formData,
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

.sign-in-container {
  width: 500px;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid rgba(0, 0, 0, 0.247);
}

h1 {
  text-align: center;
  font-weight: bold;
  margin: 8px 0;
}

.input-block {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 10px 0;
  width: 100%;
}

.signup-pw {
  position: relative;
  top: 10px;
}

.signup-label {
  margin-bottom: 10px;
  font-weight: bold;
}

.signup-field {
  display: flex;
  width: 404px;
  height: 35px;
}

.signup-field-input {
  flex: 1;
  padding: 5px 0px 5px 10px;
  border: 1px solid rgba(0, 0, 0, 0.253);
  border-radius: 0.1875rem;
  box-sizing: border-box;
  margin-right: 10px;
  width: 100px;
  height: 40px;
  outline: none;
}

.signup-field-input:hover {
  border: 1px solid black;
}

.signup-id-find {
  margin-left: 283px;
  font-size: 12px;
  color: #1a73e8;
  cursor: pointer;
}

.signup-pw-find {
  margin-left: 243px;
  font-size: 12px;
  color: #1a73e8;
  cursor: pointer;
}

.signup-sign-in-btn {
  width: 394px;
  height: 40px;
  margin-top: 40px;
  border-radius: 0.1875rem;
  border: 1px solid;
  text-align: center;
  background-color: #1a73e8;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.signup-social {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
  width: 394px;
}

.signup-social-btn {
  width: 56px;
  height: 52px;
  display: flex;
  align-items: center;
  border: none;
  cursor: pointer;
  background-color: white;
  border-radius: 100%;
  font-weight: bold;
}

.signup-social-images {
  /* width: 60px; */
  width: 100%;
  /* height: 48px; */
  height: 100%;
  border-radius: 100%;
}

.signup-account {
  font-size: 14px;
  margin-top: 35px;
  margin-left: 80px;
  position: relative;
}

.signup-join {
  margin-left: 20px;
  color: #1a73e8;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
}
</style>
