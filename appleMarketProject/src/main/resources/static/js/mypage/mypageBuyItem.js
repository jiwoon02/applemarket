$(function() {
	// 리뷰 작성 버튼 클릭 시 이벤트 처리
    $(".reviewBtn").on("click", function() {
		// 클릭한 버튼에 해당하는 상품의 데이터를 가져오기
	    var row = $(this).closest("tr");
	    var productName = document.getElementById("productName").value; 
	    var productId = row.find(".productId").val();                   // jQuery에서 # 사용
	    
	    // 리뷰 페이지로 이동할 URL 생성
	    var reviewUrl = "/mypage/addReview/" + productName + "/" + productId;

	    // 페이지 이동
	    location.href = reviewUrl;
    });
});

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
	// orderDetailLink 클릭 시 이벤트 처리
    $(".orderDetailLink").on("click", function() {
        // 클릭한 셀에 해당하는 행에서 orderId 값을 가져옴
        var orderId = $(this).closest("tr").find("td:first-child").text();
        // 페이지 이동
        location.href = '/order/' + orderId;
    });
	
	var reviewProductIds = document.getElementsByClassName("reviewProductIds");
	var productId = document.getElementsByClassName("productId");
	for (var i = 0; i < productId.length; i++) {
	    // 각각의 productId와 reviewProductIds를 비교
	    for (var j = 0; j < reviewProductIds.length; j++) {
	        if (reviewProductIds[j].textContent.trim() == productId[i].value.trim()) {
	            // 현재 productId에 해당하는 reviewBtn을 찾아서 "수정하기"로 변경
	            $(productId[i]).closest(".trSellItem").find(".reviewBtn").html("수정하기");
	        }
	    }
	}
});