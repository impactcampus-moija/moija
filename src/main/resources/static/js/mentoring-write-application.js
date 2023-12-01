const submit_mentoring = document.getElementById('apply-mentoring')
submit_mentoring.addEventListener('click',function(){
    const agree_number = document.getElementById('application-agree-number');
    const number = null
    if(agree_number.checked){
        number = document.getElementById('application-number')
    }
    const brief = document.getElementById('application-write-aline')
    const content = document.getElementById('application-write-free')
    const requestBody = {
        "phone":number,
        "brief":brief,
        "content":content
    }
    const url = `/mentorings/${mentorID}/mentees`;
    var myHeader = new Headers();
    myHeader.append('Authorization',`Bearer ${token}`);  
    myHeader.append('Content-Type','application/json');
    fetch(url,{
        headers : myHeader,
        body:JSON.stringify(requestBody),
        method:"POST"
    })
    .then((response)=>{
        if(response.statusCode===201){
            return response.json();
        }
        else{
            throw new Error(response.status)
        }
    })
    .catch((error)=>{
        console.error(error);
    })
})