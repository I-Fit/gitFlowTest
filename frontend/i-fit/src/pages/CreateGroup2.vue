<template>
    <main>
        <div class="main-container">
            <div class="contain-topwrap">
                <div class="topwrap-title">
                    <textarea v-model="title" placeholder="제목을 입력하세요."></textarea>
                </div>
                <div class="topwrap-box" role="textbox">
                    <div class="topbox-content" contenteditable="true" ref="topboxContent" @input="updateTopboxContent">
                    </div>
                </div>
                <div class="topwrap-tag">
                    <p class="tag-item">{{ formData.sport }}</p>
                    <p class="item" id="location">{{ formData.location }}</p>
                    <p class="item" id="date">{{ formData.selectedDate }}</p>
                    <p class="item" id="time">{{ formData.selectedTime }}</p>
                    <p class="item" id="person">{{ formData.person }}</p>
                </div>
            </div>
            <div class="contain-category">
                <p class="category-sidetext">Choose Category</p>
                <div class="category-list">
                    <button class="category-btn" type="button" v-for="category in categories" :key="category"
                        :class="{ selected: selectCategory === category }" @click="selectCategory(category)">
                        {{ category }}
                    </button>
                </div>
                <div class="category-input">
                    <input class="input-event" v-model="sportInput" type="text" placeholder="운동 종목을 입력하세요." />
                    <button class="input-button" @click="setSport">확인</button>
                </div>
                <p class="category-text">Choose Location</p>
                <div class="category-input">
                    <input class="input-event" v-model="locationInput" type="text" placeholder="위치를 검색하세요." />
                    <button class="input-button" @click="setLocation">확인</button>
                </div>
                <p class="category-text">Choose Date and Time</p>
                <div class="category-date">
                    <button class="date-btn" @click="showDatePicker = !showDatePicker">
                        <span class="date-calendar">&#128198;</span>날짜 선택
                    </button>
                    <div v-if="showDatePicker">
                        <input type="date" @change="handleDateChange" />
                    </div>
                    <select v-model="formData.selectedTime" class="date-time">
                        <option value="" disabled>시간</option>
                        <option v-for="time in times" :key="time" :value="time">
                            {{ time }}
                        </option>
                    </select>
                </div>
                <p class="category-text">Choose Group Size</p>
                <div class="category-input">
                    <input class="input-event" v-model="personInput" type="text" placeholder="인원을 입력하세요." />
                    <button class="input-button" @click="setPerson">확인</button>
                </div>
                <button class="category-register" @click="registerGroup">등록</button>
            </div>
        </div>
    </main>
</template>

<script>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
    name: "CreateGroup2",

    setup() {
        const router = useRouter();
        const title = ref('');
        const sportInput = ref('');
        const locationInput = ref('');
        const personInput = ref('');
        // 날짜
        const showDatePicker = ref(false);

        const formData = reactive({
            title: "",
            topboxContent: "",
            sport: "종목",
            location: "",
            person: "",
            selectedDate: "",
            selectedTime: "",
        });

        const categories = [
            "러닝",
            "웨이트",
            "라이딩",
            "요가",
            "수영",
            "등산",
            "테니스",
            "클라이밍",
            "필라테스",
            "GX",
        ];
        const times = ['08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00'];

        // 메서드
        const updateTopboxContent = () => {
            formData.topboxContent = (document.querySelector('.topbox-content') || '').innerText;
        };

        const selectCategory = (category) => {
            formData.sport = category;
            selectCategory.value = category;
        };

        const setSport = () => {
            formData.sport = sportInput.value;
            sportInput.value = '';
        };

        const setLocation = () => {
            formData.location = locationInput.value;
            locationInput.value = '';
        };

        const handleDateChange = (event) => {
            formData.selectedDate = event.target.value;
        };

        const setPerson = () => {
            formData.person = personInput.value;
            personInput.value = '';
        };

        const additionalData = reactive({
            userId: "",
            communityId: "",
            user_img: "",
            username: "",
        });

        const registerGroup = async () => {
            try {
                const response = await axios.post('/api/create-group', formData);
                const { userId, communityId, user_img, username } = response.data;

                additionalData.userId = userId;
                additionalData.communityId = communityId;
                additionalData.user_img = user_img;
                additionalData.username = username;

                router.push({ name: "Home",
                    query: {
                        userId,
                        communityId,
                        user_img,
                        username,
                        ...formData
                    }
                });
            } catch (error) {
                console.error("Error", error);
            }
        };
        return {
            title,
            sportInput,
            locationInput,
            personInput,
            showDatePicker,

            formData,
            categories,
            times,
            updateTopboxContent,
            selectCategory,
            setSport,
            setLocation,
            handleDateChange,
            setPerson,
            additionalData,
            registerGroup,
        };
    }
}
</script>

<style scoped>
.category-btn.selected {
    border: 2px solid black;
}

/* content 부분 */
main {
    width: 100%;
    height: 700px;
    display: flex;
    justify-content: center;
}

.main-container {
    width: 1200px;
    height: 100%;
    display: grid;
    grid-template-columns: 1fr 360px;
}

.contain-topwrap {
    display: flex;
    /* align-items: center; */
    flex-direction: column;
    margin-top: 20px;
}

.topwrap-title {
    display: flex;
    flex-direction: column;
    width: 700px;
    height: 60px;
    border-radius: 10px;
    margin: 10px 0;
}

.topwrap-title::placeholder {
    font-size: smaller;
}

.topwrap-title textarea {
    border: none;
    font-size: 20px;
    width: 100%;
    height: 40px;
    color: #202020;
    border: none;
    outline: none;
    resize: none;
    overflow: hidden;
}

.topwrap-title::after {
    content: "";
    display: block;
    width: 100%;
    height: 1px;
    align-items: center;
    background-color: #eee5e5;
    margin-top: 5px;
    position: relative;
    bottom: -5px;
    left: 50%;
    transform: translateX(-50%);
}

.topwrap-box {
    display: block;
    width: 715px;
    overflow: hidden;
    cursor: text;
}

.topbox-content {
    width: 100%;
    height: 500px;
    cursor: text;
    border: none;
    overflow-y: scroll;
}

.topbox-content::-webkit-scrollbar {
    display: none;
}

[contenteditable] {
    outline: 0px solid transparent;
    font-size: large;
}

.topwrap-tag {
    display: flex;
    flex-direction: row;
}

.item {
    margin: 5px 10px 5px 10px;
    font-weight: lighter;
    font-size: 13px;
}

.tag-item {
    width: 58px;
    height: 24px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 13px;
    border-radius: 10px;
    background-color: #f2f2f2;
    text-align: center;
}

.contain-category {
    display: flex;
    flex-direction: column;
    position: relative;
}

.contain-category::before {
    content: "";
    position: absolute;
    width: 2px;
    height: 90%;
    top: 0px;
    left: -30px;
    background-color: whitesmoke;
}

.category-sidetext {
    text-align: start;
    margin-top: 30px;
    margin-bottom: 10px;
}

.category-list {
    width: 340px;
    height: 130px;
}

.category-btn {
    width: 70px;
    height: 30px;
    border: 1px solid whitesmoke;
    border-radius: 10px;
    margin: 4px;
    padding: 0;
}

.category-input {
    width: 404px;
    height: 35px;
    display: flex;
    margin: 5px 0 15px 0;
    align-items: center;
}

.category-text {
    margin-bottom: 10px;
}

.input-event {
    width: 245px;
    height: 35px;
    border: 1px solid #ccc;
    border-radius: 10px;
    margin: 5px 5px 5px 0px;
    padding: 5px 0px 5px 10px;
}

input::placeholder {
    font-size: small;
}

.input-button {
    width: 60px;
    height: 35px;
    margin-left: 5px;
    padding: 8px 16px;
    border: none;
    background-color: #1a73e8;
    color: white;
    border-radius: 10px;
    font-size: 13.3333px;
    font-weight: bold;
    cursor: pointer;
}

.category-date {
    display: flex;
    margin: 5px 0 15px 5px;
}

.date-btn {
    width: 160px;
    height: 35px;
    font-size: 13.3333px !important;
    border: 1px solid #ccc;
    background-color: #fff;
    border-radius: 10px;
    font-weight: lighter !important;
}

.date-calendar {
    margin-right: 7px;
}

.date-time {
    width: 80px;
    height: 35px;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 10px;
    margin-left: 10px;
    font-size: 13.3333px;
}

.category-register {
    margin-top: 115px;
    width: 323px;
    height: 40px;
    border: none;
    border-radius: 10px;
    background-color: #1a73e8;
    color: white;
    font-size: 20px;
    font-weight: bold;
    cursor: pointer;
}
</style>
