
const url = `/mentees/${menteeId}`;
var myHeader = new Headers();
myHeader.append('Authorization',`Bearer ${token}`);  
myHeader.append('Content-Type','application/json');
fetch(url,{
    headers : myHeader,
    method:"GET"
})
.then((response)=>{
    if(response.statusCode===200){
        return response.json();
    }
    else{
        throw new Error(response.status)
    }
})
.then(data=>{
    set_data(data);
})
.catch((error)=>{
    console.error(error);
})
function set_data(){
    
}