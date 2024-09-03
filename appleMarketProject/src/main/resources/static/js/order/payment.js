async function requestPayment() {
	
	// 유효성 검사 추가
	if (!validatePaymentDetails()) {
		return; // 유효성 검사 실패 시 결제 진행 중지
	}
	
	const paymentId = `payment-${crypto.randomUUID()}`;
	
	const response = await PortOne.requestPayment({
		storeId: "store-cdd66812-062c-4a26-ade7-8d31f74af3f5", // 고객사 storeId
		channelKey: "channel-key-e7651dff-7552-4056-ba4f-d7275837ac69", // 채널 연동 시 생성된 채널 키
		paymentId: paymentId,
		orderName: $("#productName").text(),
		totalAmount: parseInt($("#productPrice").text())+parseInt($("#postPrice").val()),
		currency: "CURRENCY_KRW",
		payMethod: "CARD",
		customer:{
			fullName:$("#name").val()	//구매자 이름
		}
  });

  if (response.code != null) {
    // 결제 중 오류 발생
    return alert(response.message);
  }

  //결제 성공 시 주문 정보 생성, 전송
  await sendOrderDetails(paymentId);  
}

// 유효성 검사 함수
function validatePaymentDetails() {
    if (!chkData("#name", "이름을")) return false;
    if (!chkData("#phone", "전화번호를")) return false;
    if (!chkData("#postcode", "우편번호를")) return false;
    if (!chkData("#roadAddress", "도로명주소를")) return false;
    return true; 
}

async function sendOrderDetails(paymentId) {
  const orderData = {
    paymentId: paymentId,
    paymentMethod: "Credit Card",
    postAddress: $("#postcode").val()+" "+$("#roadAddress").val()+" "+$("#detailAddress").val(),
    postNumber: null,
    requestText: $("#requestText").val(),
    user: {
      userNo: 1 // 사용자 ID 임시저장
    },
    product: {
      productID: $("#productID").val(),
      productName: $("#productName").text(),
      productPrice: parseInt($("#productPrice").text()),
    },
	productImages: {
		productImageID: $("#productImageID").val()
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
	  alert("주문이 완료되었습니다. 주문 내역 페이지로 이동합니다.");
      window.location.href = "/order/orderList"; // 주문 목록 페이지로 이동
    } 
	else {
		const errorMessage = await response.text(); // 서버에서 반환된 에러 메시지 확인
		console.error('Error submitting order:', errorMessage);
		throw new Error('Failed to submit order');
    }
  } catch (error) {
    console.error('Error submitting order:', error);
  }
}
