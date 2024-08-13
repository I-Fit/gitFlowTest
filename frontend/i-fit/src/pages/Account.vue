<template>
  <main>
    <form @submit.prevent="handleSubmit" method="post" class="signup">
      <div class="signup-container">
        <h1>회원가입</h1>
        <p>회원이 되어 다양한 헤택을 경험해 보세요!</p>
        <div class="choose-member">
          <label class="general-user">
            <input type="radio" name="contact" value="user" v-model="formData.contact" />
            <span>일반 회원</span>
          </label>
          <label>
            <input type="radio" name="contact" value="teacher" v-model="formData.contact" />
            <span>강사</span>
          </label>
        </div>
        <div class="signup-id input-block">
          <label class="signup-label" for="id">아이디
            <span class="available" v-if="idAvailable === true">사용 가능한 아이디입니다.</span>
            <span class="notavailable" v-else-if="idAvailable === false">
              이미 사용 중인 아이디입니다.
            </span>
          </label>
          <div class="signup-field">
            <input type="text" id="id" name="id" placeholder="아이디를 입력하세요." class="signup-input" v-model="formData.id" />
            <button type="submit" class="signup-btn" @click="checkId">
              중복 확인
            </button>
          </div>
        </div>
        <div class="signup-pw input-block">
          <label class="signup-label" for="password">비밀번호</label>
          <div class="signup-field">
            <input type="password" id="password" name="password" placeholder="비밀번호를 입력하세요." class="signup-input"
              v-model="formData.password" />
          </div>
        </div>
        <div class="signup-pw-check input-block">
          <label class="signup-label" for="password-check">비밀번호 확인
            <!-- 일치하면 메세지 -->
            <span v-if="Bothpasswords && passwordsMatch" class="password-match">일치합니다</span>
            <!-- 불일치하면 메세지 -->
            <span v-else-if="Bothpasswords && !passwordsMatch" class="password-mismatch">일치하지 않습니다</span>
          </label>
          <div class="signup-field">
            <input type="password" id="password-check" name="passwordcheck" placeholder="비밀번호 재입력" class="signup-input"
              v-model="formData.passwordCheck" />
          </div>
        </div>
        <div class="signup-name input-block">
          <label class="signup-label" for="name">이름</label>
          <div class="signup-field">
            <input type="text" id="name" name="name" placeholder="이름을 입력해주세요." class="signup-input"
              v-model="formData.name" />
          </div>
        </div>
        <div class="signup-phone input-block">
          <label class="signup-label" for="phone">전화번호</label>
          <div class="signup-field">
            <input type="text" id="phone" name="phone" placeholder="전화번호를 입력해주세요." class="signup-input"
              v-model="formData.phone" />
          </div>
        </div>
        <div class="signup-email input-block">
          <label class="signup-label" for="email">이메일</label>
          <div class="signup-field">
            <input type="text" id="email" name="email" placeholder="이메일을 입력해주세요." class="signup-input"
              v-model="email" />
            <button type="submit" class="signup-btn" @click="sendEmail" :disabled="timerStarted">
              인증 요청
            </button>
          </div>
        </div>
        <div class="signup-email-auth input-block">
          <label class="signup-label" for="email-number">이메일 인증번호
            <span v-if="timerStarted && timeLeft > 0" class="timer">{{ minutes }}:{{ seconds }}</span>
            <button v-if="!timerStarted && timeLeft === 0" class="re-request-btn" @click="handleReRequest">
              재전송
            </button>
          </label>
          <div class="signup-field">
            <input type="text" id="email-number" name="email-number" placeholder="인증번호를 입력해주세요." class="signup-input"
              v-model="enteredCode" />
            <button type="submit" class="signup-btn" @click="updateEmailAfterCheck" :disabled="timerStarted && timeLeft > 0">
              확인
            </button>
          </div>
        </div>
        <div class="signup-terms">
          <label class="signup-terms-label">
            <input type="checkbox" name="terms" value="agree" v-model="agreeAll" @click="selectAll" />
            <span class="signup-terms-text">이용약관 전체 동의<span @click="goTos">&#62;</span></span>
          </label>
          <label class="signup-terms-label">
            <input type="checkbox" name="terms1" value="agree-1" id="agree01" v-model="agree01" />
            <span class="signup-terms-text">이용약관 동의(필수)<span @click="goTos">&#62;</span></span>
          </label>
          <label class="signup-terms-label">
            <input type="checkbox" name="terms2" value="agree-2" id="agree02" v-model="agree02" />
            <span class="signup-terms-text">개인정보 수집 및 이용 동의(필수)<span @click="goTos">&#62;</span></span>
          </label>
          <label class="signup-terms-label">
            <input type="checkbox" name="terms3" value="agree-3" id="agree03" v-model="agree03" />
            <span class="signup-terms-text">프로모션 정보 수신 동의(선택)<span @click="goTos">&#62;</span></span>
          </label>
        </div>
        <button class="signup-terms-btn" type="submit" @click="account">
          회원가입
        </button>
        <p class="signup-account-check">
          이미 계정이 있으신가요?<span class="signup-account-check-text" @click="goLogin">로그인 하기</span>
        </p>
      </div>
    </form>
  </main>
</template>

<script>
import { ref, computed, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useEmail } from "@/services/sendEmail";
import axios from "axios";
import { toRaw } from "vue";

export default {
  name: "Account",

  setup() {
    const router = useRouter();

    const formData = ref({
      contact: "",
      id: "",
      password: "",
      passwordCheck: "",
      name: "",
      phone: "",
      email: "",
    });
    
    // formData에 값 콘솔에서 볼려고 작성함
    console.log(toRaw(formData));
    // 아이디 사용 가능 여부 저장 상태
    const idAvailable = ref(null);

    const passwordsMatch = computed(() => {
      return formData.value.password === formData.value.passwordCheck;
    })
    // 패스워드 입력이 끝난 후 메시지 표시
    const Bothpasswords = computed(
      () => formData.value.password && formData.value.passwordCheck
    );

    // 아이디 중복 확인 요청
    const checkId = async () => {
      try {
        const response = await axios.get("/api/check-id", {
          params: { id: formData.value.id },
        });
        idAvailable.value = response.data.available;
      } catch (error) {
        console.error(error);
        idAvailable.value = false;
      }
    };

    // 회원가입 버튼 클릭 시 사용자 데이터(formData) 서버에 보냄
    const account = async () => {
      if (!passwordsMatch.value) {
        alert("비밀번호가 불일치 합니다.");
        return;
      }
      if (!idAvailable.value) {
        alert("아이디 중복 확인 해주세요.");
        return;
      }

      try {
        await axios.post("/api/user-account", formData.value);
        alert("회원가입 성공!");
        router.push({ name: "Login" });
      } catch (error) {
        console.log("회원가입 시 서버와 통신 에러");
      }
    };

    const goTos = () => {
      router.push({ name: "Tos" });
    };

    const goLogin = () => {
      router.push({ name: "Login" });
    };

    const selectAll = () => {
      const allSelected = agree01.value && agree02.value && agree03.value;
      agree01.value = !allSelected;
      agree02.value = !allSelected;
      agree03.value = !allSelected;
    };

    // tos에서 선택한 동의 선택 데이터 가져오기
    const route = useRoute();
    const query = route.query;

    const agree01 = ref(query.agree01 === "true");
    const agree02 = ref(query.agree02 === "true");
    const agree03 = ref(query.agree03 === "true");
    const agreeAll = ref(query.agreeAll === "true");

    const {
      email,
      enteredCode,
      emailKey,
      sendEmail,
      emailCheck,
      timeLeft,
      minutes,
      seconds,
      handleReRequest,
      timerStarted,
    } = useEmail();

    // 이메일이 변경되면 formData를 업데이트
    watch(email, (newEmail) => {
      if (newEmail) {
        formData.value.email = newEmail;
      }
    });

    const updateEmailAfterCheck = async () => {
      try {
        const result = await emailCheck();
        if (result === "확인 완료") {
          formData.value.email = email.value; // 인증 완료 후 email을 formData에 업데이트
        }
      } catch (error) {
        console.error("이메일 인증 오류: ", error);
      }
    };

    return {
      goTos,
      goLogin,
      account,

      agreeAll,
      agree01,
      agree02,
      agree03,
      selectAll,

      email,
      emailKey,
      updateEmailAfterCheck,

      timeLeft,
      minutes,
      seconds,
      timerStarted,
      enteredCode,
      sendEmail,
      emailCheck,
      handleReRequest,

      formData,
      idAvailable,
      checkId,
      passwordsMatch,
      Bothpasswords,
    };
  },
};
</script>

<style scoped>
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

.password-match {
  margin-left: 225px;
  font-size: 14px;
  color: blue;
}

.password-mismatch {
  margin-left: 180px;
  font-size: 14px;
  color: red;
}

main {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 1400px;
}

.signup {
  margin-top: 50px;
}

.signup-container {
  width: 500px;
  height: 1190px;
  text-align: center;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(0, 0, 0, 0.164);
}

.signup-container h1 {
  margin: 22px 0px;
}

.signup-container p {
  margin: 20px 0px;
}

.general-user::after {
  content: "|";
  color: rgba(0, 0, 0, 0.356);
  margin: 5px;
}

.input-block {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

.signup-label {
  margin-bottom: 10px;
  font-weight: bold;
}

/* 아이디 중복 확인 메시지 위치 */
.available {
  font-size: 14px;
  margin-left: 84px;
}

.notavailable {
  margin-left: 66px;
  font-size: 14px;
}

.signup-field {
  display: flex;
  width: 404px;
  height: 35px;
}

.signup-input {
  flex: 1;
  padding: 5px 0px 5px 10px;
  border: 1px solid rgba(0, 0, 0, 0.253);
  border-radius: 10px;
  box-sizing: border-box;
  width: 100px;
  height: 35px;
  outline: none;
}

.signup-btn {
  padding: 8px 16px;
  margin-left: 10px;
  font-weight: bold;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 10px;
  cursor: pointer;
}

.signup-id,
.signup-pw,
.signup-pw-check,
.signup-name,
.signup-phone,
.signup-email,
.signup-email-auth,
.signup-account-check {
  margin: 30px 0px 0px 40px;
}

input::placeholder {
  font-size: small;
}

.at {
  margin: 8px 0px 5px 10px;
}

.signup-email-domain {
  margin-left: 10px;
  padding: 8px;
  border: 1px solid rgba(0, 0, 0, 0.253);
  background-color: white !important;
  border-radius: 10px;
  font-size: 13.3333px;
  color: rgba(0, 0, 0, 0.616);
  outline: none;
}

.signup-terms {
  width: 380px;
  text-align: left;
  margin-top: 10px;
  margin: 30px 0px 0px 40px;
}

.signup-terms-label {
  display: block;
  margin-top: 20px;
  font-weight: bold;
}

.signup-terms-text span {
  font-size: 14px;
  font-weight: bold;
  float: right;
  cursor: pointer;
}

.signup-terms-text {
  margin-left: 5px;
  font-size: 13px;
}

.signup-terms-btn {
  width: 200px;
  height: 40px;
  border: none;
  border-radius: 10px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  font-size: 20px;
  font-weight: bold;
  margin: 40px 0px 0px 125px;
}

.signup-account-check-text {
  color: #007bff;
  margin-left: 20px;
  margin-right: 45px;
  cursor: pointer;
}
</style>
