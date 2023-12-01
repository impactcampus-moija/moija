// .navList 내부의 모든 li 요소를 가져옵니다.
const liElements = document.querySelectorAll('.nav-menu li');
const loginWrap = document.getElementById('login-wrap')
const logoutWrap = document.getElementById('logout-wrap')


if (localStorage.getItem("Refresh-Token") == null){
    logoutWrap.style.display = "none";
    loginWrap.style.display = "flex";
} else{
    logoutWrap.style.display ="flex";
    loginWrap.style.display = "none";
}