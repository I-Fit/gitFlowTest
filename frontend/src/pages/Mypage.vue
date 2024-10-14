<template>
  <main>
    <AppNav />
    <div class="item">
      <div class="item-top">
        <h2>마이페이지</h2>
        <div class="top-profile">
          <div class="top-image-box">
            <img :src="profileImage" alt="Profile Image" class="profile-img" />
            <div class="top-box-icon" @click="triggerFileInput">&#43;</div>
          </div>
          <!-- 숨겨진 파일 입력 요소 -->
          <input type="file" ref="fileInput" @change="handleFileChange" accept="images/*" style="display: none" />
          <div class="top-boxinfo">
            <span class="boxinfo-username">{{ username }}</span>
            <span class="boxinfo-logout" @click="logoutUser">로그아웃</span><br />
            <span class="boxinfo-membership">나는야 득근을 꿈꾸는 근린이!</span>
            <button type="submit" class="del-account-btn" @click="showConfirmPopup = true">
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
                <span id="id">{{ loginId }}</span>
              </div>
            </div>
            <div class="left-info">
              <div class="info-title">이메일</div>
              <div class="profile-frame">
                <span id="email">{{ email }}</span>
              </div>
              <button type="submit" class="changec-btn" @click="goToEditEmail">
                변경
              </button>
            </div>
            <div class="left-info">
              <div class="info-title">포인트</div>
              <div class="profile-frame">
                <span id="point">{{ totalPoints }}P</span>
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
              <button type="submit" class="changep-btn" @click="goToChangePassword">
                변경
              </button>
            </div>
            <div class="right-info">
              <div class="info-title">멤버 등급</div>
              <div class="profile-frame">
                <span id="member-rank">{{ MembershipGrade }}</span>
              </div>
              <button type="submit" class="changem-btn" @click="goToMembership">
                변경
              </button>
            </div>
            <div class="right-info">
              <div class="info-title">쿠폰</div>
              <div class="profile-frame">
                <span id="coupon-count">{{ coupons.length }}개</span>
              </div>
              <button type="submit" class="search-coupon-btn" @click="openModal">
                조회
              </button>
              <!-- 쿠폰 조회버튼 클릭 시 모달창 오픈 -->
              <!-- 모달 창 -->
              <div v-if="isModalOpen" class="modal-overlay" @click="closeModal">
                <div class="modal-content" @click.stop>
                  <h2>보유 쿠폰 내역</h2>
                  <ul v-if="coupons.length > 0">
                    <li v-for="coupon in coupons" :key="coupon.id">
                      {{ coupon.name }} - 만료일: {{ new Date(coupon.expiredAt).toLocaleDateString() }}
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
import { ref, onMounted, onBeforeMount } from "vue";
import { useRouter } from "vue-router";
import apiClient from "@/api/apiClient";
import AppNav from "@/components/layout/AppNav.vue";
import VueCookies from 'vue-cookies';
import { useStore } from 'vuex';


export default {
  name: "MypageComponent",
  components: {
    AppNav,
  },

  setup() {
    const router = useRouter();
    const store = useStore();

    const loginId = ref("");
    const username = ref("");
    const email = ref("");
    const MembershipGrade = ref("");
    const loading = ref(false); // 로딩 상태?
    const profileImage = ref("");
    const fileInput = ref(null);
    const coupons = ref([]);
    const isModalOpen = ref(false);
    const totalPoints = ref(0); // 포인트 상태


    const fetchUserProfile = async () => {
      try {
        const response = await apiClient.get('/profile');

        loginId.value = response.data.loginId;
        username.value = response.data.username;
        email.value = response.data.email;
        profileImage.value = response.data.profileUrl ? `data:image/png;base64,${response.data.profileUrl}` : require("@/assets/images/default-profile.png");

        totalPoints.value = response.data.points;
        MembershipGrade.value = response.data.membershipGrade || "기본 회원";
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    };


    //파일 선택 창 트리거 함수
    const triggerFileInput = () => {
      fileInput.value.click();
    };

    // 파일 변경 핸들러
    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file) {
        uploadImage(file);
      }
    };

    const uploadImage = async (file) => {
      const formData = new FormData();
      formData.append('image', file);

      try {
        const response = await apiClient.post('/profile/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        profileImage.value = `data:image/png;base64,${response.data.profileUrl}`;
      } catch (error) {
        console.error("Error uploading profile image:", error);
      }
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



    // 쿠폰 조회 함수
    const fetchCoupons = async () => {
      try {
        const response = await apiClient.get("/coupon"); // userId로 찾기
        coupons.value = response.data;
      } catch (error) {
        console.error("쿠폰 조회 중 오류 발생:", error);
      }
    };

    // 포인트 조회 함수
    // const fetchPoints = async () => {
    //   try {
    //     const response = await apiClient.get(`point/amount'${userId.value}`); // userId로 찾기
    //     if (!response.ok) {
    //       throw new Error("네트워크 응답이 정상적이지 않습니다.");
    //     }
    //     totalPoints.value = response.data; // 받아온 포인트 값을 설정
    //   } catch (error) {
    //     console.error("포인트 조회 중 오류 발생:", error);
    //   }
    // };

    onBeforeMount(async () => {
      await fetchUserProfile();
    })

    // 마운트 시 쿠폰과 포인트 조회
    onMounted(async () => {
      await fetchCoupons();
    });

    // 모달 열기
    const openModal = () => {
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
      router.push("/change-password");
    };

    const goToEditEmail = () => {
      router.push("/edit-email");
    };

    const showConfirmPopup = ref(false);
    // const { deleteAccount } = mapActions('isLogged', ['deleteAccount']);

    const confirmDeletion = async () => {
      try {
        const success = await store.dispatch('isLogged/deleteAccount');
        if (success) {
          alert("회원 탈퇴가 완료되었습니다.");
          router.push("/");
        } else {
          alert("회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
        }

      } catch (error) {
        console.error("회원 탈퇴 중 오류 발생: ", error);
      } finally {
        showConfirmPopup.value = false;
      }
    };


    const cancelDeletion = () => {
      showConfirmPopup.value = false;
    };

    onMounted(() => {
      fetchUserProfile();
    })

    return {
      // userId,
      loginId,
      username,
      email,
      MembershipGrade,
      profileImage,
      fileInput,
      showConfirmPopup,
      triggerFileInput,
      handleFileChange,
      coupons,
      totalPoints,
      isModalOpen,
      openModal,
      closeModal,
      confirmDeletion,
      cancelDeletion,
      // deleteAccount,
      goToMembership,
      goToChangePassword,
      logoutUser,
      goToEditEmail,
      loading,
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
  margin-left: 75px;
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
  margin-left: 75px;
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
  border-radius: 0.1875rem;
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
  border-radius: 0.1875rem;
  border: none;
  background-color: #a5a5a9;
  color: #fff;
  font-size: 15px;
  font-weight: bold;
  margin-left: 440px;
  margin-top: 290px;
  cursor: pointer;
}

.del-account-btn:hover {
  background-color: #ddd;
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
  border-radius: 50%;
  /* object-fit: cover; */
  image-rendering: crisp-edges;
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
  border-radius: 0.1875rem;
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
