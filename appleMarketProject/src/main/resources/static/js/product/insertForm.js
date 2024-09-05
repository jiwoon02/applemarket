var fileNo = 0;
var filesArr = [];

// 첨부파일 추가 메서드
function addFile(obj) {
    var maxFileCnt = 3;   // 첨부파일 최대 개수
    var attFileCnt = filesArr.length;    // 기존 추가된 첨부파일 개수
    var remainFileCnt = maxFileCnt - attFileCnt;    // 추가로 첨부가능한 개수
    var curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수

    // 첨부파일 개수 확인
    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
        return;
    } 

    for (const file of obj.files) {
        // 첨부파일 검증
        if (validation(file)) {
            // 파일 배열에 담기
            filesArr.push(file);

            // 파일 미리보기 추가
            const reader = new FileReader();
            reader.onload = function(e) {
				const imgWrapper = $('<div>').addClass('img-wrapper').attr('id', 'file' + fileNo);
				console.log(fileNo);
                const img = $('<img>').attr('src', e.target.result).addClass('img-thumbnail');
                $('.imagePreview').append(img);
				const deleteBtn = $('<a>').text('삭제').attr('onclick', 'deleteFile(' + fileNo + ')').addClass('deleteBtn');
				imgWrapper.append(img).append(deleteBtn);
				$('.imagePreview').append(imgWrapper);
				fileNo++;
			};
            reader.readAsDataURL(file);

        } else {
            continue;
        }
    }
    // 초기화
    obj.value = "";
}

// 첨부파일 검증 (파일 타입 및 크기 체크 등)
function validation(file) {
    var maxSize = 5 * 1024 * 1024; // 5MB 제한
    if (file.size > maxSize) {
        alert("파일 사이즈는 5MB를 초과할 수 없습니다.");
        return false;
    }

    var validExtensions = ["gif", "png", "jpg"];
    var fileExtension = file.name.split('.').pop().toLowerCase();
    if ($.inArray(fileExtension, validExtensions) === -1) {
        alert("허용된 파일 형식이 아닙니다. (gif, png, jpg만 허용)");
        return false;
    }
    return true;
}

// 첨부파일 삭제 메서드
function deleteFile(fileNo) {
    $('#file' + fileNo).remove();
	console.log(fileNo);
    filesArr[fileNo] = null;  // 해당 파일 배열에서 삭제 (null로 설정)
    filesArr = filesArr.filter(file => file !== null); // 배열에서 null 제거
}

// 저장 버튼 클릭 시 처리
$("#productInsertBtn").on("click", function(){
    if (!chkData("#productName", "상품명을")) return;
	else if(!chkData("#productDescription", "상품 설명을")) return;
	else if(!chkData("#productPrice", "상품가격을")) return;
	else if(!chkData("#postPrice", "배송비를")) return;
	
    var formData = new FormData($('#insertForm')[0]);

    // 파일 배열에서 실제로 존재하는 파일만 서버에 전송
    filesArr.forEach(function(file) {
        formData.append('files', file);
    });

    // 서버로 데이터 전송
    $.ajax({
        url: '/product/productInsert',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(data) {
            alert('상품이 등록되었습니다.');
            location.href = '/';
        },
        error: function(xhr, status, error) {
            alert('상품 등록 중 오류가 발생했습니다.');
        }
    });
});

$("#productCancelBtn").on("click", function(){
	$("#insertForm").each(function(){
		this.reset();
		
	});
});

$("#productListBtn").on("clcik", function(){
	location.href="/";
})


    /*
    // 파일을 배열에 추가하고 미리보기 생성
    $.each(files, function(index, file) {
        // 첨부파일 검증
        if (validation(file)) {
            filesArr.push(file);  // 파일 배열에 담기
            
            const reader = new FileReader();
            reader.onload = function(event){
                const img = $('<img>').attr('src', event.target.result).addClass('img-thumbnail');
                $('.imagePreview').append(img);
            }
            reader.readAsDataURL(file);
            fileNo++;
        }
    });

    // 초기화
    $(this).val("");
});
*/
/*
$("#productInsertBtn").on("click", function(){
    if (!chkData("#product-name", "상품명을")) return;
    else {
        if ($("#fileInput").val() != "") {
            if (!chkFile("#fileInput")) return;
        }
        $("#insertForm").attr({
            "method": "post",
            "enctype": "multipart/form-data",
            "action": "/product/productInsert"  // 절대 경로로 수정
        });
        $("#insertForm").submit();
    }
});
*/


/*
$(document).on('change', '#fileInput', function(){
    const files = this.files;
    if(files.length > 0){
        for(let i = 0; i < files.length; i++){
            const reader = new FileReader();
            reader.onload = function(event){
                const img = $('<img>').attr('src', event.target.result).addClass('img-thumbnail');
                $('.imagePreview').append(img);
            }
            reader.readAsDataURL(files[i]);
        }
        $('.imagePreview').show();  // 미리보기 영역 표시
    }
});
*/