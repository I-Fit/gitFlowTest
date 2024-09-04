import { ref, computed } from 'vue';

export function useTimer(initailTime = 180) {
    const timeLeft = ref(initailTime);
    let timer = null;
    const timerStarted = ref(false);

    const minutes = computed(() => {
        const min = Math.floor(timeLeft.value / 60);
        return min < 10 ? "0" + min : min;
    });

    const seconds = computed(() => {
        const sec = timeLeft.value % 60;
        return sec < 10 ? "0" + sec : sec;
    });

    function start() {
        if (timer) {
            clearInterval(timer);
        }
        timerStarted.value = true;
        timer = setInterval(() => {
            if (timeLeft.value > 0) {
                timeLeft.value--;
            } else {
                clearInterval(timer);
                timerStarted.value = false;
            }
        }, 1000);
    }

    function reset(newTime = initailTime) {
        clearInterval(timer);
        timeLeft.value = newTime;
        timerStarted.value = false;
        start();
    }

    return {
        timeLeft,
        minutes,
        seconds,
        start,
        reset,
        timerStarted,
    };
}
