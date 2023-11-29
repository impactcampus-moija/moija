const container1 = document.getElementById('mentor-accept-container-1');
const container2 = document.getElementById('mentor-accept-container-2');
const container3 = document.getElementById('mentor-accept-container-3');

const agree_mentoring = document.getElementById('agree-mentoring')
const reject_mentoring = document.getElementById('reject-mentoring')

$('#mentor-accept-container-1').show();
$('#mentor-accept-container-2').hide();
$('#mentor-accept-container-3').hide();

agree_mentoring.addEventListener('click',function(){
    $('#mentor-accept-container-1').hide();
    $('#mentor-accept-container-2').show();
});

reject_mentoring.addEventListener('click',function(){
    $('#mentor-accept-container-1').hide();
    $('#mentor-accept-container-3').show();
});