// 이용약관 선택 및 해제
document.addEventListener('DOMContentLoaded', function() {
    const radios = document.querySelectorAll('.radios');
  
    radios.forEach(function(radio) {
      radio.addEventListener('mousedown', function(event) {
        if (radio.checked) {
          radio.addEventListener('click', function(event) {
            radio.checked = false;
          }, { once:true });
        }
      });
    });
  });
  
// 이용약관 전체 동의 시 전체 선택되는 기능 및 전체 해제
document.addEventListener('DOMContentLoaded', function() {
    const checkAll = document.querySelector(".check_all");
    const checkboxes = document.querySelectorAll(".radios");

    function updateCheckbox(checked) {
        checkboxes.forEach(function(checkbox) {
            checkbox.checked = checked;
        });
    }

    checkAll.addEventListener('change', function() {
        updateCheckbox(checkAll.checked);
    });

    checkAll.addEventListener('mousedown', function() {
        if(checkAll.checked) {
            checkAll.addEventListener('click', function() {
                updateCheckbox(false);
                checkAll.checked = false;
            }, { once: true});
        }
    });
});