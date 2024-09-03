$(function() {
	$("#fullChk").on("click", function() {
		if ($("#fullChk").prop("checked")) {
			$(".sellChk").prop("checked", true);
		} else {
			$(".sellChk").prop("checked", false);
		}
	});
	
	var url = location.href; // 현재 URL 가져오기
    var userNo = getUserNoFromUrl(); // URL에서 userNo를 가져옴

    // URL에 따라 select 태그 값 설정
    if (url.includes('/mypage/sell' + userNo)) {
        $('#itemState').val('selling');
		$(".productState").html("판매중");
    } else if (url.includes('/mypage/sold' + userNo)) {
        $('#itemState').val('selled');
		$(".productState").html("판매완료");
    } else if (url.includes('/mypage/sellAll' + userNo)) {
        $('#itemState').val('full');
    }

	$('#itemState').change(function() {
	    var userNo = getUserNoFromUrl(); // URL에서 userNo를 가져옴
	    var selectedState = $(this).val();
	    console.log(selectedState);
		
	    if (selectedState === 'selling') {
	        location.href = '/mypage/sell' + userNo;
			$("#selling").prop("selected", true);
	    } else if (selectedState === 'selled') {
	        location.href = '/mypage/sold' + userNo;
			$("#selled").prop("selected", true);
	    } else {
	        location.href = '/mypage/sellAll' + userNo;
			$("#full").prop("selected", true);
	    }
	});
	
	$("#deleteBtn").on("click", function(e) {
		// 선택된 항목 삭제 버튼 클릭 시
		e.preventDefault();
		
        if ($(".sellChk:checked").length > 0) {
            if (confirm("선택한 항목을 삭제하시겠습니까?")) {
                $("#deleteForm").submit();
            }
        } else {
            alert("삭제할 항목을 선택하세요.");
        }
    });
})

function getUserNoFromUrl() {
    var url = window.location.pathname; // 현재 URL 경로를 가져옴
    var segments = url.split('/'); // URL을 '/'로 분리
    var lastSegment = segments.pop(); // 마지막 부분을 가져옴
    var userNo = lastSegment.replace(/\D/g, ''); // 숫자가 아닌 문자를 제거하여 userNo만 추출
    return userNo;
}