(function ($) {

    "use strict";

// Lazy load
    var lazyLoadInstance = new LazyLoad({
        elements_selector: ".lazy"
    });

// Jquery select
    $('.custom_select select').niceSelect();

// Carousel categories home page
    $('.categories_carousel').owlCarousel({
        center: false,
        items: 2,
        loop: false,
        margin: 20,
        dots: false,
        nav: true,
        lazyLoad: true,
        navText: ["<i class='arrow_carrot-left'></i>", "<i class='arrow_carrot-right'></i>"],
        responsive: {
            0: {
                nav: false,
                dots: true,
                items: 1
            },
            480: {
                nav: false,
                dots: true,
                items: 2
            },
            768: {
                nav: false,
                dots: true,
                items: 3
            },
            1025: {
                nav: false,
                dots: true,
                items: 4
            },
            1340: {
                nav: true,
                dots: false,
                items: 5
            }
        }
    });

    // Carousel single slide
    $('.carousel_1').owlCarousel({
        items: 1,
        loop: false,
        lazyLoad: true,
        margin: 0,
        dots: true,
        nav: false
    });

    // Carousel home page
    $('.carousel_4').owlCarousel({
        items: 4,
        loop: false,
        margin: 20,
        dots: false,
        lazyLoad: true,
        navText: ["<i class='arrow_carrot-left'></i>", "<i class='arrow_carrot-right'></i>"],
        nav: true,
        responsive: {
            0: {
                items: 1,
                nav: false,
                dots: true
            },
            560: {
                items: 2,
                nav: false,
                dots: true
            },
            768: {
                items: 2,
                nav: false,
                dots: true
            },
            991: {
                items: 3,
                nav: true,
                dots: false
            },
            1230: {
                items: 4,
                nav: true,
                dots: false
            }
        }
    });

    // Sticky nav
    $(window).on('scroll', function () {
        if ($(this).scrollTop() > 1) {
            $('.element_to_stick').addClass("sticky");
        } else {
            $('.element_to_stick').removeClass("sticky");
        }
        
        // 모바일에서 스크롤 시 검색창 표시/숨김
        if ($(window).width() <= 991) {
            if ($(this).scrollTop() > 100) {
                $('.mobile-search-header').addClass("sticky");
            } else {
                $('.mobile-search-header').removeClass("sticky");
            }
        }
    });
    $(window).scroll();

    // 모바일 검색창 이벤트 동기화
    $(document).ready(function() {
        // 모바일 검색창 입력값을 기존 검색창과 동기화
        $('#mobileKeywordSearch').on('input', function() {
            $('#keywordSearch').val($(this).val());
        });
        
        $('#keywordSearch').on('input', function() {
            $('#mobileKeywordSearch').val($(this).val());
        });
        
        // 모바일 검색 카테고리 동기화
        $('#mobileSearchCategory').on('change', function() {
            $('#searchCategory').val($(this).val());
        });
        
        $('#searchCategory').on('change', function() {
            $('#mobileSearchCategory').val($(this).val());
        });
        
        // 모바일 검색 버튼 클릭 시 기존 검색 버튼과 동일한 동작
        $('#mobileSearchButton').on('click', function() {
            $('#searchButton').click();
        });
        
        // 모바일 필터 표시 동기화
        $('#mobileFilterDisplay').on('click', function() {
            $('#filterDisplay').click();
        });
    });

    // Header background
    $('.background-image').each(function () {
        $(this).css('background-image', $(this).attr('data-background'));
    });

    // Rotate icons
    $(".categories_carousel .item a").hover(
        function () {
            $(this).find("i").toggleClass("rotate-x");
        }
    );

    // Menu
    $('a.open_close').on("click", function () {
        $('.main-menu').toggleClass('show');
        $('.layer').toggleClass('layer-is-visible');
    });
    $('a.show-submenu').on("click", function () {
        $(this).next().toggleClass("show_normal");
    });

    // Opacity mask
    $('.opacity-mask').each(function () {
        $(this).css('background-color', $(this).attr('data-opacity-mask'));
    });

    // Scroll to top
    var pxShow = 800; // height on which the button will show
    var scrollSpeed = 500; // how slow / fast you want the button to scroll to top.
    $(window).scroll(function () {
        if ($(window).scrollTop() >= pxShow) {
            $("#toTop").addClass('visible');
        } else {
            $("#toTop").removeClass('visible');
        }
    });
    $('#toTop').on('click', function () {
        $('html, body').animate({scrollTop: 0}, scrollSpeed);
        return false;
    });

    //Footer collapse
    var $headingFooter = $('footer h3');
    $(window).resize(function () {
        if ($(window).width() <= 768) {
            $headingFooter.attr("data-bs-toggle", "collapse");
        } else {
            $headingFooter.removeAttr("data-bs-toggle", "collapse");
        }
    }).resize();
    $headingFooter.on("click", function () {
        $(this).toggleClass('opened');
    });

    // Scroll to position
    $('a[href^="#"].btn_scroll').on('click', function (e) {
        e.preventDefault();
        var target = this.hash;
        var $target = $(target);
        $('html, body').stop().animate({
            'scrollTop': $target.offset().top
        }, 800, 'swing', function () {
            window.location.hash = target;
        });
    });

    // 공통 로그인 모달 호출 함수
    function openLoginModal() {
        $.magnificPopup.open({
            items: {
                src: '#sign-in-dialog'
            },
            type: 'inline',
            fixedContentPos: true,
            fixedBgPos: true,
            overflowY: 'auto',
            closeBtnInside: true,
            preloader: false,
            midClick: true,
            removalDelay: 300,
            closeMarkup: '<button title="%title%" type="button" class="mfp-close"></button>',
            mainClass: 'my-mfp-zoom-in',
            callbacks: {
                open: function() {
                    // 로그인 시 마지막 접속 url 저장
                    var url = window.location.pathname + window.location.search + window.location.hash;
                    $('#redirectUrlInput').val(url);
                }
            }
        });
    }

    // 로그인이 필요한 모든 버튼에 공통 적용
    $('.login-required').on('click', function(e) {
        e.preventDefault();
        
        // 로그인 상태 체크 - hidden input 값 사용
        var isLoggedIn = $('#is-logged-in').val() === 'true';
        
        if (!isLoggedIn) {
            openLoginModal();
            return false; // 이벤트 중단
        }
        // 로그인된 경우는 이벤트를 계속 진행
    });

    // Modal Sign In
    $('.sign-in').magnificPopup({
        type: 'inline',
        fixedContentPos: true,
        fixedBgPos: true,
        overflowY: 'auto',
        closeBtnInside: true,
        preloader: false,
        midClick: true,
        removalDelay: 300,
        closeMarkup: '<button title="%title%" type="button" class="mfp-close"></button>',
        mainClass: 'my-mfp-zoom-in',
        callbacks: {
            open: function() { // 로그인 시 마지막 접속 url 저장
                var url = window.location.pathname + window.location.search + window.location.hash;
                $('#redirectUrlInput').val(url);
            }
        }
    });

    // Show hide password
    $('#password, #password_sign').hidePassword('focus', {
        toggle: {
            className: 'my-toggle'
        }
    });

    // Accordion
    function toggleChevron(e) {
        $(e.target)
            .prev('.card-header')
            .find("i.indicator")
            .toggleClass('icon_minus-06 icon_plus');
    }

    $('.accordion_2').on('hidden.bs.collapse shown.bs.collapse', toggleChevron);

    function toggleIcon(e) {
        $(e.target)
            .prev('.panel-heading')
            .find(".indicator")
            .toggleClass('icon_minus-06 icon_plus');
    }

    // 로그아웃 링크에 현재 URL을 redirectUrl로 동적으로 세팅
    document.addEventListener("DOMContentLoaded", function() {
      var logoutLinks = document.querySelectorAll('.logout-link');
      var currentUrl = window.location.pathname + window.location.search + window.location.hash;
      logoutLinks.forEach(function(link) {
        link.setAttribute('href', '/logout?redirectUrl=' + encodeURIComponent(currentUrl));
      });
    });

})(window.jQuery);