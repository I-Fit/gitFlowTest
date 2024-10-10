import { ref } from "vue";
import axios from "axios";
import { useTimer } from "@/utils/timer";
import apiClient from "@/api/apiClient";

export function useEmail() {
  const sendStatus = ref(null); // 이메일 전송 상태 저장
  const loading = ref(false);
  const { timeLeft, minutes, seconds, start, reset, timerStarted } =
    useTimer(300); // 타이머 관련 상태와 메서드를 가져옴

  const sendEmail = async (email) => {
    if (email == "") {
      alert("이메일을 입력해주세요.");
      return;
    }
    loading.value = true;
    try {
      const response = await axios.post("/api/sendVerificationCode", {
        email: email, // data 키를 제거하고 직접 이메일을 전송
      });

      // 서버의 응답 메시지를 사용자에게 보여줌
      alert(response.data); // 서버에서 보낸 메시지를 그대로 사용

      if (response.status === 200) {
        start(); // 타이머 시작
      } else {
        alert("잘못된 이메일입니다");
      }
      sendStatus.value = response.status === 200 ? "success" : "failed";
    } catch (error) {
      sendStatus.value = "failed";
      alert("이메일 전송에 실패했습니다."); // 에러 메시지 추가
    } finally {
      loading.value = false;
    }
  };

  const handleReRequest = async () => {
    reset();
    await sendEmail();
  };

  const verifyEmail = async (email, enteredCode) => {
    try {
      const response = await axios.post("/api/verifyEmail", {
        email: email,
        enteredCode: enteredCode,
      });
      if (response.status === 200) {
        return true;
      } else {
        alert("인증 실패: " + response.data.message);
        return false;
      }
    } catch (error) {
      alert("인증 요청에 실패했습니다.");
      return false;
    }
  };

  // ==============================================================

  const sendEmailToken = async (email) => {
    if (email == "") {
      alert("이메일을 입력해주세요.");
      return;
    }
    loading.value = true;
    try {
      const response = await apiClient.post(
        "/updateEmail/sendVerificationCode",
        {
          email: email, // data 키를 제거하고 직접 이메일을 전송
        }
      );

      // 서버의 응답 메시지를 사용자에게 보여줌
      alert(response.data); // 서버에서 보낸 메시지를 그대로 사용

      if (response.status === 200) {
        start(); // 타이머 시작
      } else {
        alert("잘못된 이메일입니다");
      }
      sendStatus.value = response.status === 200 ? "success" : "failed";
    } catch (error) {
      sendStatus.value = "failed";
      alert("이메일 전송에 실패했습니다."); // 에러 메시지 추가
    } finally {
      loading.value = false;
    }
  };

  const handleReRequestToken = async () => {
    reset();
    await sendEmailToken();
  };

  const verifyEmailToken = async (email, enteredCode) => {
    try {
      const response = await apiClient.post("/updateEmail/verifyEmail", {
        email: email,
        enteredCode: enteredCode,
      });
      if (response.status === 200) {
        return true;
      } else {
        alert("인증 실패: " + response.data.message);
        return false;
      }
    } catch (error) {
      alert("인증 요청에 실패했습니다.");
      return false;
    }
  };

  return {
    sendEmail,
    handleReRequest,
    verifyEmail,

    sendEmailToken,
    handleReRequestToken,
    verifyEmailToken,

    timeLeft,
    minutes,
    seconds,
    timerStarted,
  };
}

export default useEmail;
