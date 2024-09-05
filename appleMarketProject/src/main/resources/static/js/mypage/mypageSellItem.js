document.addEventListener('DOMContentLoaded', function () {
	
	//tr에 링크 추가
	var rows = document.querySelectorAll('.trSellItem');
    rows.forEach(function(row) {
        // 각 tr 요소에 클릭 이벤트 추가
        row.addEventListener('click', function() {
            // 해당 row의 데이터에서 productID 값을 가져옴
            var productId = this.querySelector('td:first-child').innerText;
            // 페이지 이동
            window.location.href = '/product/' + productId;
        });
    });
	
    const itemStateSelect = document.getElementById('itemState');
    
    itemStateSelect.addEventListener('change', function () {
        const selectedState = this.value; // 선택된 옵션 값 (full, selling, selled)
        const rows = document.querySelectorAll('tbody tr.trSellItem'); // 모든 상품 행을 선택

        rows.forEach(row => {
            const status = row.querySelector('td:last-child').textContent.trim(); // 상품 상태를 가져옴

            if (selectedState === '전체') {
                // 전체를 선택하면 모든 항목을 보여줌
                row.style.display = '';
            } else if (status === selectedState) {
                // 선택된 상태와 일치하는 상품만 보여줌
                row.style.display = '';
            } else {
                // 일치하지 않는 상품은 숨김
                row.style.display = 'none';
            }
        });

        // 표시된 상품이 없을 경우 "판매내역이 존재하지 않습니다." 메시지 처리
        const visibleRows = Array.from(rows).filter(row => row.style.display !== 'none');
        const emptyMessageRow = document.querySelector('.emptyTbl');
        
        if (visibleRows.length === 0) {
            emptyMessageRow.style.display = '';
        } else {
            emptyMessageRow.style.display = 'none';
        }
    });
});

