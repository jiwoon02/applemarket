$(function(){
	// 주문 내역이 담긴 카드 리스트 선택
	const transactionListContainer = $('.transaction-list');

	// 동적으로 카드를 추가
    const addCard = (order) => {
        const card = $('<div>').addClass('card mb-3').attr('data-order-id', order.orderID);
        const cardBody = $('<div>').addClass('card-body');
        
        const dateDiv = $('<div>').addClass('date').text(new Date(order.orderRegDate).toLocaleDateString());
        const detailsDiv = $('<div>').addClass('details d-flex');
        const img = $('<img>').addClass('img-thumbnail').attr('src', '/images/placeholder.png').attr('alt', 'Product Image');
        
        const infoDiv = $('<div>').addClass('info ml-3');
        const priceSpan = $('<span>').addClass('price').text(order.product.productPrice + '원');
        /*const itemNameLink = $('<a>').addClass('item-name').attr('href', '/order/' + order.product.productID).text(order.product.productName);*/
        const itemNameLink = $('<a>').addClass('item-name').attr('href', '/product/' + order.product.productID).text(order.product.productName);
        const postAddressSpan = $('<span>').addClass('post-address').text(order.postAddress);

        // 요소들을 조합하여 카드 구조 완성
        infoDiv.append(priceSpan, itemNameLink, postAddressSpan);
        detailsDiv.append(img, infoDiv);
        cardBody.append(dateDiv, detailsDiv);
        card.append(cardBody);
        
        // 완성된 카드를 컨테이너에 추가
        transactionListContainer.append(card);
    };

	$(".card").on("click",function(){
		const orderID = $(this).data('order-id');
		locationProcess("/order/"+orderID);
	});
});



