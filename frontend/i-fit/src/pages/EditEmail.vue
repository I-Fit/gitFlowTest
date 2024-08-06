<template>
    <main>
        <div class="edit-box">
              <form @submit.prevent="handleSubmit" class="edit-form">
                <h1>이메일 변경</h1>
                <div class="edit-contact input-block">
                    <label class="edit-label" for="id">이메일 주소</label>
                    <div class="edit-field">
                        <input type="text" id="id" name="id" placeholder="변경할 이메일을 입력하세요."
                            class="edit-field-input" v-model="phoneNumber" />
                        <button class="confirm-btn" type="button" @click="sendVerification">인증</button>
                    </div>
                </div>
                <div class="auth input-block">
                    <label class="edit-label" for="auth-number">인증번호</label>
                    <div class="edit-field">
                        <input type="text" id="auth-number" name="auth-number" placeholder="메일로 전송된 인증번호를 입력해주세요."
                            class="edit-field-input" v-model="authCode" />
                        <button class="confirm-btn" type="button" @click="verifyCode">확인</button>
                    </div>
                </div>
                <button type="submit" class="completed-btn">변경 완료</button>
            </form>
        </div>
    </main>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
    name: 'EditEmailComponent',
    setup() {
        const router = useRouter();
        
        // TODO - SMTP 활용한 메일 인증 구현방식으로 수정
        const phoneNumber = ref('');
        const authCode = ref('');

        const sendVerification = () => {
            // 폰 번호로 인증 코드 전송 로직
            console.log('인증 코드 전송:', phoneNumber.value);

            // 인증번호가 전송되었다는 알림
            alert('인증번호가 전송되었습니다.');
        };

        const verifyCode = () => {
            // 인증 코드 확인 로직
            console.log('인증 코드 확인:', authCode.value);
        };

        const handleSubmit = () => {
            // 폼 데이터 처리 로직 (예: 서버로 보내기)
            console.log('폼 제출:', phoneNumber.value, authCode.value);
            
            // 폼 데이터가 유효하다고 가정하고 페이지 이동
            router.push('/mypage');
        };

        return { phoneNumber, authCode, sendVerification, verifyCode, handleSubmit };
    },
}
</script>

<style scoped>
@charset "utf-8";

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