<template>
  <main>
    <div class="finder-box">
      <h1>비밀번호 재설정</h1>
      <span class="finder-text">비밀번호를 잊어버리셨나요?</span>
      <form action="#" class="finder-form">
        <div class="finder-id input-block">
          <label class="finder-label" for="id">아이디</label>
          <div class="finder-field">
            <input type="text" id="id" name="id" placeholder="아이디를 입력하세요." class="finder-field-input"
              v-model="loginId" />
            <button class="finder-field-btn" type="button" @click="checkId">확인</button>
          </div>
        </div>
        <div class="finder-email input-block">
          <label class="finder-label" for="email">이메일</label>
          <div class="finder-field">
            <input type="text" id="email" name="email" placeholder="이메일을 입력하세요." class="finder-field-input"
              v-model="email" />
            <button class="finder-field-btn" type="button" @click="sendEmailRequest">
              인증 요청
            </button>
          </div>
        </div>
        <div class="finder-email-auth input-block">
          <label class="finder-label" for="email-auth">이메일 인증번호
            <span v-if="timerStarted && timeLeft > 0" class="timer">{{ minutes }}:{{ seconds }}</span>
            <button v-if="!timerStarted && timeLeft === 0" class="re-request-btn" @click="handleReRequest">
              재전송
            </button>
          </label>
          <div class="finder-field">
            <input type="text" id="email-auth" name="email-auth" placeholder="인증번호를 입력해주세요." class="finder-field-input"
              v-model="enteredCode" />
            <button class="finder-field-btn" type="button" @click="updateEmailAfterCheck">
              확인
            </button>
          </div>
        </div>
        <div class="finder-pw input-block">
          <label class="finder-label" for="password">새 비밀번호</label>
          <div class="finder-field">
            <input type="text" id="password" name="password" placeholder="비밀번호를 입력하세요." class="finder-field-input"
              v-model="password" />
            <!-- <button class="finder-field-btn" type="submit">확인</button> -->
          </div>
        </div>
        <div class="finder-pw-check input-block">
          <label class="finder-label" for="pw-check">새 비밀번호 확인
            <span v-if="Bothpasswords && passwordMatch" class="password-same">일치합니다.</span>
            <span v-else-if="Bothpasswords && !passwordMatch" class="password-different">일치하지 않습니다.</span>
          </label>
          <div class="finder-field">
            <input type="text" id="password-check" name="pw-check" placeholder="비밀번호 재입력" class="finder-field-input"
              v-model="passwordCheck" />
            <!-- <button class="finder-field-btn" type="submit">확인</button> -->
          </div>
        </div>
        <button type="button" class="finder-pw-btn" @click="goSignIn">
          비밀번호 변경완료
        </button>
      </form>
    </div>
  </main>
</template>

<script>
import { useRouter } from "vue-router";
import { useEmail } from "@/services/sendEmail";
import { ref, computed } from "vue";
import axios from "axios";

export default {
  name: "FindPassword",

  setup() {
    const router = useRouter();

    const loginId = ref("");
    const password = ref("");
    const passwordCheck = ref("");
    const email = ref("");
    const enteredCode = ref("");

    console.log(`${loginId.value}`);

    const checkId = async () => {
      try {
        const response = await axios.post("/api/password/check-id", {
          loginId: loginId.value,
        });
        alert(response.data);
      } catch (error) {
        alert("아이디 확인 실패");
      }
    };

    const {
      sendEmail,
      timeLeft,
      minutes,
      seconds,
      handleReRequest,
      timerStarted,
    } = useEmail();

    const sendEmailRequest = async () => {
      try {
        await sendEmail(email.value);
      } catch (error) {
        alert("이메일 전송에 실패했습니다.");
      }
    };

    //  이메일 인증 번호 인증 확인
    const updateEmailAfterCheck = async () => {
      try {
        const response = await axios.post("/api/password/emailVerification", {
          loginId: loginId.value,
          email: email.value,
          enteredCode: enteredCode.value,
        });
        alert(response.data);
      } catch (error) {
        alert("이메일 인증 확인 실패");
      }
    };

    const passwordMatch = computed(() => {
      return password.value === passwordCheck.value;
    });
    const Bothpasswords = computed(() => {
      return password.value && passwordCheck.value;
    });

    const goSignIn = async () => {
      if (!passwordMatch.value) {
        alert("비밀번호 확인이 되지 않았습니다.");
        return;
      }

      try {
        const response = await axios.post("/api/password/modified", {
          email: email.value,
          loginId: loginId.value,
          passwordCheck: passwordCheck.value,
          password: password.value,
        });

        if (response.status === 200) {
          alert(response.data);
          router.push({ name: "SignIn" });
        } else {
          alert("비밀번호 변경에 실패했습니다.");
        }
      } catch (error) {
        console.error("서버와의 통신 오류 발생");
      }
    };

    return {
      checkId,
      loginId,
      email,
      enteredCode,
      password,
      passwordCheck,
      passwordMatch,
      Bothpasswords,
      goSignIn,
      sendEmail,

      timeLeft,
      minutes,
      seconds,
      timerStarted,
      handleReRequest,
      updateEmailAfterCheck,
      sendEmailRequest,
    };
  },
};
</script>

<style scoped>
.same-block {
  display: flex;
}

.password-same {
  margin-left: 194px;
  font-size: 14px;
  color: blue;
}

.password-different {
  margin-left: 148px;
  font-size: 14px;
  color: red;
}

.timer {
  color: red;
  font-size: 14px;
  margin-left: 170px;
}

.re-request-btn {
  margin-left: 170px;
  border: none;
  background-color: white;
  color: blue;
}

main {
  width: 100%;
  height: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.finder-box {
  width: 500px;
  height: 80%;
  border-radius: 4px;
  display: flex;
  align-items: center;
  flex-direction: column;
  margin-top: 30px;
  border: 1px solid rgba(0, 0, 0, 0.164);
}

h1 {
  margin-top: 21px;
  font-size: 30px;
}

.finder-text {
  margin-top: 20px;
  /* margin-bottom: 30px; */
  font-size: 15px;
  color: grey;
}

.input-block {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 15px 0;
  width: 100%;
  text-align: center;
}

.finder-label {
  margin-bottom: 10px;
  font-weight: bold;
}

.finder-field {
  display: flex;
  align-items: center;
  width: 404px;
  height: 40px;
}

.finder-field-input {
  flex: 1;
  padding: 5px 0 5px 10px;
  border: 1px solid rgba(0, 0, 0, 0.253);
  border-radius: 0.1875rem;
  box-sizing: border-box;
  width: 100px;
  height: 40px;
  outline: none;
}

.finder-field-input:hover {
  border: 1px solid black;
}

.finder-field-btn {
  padding: 8px 16px;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 0.1875rem;
  margin-left: 10px;
  font-size: 14px;
  cursor: pointer;
  font-weight: bold;
  height: 39px;
}

input::placeholder {
  font-size: small;
}

.finder-field-domain {
  margin-right: 10px;
  margin-left: 10px;
  padding: 8px;
  border: 1px solid rgba(0, 0, 0, 0.253);
  border-radius: 10px;
  background-color: #fff !important;
  font-size: 12.3px;
}

.finder-pw-btn {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 0.1875rem;
  background-color: #1a73e8;
  color: white;
  margin-top: 20px;
  font-weight: bold;
  cursor: pointer;
}
</style>
