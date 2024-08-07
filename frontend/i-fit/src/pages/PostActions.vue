<template>
  <div v-if="visible" class="post-actions">
    <button 
      class="action-btn" 
      @click="navigate('PostModify')" 
      style="background-color: white; color: black;"
    >
      수정
    </button>
    <button 
      class="action-btn" 
      @click="confirmDelete" 
      style="background-color: white; color: black;"
    >
      삭제
    </button>

    <!-- 모달 창 -->
    <div v-if="showDeleteModal" class="modal">
      <div class="modal-content">
        <p>정말로 삭제하시겠습니까?</p>
        <div class="modal-actions">
          <button class="modal-btn" @click="deletePost">확인</button>
          <button class="modal-btn" @click="closeModal">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      showDeleteModal: false,
    };
  },
  methods: {
    navigate(action) {
      this.$emit("navigate", action);
    },
    confirmDelete() {
      this.showDeleteModal = true;
    },
    closeModal() {
      this.showDeleteModal = false;
    },
    deletePost() {
      this.$emit("navigate", "delete");
      this.closeModal();
    },
  },
};
</script>

<style scoped>
.post-actions {
  position: absolute;
  top: 30px;
  right: -15px;
  border: none;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 2;
}

.action-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 30px;
  text-align: center;
  
  border: 1px solid black;
  border-radius: 4px;
  padding: 10px 5px;
  margin: 2px 0;
  cursor: pointer;
  font-size: 14px;
}

.modal {
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

.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  font-weight: bold;
}

.modal-actions {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}

.modal-btn {
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

.modal-btn:hover {
  background-color: #87cefa;
}

.modal-btn:nth-child(2) {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}

/* .modal-btn:nth-child(2):hover {
  background-color: #5a6268;
} */
</style>
