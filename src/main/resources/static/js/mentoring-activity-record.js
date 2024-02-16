let roundCount = 2;

function addRound() {
    const newRound = document.createElement('div');
    newRound.classList.add('application-status-list');

    newRound.innerHTML = `
                <p class='application-status-list-bold-font'>${roundCount}회차</p>
                <p class='application-status-list-light-font'>${roundCount}회차 주제</p>
                <div class='application-status-right-list'>
                    <div class='application-status-btn-container'>
                        <button class='application-status-orange-btn'>
                            <p>기록하기</p>
                        </button>
                    </div>
                </div>
            `;

    document.getElementById('roundsContainer').appendChild(newRound);
    roundCount++;
}