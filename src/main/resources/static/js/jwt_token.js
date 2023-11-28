export function tokenExist(){
    if(localStorage.getItem("access_token") != null){
        return true;
    }else{
        return false;
    }
}

export function tokenValid(){
    const now = new Date().getTime();
    const tokenExpiredAt = Number(localStorage.getItem('expiredAt'));
    const refresh_token = localStorage.getItem('Refresh-Token');
    
    myHeaders = new Headers();
    myHeaders.append('Refresh-Token',`Bearer ${refresh_token}`);

    if (tokenExpiredAt < now) {
        fetch('/users/refresh',{
            method : 'POST',
            headers:myHeaders
        })
        .then((Response)=>{
            if (Response.statusCode ===200){
                return Response.json();
            }
        }).catch((error)=>console.error(error))
        .then((Response)=>{
            localStorage.setItem('access_token',Response.data.token);
            localStorage.setItem('expiredAt',Response.data.expiredAt);
        })
        
    }
}