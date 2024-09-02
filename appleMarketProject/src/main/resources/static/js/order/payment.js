

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
      userNo: 2 // 사용자 ID 임시저장
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
      throw new Error('Failed to submit order');
    }
  } catch (error) {
    console.error('Error submitting order:', error);
  }
}

/*async function sendOrderDetails(paymentId) {
  const formData = new FormData();
  formData.append('paymentId', paymentId);
  formData.append('paymentMethod', 'Credit Card');
  formData.append('postAddress', $("#postcode").val()+" "+$("#roadAddress").val()+" "+$("#detailAddress").val());
  formData.append('requestText', $("#requestText").val());
  formData.append('userNo', '2'); // 사용자 ID 임시저장
  formData.append('productID', $("#productID").val());
  formData.append('productName', $("#productName").text());
  formData.append('productPrice', $("#productPrice").text());
  // 파일이 있는 경우
  const fileInput = document.getElementById('productImageFile');
  if (fileInput && fileInput.files[0]) {
    formData.append('file', fileInput.files[0]);
  }

  try {
    const response = await fetch('/order/orderInsert', {
      method: 'POST',
      body: formData // Content-Type은 자동으로 설정
    });

    if (response.ok) {
      console.log('Order submitted successfully');
      alert("주문이 완료되었습니다. 주문 내역 페이지로 이동합니다.");
      window.location.href = "/order/orderList"; // 주문 목록 페이지로 이동
    } else {
      const errorText = await response.text(); // 서버 에러 메시지 읽기
      throw new Error('Failed to submit order: ' + errorText);
    }
  } catch (error) {
    console.error('Error submitting order:', error);
    alert(error.message); // 사용자에게 에러 메시지 표시
  }
}*/
