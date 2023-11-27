const signup_data = {};
//생년월일 드롭박스 설정
const selectElement_year = document.getElementById('birthday-year');
for(let year = 2007; year >= 1950;year--){
    const option = document.createElement('option');
    option.value = year;
    option.textContent = year;
    selectElement_year.appendChild(option)
}
const selectElement_month = document.getElementById('birthday-month');
for(let month = 1; month <= 12;month++){
    const option = document.createElement('option');
    option.value = month;
    option.textContent = month;
    selectElement_month.appendChild(option)
}
const selectElement_day = document.getElementById('birthday-day');
for(let day = 1; day <= 31;day++){
    const option = document.createElement('option');
    option.value = day;
    option.textContent = day;
    selectElement_day.appendChild(option)
}

//월별 일 수 변경

const month30Days = [4,6,9,11];

function updateDays(){
    const month = parseInt(selectElement_month.value);
    month.innerHTML = '';

    let dayInMonth = 31;
    if (month30Days.includes(month)){
        dayInMonth = 30;
    }else if (month ===2){
        dayInMonth = 28;
    }

    for(let day = 1; day <= dayInMonth;day++){
        const option = document.createElement('option');
        option.value = day;
        option.textContent = day;
        selectElement_day.appendChild(option)
    }
}

selectElement_month.addEventListener('change', updateDays);

//버튼 누르면 다음 페이지로
const container1 = document.getElementById('signup-container-1');
const container2 = document.getElementById('signup-container-2');
const container3 = document.getElementById('signup-container-3');

const nextButton1 = document.getElementById('signup-button-1');
const nextButton2 = document.getElementById('signup-button-2');
const finishButton = document.getElementById('signup-button-3');

$('#signup-container-1').show();
$('#signup-container-2').hide();
$('#signup-container-3').hide();

nextButton1.addEventListener('click',function(){
    signup_data.email = document.getElementById('signup-input-email').value;
    signup_data.password = document.getElementById('signup-input-pw').value;
    signup_data.nickname = document.getElementById('signup-input-nickname').value;

    $('#signup-container-1').hide();
    $('#signup-container-2').show();

    console.log(signup_data)
});

nextButton2.addEventListener('click',function(){
    signup_data.name = document.getElementById('signup-input-name').value;
    signup_data.birthday = returnBirth(document.getElementById('birthday-year').value,document.getElementById('birthday-month').value,document.getElementById('birthday-day').value);
    signup_data.location = document.getElementById('signup-input-region').value;
    signup_data.gender = returnSelect(document.getElementsByName('gender'));

    $('#signup-container-2').hide();
    $('#signup-container-3').show();
    console.log(signup_data)
});

finishButton.addEventListener('click',function(){
    alert('등록이 완료 되었습니다.')
    submit()
});

// 생일 날짜 형식 반환하는 함수
function returnBirth(year,month,day){
    month = String(month).padStart(2,'0');
    day = String(month).padStart(2,'0');

    const date = `${year}-${month}-${day}`;
    return date;
}

// 라디오 선택한 value 반환하는 함수
function returnSelect(value){
    let selectedValue = ''
    value.forEach(radio=>{
        if(radio.checked){
            selectedValue = radio.value;
        }
    });
    console.log(selectedValue);
    return selectedValue;
}


// //select 선택 후 글씨 색상 변경
// document.addEventListener('DOMContentLoaded', function() {
//     const selectElement = document.querySelector('.region');

//     selectElement.addEventListener('change', function() {
//         const selectedOption = selectElement.options[selectElement.selectedIndex];

//         if (selectedOption.value !== 'default') {
//             selectElement.style.color = '#000000'; 
//         } else {
//             selectElement.style.color = '#D9D9D9'; 
//         }
//     });
// });

//비밀번호 유효성 검사 및 비밀번호 확인
function validPW(text){
    const passwordRegex = /^(?=.*[!@#$%^&*()\-_=+\\|\[\]{};:'",.<>/?]).{10,}$/;
    const isValid = passwordRegex.test(text);

    if (isValid){
        return true;
    }
    else{
        return false;
    }
}
const checkpw = document.getElementById('signup-input-pw');
checkpw.addEventListener('change',function(){
    if (validPW(checkpw.value)){
        passwordInput.style.border = '1px solid #FCA340';
    }
});

function validSamePW(text){
    if (text == checkpw.value){
        passwordInput.style.border = '1px solid #FCA340';
    }
}
const checksamepw = document.getElementById('signup-input-pw-check');
checksamepw.addEventListener('change',function(){
    validSamePW(checksamepw.value)
})

// 백엔드 통신
function submit(){
    const url = '/users/signup';
    fetch(url,{
        headers:{
            'Content-Type' : 'application/json'
        },
        body:signup_data,
        method:"POST"
    })
    .then((Response)=>{
        if(Response.statusCode === 200){
            console.log('가입성공')
        }else if(Response.statusCode===40002){
            console.log('전화번호 중복')
        }
    })
    .then((result)=>{
        console.log(result);
    })
    .catch((error)=>{
        console.error(error);
    })
}