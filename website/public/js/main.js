(function($) {

	"use strict";


	$(window).stellar({
    responsive: true,
    parallaxBackgrounds: true,
    parallaxElements: true,
    horizontalScrolling: false,
    hideDistantElements: false,
    scrollProperty: 'scroll'
  });


	// loader
	var loader = function() {
		setTimeout(function() { 
			if($('#xbribe-loader').length > 0) {
				$('#xbribe-loader').removeClass('show');
			}
		}, 1);
	};
	loader();

	// magnific popup
	$('.image-popup').magnificPopup({
    type: 'image',
    closeOnContentClick: true,
    closeBtnInside: false,
    fixedContentPos: true,
    mainClass: 'mfp-no-margins mfp-with-zoom', // class to remove default margin from left and right side
     gallery: {
      enabled: true,
      navigateByImgClick: true,
      preload: [0,1] // Will preload 0 - before current, and 1 after the current image
    },
    image: {
      verticalFit: true
    },
    zoom: {
      enabled: true,
      duration: 300 // don't foget to change the duration also in CSS
    }
  });


  var counter = function() {
		
		$('#section-counter').waypoint( function( direction ) {

			if( direction === 'down' && !$(this.element).hasClass('xbribe-animated') ) {

				var comma_separator_number_step = $.animateNumber.numberStepFactories.separator(',')
				$('.number').each(function(){
					var $this = $(this),
						num = $this.data('number');
						console.log(num);
					$this.animateNumber(
					  {
					    number: num,
					    numberStep: comma_separator_number_step
					  }, 7000
					);
				});
				
			}

		} , { offset: '95%' } );

	}
	counter();

	var contentWayPoint = function() {
		var i = 0;
		$('.xbribe-animate').waypoint( function( direction ) {

			if( direction === 'down' && !$(this.element).hasClass('xbribe-animated') ) {
				
				i++;

				$(this.element).addClass('item-animate');
				setTimeout(function(){

					$('body .xbribe-animate.item-animate').each(function(k){
						var el = $(this);
						setTimeout( function () {
							var effect = el.data('animate-effect');
							if ( effect === 'fadeIn') {
								el.addClass('fadeIn xbribe-animated');
							} else if ( effect === 'fadeInLeft') {
								el.addClass('fadeInLeft xbribe-animated');
							} else if ( effect === 'fadeInRight') {
								el.addClass('fadeInRight xbribe-animated');
							} else {
								el.addClass('fadeInUp xbribe-animated');
							}
							el.removeClass('item-animate');
						},  k * 50, 'easeInOutExpo' );
					});
					
				}, 100);
				
			}

		} , { offset: '95%' } );
	};
	contentWayPoint();


	$('#notification-fab').click(()=> {
		$('#notif-container').toggle().scrollTop(0)
	})
	

	// var fullHeight = function() {

	// 	$('.js-fullheight').css('height', $(window).height());
	// 	$(window).resize(function(){
	// 		$('.js-fullheight').css('height', $(window).height());
	// 	});

	// };
	// fullHeight();


	 //  var carousel = function() {
	// 	$('.carousel-testimony').owlCarousel({
	// 		center: true,
	// 		loop: true,
	// 		items:1,
	// 		margin: 30,
	// 		stagePadding: 0,
	// 		nav: false,
	// 		navText: ['<span class="ion-ios-arrow-back">', '<span class="ion-ios-arrow-forward">'],
	// 		responsive:{
	// 			0:{
	// 				items: 1
	// 			},
	// 			600:{
	// 				items: 2
	// 			},
	// 			1000:{
	// 				items: 3
	// 			}
	// 		}
	// 	});

	// };
	// carousel();

	// $('nav .dropdown').hover(function(){
	// 	var $this = $(this);
	// 	// 	 timer;
	// 	// clearTimeout(timer);
	// 	$this.addClass('show');
	// 	$this.find('> a').attr('aria-expanded', true);
	// 	// $this.find('.dropdown-menu').addClass('animated-fast fadeInUp show');
	// 	$this.find('.dropdown-menu').addClass('show');
	// }, function(){
	// 	var $this = $(this);
	// 		// timer;
	// 	// timer = setTimeout(function(){
	// 		$this.removeClass('show');
	// 		$this.find('> a').attr('aria-expanded', false);
	// 		// $this.find('.dropdown-menu').removeClass('animated-fast fadeInUp show');
	// 		$this.find('.dropdown-menu').removeClass('show');
	// 	// }, 100);
	// });


	// $('#dropdown04').on('show.bs.dropdown', function () {
	//   console.log('show');
	// });

})(jQuery);

