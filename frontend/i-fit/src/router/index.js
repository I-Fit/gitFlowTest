import { createRouter, createWebHistory } from "vue-router";

import Home from "@/pages/Home.vue";
import Login from "@/pages/Login.vue";
import Account from "@/pages/Account.vue";
import FindId from "@/pages/FindId.vue";
import FindIdComplete from "@/pages/FindIdComplete.vue";
import FindPw from "@/pages/FindPw.vue";
import Tos from "@/pages/Tos.vue";
import PostHistory from "@/pages/PostHistory.vue";
import MyComments from "@/pages/MyComments.vue";
import MyLikepost from "@/pages/MyLikepost.vue";
import BoardDetail from "@/pages/BoardDetail.vue";
import MainBoard from "@/pages/MainBoard.vue";
import PostUpload from "@/pages/PostUpload.vue";
import PostModify from "@/pages/PostModify.vue";

import MembershipMain from "@/pages/MembershipMain.vue";
import Payment from "@/pages/Payment.vue";
import Mypage from "@/pages/Mypage.vue";
import AccountDeleted from "@/pages/AccountDeleted.vue";
import EditEmail from "@/pages/EditEmail.vue";

import MyCreateGroup from "@/pages/MyCreateGroup.vue";
import GroupJoinList from "@/pages/GroupJoinList.vue";
import LikeGroup from "@/pages/LikeGroup.vue";
import CreateGroup from "@/pages/CreateGroup.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },

  {
    path: "/login",
    name: "Login",
    component: Login,
  },

  {
    path: "/account",
    name: "Account",
    component: Account,
  },

  {
    path: "/findid",
    name: "FindId",
    component: FindId,
  },

  {
    path: "/findidComplete",
    name: "FindIdComplete",
    component: FindIdComplete,
    props: (route) => ({ userId: route.params.userId }),
  },

  {
    path: "/findpw",
    name: "FindPw",
    component: FindPw,
  },

  {
    path: "/tos",
    name: "Tos",
    component: Tos,
  },

  {
    path: "/mainboard",
    name: "MainBoard",
    // meta: { showNav: true },
    component: MainBoard,
  },

  {
    path: "/PostHistory",
    name: "PostHistory",
    // meta: { showNav: true },
    component: PostHistory,
  },

  {
    path: "/MyComments",
    name: "MyComments",
    // meta: { showNav: true },
    component: MyComments,
  },

  {
    path: "/MyLikepost",
    name: "MyLikepost",
    // meta: { showNav: true },
    component: MyLikepost,
  },

  {
    path: "/BoardDetail",
    name: "BoardDetail",
    // meta: { showNav: false },
    component: BoardDetail,
  },

  {
    path: "/PostUpload",
    name: "Postupload",
    // meta: { showNav: false },
    component: PostUpload,
  },

  {
    path: "/PostModify",
    name: "PostModify",
    // meta: { showNav: false },
    component: PostModify,
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
    path: "/mycreate-group",
    name: "MyCreateGroup",
    component: MyCreateGroup,
  },

  {
    path: "/groupjoinlist",
    name: "GroupJoinList",
    component: GroupJoinList,
  },

  {
    path: "/likegroup",
    name: "LikeGroup",
    component: LikeGroup,
  },

  {
    path: "/creategroup",
    name: "CreateGroup",
    component: CreateGroup,
  },

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
