$(function() {
	// 별점 클릭 이벤트 핸들러
	$(".starImg2 img").on("click", function() {
	    var rating = $(this).data("rating");

	    // 모든 별 이미지를 초기화
	    $(".starImg2 img").attr("src", "/images/usershop/star2.png");

	    // 선택한 별까지 이미지를 채워줌
	    for (var i = 1; i <= rating; i++) {
	        $("#star" + i).attr("src", "/images/usershop/star.png");
	    }

	    // 선택한 별점 값을 hidden input에 설정
	    $("#starRating").val(rating);
	});


    // 리뷰 항목 클릭 이벤트 핸들러
    $(".comment2").on("click", function() {
		var index = $(this).data("review-index");
        var inputField = $("#selectReview" + index);

        // 배경색 변경 및 값 토글
        var currentColor = $(this).css("background-color");
        
        if (currentColor === "rgb(230, 230, 230)") {
            $(this).css("background-color", "rgb(246, 246, 246)");
            inputField.val(0);
        } else {
            $(this).css("background-color", "rgb(230, 230, 230)");
            inputField.val(1);
        }
    });

    // 제출 버튼 클릭 이벤트 핸들러
    $(".submitBtn").on("click", function(event) {
        event.preventDefault(); // 기본 제출 동작 방지
        if (confirm("리뷰 작성을 완료하시겠습니까?")) {
            $("#reviewForm").submit(); // 확인 시 폼 제출
        }
    });

    // 취소 버튼 클릭 이벤트 핸들러
    $('.cancelBtn').on('click', function() {
        $('#reviewForm')[0].reset(); // 폼 초기화
        // 초기화 후 별 이미지 및 리뷰 항목 배경색도 초기화
        $(".starImg2 img").attr("src", "/images/usershop/star2.png");
        $(".comment2").css("background-color", "rgb(246, 246, 246)");
    });
	
	// 별점 평균값에 따른 이미지
	if ($("#averageStarRating").val() == 0) {
	    $(".reviewStar1").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar2").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar3").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar4").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar5").attr("src", "/images/usershop/star2.png");
	} else if ($("#averageStarRating").val() == 1) {
	    $(".reviewStar1").attr("src", "/images/usershop/star.png");
	    $(".reviewStar2").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar3").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar4").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar5").attr("src", "/images/usershop/star2.png");
	} else if ($("#averageStarRating").val() == 2) {
	    $(".reviewStar1").attr("src", "/images/usershop/star.png");
	    $(".reviewStar2").attr("src", "/images/usershop/star.png");
	    $(".reviewStar3").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar4").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar5").attr("src", "/images/usershop/star2.png");
	} else if ($("#averageStarRating").val() == 3) {
	    $(".reviewStar1").attr("src", "/images/usershop/star.png");
	    $(".reviewStar2").attr("src", "/images/usershop/star.png");
	    $(".reviewStar3").attr("src", "/images/usershop/star.png");
	    $(".reviewStar4").attr("src", "/images/usershop/star2.png");
	    $(".reviewStar5").attr("src", "/images/usershop/star2.png");
	} else if ($("#averageStarRating").val() == 4) {
	    $(".reviewStar1").attr("src", "/images/usershop/star.png");
	    $(".reviewStar2").attr("src", "/images/usershop/star.png");
	    $(".reviewStar3").attr("src", "/images/usershop/star.png");
	    $(".reviewStar4").attr("src", "/images/usershop/star.png");
	    $(".reviewStar5").attr("src", "/images/usershop/star2.png");
	} else if ($("#averageStarRating").val() == 5) {
	    $(".reviewStar1").attr("src", "/images/usershop/star.png");
	    $(".reviewStar2").attr("src", "/images/usershop/star.png");
	    $(".reviewStar3").attr("src", "/images/usershop/star.png");
	    $(".reviewStar4").attr("src", "/images/usershop/star.png");
	    $(".reviewStar5").attr("src", "/images/usershop/star.png");
	}

});
