import { createRouter, createWebHistory } from "vue-router";

import Home from "@/pages/Home.vue";
import SignIn from "@/pages/SignIn.vue";
import SignUp from "@/pages/SignUp.vue";
import FindId from "@/pages/FindId.vue";
import IdFound from "@/pages/IdFound.vue";
import FindPassword from "@/pages/FindPassword.vue";
import TermsOfService from "@/pages/TermsOfService.vue";
import MyPosts from "@/pages/MyPosts.vue";
import MyComments from "@/pages/MyComments.vue";
import LikedPosts from "@/pages/LikedPosts.vue";
import Post from "@/pages/Post.vue";
import Board from "@/pages/Board.vue";
import CreatePost from "@/pages/CreatePost.vue";
import EditPost from "@/pages/EditPost.vue";

import Membership from "@/pages/Membership.vue";
import Payment from "@/pages/Payment.vue";
import Mypage from "@/pages/Mypage.vue";
import AccountDeleted from "@/pages/AccountDeleted.vue";
import EditEmail from "@/pages/EditEmail.vue";

import CreatedGroups from "@/pages/CreatedGroups.vue";
import JoinedGroups from "@/pages/JoinedGroups.vue";
import LikedGroups from "@/pages/LikedGroups.vue";
import AddNewGroup from "@/pages/AddNewGroup.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    // meta: {requiresAuth: false },
  },

  {
    path: "/sign-in",
    name: "SignIn",
    component: SignIn,
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
    path: "/id-found",
    name: "IdFound",
    component: IdFound,
    props: (route) => ({ userId: route.params.userId }),
  },

  {
    path: "/find-password",
    name: "FindPassword",
    component: FindPassword,
  },

  {
    path: "/terms-of-service",
    name: "TermsOfService",
    component: TermsOfService,
  },

  {
    path: "/board",
    name: "Board",
    // meta: { showNav: true },
    component: Board,
  },

  {
    path: "/my-posts",
    name: "MyPosts",
    component: MyPosts,
    meta: { requiresAuth: true },
  },

  {
    path: "/my-comments",
    name: "MyComments",
    // meta: { showNav: true },
    component: MyComments,
  },

  {
    path: "/liked-posts",
    name: "LikedPosts",
    // meta: { showNav: true },
    component: LikedPosts,
  },

  {
    path: '/post/:id',
    name: 'Post',
    // meta: { showNav: false },
    component: Post,
    props: true,
  },

  {
    path: "/create-post",
    name: "CreatePost",
    // meta: { showNav: false },
    component: CreatePost,
  },

  {
    path: "/edit-post/:id",
    name: "EditPost",
    // meta: { showNav: false },
    component: EditPost,
  },

  {
    path: "/membership",
    name: "Membership",
    component: Membership,
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
    path: "/created-groups",
    name: "CreatedGroups",
    component: CreatedGroups,
  },

  {
    // 참여한 모임
    path: "/joined-groups",
    name: "JoinedGroups",
    component: JoinedGroups,
  },

  // 찜한 모임
  {
    path: "/liked-groups",
    name: "LikedGroups",
    component: LikedGroups,
  },

  {
    // 모임 생성
    path: "/add-new-group",
    name: "AddNewGroup",
    component: AddNewGroup,
  },

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;