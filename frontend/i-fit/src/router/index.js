import { createRouter, createWebHistory } from "vue-router";
// import { useStore } from "vuex";

import Home from "@/pages/Home.vue";
import Login from "@/pages/Login.vue";
import SignUp from "@/pages/SignUp.vue";
import FindId from "@/pages/FindId.vue";
import CompletedId from "@/pages/CompletedId.vue";
import FindPassword from "@/pages/FindPassword.vue";
import Tos from "@/pages/Tos.vue";
import HistoryPosts from "@/pages/HistoryPosts.vue";
import WrittenComments from "@/pages/WrittenComments.vue";
import LikedPosts from "@/pages/LikedPosts.vue";
import DetailPost from "@/pages/DetailPost.vue";
import Board from "@/pages/Board.vue";
import UploadPost from "@/pages/UploadPost.vue";
import EditPost from "@/pages/EditPost.vue";

import MembershipMain from "@/pages/MembershipMain.vue";
import Payment from "@/pages/Payment.vue";
import Mypage from "@/pages/Mypage.vue";
import AccountDeleted from "@/pages/AccountDeleted.vue";
import EditEmail from "@/pages/EditEmail.vue";

import HistoryGroups from "@/pages/HistoryGroups.vue";
import JoinedGroups from "@/pages/JoinedGroups.vue";
import LikedGroups from "@/pages/LikedGroups.vue";
import AddGroup from "@/pages/AddGroup.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    // meta: {requiresAuth: false },
  },

  {
    path: "/login",
    name: "Login",
    component: Login,
  },

  {
    path: "/sign-up",
    name: "SignUp",
    component: SignUp,
  },

  {
    path: "/find-id",
    name: "FindId",
    component: FindId,
  },

  {
    path: "/completed-id",
    name: "CompletedId",
    component: CompletedId,
    props: (route) => ({ userId: route.params.userId }),
  },

  {
    path: "/find-password",
    name: "FindPassword",
    component: FindPassword,
  },

  {
    path: "/tos",
    name: "Tos",
    component: Tos,
  },

  {
    path: "/board",
    name: "Board",
    // meta: { showNav: true },
    component: Board,
  },

  {
    path: "/history-posts",
    name: "HistoryPosts",
    component: HistoryPosts,
    meta: { requiresAuth: true },
  },

  {
    path: "/written-comments",
    name: "WrittenComments",
    // meta: { showNav: true },
    component: WrittenComments,
  },

  {
    path: "/liked-posts",
    name: "LikedPosts",
    // meta: { showNav: true },
    component: LikedPosts,
  },

  {
    path: "/detail-post",
    name: "DetailPost",
    // meta: { showNav: false },
    component: DetailPost,
  },

  {
    path: "/upload-post",
    name: "UploadPost",
    // meta: { showNav: false },
    component: UploadPost,
  },

  {
    path: "/edit-post",
    name: "EditPost",
    // meta: { showNav: false },
    component: EditPost,
  },

  {
    path: "/membership",
    name: "Membership",
    component: MembershipMain,
  },

  {
    path: "/payment",
    name: "Payment",
    component: Payment,
    props: (route) => ({
      membershipType: route.query.name,
      membershipPrice: route.query.price,
      userName: route.query.user,
    }),
  },

  {
    path: "/mypage",
    name: "Mypage",
    component: Mypage,
  },

  {
    path: "/account-deleted",
    name: "AccountDeleted",
    component: AccountDeleted,
  },

  {
    path: "/edit-email",
    name: 'EditEmail',
    component: EditEmail,
  },

  {
    // 내가 만든 모임
    path: "/history-groups",
    name: "HistoryGroups",
    component: HistoryGroups,
  },

  {
    // 참여한 모임
    path: "/joined-groups",
    name: "JoinedGroups",
    component: JoinedGroups,
  },

  {
    path: "/liked-groups",
    name: "LikedGroups",
    component: LikedGroups,
  },

  {
    // 모임 생성
    path: "/add-group",
    name: "AddGroup",
    component: AddGroup,
  },

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach((to, next) => {
//   const store = useStore();
//   const isLoggedIn = store.getters.LoggedIn;

//   if(to.matched.some(record => record.meta.requiresAuth)) {
//     if (!isLoggedIn) {
//       next('/login');      // 로그인 하지않은 경우 로그인 페이지로 이동
//     } else {
//       next();              // 인증된 경우 이동
//     }
//   } else {
//     next();                // 인증이 필요 없는 페이지는 이동
//   }
// });

export default router;