var filesArr = [];
var existingImages = [];
var fileNo = 0;

$(document).on('click', '.deleteBtn', function() {
    var filename = $(this).data('filename');
    $('#file-' + filename.replace(/\./g, '\\.')).remove(); // 점(.)을 포함한 ID 선택자를 위해 점을 이스케이프 처리
    existingImages.push(filename);
});

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
};



$("#productUpdateBtn").on("click", function(){
	if (!chkData("#productName", "상품명을")) return;
	else if(!chkData("#productDescription", "상품 설명을")) return;
	else if(!chkData("#productPrice", "상품가격을")) return;
	else if(!chkData("#postPrice", "배송비를")) return;
	
	var formData = new FormData($('#updateForm')[0]);
	
	filesArr.forEach(function(file){
		formData.append('files', file);
		
	});
	
	existingImages.forEach(function(productImageID){
		formData.append('deletedImages', productImageID)
	});
	
	$.ajax({
		url:'/product/productUpdate',
		type:'POST',
		data:formData,
		processData:false,
		contentType:false,
		success:function(data){
			alert('상품이 수정되었습니다.');
			location.href='/product/productList';
		},
		error: function(xhr, status, error) {
		            alert('상품 수정 중 오류가 발생했습니다.');
		        }
	})
});

$("#productCancelBtn").on("click", function(){
	$("#updateForm").each(function(){
		this.reset();
	});
});

$("#productListBtn").on("clcik", function(){
	location.href="/product/productList";
});
