$(document).ready(function() {
    const td1 = document.getElementById('Name');
    const td2 = document.getElementById('phoneNumber');
    const td3 = document.getElementById('Gmail');
    const td4 = document.getElementById('char');
    const td = document.getElementById('price');
    const td6 = document.getElementById('pay');

    const ten = sessionStorage.getItem('ten');
    const full = sessionStorage.getItem('fullname');
    const sdt = sessionStorage.getItem('sdt');
    const email = sessionStorage.getItem('gmail');
    const soghe = sessionStorage.getItem('cac ghe');
    const tong = sessionStorage.getItem('tong ghe');
    const coachID = sessionStorage.getItem('coachID');
    const departure = sessionStorage.getItem('form');
    const destination = sessionStorage.getItem('to');
    const departDate = sessionStorage.getItem('departDate');
    const timeGo = sessionStorage.getItem('timeGo');
    
    
    console.log(soghe);
    td3.textContent = email;
    td1.textContent = full + " " + ten;
    td2.textContent = sdt;
    td4.textContent = soghe;
    
    let number = parseInt(tong);
    let sum = number * 150000;
    console.log(sum);
    td6.textContent = sum.toString();
    const seatID = JSON.parse(sessionStorage.getItem('mang'));
    $(".container").click(function() {
        const data = {
            "name": full + " " + ten,
            "phone": sdt,
            "email": email,
            "coachID": coachID,
            "departure": departure,
            "destination": destination,
            "departDate": departDate,
            "timeGo": timeGo,
            "seatsID": seatID,
        };
        console.log(data);
        let intArr = seatID.map(str => parseInt(str));
        fetch('http://localhost:8081/v1/payment/zalo-pay', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                
                "name": full + " " + ten,
                "phone": sdt,
                "email": email,
                "coachID": coachID,
                "departure": departure,
                "destination": destination,
                "departDate": departDate,
                "timeGo": timeGo,
                "seatsID": intArr,
            })
        })
        .then(response => response.json())
        .then(data => {
            window.location.href= data.orderurl;
            console.log(data);// Chuyển hướng người dùng tới URL thanh toán của Zalo Pay
        })
        .catch(error => {
            alert("Ghế bạn chọn vừa có người đặt vui lòng chọn ghế khác");  
        });
    });
});
