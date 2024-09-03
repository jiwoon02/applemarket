 function execDaumPostcode() {

    new daum.Postcode({
        oncomplete: function(data) {
            let roadAddr = data.roadAddress;
            let extraRoadAddr = '';

            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }

            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }

            if (extraRoadAddr !== '') {
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }

            const postcodeElement = document.getElementById('postcode');
            const roadAddressElement = document.getElementById("roadAddress");
            const extraAddressElement = document.getElementById("extraAddress");
            const guideTextBox = document.getElementById("guide");

            if (postcodeElement) {
                postcodeElement.value = data.zonecode;
            }

            if (roadAddressElement) {
                roadAddressElement.value = roadAddr;
            }

            if (extraAddressElement) {
                if (roadAddr !== '') {
                    extraAddressElement.value = extraRoadAddr;
                } else {
                    extraAddressElement.value = '';
                }
            }

            if (guideTextBox) {
                if (data.autoRoadAddress) {
                    let expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';
                } else {
                    guideTextBox.innerHTML = '';
                    guideTextBox.style.display = 'none';
                }
            }
        }
    }).open();

};