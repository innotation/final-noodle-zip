(function ($) {

	"use strict";

// Sticky sidebar
$('#sidebar_fixed').theiaStickySidebar({
    minWidth: 991,
    updateSidebarHeight: true,
    additionalMarginTop: 25
});

// Read more 
$(".content_more").hide();
    $(".show_hide").on("click", function () {
        var txt = $(".content_more").is(':visible') ? 'Read More' : 'Read Less';
        $(this).text(txt);
        $(this).prev('.content_more').slideToggle(200);
});

// Time and people select
$('.radio_select input[type="radio"]').on("click", function () {
    var value = $("input[name='time']:checked").val();
    $('#selected_time').text(value);
});

$('.radio_select input[type="radio"]').on("click", function (){
    var value = $("input[name='people']:checked").val();
    $('#selected_people').text(value);
});

$('.radio_select input[type="radio"]').on("click", function (){
    var value = $("input[name='day']:checked").val();
    $('#selected_day').text(value);
});

// Image popups
	$('.magnific-gallery').each(function () {
		$(this).magnificPopup({
			delegate: 'a',
			type: 'image',
            preloader: true,
			gallery: {
				enabled: true
			},
			removalDelay: 500, //delay removal by X to allow out-animation
			callbacks: {
				beforeOpen: function () {
					// just a hack that adds mfp-anim class to markup 
					this.st.image.markup = this.st.image.markup.replace('mfp-figure', 'mfp-figure mfp-with-anim');
					this.st.mainClass = this.st.el.attr('data-effect');
				}
			},
			closeOnContentClick: true,
			midClick: true // allow opening popup on middle mouse click. Always set it to true if you don't provide alternative source.
		});
	});

// Image popups menu
	$('.menu-gallery').each(function () {
		$(this).magnificPopup({
			delegate: 'figure a',
			type: 'image',
            preloader: true,
			gallery: {
				enabled: true
			},
			removalDelay: 500, //delay removal by X to allow out-animation
			callbacks: {
				beforeOpen: function () {
					// just a hack that adds mfp-anim class to markup 
					this.st.image.markup = this.st.image.markup.replace('mfp-figure', 'mfp-figure mfp-with-anim');
					this.st.mainClass = this.st.el.attr('data-effect');
				}
			},
			closeOnContentClick: true,
			midClick: true // allow opening popup on middle mouse click. Always set it to true if you don't provide alternative source.
		});
	});

// Reserve Fixed on mobile
$('.btn_reserve_fixed a').on('click', function() {
     $(".box_booking").show();
});
$(".close_panel_mobile").on('click', function (event){
    event.stopPropagation();
    $(".box_booking").hide();
  });

// document.addEventListener('DOMContentLoaded', function() {
//   // Follow 버튼 토글 기능
//   const followBtn = document.getElementById('follow-btn');
//   if (followBtn) {
//     console.log("로그인이 안되어있음")
//     followBtn.addEventListener('click', function(e) {
//       // 로그인 상태 체크 - 로그인이 안 되어 있으면 이벤트 중단 (login-required가 처리)
//       window.location.reload();
//       var isLoggedInInput = document.getElementById('is-logged-in');
//       var isLoggedIn = isLoggedInInput && isLoggedInInput.value === 'true';
//       if (!isLoggedIn) {
//         return; // common_func.js의 login-required 로직이 처리하도록 함
//       }
//       e.preventDefault();
//
//       const userId = followBtn.getAttribute('data-user-id');
//       fetch(`/users/${userId}/subscribe`, { method: 'GET', credentials: 'same-origin' })
//         .then(response => {
//           console.log("구독 처리중")
//           if (!response.ok) throw new Error("Network response was not ok");
//           const isSubscribed = followBtn.getAttribute("data-subscribed") === "true";
//           followBtn.setAttribute("data-subscribed", (!isSubscribed).toString());
//           followBtn.textContent = !isSubscribed ? "구독 취소" : "구독";
//         })
//         .catch(() => alert('네트워크 오류로 팔로우 처리에 실패했습니다.'));
//     });
//   }
// });



  /// 이 부분 mypage/main.html 아래 동작되는 코드 작성해 두었습니다.

})(window.jQuery); 