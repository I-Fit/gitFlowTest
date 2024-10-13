<template>
  <main>
    <div class="content-container">
      <div class="payment-info">
        <!-- 결제 정보 섹션 -->
        <h2>결제 정보</h2>
        <p>이름: {{ username }}</p>
        <p>결제 상품: iFit 멤버십 - {{ membershipInfo.grade }}</p>
        <p>결제 금액: {{ membershipInfo.price.toLocaleString() }}원</p>
      </div>

      <div class="discount-section">
        <h2>할인 적용</h2>
        <div class="coupon-section">
          <label for="coupon">쿠폰 선택:</label>
          <select v-model="selectedCoupon" @change="applyCoupon">
            <option :value="null">쿠폰을 선택하세요</option>
            <option v-for="coupon in coupons" :key="coupon.id" :value="coupon">
              {{ coupon.name }} ({{ coupon.percentage }}% 할인)
            </option>
          </select>
        </div>
        <div class="point-section">
          <label for="point">포인트 사용:</label>
          <input type="number" v-model.number="pointsToUse" @input="applyPoints" :max="points" />
          <p>사용 가능 포인트: {{ points }}</p>
        </div>
      </div>

      <div class="final-payment">
        <h2>최종 결제 금액</h2>
        <p>멤버십 금액: {{ membershipInfo.price.toLocaleString() }}원</p>
        <p>쿠폰 할인: {{ couponDiscount.toLocaleString() }}원</p>
        <p>포인트 사용: {{ pointsToUse.toLocaleString() }}원</p>
        <p>최종 금액: {{ finalAmount.toLocaleString() }}원</p>
      </div>

      <div class="payment-options">
        <h2>결제 수단 선택</h2>
        <div class="button-container">
          <button @click="showPreparingMessage('일반 카드 결제')" class="card-payment-button">
            일반 카드 결제
          </button>
          <button @click="initiateKakaoPayment" :disabled="!canProceedPayment || isLoading" class="kakao-pay-button">
            {{ isLoading ? "처리 중..." : "카카오페이" }}
          </button>
          <button @click="showPreparingMessage('네이버페이')" class="naver-pay-button">
            네이버페이
          </button>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { ref, reactive, computed, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import apiClient from "@/api/apiClient";

export default {
  name: "Payment",

  setup() {
    const router = useRouter();
    const route = useRoute();

    const username = ref(route.query.username || "");
    const membershipInfo = reactive(
      JSON.parse(route.query.membershipInfo || '{"grade":"", "price":0}')
    );
    const points = ref(Number(route.query.points) || 0);
    const coupons = ref(JSON.parse(route.query.coupons || "[]"));

    const selectedCoupon = ref(null);
    const pointsToUse = ref(0);
    const isLoading = ref(false);

    const couponDiscount = computed(() => {
      if (selectedCoupon.value) {
        return Math.round(
          membershipInfo.price * (selectedCoupon.value.percentage / 100)
        );
      }
      return 0;
    });

    const finalAmount = computed(() => {
      return Math.max(
        membershipInfo.price - couponDiscount.value - pointsToUse.value,
        0
      );
    });

    const canProceedPayment = computed(() => finalAmount.value > 0);

    const applyCoupon = () => {
      // 쿠폰 적용 로직 (필요한 경우 서버와 통신)
    };

    const applyPoints = () => {
      if (pointsToUse.value > points.value) {
        pointsToUse.value = points.value;
      }
      if (pointsToUse.value > membershipInfo.price - couponDiscount.value) {
        pointsToUse.value = membershipInfo.price - couponDiscount.value;
      }
    };

    const validatePaymentData = () => {
      if (!username.value || !membershipInfo.grade || finalAmount.value <= 0) {
        throw new Error("필수 결제 정보가 누락되었습니다.");
      }
    };

    const showPreparingMessage = (paymentMethod) => {
      alert(
        `${paymentMethod} 기능은 현재 준비 중입니다. 불편을 드려 죄송합니다.`
      );
    };

    const initiateKakaoPayment = async () => {
      try {
        isLoading.value = true;
        validatePaymentData();

        const kakaoPayReadyDtoReq = {
          membershipGrade: membershipInfo.grade,
          totalAmount: finalAmount.value,
          couponId: selectedCoupon.value?.id || null,
          pointsUsed: pointsToUse.value,
          partnerUserId: username.value,
          //approvalUrl: `${window.location.origin}/payment/success`,
        };

        console.log("Sending request to Kakao Pay:", kakaoPayReadyDtoReq);

        const response = await apiClient.post(
          "/payment/kakao/ready",
          kakaoPayReadyDtoReq
        );

        console.log("Received response from Kakao Pay:", response.data);

        if (response.data && response.data.next_redirect_pc_url) {
          localStorage.setItem(
            "kakaoPaymentInfo",
            JSON.stringify({
              tid: response.data.tid,
              partnerOrderId: response.data.partner_order_id,
            })
          );

          window.location.href = response.data.next_redirect_pc_url;
        } else {
          throw new Error("서버로부터 유효하지 않은 응답을 받았습니다.");
        }
      } catch (error) {
        console.error("카카오페이 결제 초기화 중 오류 발생:", error);
        if (error.response) {
          console.error("Error response:", error.response.data);
          console.error("Error status:", error.response.status);
          console.error("Error headers:", error.response.headers);
        } else if (error.request) {
          console.error("Error request:", error.request);
        } else {
          console.error("Error message:", error.message);
        }
        alert(`결제 초기화 중 오류가 발생했습니다: ${error.message}`);
      } finally {
        isLoading.value = false;
      }
    };

    const handleKakaoPaymentComplete = async () => {
      const pgToken = route.query.pg_token;
      if (pgToken) {
        try {
          isLoading.value = true;
          const storedPaymentInfo = JSON.parse(
            localStorage.getItem("kakaoPaymentInfo") || "{}"
          );

          const kakaoPayApproveDtoReq = {
            tid: storedPaymentInfo.tid,
            pgToken: pgToken,
            partnerOrderId: storedPaymentInfo.partnerOrderId,
            partnerUserId: username.value,
          };

          const response = await apiClient.post(
            "/payment/kakao/approve",
            kakaoPayApproveDtoReq
          );

          if (response.data && response.data.aid) {
            alert("카카오페이 결제가 성공적으로 완료되었습니다.");
            localStorage.removeItem("kakaoPaymentInfo");
            router.push({ name: "MyPage" });
          } else {
            throw new Error("서버로부터 유효하지 않은 응답을 받았습니다.");
          }
        } catch (error) {
          console.error("카카오페이 결제 승인 중 오류 발생:", error);
          alert(
            `카카오페이 결제 승인 중 오류가 발생했습니다: ${error.message}`
          );
          router.push({ name: "PaymentFail" });
        } finally {
          isLoading.value = false;
        }
      }
    };

    onMounted(() => {
      if (route.name === 'PaymentSuccess') {
        handleKakaoPaymentComplete();
      }
    });

    return {
      username,
      membershipInfo,
      points,
      coupons,
      selectedCoupon,
      pointsToUse,
      couponDiscount,
      finalAmount,
      canProceedPayment,
      isLoading,
      applyCoupon,
      applyPoints,
      initiateKakaoPayment,
      showPreparingMessage,
      handleKakaoPaymentComplete,
    };
  },
};
</script>

<style scoped>
main {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 20px;
}

.content-container {
  width: 100%;
  max-width: 600px;
}

.payment-info,
.discount-section,
.final-payment,
.payment-options {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

h2 {
  margin-bottom: 10px;
  color: #333;
}

select,
input {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.button-container {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.payment-options button {
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  white-space: nowrap;
  transition: background-color 0.3s, opacity 0.3s;
}

.card-payment-button {
  background-color: #3498db;
  /* 새로운 파란색 */
  color: white;
}

.card-payment-button:hover {
  background-color: #2980b9;
  /* 호버 시 더 진한 파란색 */
}

.kakao-pay-button {
  background-color: #fee500;
  color: #000;
}

.kakao-pay-button:hover {
  opacity: 0.9;
}

.naver-pay-button {
  background-color: #1ec800;
  color: white;
}

.naver-pay-button:hover {
  background-color: #1ab000;
}

button:disabled {
  background-color: #ddd;
  color: #666;
  cursor: not-allowed;
}

button:disabled:hover {
  opacity: 1;
}
</style>
