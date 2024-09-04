<template>
  <main>
    <div class="content-container">
      <div class="payment-container">
        <div class="payment-content">
          <p class="title">결제 정보</p>
          <div class="user-info">
            <p class="username">이름 : {{ username }}</p>
            <p class="product">
              결제 상품 : iFit 멤버십 - {{ membershipType }}
            </p>
          </div>
          <p class="title">결제 수단 선택</p>
          <div class="payment-method">
            <button
              class="card-pay"
              type="submit"
              :class="{
                selected: selectedPayment === 'card',
                'payment-button': true,
              }"
              :disabled="selectedPayment !== null && selectedPayment !== 'card'"
              @click="togglePayment('card')"
            >
              카드 결제
            </button>
            <button
              class="kakao-pay"
              type="submit"
              :class="{
                selected: selectedPayment === 'kakao',
                'payment-button': true,
              }"
              :disabled="
                selectedPayment !== null && selectedPayment !== 'kakao'
              "
              @click="togglePayment('kakao')"
            >
              카카오페이
            </button>
            <button
              class="naver-pay"
              type="submit"
              :class="{
                selected: selectedPayment === 'naver',
                'payment-button': true,
              }"
              :disabled="
                selectedPayment !== null && selectedPayment !== 'naver'
              "
              @click="togglePayment('naver')"
            >
              네이버페이
            </button>
          </div>
          <div class="discount">
            <p class="title">혜택 선택</p>
            <div class="coupon-container">
              <label class="coupon" for="coupon">쿠폰</label>
              <div class="coupon-block">
                <div class="dropdown">
                  <button class="dropdown-button" @click="toggleDropdown">
                    {{
                      selectedCoupon
                        ? selectedCoupon.name
                        : "쿠폰을 선택하세요."
                    }}
                  </button>
                  <div v-if="isDropdownOpen" class="dropdown-content">
                    <div
                      v-for="coupon in coupons"
                      :key="coupon.id"
                      @click="selectCoupon(coupon)"
                      class="dropdown-item"
                    >
                      {{ coupon.name }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="point-container">
              <label class="point" for="point">포인트</label>
              <!-- 사용 가능 포인트 데이터 가져옴 -->
              <span class="available-point"> 사용 가능 포인트 : 0원 </span>
              <div class="point-block">
                <input
                  type="number"
                  class="point-input"
                  name="point-check"
                  v-model.number="pointDiscountAmount"
                  placeholder="포인트를 입력하세요."
                />
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="price-container">
        <div class="price-content">
          <p class="payment-title">멤버십 결제하기</p>
          <div class="price-box">
            <p class="title">결제 금액</p>
            <!-- <p class="product-price">멤버십 금액 : </p><p class="price-data">{{ membershipPrice }}</p>
            <p class="coupon-amount">쿠폰 할인 금액 : {{ couponDiscountAmount }}</p>
            <p class="point-amount">포인트 할인 금액 : {{ pointDiscountAmount }}</p>
            <p class="payment-amount">최종 결제 금액 : {{ finalAmount }}</p> -->

            <div class="price-row">
              <span class="price-label">멤버십 금액 :</span
              ><span class="price-value">{{ membershipPrice }}원</span>
            </div>
            <div class="price-row">
              <span class="price-label">쿠폰 할인 금액 :</span
              ><span class="price-value">{{ couponDiscountAmount }}원</span>
            </div>
            <div class="price-row">
              <span class="price-label">포인트 할인 금액 :</span
              ><span class="price-value">{{ pointDiscountAmount }}원</span>
            </div>
            <div class="price-row">
              <span class="price-label">최종 결제 금액 :</span
              ><span class="price-value">{{ finalAmount }}원</span>
            </div>
          </div>
          <button type="submit" class="payment-btn">
            위 내용을 확인하였으며, 결제 진행에 동의합니다.
          </button>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { ref, computed } from "vue";
import { useRoute } from "vue-router";

export default {
  name: "PaymentComponent",
  props: {
    username: {
      type: String,
      required: true,
    },
    couponName: {
      type: String,
      required: true,
    },
  },
  setup() {
    const route = useRoute();

    const selectedPayment = ref(null);
    const togglePayment = (paymentMethod) => {
      if (selectedPayment.value === paymentMethod) {
        // 이미 선택된 결제 수단을 다시 클릭하면 선택 해제
        selectedPayment.value = null;
      } else {
        // 새로운 결제 수단을 클릭하면 선택
        selectedPayment.value = paymentMethod;
      }
    };

    const membershipType = ref(route.query.membershipType || 0);
    const membershipPrice = ref(parseInt(route.query.membershipPrice) || 0);

    // 쿠폰 목록을 정의
    const coupons = ref([
      {
        id: 0,
        name: "회원가입 축하 쿠폰 (10% off)",
        discount: 0.1,
      },
      {
        id: 1,
        name: "생일 축하 쿠폰 (5% off)",
        discount: 0.05,
      },
    ]);

    // 드롭다운 상태와 선택한 쿠폰 상태를 정의
    const isDropdownOpen = ref(false);
    const selectedCoupon = ref(null);

    // 포인트 금액
    const pointDiscountAmount = ref(0);

    // 할인 금액을 계산하는 계산된 속성
    const couponDiscountAmount = computed(() => {
      if (selectedCoupon.value && membershipPrice.value) {
        return membershipPrice.value * selectedCoupon.value.discount;
      } else {
        return 0;
      }
    });

    // 최종 결제 금액 계산
    const finalAmount = computed(() => {
      return (
        membershipPrice.value -
        couponDiscountAmount.value -
        pointDiscountAmount.value
      );
    });

    // 드롭다운 토글 함수
    const toggleDropdown = () => {
      isDropdownOpen.value = !isDropdownOpen.value;
    };

    // 쿠폰 선택 함수
    const selectCoupon = (coupon) => {
      selectedCoupon.value = coupon;
      isDropdownOpen.value = false; // 쿠폰 선택 후 드롭다운 닫기
    };

    return {
      membershipType,
      membershipPrice,
      selectedPayment,
      togglePayment,
      coupons,
      isDropdownOpen,
      selectedCoupon,
      couponDiscountAmount,
      toggleDropdown,
      selectCoupon,
      pointDiscountAmount,
      finalAmount,
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
}

.content-container {
  width: 1536px;
  height: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.payment-container {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: left;
}

.payment-content {
  width: 580px;
  height: 100%;
  text-align: left;
  margin-top: 150px;
}

.title {
  margin: 65px 0 20px 0px;
  font-size: 24px;
  color: #8d8ba7;
  text-align: left;
  font-weight: bold;
}

.user-info {
  margin-top: 10px;
  /* margin-bottom: 10px; */
}

.username,
.product {
  color: #8d8ba7;
  font-size: 18px;
  font-weight: bold;
  text-align: left;
  margin-bottom: 20px;
}

.card-pay,
.kakao-pay,
.naver-pay {
  width: 144px;
  height: 50px;
  border: none;
  border-radius: 5px;
  background-color: #1a73e8;
  color: white;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin: 5px;
}

.card-pay:hover,
.kakao-pay:hover,
.naver-pay:hover {
  background-color: #87cefa;
}

.card-pay:active,
.kakao-pay:active,
.naver-pay:active {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}

.coupon-container,
.point-container {
  margin-top: 30px;
}

.point,
.coupon {
  font-size: 18px;
  font-weight: bold;
  color: #8d8ba7;
}

.point-input {
  margin-top: 10px;
  padding-left: 10px;
  width: 464px;
  height: 50px;
  border: 1px solid rgba(0, 0, 0, 0.281);
  border-radius: 5px;
  -moz-appearance: textfield;
}

.point-input::-webkit-inner-spin-button,
.point-input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.available-point {
  font-size: 12px;
  font-weight: bold;
  color: red;
  margin-left: 280px;
}

.price-container {
  display: flex;
  justify-self: center;
  align-items: center;
  margin-top: -20px;
}

.price-box {
  margin-bottom: 20px;
  margin-top: -10px;
}

.price-content {
  width: 490px;
  display: flex;
  flex-direction: column;
  margin-bottom: 30px;
  margin-left: 100px;
}

.price-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 18px;
}

.price-label {
  font-size: 18px;
  color: #8d8ba7;
  font-weight: bold;
}

.price-value {
  text-align: right;
  flex: 1;
  font-size: 18px;
  color: #8d8ba7;
  font-weight: bold;
  min-width: 100px;
}

.payment-title {
  font-size: 44px;
  font-weight: bold;
  color: #5d5a88;
  margin: 0px 0px 325px 160px;
}

/* .main--container__payment__container__price__amount {
font-size: 24px;
font-weight: bold;
color: #8d8ba7;
padding: 10px;
} */

.product-price,
.coupon-amount,
.point-amount,
.payment-amount {
  font-size: 18px;
  color: #8d8ba7;
  /* padding: 10px; */
  font-weight: bold;
  text-align: left;
}

.payment-btn {
  width: 490px;
  height: 50px;
  border: none;
  border-radius: 5px;
  color: white;
  background-color: #1a73e8;
  font-weight: bold;
  font-size: 15px;
  cursor: pointer;
}

.payment-btn:hover {
  background-color: #87cefa;
}

.payment-btn:active {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}

/* 쿠폰 드롭다운 */
.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-button {
  background-color: white;
  width: 464px;
  height: 50px;
  padding: 10px;
  margin-top: 10px;
  font-size: 15px;
  text-align: left;
  border: 1px solid rgba(0, 0, 0, 0.281);
  cursor: pointer;
  border-radius: 5px;
}

.dropdown-content {
  display: block;
  background-color: #f9f9f9;
  position: absolute;
  /* min-width: 200px; */
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 1;
  border-radius: 5px;
  top: 100%;
  left: 0;
}

.dropdown-item {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f1f1f1;
}

/* 기본 버튼 스타일 */
.payment-button {
  width: 144px;
  height: 47px;
  border: none;
  border-radius: 5px;
  background-color: #1a73e8;
  color: white;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin: 5px;
}

/* 선택된 버튼 스타일 */
.selected {
  background-color: #87cefa;
  color: white;
}

/* 버튼 비활성화 시 */
/* .payment-button:disabled {
background-color: #e0e0e0;
} */
</style>