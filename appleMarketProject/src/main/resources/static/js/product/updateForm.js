var filesArr = [];
var existingImages = [];
var fileNo = 0;

$(document).on('click', '.deleteBtn', function() {
    var filename = $(this).data('filename');
    $('#file-' + filename.replace(/\./g, '\\.')).remove();
    
    // 기존 이미지 삭제
    if (existingImages.includes(filename)) {
        existingImages = existingImages.filter(function(image) {
            return image !== filename;
        });
    } else {
        // 새로 추가된 파일 삭제
        filesArr = filesArr.filter(function(file) {
            return file.name !== filename;
        });
    }
});

function addFile(obj) {
    var maxFileCnt = 3; // 첨부파일 최대 개수
    var attFileCnt = filesArr.length; // 기존 추가된 첨부파일 개수
    var remainFileCnt = maxFileCnt - attFileCnt; // 추가로 첨부가능한 개수
    var curFileCnt = obj.files.length; // 현재 선택된 첨부파일 개수

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
                const imgWrapper = $('<div>').addClass('img-wrapper').attr('id', 'file-' + file.name.replace(/\./g, '\\.'));
                const img = $('<img>').attr('src', e.target.result).addClass('img-thumbnail');
                const deleteBtn = $('<a>').text('삭제').attr('onclick', 'deleteFile("' + file.name.replace(/\./g, '\\.') + '")').addClass('deleteBtn').data('filename', file.name);
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

function deleteFile(filename) {
    $('#file-' + filename.replace(/\./g, '\\.')).remove();

    // 기존 이미지 삭제
    if (existingImages.includes(filename)) {
        existingImages = existingImages.filter(function(image) {
            return image !== filename;
        });
    } else {
        // 새로 추가된 파일 삭제
        filesArr = filesArr.filter(function(file) {
            return file.name !== filename;
        });
    }
}

$("#productUpdateBtn").on("click", function() {
    var formData = new FormData($('#updateForm')[0]);

    // 기존 이미지 전송
    existingImages.forEach(function(filename) {
        formData.append('existingImages', filename);
    });

    // 새로 추가된 파일 전송
    filesArr.forEach(function(file) {
        formData.append('files', file);
    });

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

$("#productCancelBtn").on("click", function() {
    $("#updateForm").each(function() {
        this.reset();
    });
});

$("#productListBtn").on("click", function() {
    location.href = "/product/productList";
});

