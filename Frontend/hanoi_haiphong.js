
$(document).ready(function() {
    const ketqua = document.getElementById('ketqua');

    cnt=0;
    const to=sessionStorage.getItem('To');
    const from=sessionStorage.getItem('From');
    const date=sessionStorage.getItem('departDater');
    document.getElementById('tripTitle').innerText = from+"-"+to;
    try{
            const data = JSON.parse(sessionStorage.getItem('data'));    
            // Kiểm tra xem dữ liệu có tồn tại không
            if (data) {
                console.log('Dữ liệu từ sessionStorage:', data);
                // Hiển thị dữ liệu hoặc xử lý nó theo cách bạn muốn
            } else {
                console.log('Không có dữ liệu trong sessionStorage');
            }
            const ticketsContainer = document.getElementById('tickets-container');
        // Tạo vé động từ dữ liệu
            data.forEach(ticket => {
                cnt=cnt+1;
                // Tạo phần tử div cho mỗi vé
                const ticketDiv = document.createElement('div');
                ticketDiv.classList.add('ticket');
                ticketDiv.setAttribute('id', ticket.coachID); // Gán ID cho vé
                ticketDiv.setAttribute('data-value', ticket.timeGo);
                ticketDiv.innerHTML = `
                <h3 class="TimeGo">Khởi hành: ${ticket.timeGo}-${ticket.timeCome}</h3>
                <h3 class="TimeCome">Giá: 150000</h3>
                <h3 class="seatsLeft">Số ghế còn lại:${ticket.seatsLeft}</h3>
                <button class="choose-seat-button">Chọn ghế</button>
            `;
                

                // Thêm vé vào container
                ticketsContainer.appendChild(ticketDiv);
                // $('#ticketsContainer').append(ticketDiv);
            });

        // Mở modal khi nhấn vào "Chọn ghế"
            arr=[]
            $(".choose-seat-button").click(function() {
                
                // Lấy vé cụ thể mà người dùng đang thao tác
                $(".modal").css("display", "block");
                let ticketDiv = $(this).closest(".ticket"); // Vé đang được thao tác
                $(".modal").css("display", "block");

                // Lấy ID và data-value của vé
                const ticketId = ticketDiv.attr('id'); // Lấy ID của vé bằng jQuery
                const ticketValue = ticketDiv.attr('data-value'); // Lấy data-value của vé bằng jQuery
                
                // In ra kết quả
                console.log('ID của vé:', ticketId);
                console.log('Giá trị thời gian đi:', ticketValue);
                // Gửi yêu cầu POST
                i=0;
                fetch('http://localhost:8081/v1/booking?from=HaNoi&to=LaoCai&departDate=20241130', {
                    method: 'POST',
                    headers: {
                    'Content-Type': 'application/json',  // Đảm bảo Content-Type là application/json
                    },
                    body: JSON.stringify({
                        "coachID" : ticketId.toString(),
                        "timeGo" : ticketValue.toString()
                    })  // Chuyển đổi dữ liệu thành chuỗi JSON
                })
                .then(response => response.json())  // Chuyển đổi phản hồi từ server thành JSON
                .then(data => {
                    console.log('Success:', data);
                    const container = document.querySelector('.seat-grid');
                    container.innerHTML = '';
                    data.forEach(seat => {
                        const char = document.createElement('button');
                        char.classList.add('seat'); // Thêm lớp 'seat'
                        char.setAttribute('value', seat.seatID); // Thêm thuộc tính 'value' với giá trị i
                        char.textContent = seat.seatID.toString(); // Hiển thị số ghế

                        // Thêm ghế vào container
                        if(seat.seatStatus==1){
                            char.style.backgroundColor = "rgb(100, 150, 190)";
                        }
                        container.appendChild(char);
                    });
                })
                .catch(error => {
                    console.log('Error:', error);  // Xử lý lỗi nếu có
                });


                
                $(".buy-ticket-button").click(function() {
                    arr.sort((a, b) => {
                        return Number(a) - Number(b);
                    });              
                    console.log(arr);
                    if (arr.length == 0) {
                        alert("Vui lòng chọn ghế");
                    } else {
                        $(".modal").css("display", "none");
                        $(".KhachHang").css("display", "Block");
                        $(".close_Thongtin").click(function() {
                            $(".KhachHang").css("display", "none");
                        });
                    }
                    $(".xacnhan_thongtin").click(function(){
                        const fullName = document.getElementById("fullName").value.trim();
                        const ten = document.getElementById("ten").value.trim();
                        const sdt = document.getElementById("sdt").value.trim();
                        const gmail = document.getElementById("gmail").value.trim();
                    
                        // Kiểm tra các trường có được điền đầy đủ hay không
                        if (!fullName || !ten || !sdt || !gmail) {
                            alert("Vui lòng điền đầy đủ thông tin.");
                            return;
                        }
                        let result = arr.join(',');
                        sessionStorage.setItem('fullname', fullName);
                        sessionStorage.setItem('ten', ten);
                        sessionStorage.setItem('sdt', sdt);
                        sessionStorage.setItem('gmail', gmail);
                        sessionStorage.setItem('cac ghe', result);
                        sessionStorage.setItem('tong ghe', arr.length.toString());
                        sessionStorage.setItem('mang', JSON.stringify(arr));
                        // Lưu dữ liệu vào đối tượng
                    
                        const formData = {
                            fullName: fullName,
                            name: ten,
                            phone: sdt,
                            email: gmail
                        };
                        // Hiển thị dữ liệu trong console (hoặc lưu vào nơi cần thiết)
                        console.log("Dữ liệu người dùng:", formData);
                        window.location.href = `thong_tin.html`;
                        // Hiển thị dữ liệu trong div .results
                        const resultsDiv = document.querySelector(".results");
                        sessionStorage.setItem('coachID', ticketId);
                        sessionStorage.setItem('timeGo', ticketValue);
                    });
                });
                $(document).on("click", ".seat", function() {
                    let seatValue = this.value;  // Lấy giá trị của ghế đã chọn
                    console.log("Selected seat: " + seatValue);
                    
                    // Kiểm tra trạng thái màu nền hiện tại và thay đổi
                    if ($(this).css("background-color") === "rgb(0, 0, 244)") { // Màu xanh (blue)
                        let index = arr.indexOf(seatValue);
                        console.log("Array before removal: ", arr);
                        
                        // Kiểm tra xem phần tử có tồn tại trong mảng không
                        if (index !== -1) {
                            let index = arr.indexOf(seatValue);
                            console.log("Array before removal: ", arr);
                            // Kiểm tra xem phần tử có tồn tại trong mảng không
                            if (index !== -1) {
                                arr.splice(index, 1);  // Xóa phần tử tại vị trí index
                                
                                // Thay đổi màu nền
                                $(this).css("background-color", "rgb(160, 208, 240)"); 
                        
                                // Dùng setTimeout để kiểm tra màu sau khi đã thay đổi
                                setTimeout(() => {
                                    console.log("Current background-color: ", $(this).css("background-color"));
                                }, 0);  // Trì hoãn kiểm tra để đảm bảo DOM được cập nhật
                            }
                        }
                        
                        console.log("Array after removal: ", arr);
                    } else if($(this).css("background-color") === "rgb(160, 208, 240)") {
                        // Thêm phần tử vào mảng nếu chưa có
                        if (arr.indexOf(seatValue) === -1) {
                            arr.push(seatValue);
                            $(this).css("background-color", "rgb(0, 0, 244)"); // Thay đổi màu nền
                            console.log("Array after addition: ", arr);
                        }
                    } else {
                        alert("Ghế này đã được đặt vui lòng chọn ghế khác");
                        console.log($(this).css("background-color"))
                    }
                });
                // $(document).on('click', function(event) {
                //     if (!$(event.target).closest('.modal, .close-btn').length) {
                //         $(".modal").css("display", "none");
                //     }
                // });
                
                $(document).on('click', '.close-btn', function() {
                    $(".modal").css("display", "none");
                });
                
            });
            
        
    }
    catch (error) {
    console.log("khong tim thay")
    const ketqua=document.getElementById('ketqua');
    ketqua.textContent="Kết quả tìm được: "+"0";
    }
    ketqua.textContent="Kết quả "+cnt+" chuyến";
    if(cnt===0){
        $(".container").css("border","2px solid #f4f4f9 ")
    }
});

