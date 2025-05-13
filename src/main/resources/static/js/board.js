// 게시판 인터랙션 스크립트
document.addEventListener('DOMContentLoaded', function() {
    // 메시지 알림 자동 사라짐
    const messageAlerts = document.querySelectorAll('.bg-green-100, .bg-red-100');
    messageAlerts.forEach(function(alert) {
        setTimeout(function() {
            alert.style.transition = 'opacity 1s ease-out';
            alert.style.opacity = '0';
            setTimeout(function() {
                alert.style.display = 'none';
            }, 1000);
        }, 3000);
    });

    // 게시글 내용 내의 링크 처리
    const postContent = document.querySelector('.prose');
    if (postContent) {
        const text = postContent.innerHTML;
        const urlRegex = /(https?:\/\/[^\s]+)/g;
        postContent.innerHTML = text.replace(urlRegex, function(url) {
            return '<a href="' + url + '" class="text-blue-500 hover:underline" target="_blank">' + url + '</a>';
        });
    }
});