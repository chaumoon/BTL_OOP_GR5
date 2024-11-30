var btnOpen = document.querySelector('.open_modal_btn');
var modal = document.querySelector('.modal'); 
var iconClose = document.querySelector('.modal_header i');
var btnClose = document.querySelector('.modal_footer button');
var modalInner = document.querySelector('.modal_inner');

function toggleModal() {
    modal.classList.toggle('hide');
}

// Gán sự kiện click cho nút mở modal
btnOpen.addEventListener('click', function (e) {
    e.preventDefault(); // Ngăn hành động mặc định của form
    toggleModal();
}); 

// Gán sự kiện click cho nút đóng modal
btnClose.addEventListener('click', toggleModal);
iconClose.addEventListener('click', toggleModal);

// Đóng modal khi click ra ngoài
modal.addEventListener('click', function (e) {
    if (!modalInner.contains(e.target)) {
        toggleModal();
    }
});

// Ngăn chặn sự kiện click từ nội dung modal lan ra ngoài
modalInner.addEventListener('click', function (e) {
    e.stopPropagation();
});
