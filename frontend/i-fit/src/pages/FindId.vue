<template>
  <main>
    <div class="finder-box">
      <form action="#" class="finder-form">
        <h1>아이디 찾기</h1>
        <p class="finder-text">아이디를 잊어버리셨나요?</p>
        <div class="finder-id input-block">
          <label class="finder-label" for="id">이름</label>
          <div class="finder-field">
            <input type="text" id="id" name="id" placeholder="이름을 입력하세요." class="finder-field-input" v-model="name" />
            <button class="finder-field-btn" type="submit">확인</button>
          </div>
        </div>
        <div class="finder-email input-block">
          <label class="finder-label" for="email">이메일</label>
          <div class="finder-field">
            <input type="email" id="email" name="email" placeholder="이메일을 입력하세요." class="finder-field-input"
              v-model="email" />
            <button class="finder-field-btn" type="submit" @click="sendEmail">
              인증요청
            </button>
          </div>
        </div>
        <div class="finder-email-auth input-block">
          <label class="finder-label" for="email-number">이메일 인증번호
            <span v-if="timerStarted && timeLeft > 0" class="timer">{{ minutes }}:{{ seconds }}</span>
            <button v-if="!timerStarted && timeLeft === 0" class="re-request-btn" @click="handleReRequest">
              재전송
            </button>
          </label>
          <div class="finder-field">
            <input type="text" id="email-number" name="email-number" placeholder="인증번호를 입력해주세요."
              class="finder-field-input" v-model="enteredCode" />
            <button class="finder-field-btn" type="submit" @click="emailCheck">
              확인
            </button>
          </div>
        </div>
        <button type="submit" class="finder-id-btn" @click="findComplete">
          아이디 찾기
        </button>
      </form>
    </div>
  </main>
</template>

<script>
import { useRouter } from "vue-router";
import { useEmail } from "@/services/sendEmail";
import { default as axios } from "axios";

export default {
  name: "FindId",

  setup() {
    const { email, sendEmail } = useEmail();

    const router = useRouter();

    const findComplete = async () => {
      await sendEmail();

      try {
        // 이메일로 아이디 요청
        const response = await axios.post("서버_주소_아이디_찾기", {
          email: email.value,
        });

        if (response.data.id) {
          // 아이디 찾기 성공
          router.push({
            name: "FindIdComplete",
            params: { userId: response.data.id },
          });
        } else {
          alert("해당 이메일로 찾을 수 있는 아이디가 없습니다.");
        }
      } catch (error) {
        console.error("아이디 찾기 요청 중 오류 발생:", error);
        alert("서버와 통신 오류");
      }
    };

    return {
      findComplete,
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

main {
  width: 100%;
  height: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.finder-box {
  width: 500px;
  height: 500px;
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

.finder-label {
  margin-bottom: 10px;
  font-weight: bold;
}

.finder-field {
  display: flex;
  text-align: center;
  width: 404px;
  height: 35px;
}

.finder-field-input {
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

.finder-field-btn {
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
  margin: 50px 0px 0px 0px;
}

.finder-id-btn {
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
</style>
