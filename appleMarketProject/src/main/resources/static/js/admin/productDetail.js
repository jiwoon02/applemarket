var productID = $('#productID').val();
$("#categoryChangeButton").on("click", function() {
    var categoryID = $('#categoryID').val();
    
	console.log(productID);
	
    $.ajax({
        url: '/admin/success/product/' + productID + '/category/change',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ categoryID: categoryID}),
        success: function(response) {
            alert('카테고리가 성공적으로 변경되었습니다.');
            window.location.reload();
        },
        error: function(xhr, status, error) {
            alert('카테고리 변경에 실패했습니다.');
        }
    });
});

$("#deleteBtn").on("click", function() {
	if(confirm("정말 삭제하시겠습니까?")){
		$.ajax({
			url: '/admin/success/product/' + productID + '/product/delete',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify([productID]),
			success: function(response) {
				alert('상품이 삭제되었습니다.');
			},
			error: function(xhr, status, error) {
				alert('상품 삭제에 실패했습니다.');
			}
		})
		
		setTimeout(function() {
			locationProcess("../product");
		}, 100);
	}
	
})
