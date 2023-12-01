const accept_button = document.getElementById('acceptBtn');
const reject_button = document.getElementById('rejectBtn')


reject_button.addEventListener('click',function(){
    window.location.href = ''
});
document.addEventListener('DOMContentLoaded', function() {
    getData();
});

function getData(){
//멘토 신청 현황 백엔드 통신
    const url = '/mentoring/mentor';
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
        mento_apply_status(data);
    })
    .catch((error)=>{
        console.error(error);
    })
// 멘티 신청 현황 백엔드 통신
    url = '/mentoring/mentee';
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
        mentee_apply_status(data);
    })
    .catch((error)=>{
        console.error(error);
    })
// 나의 멘토 백엔드 통신
    url = '/mentors/me';
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
        myMentor_list(data);
    })
    .catch((error)=>{
        console.error(error);
    })
    url = '/mentees/me';
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
        myMentee_list(data);
    })
    .catch((error)=>{
        console.error(error);
    })
}
// 나의 멘토 현황 html 추가
function myMentor_list(response){
    const mentorsListContainer = document.querySelector('.mentors-list');
    response.data.forEach(mento=>{
        const mentorsInfoContainer = document.createElement('div');
        mentorsInfoContainer.classList.add('mentors-info-container');

        const leftContainer = document.createElement('div');
        leftContainer.classList.add('mentors-list-left');
        
        if(mento.imageUrl != null){
            const imgElement = document.createElement('img');
            imgElement.classList.add('mentors-list-img');
            imgElement.src(mento.imageUrl)
            leftContainer.appendChild(imgElement);
        }
        else{
            const imgDiv = document.createElement('div');
            imgDiv.classList.add('mentors-list-img'); 
            leftContainer.appendChild(imgDiv);
        }
        

        const rightContainer = document.createElement('div');
        rightContainer.classList.add('mentors-list-right');

        const upperContainer = document.createElement('div');
        upperContainer.classList.add('mentors-list-right-upper');

        const namePara = document.createElement('p');
        namePara.classList.add('mentors-list-name');
        namePara.textContent = mento.name;

        const explainPara = document.createElement('p');
        explainPara.classList.add('mentors-list-explain');
        explainPara.textContent = '';

        const lowerContainer = document.createElement('div');
        lowerContainer.classList.add('mentor-list-right-lower');

        const matchingLabel = document.createElement('div');
        matchingLabel.classList.add('mentor-list-label');
        //이후 수정 필요
        matchingLabel.innerHTML = `<p>멘토매칭 ${mento.mentoringCount}건</p>`;

        const reviewLabel = document.createElement('div');
        reviewLabel.classList.add('mentor-list-label');
        //이후 수정 필요
        reviewLabel.innerHTML = `<p>리뷰 ${mento.reviewCount}개</p>`;

        // 요소들을 조립
        upperContainer.appendChild(namePara);
        upperContainer.appendChild(explainPara);

        lowerContainer.appendChild(matchingLabel);
        lowerContainer.appendChild(reviewLabel);

        rightContainer.appendChild(upperContainer);
        rightContainer.appendChild(lowerContainer);

        

        mentorsInfoContainer.appendChild(leftContainer);
        mentorsInfoContainer.appendChild(rightContainer);

        // 생성한 요소를 해당 컨테이너에 추가
        mentorsListContainer.appendChild(mentorsInfoContainer);
    })
}
//나의 멘티 현황 html 추가
function myMentee_list(response){

}
//멘토(나)가 멘티를 수락/거절
function mento_apply_status(response){
    response.data.forEach(item=>{
        const newStatusList = document.createElement('div');
        newStatusList.classList.add('application-status-list');

        const boldText = document.createElement('p');
        boldText.classList.add('application-status-list-bold-font');
        boldText.textContent = item.mentee.name;

        const lightText = document.createElement('p');
        lightText.classList.add('application-status-list-light-font');
        lightText.textContent = item.mentee.content;

        const rightList = document.createElement('div');
        rightList.classList.add('application-status-right-list');

        const btnContainer = document.createElement('div');
        btnContainer.classList.add('application-status-btn-container');

        const acceptBtn = document.createElement('button');
        acceptBtn.classList.add('application-status-orange-btn');
        acceptBtn.innerHTML = `
            <img src='../src/mentoring-detail-card-orange.png'>
            <p>수락하기</p>
        `;

        const rejectBtn = document.createElement('button');
        rejectBtn.classList.add('application-status-white-btn');
        rejectBtn.innerHTML = `
            <img src='../src/mentoring-detail-card-gray.png'>
            <p>거절하기</p>
        `;

        // 요소들을 조립
        btnContainer.appendChild(acceptBtn);
        btnContainer.appendChild(rejectBtn);

        rightList.appendChild(btnContainer);

        newStatusList.appendChild(boldText);
        newStatusList.appendChild(lightText);
        newStatusList.appendChild(rightList);

        document.querySelector('.application-status-list-container').appendChild(newStatusList);

        accept_button.addEventListener('click',function(){
            mento_mentoring_accept(item.id)
        });
    })
}
//멘토가 수락 버튼을 눌럿을 때
function mento_mentoring_accept(id){
    const url = `/mentorings/${id}`;
    var myHeader = new Headers();
    myHeader.append('Authorization',`Bearer ${token}`);  
    myHeader.append('Content-Type','application/json');
    fetch(url,{
        headers : myHeader,
        body:JSON.stringify({
            "status":"PROGRESS",
            "reason":null
        }),
        method:"POST"
    })
    .then((response)=>{
        if(response.statusCode===200){
            return response.json();
        }
        else{
            throw new Error(response.status)
        }
    })
    .catch((error)=>{
        console.error(error);
    })
    alert('멘티와 매칭되었어요!')
    window.location.href = ''
}
//멘티(나)가 지원한 멘토링에 대한 목록 현황
function mentee_apply_status(response){
    response.data.forEach(item=>{
        const statusListContainer = document.querySelector('.application-status-list-container');

        const newStatusList = document.createElement('div');
        newStatusList.classList.add('application-status-list');

        const boldText = document.createElement('p');
        boldText.classList.add('application-status-list-bold-font');
        boldText.textContent = item.mentee.name;

        const lightText = document.createElement('p');
        lightText.classList.add('application-status-list-light-font');
        lightText.textContent = item.mentee.content;

        const rightList = document.createElement('div');
        rightList.classList.add('application-status-right-list');
        if(item.status == 'PROGRESS'){
            const statusBtn = document.createElement('button');
            statusBtn.classList.add('application-mentee-status-orange-btn');
            statusBtn.textContent = '진행 중인 멘토링';
        }else if(item.status == 'CLOSE'){
            const statusBtn = document.createElement('button');
            statusBtn.classList.add('application-mentee-status-gray-btn');
            statusBtn.textContent = '종료된 멘토링';
        }else if(item.status == 'REFUSE'){
            const statusBtn = document.createElement('button');
            statusBtn.classList.add('application-mentee-status-gray-btn');
            statusBtn.textContent = '거절된 멘토링';
        }
        rightList.appendChild(statusBtn);

        newStatusList.appendChild(boldText);
        newStatusList.appendChild(lightText);
        newStatusList.appendChild(rightList);

        statusListContainer.appendChild(newStatusList);
    });
    

    
}