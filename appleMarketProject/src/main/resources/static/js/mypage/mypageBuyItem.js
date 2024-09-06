$(function() {
	// 리뷰 작성 버튼 클릭 시 이벤트 처리
    $(".reviewBtn").on("click", function() {
        // 클릭한 버튼에 해당하는 상품의 데이터를 가져오기
        var row = $(this).closest("tr");
        var productName = document.getElementById("itemName").textContent
        var shopId = document.getElementById("shopId").textContent
        var productId = row.find(".buyChk").val();
        var userNo = $("#userNoHide").val(); // userNo를 서버에서 전달받아 설정
        
        // 리뷰 페이지로 이동할 URL 생성
        var reviewUrl = "/mypage/addReview/" + productName + "/" + shopId + "/" + productId + "/" + userNo;

        // 페이지 이동
        location.href = reviewUrl;
    });
});

/*
document.addEventListener('DOMContentLoaded', function () {
	//tr에 링크 추가
	var rows = document.querySelectorAll('.trSellItem');
    rows.forEach(function(row) {
        // 각 tr 요소에 클릭 이벤트 추가
        row.addEventListener('click', function() {
            // 해당 row의 데이터에서 productID 값을 가져옴
            var orderId = this.querySelector('td:first-child').innerText;
            // 페이지 이동
            window.location.href = '/order/' + orderId;
        });
    });
	
});
*/