<template>
  <main>
    <AppNav />
    <div class="item">
      <div class="item-top">
        <h2>마이페이지</h2>
        <div class="top-profile">
          <div class="top-image-box">
            <!-- <div class="top-box-img"> -->
            <img :src="profileImage" alt="Profile Image" class="profile-img" />
            <!-- </div> -->
            <div class="top-box-icon" @click="triggerFileInput">&#43;</div>
          </div>
          <!-- 숨겨진 파일 입력 요소 -->
          <input
            type="file"
            ref="fileInput"
            @change="handleFileChange"
            accept="images/*"
            style="display: none"
          />

          <div class="top-boxinfo">
            <span class="boxinfo-username">김계란</span>
            <span class="boxinfo-logout" @click="logoutUser">로그아웃</span><br />
            <span class="boxinfo-membership">나는야 득근을 꿈꾸는 근린이!</span>

            <button
              type="submit"
              class="del-account-btn"
              @click="showConfirmPopup = true"
            >
              회원 탈퇴
            </button>
            <!-- 확인 팝업 -->
            <div v-if="showConfirmPopup" class="confirm-popup">
              <div class="popup-content">
                <p>회원 탈퇴를 진행하시겠습니까?</p>
                <button class="confirm-btn" @click="confirmDeletion">
                  확인
                </button>
                <button class="cancle-btn" @click="cancelDeletion">취소</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="item-bottom">
        <div class="left-section">
          <div>
            <div class="left-info">
              <div class="info-title">ID</div>
              <div class="profile-frame">
                <span id="id">ifitkosta</span>
              </div>
            </div>
            <div class="left-info">
              <div class="info-title">이메일</div>
              <div class="profile-frame">
                <span id="phone">ifit@kosta.com</span>
              </div>
              <button type="submit" class="changec-btn" @click="goToEditEmail">
                변경
              </button>
            </div>
            <div class="left-info">
              <div class="info-title">포인트</div>
              <div class="profile-frame">
                <span id="point">100P</span>
              </div>
            </div>
          </div>
        </div>
        <div class="right-section">
          <div>
            <div class="right-info">
              <div class="info-title">비밀번호</div>
              <div class="profile-frame">
                <span id="password">*******</span>
              </div>
              <button
                type="submit"
                class="changep-btn"
                @click="goToChangePassword"
              >
                변경
              </button>
            </div>
            <div class="right-info">
              <div class="info-title">멤버 등급</div>
              <div class="profile-frame">
                <span id="member-rank">근린이</span>
              </div>
              <button type="submit" class="changem-btn" @click="goToMembership">
                변경
              </button>
            </div>
            <div class="right-info">
              <div class="info-title">쿠폰</div>
              <div class="profile-frame">
                <span id="email">1개</span>
              </div>
              <button
                type="submit"
                class="search-coupon-btn"
                @click="openModal"
              >
                조회
              </button>
              <!-- 쿠폰 조회버튼 클릭 시 모달창 오픈 -->
              <!-- 모달 창 -->
              <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
                <div class="modal-content" @click.stop>
                  <h2>보유 쿠폰 내역</h2>
                  <ul v-if="coupons.length > 0">
                    <li v-for="coupon in coupons" :key="coupon.id">
                      {{ coupon.name }} - {{ coupon.discount }}% 할인
                    </li>
                  </ul>
                  <p v-else>보유한 쿠폰이 없습니다.</p>
                  <button class="modal-close-btn" @click="closeModal">
                    닫기
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex"
import AppNav from "@/components/layout/AppNav.vue";
// import axios from 'axios';
import VueCookies from 'vue-cookies';

export default {
  name: "MypageComponent",
  components: {
    AppNav,
  },

  setup() {
    const router = useRouter();
    const store = useStore();

    const profileImage = ref(require("@/assets/images/default-profile.png"));
    const fileInput = ref(null);

    //파일 선택 창 트리거 함수
    const triggerFileInput = () => {
      fileInput.value.click();
    };

    // 파일 변경 핸들러
    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          profileImage.value = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    };

    // 쿠폰 내역 상태
    const coupons = ref([]);
    const isModalOpen = ref(false);

    // 쿠폰 조회 함수
    const fetchCoupons = async () => {
      try {
        const response = await fetch("/api/coupons?userId=${userId}"); // userId로 찾기
        if (!response.ok) {
          throw new Error("네트워크 응답이 정상적이지 않습니다.");
        }
        const data = await response.json();
        coupons.value = data;
      } catch (error) {
        console.error("쿠폰 조회 중 오류 발생:", error);
      }
    };

    // 모달 열기
    const openModal = async () => {
      await fetchCoupons();
      isModalOpen.value = true;
    };

    // 모달 닫기
    const closeModal = () => {
      isModalOpen.value = false;
    };

    const goToMembership = () => {
      router.push({ path: "/membership", hash: "#target" });
    };

    const goToChangePassword = () => {
      router.push("/find-password");
    };

    const goToAccountDeleted = () => {
      router.push("/account-deleted");
    };

    const goToEditEmail = () => {
      router.push("/edit-email");
    };

    // 로그아웃 요청
    const logoutUser = async () => {
      const refreshToken = VueCookies.get('refreshToken');
      
      if (!refreshToken) {
        alert("로그아웃에 실패했습니다. 다시 시도해주세요.");
        return;
      }

      try {
        const success = await store.dispatch('isLogged/logout'); // Vuex 액션 호출

        if (success) {
            router.push('/'); // 로그아웃 성공 시 홈페이지로 리디렉션
            alert("로그아웃 성공"); // 로그아웃 성공 메시지
        } else {
            alert("로그아웃에 실패했습니다. 다시 시도해주세요.");
        }
    } catch (error) {
        console.error("로그아웃 요청 중 오류 발생:", error);
        alert("로그아웃에 실패했습니다. 다시 시도해주세요.");
    }
};

    const showConfirmPopup = ref(false);

    const confirmDeletion = async () => {
      try {
        // 백엔드 API 호출
        const response = await fetch("/api/delete-account", {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer YOUR_AUTH_TOKEN", // 인증 토큰이 필요한 경우
          },
        });

        if (!response.ok) {
          throw new Error("회원 탈퇴 요청이 실패했습니다.");
        }

        alert("회원 탈퇴가 완료되었습니다.");
        // 성공적인 회원탈퇴 후 처리
        window.location.href = "/account-deleted"; // 탈퇴완료 페이지로 리다이렉트
      } catch (error) {
        console.error("회원 탈퇴 중 오류 발생:", error);
        alert("회원 탈퇴 중 오류가 발생했습니다.");
      }

      showConfirmPopup.value = false;
    };

    const cancelDeletion = () => {
      showConfirmPopup.value = false;
    };

    return {
      profileImage,
      fileInput,
      triggerFileInput,
      handleFileChange,
      coupons,
      isModalOpen,
      openModal,
      closeModal,
      showConfirmPopup,
      confirmDeletion,
      cancelDeletion,
      goToMembership,
      goToChangePassword,
      goToAccountDeleted,
      goToEditEmail,
      logoutUser,
    };
  },
};
</script>

<style scoped>
main {
  width: 100%;
  height: 900px;
  display: grid;
  grid-template-columns: 180px 1fr;
}

/* main-top */
.item {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 225px 1fr;
}

.item-top {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.item-top h2 {
  font-size: 50px;
  font-weight: bold;
  color: #5d5a88;
  margin: 0;
  margin-left: 1020px;
}

.top-profile {
  display: flex;
  /* width: 100%; */
  height: 159px;
  align-items: center;
  margin-left: 60px;
}

.top-image-box {
  display: flex;
  margin: 50px 20px 14px 70px;
}

.top-box-img {
  /* background: url("../assets/imagess/user_img.png") no-repeat center; */
  background-size: cover;
  z-index: 44;
  width: 102px;
  height: 95px;
  border-radius: 100%;
}

.top-box-icon {
  display: flex;
  justify-content: center;
  align-items: center;

  width: 20px;
  height: 20px;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}

.top-boxinfo {
  width: 300px;
  height: 94px;
  margin-top: 70px;
  margin-left: 40px;
}

.boxinfo-username {
  font-size: 30px;
  font-weight: bold;
  margin-bottom: 10px;
}

.boxinfo-logout {
  font-size: 12px;
  font-weight: 400;
  margin-left: 10px;
  color: #0a4eff;
  font-family: DM Sans, var(--default-font-family);
  line-height: 30px;
  text-decoration: underline;
  cursor: pointer;
}

.boxinfo-membership {
  font-size: 16px;
  font-weight: 700;
  margin-left: 5px;
  color: #6e6d6d;
}

/* main-bottom */
.item-bottom {
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
  margin-left: 65px;
}

.left-section {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
}

.right-section {
  display: flex;
  flex-direction: column;
}

.left-info {
  display: flex;
  width: 600px;
  margin-top: 20px;
  margin-left: 50px;
  align-items: center;
}

.right-info {
  display: flex;
  width: 600px;
  margin-top: 20px;
  margin-left: 0px;
  align-items: center;
}

.info-title {
  display: flex;
  width: 100px;
  font-size: 18px;
  font-weight: bold;
  line-height: 24px;
  margin: 24px;
}

.profile-frame {
  display: flex;
  font-size: 16px;
  margin: 24px;
  background: #00000014;
  width: 265px;
  height: 40px;
  border-radius: 10px;
  text-align: center;
  color: #a5a5a9;
  margin-left: 15px;
  align-content: center;
}

.profile-frame span {
  width: 100%;
  text-align: center;
  margin: auto;
}

.changem-btn,
.changep-btn,
.changec-btn,
.search-coupon-btn {
  width: 85px;
  height: 36px;
  border-radius: 8px;
  border: none;
  background-color: #1a73e8;
  color: #fff;
  font-size: 15px;
  font-weight: bold;
  cursor: pointer;
}

.changem-btn:hover,
.changep-btn:hover,
.changec-btn:hover,
.search-coupon-btn:hover {
  background-color: #87cefa;
}

.changem-btn:active,
.changep-btn:active,
.changec-btn:active,
.search-coupon-btn:active {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}

.del-account-btn {
  width: 98px;
  height: 45px;
  border-radius: 8px;
  border: none;
  background-color: #1a73e8;
  color: #fff;
  font-size: 15px;
  font-weight: bold;
  margin-left: 850px;
  margin-top: -100px;
  cursor: pointer;
}

.del-account-btn:hover {
  background-color: #87cefa;
}

.del-account-btn:active {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}

.top-box-img {
  width: 102px;
  height: 95px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f0f0;
}

.profile-img {
  width: 102px;
  height: 102px;
  object-fit: cover;
}

/* 모달 오버레이 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 모달 내용 스타일 */
.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 80%;
  max-width: 500px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.modal-close-btn {
  margin-top: 20px;
  margin-left: 380px;
  padding: 10px 20px;
  font-size: 16px;
  color: #fff;
  background-color: #dc3545;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.modal-close-btn:hover {
  background-color: #c82333;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  padding: 5px 0;
  border-bottom: 1px solid #ddd;
}

/* 팝업 스타일링 */
.confirm-popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  font-weight: bold;
}

.popup-content button {
  margin: 20px;
  width: 100px;
  height: 35px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  border: none;
  color: white;
  background-color: #1a73e8;
}

.popup-content button:hover {
  background-color: #87cefa;
}

.popup-content button:active {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}
</style>
