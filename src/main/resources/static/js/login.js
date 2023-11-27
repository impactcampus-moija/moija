const user_email = document.getElementById('login-id');
const user_password = document.getElementById('login-pw');
const login_button= document.getElementById('login-button-container');

login_button.addEventListener('click',function(){
    login_submit();
});

function login_submit(){
    fetch('/users/login',{
        headers:{
            'Content-Type' : 'application/json'
        },
        body:{
            email : user_email,
            password : user_password
        },
        method : 'POST'
    }).then((Response)=>{
        if(Response.statusCode ===200){
            return Response.json();
        }
    }).catch((error)=>console.error(error))
    .then((Response)=>{
        localStorage.setItem('access_token',Response.data.token);
        localStorage.setItem('expiredAt',Response.data.expiredAt);
        alert('로그인 성공')
        window.location.href = '../html/main.html'
    })
}