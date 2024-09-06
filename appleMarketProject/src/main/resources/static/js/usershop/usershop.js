$(function() {
    showItems(); 
    
	$("#nickname").css("display", "none");
	
    // 초기 화면에서 divCommentSelect의 높이를 40%로 설정
    $('.divCommentSelect').css('height', '200px');
            
    // heightImg 클릭 시 높이를 토글하는 이벤트 핸들러 추가
    $('.heightImg').click(function() {
        var commentSelect = $('.divCommentSelect');
        if (commentSelect.css('height') == '200px') {
            commentSelect.css('height', '330px');  // 높이를 100%로 늘림
            $(this).attr('src', '../images/usershop/heightImg2.png');  // 이미지 변경
        } else {
            commentSelect.css('height', '200px');  // 높이를 40%로 줄임
            $(this).attr('src', '../images/usershop/heightImg1.png');  // 원래 이미지로 변경
        }
    });
	
	// 초기 화면에서 divContent영역 숨기기
	$(".divContent").css("display", "none");
	
	// modifyBtn 버튼 클릭 시 
	$(".modifyBtn").on("click", function() {
		$(".divContent").css("display", "block");
		$(".divModifyContent").css("display", "none");
		$(".divModifyBtn").css("display", "none");
		
		$(".divShopName2").css("display", "none");
		$("#nickname").css("display", "block");
		
	});
	
	$(".profileImg").on("click", function() {
		$('#fileInput').click();
	});
	
	// 파일 선택 후, actionFileProcess 실행
    $('#fileInput').change(function() {
        var file = this.files[0];
        if (file) {
            // 선택한 파일에 대해 추가 작업 수행 (예: 미리보기, 서버 전송)
            var reader = new FileReader();
            reader.onload = function(e) {
                $('.profileImg').attr('src', e.target.result);
            }
            reader.readAsDataURL(file);

            // 파일이 선택되면 actionFileProcess 함수 호출
            actionFileProcess("#shopImg", "POST", "/usershop/shopImgUpdate");
        }
    });
	
	$(".confirmBtn").on("click", function() {
	    $("#textUpdate").attr({
	        "method": "post",
	        "action": "/usershop/usershopUpdate"
	    });
	    $("#textUpdate").submit();
		
		$(".divShopName2").css("display", "block");
		$("#nickname").css("display", "none");
	});
	
	// 별점 평균값에 따른 이미지
	if ($(".starNum").html() == 0) {
		$(".reviewStar1").attr("src", "../images/usershop/star2.png");
		$(".reviewStar2").attr("src", "../images/usershop/star2.png");
		$(".reviewStar3").attr("src", "../images/usershop/star2.png");
		$(".reviewStar4").attr("src", "../images/usershop/star2.png");
		$(".reviewStar5").attr("src", "../images/usershop/star2.png");
	} else if ($(".starNum").html() == 1) {
		$(".reviewStar1").attr("src", "../images/usershop/star.png");
		$(".reviewStar2").attr("src", "../images/usershop/star2.png");
		$(".reviewStar3").attr("src", "../images/usershop/star2.png");
		$(".reviewStar4").attr("src", "../images/usershop/star2.png");
		$(".reviewStar5").attr("src", "../images/usershop/star2.png");
	} else if ($(".starNum").html() == 2) {
		$(".reviewStar1").attr("src", "../images/usershop/star.png");
		$(".reviewStar2").attr("src", "../images/usershop/star.png");
		$(".reviewStar3").attr("src", "../images/usershop/star2.png");
		$(".reviewStar4").attr("src", "../images/usershop/star2.png");
		$(".reviewStar5").attr("src", "../images/usershop/star2.png");
	} else if ($(".starNum").html() == 3) {
		$(".reviewStar1").attr("src", "../images/usershop/star.png");
		$(".reviewStar2").attr("src", "../images/usershop/star.png");
		$(".reviewStar3").attr("src", "../images/usershop/star.png");
		$(".reviewStar4").attr("src", "../images/usershop/star2.png");
		$(".reviewStar5").attr("src", "../images/usershop/star2.png");
	} else if ($(".starNum").html() == 4) {
		$(".reviewStar1").attr("src", "../images/usershop/star.png");
		$(".reviewStar2").attr("src", "../images/usershop/star.png");
		$(".reviewStar3").attr("src", "../images/usershop/star.png");
		$(".reviewStar4").attr("src", "../images/usershop/star.png");
		$(".reviewStar5").attr("src", "../images/usershop/star2.png");
	} else if ($(".starNum").html() == 5) {
		$(".reviewStar1").attr("src", "../images/usershop/star.png");
		$(".reviewStar2").attr("src", "../images/usershop/star.png");
		$(".reviewStar3").attr("src", "../images/usershop/star.png");
		$(".reviewStar4").attr("src", "../images/usershop/star.png");
		$(".reviewStar5").attr("src", "../images/usershop/star.png");
	}
	
	// 정렬 기능 추가
    $('#sortNewest').on('click', function() {
        sortItems('newest');
    });

    $('#sortLowestPrice').on('click', function() {
        sortItems('lowestPrice');
    });

    $('#sortHighestPrice').on('click', function() {
        sortItems('highestPrice');
    });
	
	$(".divItems").on("click", ".divItem", function() {
	    var productId = $(this).data("product-id");
	    location.href = "/product/" + productId;
	});
	
	$(".shopPage").on("click", function() {
		var userNo = $(".userImg").val();	
		location.href="/usershop/list" + userNo
	});
	
	$(".itemBtn").on("click", function() {
		var productId = $("#productId").val();	
		location.href = "/product/" + productId;
	});
	
	$('.productName').each(function() {
        var text = $(this).text();
        var textLength = text.length;

        // 텍스트 길이가 5보다 크면 5글자만 추출하고 ...을 붙임
        if (textLength > 5) {
            var truncatedText = text.substring(0, 5) + '...';
            $(this).text(truncatedText);  // 조작한 텍스트를 다시 요소에 넣음
        }
    });
	
	$('.itemBtn').each(function() {
        var text = $(this).text();
        var textLength = text.length;

        // 텍스트 길이가 10보다 크면 10글자만 추출하고 ...을 붙임
        if (textLength > 10) {
            var truncatedText = text.substring(0, 10) + '...';
            $(this).text(truncatedText);  // 조작한 텍스트를 다시 요소에 넣음
        }
    });
	
});

function showItems() {
    $('.divTab1').css('display', 'grid');  // 상품 목록을 표시
    $('.divTab2').css('display', 'none');  // 상점 후기를 숨김

    $('.itemLink:first').css({
        'background-color': '#d3d3d3',  // 상품 버튼의 배경을 회색으로
        'color': 'black'  // 상품 버튼의 글자색을 검정으로
    });
    
    $('.itemLink:last').css({
        'background-color': '#ffffff',  // 상점후기 버튼의 배경을 흰색으로
        'color': '#999999'  // 상점후기 버튼의 글자색을 회색으로
    });
}

function showReviews() {
    $('.divTab1').css('display', 'none');  // 상품 목록을 숨김
    $('.divTab2').css('display', 'block');  // 상점 후기를 표시

    $('.itemLink:first').css({
        'background-color': '#ffffff',  // 상품 버튼의 배경을 흰색으로
        'color': '#999999'  // 상품 버튼의 글자색을 회색으로
    });
    
    $('.itemLink:last').css({
        'background-color': '#d3d3d3',  // 상점후기 버튼의 배경을 회색으로
        'color': 'black'  // 상점후기 버튼의 글자색을 검정으로
    });
}

function chkFile(item) {
	/*	참고사항
		jQuery.inArray(찾을 값, 검색 대상의 배열): 배열내의 값을 찾아서 인덱스를 반환(요소가 없을 경우 -1 반환)	
		pop(): 배열의 마지막 요소를 제거한 후, 제거한 요소를 반환
	*/
	let ext = $(item).val().split('.').pop().toLowerCase();
	if(jQuery.inArray(ext, ['gif', 'png', 'jpg']) == -1) {
		alert("gif, png, jpg 파일만 업로드 할 수 있습니다.");
		item.val("");
		return false;
	} else {
		return true;
	}
}

function sortItems(criteria) {
    let items = $('.divItem').get();  // 모든 상품 아이템을 가져옴

    items.sort(function(a, b) {
        let aValue, bValue;

        if (criteria === 'newest') {
            aValue = new Date($(a).find('.itemTime').text());
            bValue = new Date($(b).find('.itemTime').text());
            return bValue - aValue;  // 최신순 (내림차순)
        } else if (criteria === 'lowestPrice') {
            aValue = parseInt($(a).find('.itemPrice').text().replace(/[^0-9]/g, ''));
            bValue = parseInt($(b).find('.itemPrice').text().replace(/[^0-9]/g, ''));
            return aValue - bValue;  // 저가순 (오름차순)
        } else if (criteria === 'highestPrice') {
            aValue = parseInt($(a).find('.itemPrice').text().replace(/[^0-9]/g, ''));
            bValue = parseInt($(b).find('.itemPrice').text().replace(/[^0-9]/g, ''));
            return bValue - aValue;  // 고가순 (내림차순)
        }
    });
	$('.divItems').html(items);
}
