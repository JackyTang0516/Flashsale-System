// window.onload = function() {
// 	cartListController.setup();
// }

$(function() {
	$('.all-sort-list2 > .item').hover(function() {

		$(this).addClass('hover');
		$(this).children('.item-list').css('display', 'block');
	}, function() {
		$(this).removeClass('hover');
		$(this).children('.item-list').css('display', 'none');
	});

	$('.item > .item-list > .close').click(function() {
		$(this).parent().parent().removeClass('hover');
		$(this).parent().hide();
	});
});