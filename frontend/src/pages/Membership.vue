<template>
  <main>
    <div class="content-top">
      <div class="content-text">
        <div class="text-membership">
          <p class="content-title">I-Fit<br />Membership</p>
        </div>
        <p class="content-text-rank">
          <span>근린이</span><span>근성장</span><span>득근+</span>
        </p>
      </div>
      <div class="benefit-table">
        <div class="benefit01-bubble">
          <img
            class="circle"
            src="../assets/images/멤버십 혜택 동글.png"
            alt="혜택이미지 칸"
          />
          <p class="benefit01-text">혜택 01</p>
          <p class="benefit01-title">개인 맞춤형 운동플랜 제공</p>
          <p class="benefit01-content">
            º 내향인을 위한 개인 맞춤형 운동 플랜<br />
            º 운동 목적을 고려한 개인 맞춤형 식단
          </p>
        </div>
        <div class="benefit02-bubble">
          <img
            class="circle"
            src="../assets/images/멤버십 혜택 동글.png"
            alt="혜택이미지 칸"
          />
          <p class="benefit02-text">혜택 02</p>
          <p class="benefit02-title">원데이 클래스 수강권 & 할인쿠폰 제공</p>
          <p class="benefit02-content">
            º 원데이 클래스 수강권 월 2회<br />
            º 제휴 쇼핑몰 10% 할인 쿠폰
          </p>
        </div>
        <div class="benefit03-bubble">
          <img
            class="circle"
            src="../assets/images/멤버십 혜택 동글.png"
            alt="혜택이미지 칸"
          />
          <p class="benefit03-text">혜택 03</p>
          <p class="benefit03-title">PT 세션 & 1:1 컨설팅 서비스 제공</p>
          <p class="benefit03-content">
            º 개인 트레이너 1:1 PT 세션 월 4회<br />
            º 운동 전문가의 1:1 컨설팅 서비스
          </p>
        </div>
      </div>
    </div>
    <div class="content-bottom" id="target">
      <h2 class="bottom-title">
        한눈에 보는 멤버십
        <!-- <p class="bottom-subtitle">membership benefits</p> -->
      </h2>
      <div v-for="(item, index) in membership" :key="index" :class="`benefit0${index + 1}-box`">
        <img class="emoji" :src="item.icon" :alt="`${item.grade} 아이콘`" />
        <p class="benefit-price">{{ item.price.toLocaleString() }}원/월</p>
        <p class="benefit-rank">LV. {{ item.grade }}</p>
        <p class="benefit-full-text" v-html="item.description"></p>
        <button @click="goToPayment(item.id)" class="btn">
          혜택받기
        </button>
      </div>
    </div>
  </main>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import apiClient from "@/api/apiClient";

export default {
  name: "Membership",

  setup() {
    const router = useRouter();

    // 멤버십 정보를 저장할 변수
    const membership = ref([
      { 
        id: 1,
        grade: "근린이", 
        price: 5000, 
        icon: require("@/assets/images/baby.png"),
        description: "혜택 01 제공<br />개인 맞춤형 플랜이 필요한 <br />운동을 시작하는 초보에게 추천!"
      },
      { 
        id: 2,
        grade: "근성장", 
        price: 10000, 
        icon: require("@/assets/images/run.png"),
        description: "혜택 01 & 혜택 02 제공<br />개인 맞춤형 플랜에 더불어 <br />더욱 다양한 혜택이 필요한 분들에게 추천!"
      },
      { 
        id: 3,
        grade: "득근+", 
        price: 20000, 
        icon: require("@/assets/images/arm.png"),
        description: "혜택 01 & 02 & 03 제공<br />I-Fit이 제공하는 모든 혜택을 <br />누리고 싶은 분들에게 추천!"
      },
    ]);

    const goToPayment = async (id) => {
      try {
        const response = await apiClient.get('/payment/user-info',{
          params: {
            id
          }
        });
        
        const paymentData = response.data;

        router.push({
          name: "Payment",
          query: {
            username: paymentData.username,
            membershipInfo : JSON.stringify(paymentData.membershipInfo),
            points: paymentData.points,
            coupons: JSON.stringify(paymentData.coupons)
          }
        });
      } catch (error) {
        console.log("서버와의 통신 error", error);
      }
    }

    // 결제 페이지로 이동
    // const goToPayment = (membershipGrade, membershipPrice) => {
    //   console.log('혜택받기 버튼 클릭됨:', membershipGrade, membershipPrice);

    //   router.push({
    //     name: "Payment",
    //     query: { 
    //       membershipGrade,
    //       membershipPrice
    //     }
    //   });
    // };
    

    return {
      membership,
      goToPayment,
    };
  },
};
</script>


<style scoped>
/* content 부분 */
/* .container {
display: flex;
flex-direction: column;
} */

main {
  width: 100%;
  height: 1800px;
  display: grid;
  grid-template-rows: 1fr 1000px;
}

.content-top {
  width: 100%;
  height: 100%;
  display: grid;
  position: relative;
  grid-template-columns: 1fr 850px;
  /* flex: 1; */
}

.content-text {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.text-membership {
  width: 400px;
  height: 200px;
}

.text-membership::after {
  content: "";
  width: 100%;
  height: 2px;
  background-color: black;
  display: block;
}

.content-title {
  font-size: 68px;
  font-weight: bold;
  color: #5d5a88;
  margin: 245px 0px 30px 0px;
}

.content-text-rank {
  margin-right: 45px;
  margin-top: 255px;
}

.content-text-rank span {
  font-size: 32px;
  font-weight: bold;
  color: #5d5a88;
  padding: 20px;
}

.benefit-table {
  position: relative;
}

.benefit01-bubble {
  width: 387px;
  height: 362px;
  position: absolute;
  top: 45px;
  left: 0px;
}

.benefit01-text {
  position: absolute;
  top: 28%;
  left: 34%;
  font-size: 44px;
  font-weight: bold;
  color: #1e285f;
}

.benefit01-title {
  position: absolute;
  top: 47%;
  left: 20%;
  font-size: 22px;
  font-weight: bold;
  color: #5d5a88;
}

.benefit01-content {
  position: absolute;
  top: 62%;
  left: 17%;
  color: #9795b5;
}

.benefit02-bubble {
  width: 387px;
  height: 362px;
  position: absolute;
  top: 170px;
  left: 400px;
}

.benefit02-text {
  position: absolute;
  top: 30%;
  left: 33%;
  font-size: 44px;
  font-weight: bold;
  color: #1e285f;
}

.benefit02-title {
  position: absolute;
  top: 48%;
  left: 4%;
  width: 500px;
  font-size: 22px;
  font-weight: bold;
  color: #5d5a88;
}

.benefit02-content {
  position: absolute;
  top: 65%;
  left: 26%;
  color: #9795b5;
}

.benefit03-bubble {
  width: 387px;
  height: 362px;
  position: absolute;
  top: 435px;
  left: 75px;
}

.benefit03-text {
  position: absolute;
  top: 30%;
  left: 35%;
  font-size: 44px;
  font-weight: bold;
  color: #1e285f;
}

.benefit03-title {
  position: absolute;
  top: 48%;
  left: 10%;
  font-size: 22px;
  font-weight: bold;
  color: #5d5a88;
}

.benefit03-content {
  position: absolute;
  top: 62%;
  left: 21%;
  color: #9795b5;
}

.circle {
  width: 400px;
  height: 370px;
  display: block;
  position: relative;
}

/* 혜택받기 box */
.content-bottom {
  display: flex;
  justify-content: center;

  position: relative;
  /* flex: 1; */
}

.bottom-title {
  font-size: 56px;
  font-weight: bold;
  color: #5d5a88;

  margin-top: 100px;
  margin-bottom: 50px;
}

.bottom-subtitle {
  font-size: 25px;
  font-weight: bold;
  color: #5d5a88;
  text-align: center;
  margin-top: 20px;
}

.benefit01-box {
  width: 387px;
  height: 671px;
  border: 1px solid grey;
  border-radius: 10px;
  position: absolute;
  top: 220px;
  left: 140px;

  display: flex;
  flex-direction: column;
  align-items: center;
}

.emoji {
  width: 100px;
  height: 100px;
  margin-top: 120px;
  background: none;
  background-size: cover;
}

.benefit-price {
  font-size: 44px;
  font-weight: bold;
  color: #5d5a88;
  margin-top: 40px;
}

.benefit-rank {
  font-size: 20px;
  font-weight: bold;
  color: #8d8ba7;
  margin-top: 10px;
}

.benefit-full-text {
  font-size: 18px;
  color: #9795b5;
  text-align: center;
  margin-top: 40px;
  padding: 10px;
}

.btn {
  width: 320px;
  height: 60px;
  border: none;
  background-color: #1a73e8;
  border-radius: 30px;
  color: white;
  font-size: 20px;
  align-items: center;
  margin-top: 40px;
  cursor: pointer;
  z-index: 1;
  font-weight: bold;
}

.benefit02-box {
  width: 387px;
  height: 671px;
  border: 1px solid grey;
  border-radius: 10px;
  position: absolute;
  top: 220px;
  left: 575px;

  display: flex;
  flex-direction: column;
  align-items: center;
}

.benefit03-box {
  width: 387px;
  height: 671px;
  border: 1px solid grey;
  border-radius: 10px;
  position: absolute;
  top: 220px;
  left: 1005px;

  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
