// Mở modal đăng nhập

function openLoginModal() {
    document.getElementById('loginModal').style.display = 'flex';
    document.getElementById('registerModal').style.display = 'none'; // Đóng modal đăng ký nếu đang mở
}

// Đóng modal đăng nhập
function closeLoginModal() {
    document.getElementById('loginModal').style.display = 'none';
}

// Mở modal đăng ký
function openRegisterModal() {
    document.getElementById('registerModal').style.display = 'flex';
    document.getElementById('loginModal').style.display = 'none'; // Đóng modal đăng nhập nếu đang mở
}

// Đóng modal đăng ký
function closeRegisterModal() {
    document.getElementById('registerModal').style.display = 'none';
}

// Đóng modal khi nhấn ra ngoài
window.onclick = function (event) {
    const loginModal = document.getElementById('loginModal');
    const registerModal = document.getElementById('registerModal');
    if (event.target === loginModal) loginModal.style.display = 'none';
    if (event.target === registerModal) registerModal.style.display = 'none';
};

// Điều hướng đến trang giới thiệu
function navigateToIntro() {
    window.location.href = "gioithieu/index.html";
}

// Lắng nghe sự kiện submit form
//document.getElementById('bookingForm').addEventListener('submit', function(event) {
//    event.preventDefault(); // Ngăn chặn form reload

    // Lấy giá trị từ các trường input
    

    // Gửi yêu cầu GET đến API
//    fetchBookingData(departureLocation, arrivalLocation, departureDate);
//});

console.log("File JS đã được tải!");

async function fetchBookingData(event) {
    event.preventDefault(); // Chặn hành động mặc định của nút submit
    
    const From = document.getElementById("departureLocation");
    const To = document.getElementById("arrivalLocation");
    const departDate = document.getElementById("departureDate");
    // Lấy giá trị ngày và format lại
    var departDateValue = departDate.value;
    var from=From.value;
    var to=To.value;
    var fomatdate = formatDate(departDateValue);
    console.log(`${fomatdate}`);
    sessionStorage.setItem('departDate', fomatdate);
    sessionStorage.setItem('from', removeAccentsAndSpaces(from));
    sessionStorage.setItem('to',removeAccentsAndSpaces(to));
    sessionStorage.setItem('From',from);
    sessionStorage.setItem('To',to);
    try {
        // Sử dụng fetch với async/await
        const response = await fetch(`http://localhost:8081/v1/booking?from=${removeAccentsAndSpaces(from)}&to=${removeAccentsAndSpaces(to)}&departDate=${encodeURIComponent(fomatdate)}`);
        
        // Kiểm tra xem response có hợp lệ không
        const data = await response.json();
        sessionStorage.setItem('data', JSON.stringify(data));
        window.location.href = `chuyendi.html`;
        console.log(data);
    }
     catch (error) {
    }
}

function formatDate(dateString) {
    const [year, month, day] = dateString.split('-');
    return `${year}${month}${day}`; // Định dạng yyyymmdd
}




function formatDate(dateString) {
    const [year, month, day] = dateString.split('-');
    return `${year}${month}${day}`; // Định dạng yyyymmdd
}
function displayBookingData(bookings) {
    const resultsContainer = document.querySelector('.results');
    resultsContainer.innerHTML = ''; // Xóa kết quả cũ

    if (bookings.length === 0) {
        resultsContainer.innerHTML = '<p>Không tìm thấy chuyến xe phù hợp.</p>';
        return;
    }

    bookings.forEach(booking => {
        const bookingElement = document.createElement('div');
        bookingElement.classList.add('booking-item');
        bookingElement.innerHTML = `
            <p>Điểm đi: ${booking.departurePoint}</p>
            <p>Điểm đến: ${booking.destinationPoint}</p>
            <p>Ngày đi: ${booking.departDate}</p>
            <p>Giá: ${booking.price} VND</p>
        `;
        resultsContainer.appendChild(bookingElement);
    });
}
function removeVietnameseAccent(str) {
    const accents = {
        'à': 'a', 'á': 'a', 'ả': 'a', 'ã': 'a', 'ạ': 'a',
        'ă': 'a', 'ắ': 'a', 'ằ': 'a', 'ẳ': 'a', 'ẵ': 'a', 'ặ': 'a',
        'â': 'a', 'ấ': 'a', 'ầ': 'a', 'ẩ': 'a', 'ẫ': 'a', 'ậ': 'a',
        'è': 'e', 'é': 'e', 'ẻ': 'e', 'ẽ': 'e', 'ẹ': 'e',
        'ê': 'e', 'ế': 'e', 'ề': 'e', 'ể': 'e', 'ễ': 'e', 'ệ': 'e',
        'ì': 'i', 'í': 'i', 'ỉ': 'i', 'ĩ': 'i', 'ị': 'i',
        'ò': 'o', 'ó': 'o', 'ỏ': 'o', 'õ': 'o', 'ọ': 'o',
        'ô': 'o', 'ố': 'o', 'ồ': 'o', 'ổ': 'o', 'ỗ': 'o', 'ộ': 'o',
        'ơ': 'o', 'ớ': 'o', 'ờ': 'o', 'ở': 'o', 'ỡ': 'o', 'ợ': 'o',
        'ù': 'u', 'ú': 'u', 'ủ': 'u', 'ũ': 'u', 'ụ': 'u',
        'ư': 'u', 'ứ': 'u', 'ừ': 'u', 'ử': 'u', 'ữ': 'u', 'ự': 'u',
        'ỳ': 'y', 'ý': 'y', 'ỷ': 'y', 'ỹ': 'y', 'ỵ': 'y',
        'đ': 'd', 'Đ': 'D'
    };
    return str.split('').map(char => accents[char] || char).join('');
}
function removeSpaces(str) {
    return str.replace(/\s+/g, '');  // Loại bỏ tất cả khoảng trắng
}
function removeAccentsAndSpaces(str) {
    // Loại bỏ dấu
    const noAccent = removeVietnameseAccent(str);
    // Loại bỏ khoảng trắng
    return noAccent.replace(/\s+/g, '');
}
