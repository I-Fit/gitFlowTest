let currentSlide = 0;
const slides = document.querySelectorAll('.slide');
const totalSlides = slides.length;

document.getElementById('right').addEventListener('click', () => {
  moveToNextSlide();
});

document.getElementById('left').addEventListener('click', () => {
  moveToPreviousSlide();
});

function updateCarousel() {
  const carouselIn = document.querySelector('.carousel-in');
  const slideWidth = slides[0].clientWidth;
  carouselIn.style.transform = `translateX(${-currentSlide * slideWidth}px)`;
}

function moveToNextSlide() {
  if (currentSlide < totalSlides - 1) {
    currentSlide++;
  } else {
    currentSlide = 0; // Loop back to the first slide
  }
  updateCarousel();
}

function moveToPreviousSlide() {
  if (currentSlide > 0) {
    currentSlide--;
  } else {
    currentSlide = totalSlides - 1; // Loop back to the last slide
  }
  updateCarousel();
}
