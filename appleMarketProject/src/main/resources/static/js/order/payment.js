

/*function requestPayment() {
  PortOne.requestPayment({
    storeId: "store-cdd66812-062c-4a26-ade7-8d31f74af3f5", // 고객사 storeId
    paymentId: `payment-${crypto.randomUUID()}`,
    orderName: $("#productName").val(),
    totalAmount: parseInt($("#productPrice").val())+parseInt($("#postPrice").val()),
    currency: "CURRENCY_KRW",
    channelKey: "channel-key-e7651dff-7552-4056-ba4f-d7275837ac69", // 채널 연동 시 생성된 채널 키
    payMethod: "CARD",
  });
}*/


async function requestPayment() {
	const paymentId = `payment-${crypto.randomUUID()}`;
	
	const response = await PortOne.requestPayment({
		storeId: "store-cdd66812-062c-4a26-ade7-8d31f74af3f5", // 고객사 storeId
		channelKey: "channel-key-e7651dff-7552-4056-ba4f-d7275837ac69", // 채널 연동 시 생성된 채널 키
		paymentId: paymentId,
		orderName: $("#productName").val(),
		totalAmount: parseInt($("#productPrice").val())+parseInt($("#postPrice").val()),
		currency: "CURRENCY_KRW",
		payMethod: "CARD",
		customer:{
			fullName:$("#name").val()	//구매자 이름(동작여부 확인 필요!!)
		}
  });

  if (response.code != null) {
    // 결제 중 오류 발생
    return alert("response.message");
  }

  //결제 성공 시 주문 정보 생성, 전송
  await sendOrderDetails(paymentId);  
}

async function sendOrderDetails(paymentId) {
  const orderData = {
    paymentId: paymentId,
    paymentMethod: "Credit Card",
    postAddress: $("#postcode").val()+$("#roadAddress").val()+$("#detailAddress").val(),
    postNumber: null,
    requestText: $("#requestText").val(),
    user: {
      userNo: 2 // 사용자 ID 임시저장
    },
    product: {
      productID: $("#productID").val(),
      productName: $("#productName").val(),
      productPrice: $("#productPrice").val()
    }
  };

  try {
    const response = await fetch('/order/orderInsert', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(orderData)
    });

    if (response.ok) {
      console.log('Order submitted successfully');
      window.location.href = "/order/orderList"; // 주문 목록 페이지로 이동
    } 
	else {
      throw new Error('Failed to submit order');
    }
  } catch (error) {
    console.error('Error submitting order:', error);
  }
}
