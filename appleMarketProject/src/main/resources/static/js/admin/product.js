$(".productList").on("click", function() {
	let productID = $(this).data("no");
	console.log("상품번호 :" + productID);
	locationProcess("product/" + productID);

});

