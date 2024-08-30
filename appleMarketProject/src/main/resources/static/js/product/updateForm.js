// 모든 hidden input 요소를 가져와 filesArr 배열에 저장
var filesArr = Array.from(document.getElementsByName('existingFileNames')).map(input => input.value);

// 삭제된 이미지의 이름을 filesArr에서 제거
function deleteFile(obj) {
    var filename = $(obj).data('filename');
	console.log(filename);
    filesArr = filesArr.filter(file => file !== filename);
    $(obj).closest('.img-wrapper').remove();  // 이미지 프리뷰에서 삭제
}


function addFile(obj) {
    for (const file of obj.files) {
        filesArr.push(file.name);
		console.log(file.name);
        const reader = new FileReader();
        reader.onload = function(e) {
            const imgWrapper = $('<div>').addClass('img-wrapper').attr('id', 'file-' + file.name.replace(/\./g, '\\.'));
            const img = $('<img>').attr('src', e.target.result).addClass('img-thumbnail');
            const deleteBtn = $('<a>').text('삭제').attr('onclick', 'deleteFile(this)').data('filename', file.name).addClass('deleteBtn');
            imgWrapper.append(img).append(deleteBtn);
            $('.imagePreview').append(imgWrapper);
        };
        reader.readAsDataURL(file);
    }
    obj.value = "";
}

$("#productUpdateBtn").on("click", function() {
    var formData = new FormData($('#updateForm')[0]);

    // 기존 이미지 전송
    filesArr.forEach(function(filename) {
        formData.append('existingFileNames', filename);
    });

    // 새로 추가된 파일 전송
    for (var i = 0; i < $('#fileInput')[0].files.length; i++) {
        formData.append('files', $('#fileInput')[0].files[i]);
    }

    $.ajax({
        url: '/product/productUpdate',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(data) {
            alert('상품이 수정되었습니다.');
            location.href = '/product/' + $("#productID").val();
        },
        error: function(xhr, status, error) {
            alert('상품 수정 중 오류가 발생했습니다.');
        }
    });
});

// 파일 검증 함수
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

$("#productCancelBtn").on("click", function() {
    $("#updateForm").each(function() {
        this.reset();
    });
});

$("#productListBtn").on("click", function() {
    location.href = "/product/productList";
});
