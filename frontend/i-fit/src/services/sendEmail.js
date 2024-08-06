import { ref } from "vue";
import axios from "axios";
import { useTimer } from "@/utils/timer";

export function useEmail() {
  //   const id = ref("");
  const email = ref("");
  const enteredCode = ref(""); // 사용자가 입력한 인증 코드 저장
  const sendStatus = ref(null); // 이메일 전송 상태 저장
  const emailKey = ref(""); // 서버에서 받은 인증 키 저장
  const { timeLeft, minutes, seconds, start, reset, timerStarted } =
    useTimer(2); // 타이머 관련 상태와 메서드를 가져옴

  const sendEmail = async () => {
    if (email.value === "") {
      alert("이메일을 입력해주세요.");
      return;
    }

    const form = new FormData();
    form.append("email", email.value);

    try {
      const response = await axios.post("", form); //api 주소 넣기
      if (response.data.exist) {
        alert(response.data.exist);
      } else if (response.status === 200) {
        alert("이메일이 발송되었습니다.");
        start();
        emailKey.value = response.data.key; // 서버에서 받은 인증 키 값을 저장
      } else {
        alert("잘못된 이메일입니다");
      }
      sendStatus.value = response.status === 200 ? "success" : "failed";
    } catch (error) {
      sendStatus.value = "failed";
    }
  };

  const emailCheck = () => {
    return new Promise((resolve) => {
      if (enteredCode.value === emailKey.value) {
        alert("확인 완료");
        resolve("확인 완료");
      } else {
        alert("인증번호가 일치하지 않습니다.");
        resolve("인증번호 불일치");
      }
    });
  };

  const handleReRequest = async () => {
    reset();
    await sendEmail();
  };

  return {
    email,
    enteredCode,
    emailKey,
    sendEmail,
    emailCheck,
    handleReRequest,

    timeLeft,
    minutes,
    seconds,
    timerStarted,
  };
}

export default useEmail;
