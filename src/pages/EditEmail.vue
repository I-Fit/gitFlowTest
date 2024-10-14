<template>
  <main>
    <div class="edit-box">
      <form @submit.prevent="handleSubmit" class="edit-form">
        <h1>이메일 변경</h1>
        <div class="edit-contact input-block">
          <label class="edit-label" for="email">이메일</label>
          <div class="edit-field">
            <input type="text" id="email" name="email" placeholder="변경할 이메일을 입력하세요." class="edit-field-input"
              v-model="email" :disabled="isEmailVerified" />
            <button class="confirm-btn" type="button" @click="sendEmailRequest"
              :disabled="(timerStarted && timeLeft > 0) || isEmailVerified">
              인증
            </button>
          </div>
        </div>
        <div class="auth input-block" v-if="isEmailSent && !isEmailVerified">
          <label class="edit-label" for="auth-number">인증번호</label>
          <span v-if="timerStarted && timeLeft > 0" class="timer">{{ minutes }}:{{ seconds }}</span>
          <button v-if="!timerStarted && timeLeft === 0" class="re-request-btn" @click="handleReRequestToken">
            재전송
          </button>
          <div class="edit-field">
            <input type="text" id="auth-number" name="auth-number" placeholder="메일로 전송된 인증번호를 입력해주세요."
              class="edit-field-input" v-model="enteredCode" />
            <button class="confirm-btn" type="button" @click="updateEmailAfterCheck">
              확인
            </button>
          </div>
        </div>
        <button type="button" class="completed-btn" @click="handleSubmit">
          변경 완료
        </button>
      </form>
    </div>
  </main>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import apiClient from "@/api/apiClient";
import { useEmail } from "@/services/sendEmail";

export default {
  name: "EditEmailComponent",
  setup() {
    const router = useRouter();
    const email = ref("");
    const enteredCode = ref("");
    const userId = ref(null);
    const isEmailSent = ref(false);
    const isEmailVerified = ref(false); // 이메일 인증 여부

    const {
      sendEmailToken,
      handleReRequestToken,
      verifyEmailToken,
      timerStarted,
      timeLeft,
      minutes,
      seconds,
    } = useEmail();


    // 이메일 발송하고, 타이머 발동
    const sendEmailRequest = async () => {
      try {
        await sendEmailToken(email.value);
        isEmailSent.value = true;
      } catch (error) {
        alert("이메일 전송에 실패했습니다.");
      }
    };

    const updateEmailAfterCheck = async () => {
      try {
        const isEmailVerified = await verifyEmailToken(email.value, enteredCode.value);
        if (isEmailVerified) {
          alert("이메일 인증 완료");
        } else {
          alert("이메일 인증번호가 틀립니다.");
        } 
      } catch (error) {
        alert("이메일 인증 서버 통신 실패");
      }
    };

    const handleSubmit = async () => {
      try {
        const response = await apiClient.post("/updateEmail", {
          email: email.value,
        });

        if (response.status === 200) {
          alert("이메일 변경 성공");
          router.push({ name: "Mypage" });
        }
      } catch (error) {
        console.error("서버와의 통신 오류 발생");
      }
      
    };

    return {
      sendEmailToken,
      timeLeft,
      minutes,
      seconds,
      isEmailSent,
      verifyEmailToken,
      handleReRequestToken,
      timerStarted,
      sendEmailRequest,
      updateEmailAfterCheck,

      email,
      enteredCode,
      userId,
      isEmailVerified,
      
      handleSubmit,
    };
  },
};
</script>


<style scoped>
/* content 부분 */
main {
  width: 100%;
  height: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit-box {
  width: 500px;
  height: 400px;
  display: flex;
  justify-content: center;
  border: 1px solid rgba(0, 0, 0, 0.164);
}

.finder-text {
  text-align: center;
  margin-top: 20px;
}

.input-block {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 15px 0;
  width: 100%;
}

.edit-label {
  margin-top: 5px;
  margin-bottom: 10px;
  font-weight: bold;
}

.edit-field {
  display: flex;
  text-align: center;
  width: 404px;
  height: 35px;
}

.edit-field-input {
  flex: 1;
  padding: 5px 0px 5px 10px;
  border: 1px solid rgba(0, 0, 0, 0.253);
  border-radius: 10px;
  box-sizing: border-box;
  margin-right: 10px;
  width: 100px;
  height: 35px;
  outline: none;
}

.confirm-btn {
  width: 80px;
  padding: 8px 16px;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 10px;
  cursor: pointer;
  font-weight: bold;
}

.at {
  margin-top: 6px;
}

.finder-domain {
  margin-right: 10px;
  margin-left: 10px;
  padding: 8px;
  border: 1px solid rgba(0, 0, 0, 0.253);
  border-radius: 10px;
  background-color: #fff !important;
  font-size: 13.3333px;
  font-weight: lighter !important;
}

h1 {
  text-align: center;
  font-weight: bold;
  margin: 50px 0px 35px 0px;
}

.completed-btn {
  display: block;
  width: 404px;
  height: 40px;
  margin-top: 40px;
  border-radius: 10px;
  border: 1px solid;
  background-color: #1a73e8;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.completed-btn:hover,
.confirm-btn:hover {
  background-color: #87cefa;
}

.completed-btn:active,
.confirm-btn:active {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}
</style>
