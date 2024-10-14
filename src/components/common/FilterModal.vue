<template>
    <div v-if="isOpen" class="modal-backdrop">
        <div class="filter">
            <button @click="closeFilter">닫기</button>
            <h2>검색 필터 설정</h2>
            
            <!-- 운동 -->
            <label for="activity">운동:</label>
            <select id="activity" v-model="selectedActivity">
               <option disabled value="">운동 선택</option>
               <option value="축구">웨이트</option>
               <option value="축구">러닝</option>
               <option value="축구">테니스</option>
               <option value="축구">요가</option>
            </select>

            <!-- 지역 -->
            <label for="location">지역:</label>
            <!-- //todo 추후 지도 api 불러오는 방식으로 수정 -->
            <select id="location" v-model="selectedLocation">
                <option disabled value="">지역 선택</option>
               <option value="축구">강남구</option>
               <option value="축구">용산구</option>
               <option value="축구">마포구</option>
               <option value="축구">강서구</option>
            </select>

            <!-- 날짜 -->
            <label for="date">날짜:</label>
            <input id="date" type="date" v-model="selectedDate">

            <!-- 날짜 -->
            <label for="time">시간:</label>
            <input id="time" type="time" v-model="selectedTime">

            <!-- 정렬 -->
            <label for="sort">정렬</label>
            <select id="sort" v-model="selectedSort">
                <option disabled value="">정렬 기준 선택</option>
                <!-- //todo 정렬 기준 추후 수정 -->
                <option value="date">인기순</option>
                <option value="date">날짜순</option>
            </select>

            <button @click="applyFilters">필터 적용 (확인)</button>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        isOpen: Boolean
    },
    data() {
        return {
            selectedActivity: '',
            selectedLocation: '',
            selectedDate: '',
            selectedTime: '',
            selectedSort: '',
        };
    },
    methos: {
        closeFilter() {
            this.$emit('close');
        },
        applyFilters() {
            const filters = {
                activity: this.selectedActivity,
                location: this.selectedLocation,
                date: this.selectedDate,
                time: this.selectedTime,
                sort: this.selectedSort,
            };
            this.$emit('apply-filters', filters);
            this.closeFilter();
        }
    }
};
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.filter {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  text-align: center;
}
</style>