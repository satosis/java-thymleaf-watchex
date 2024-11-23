$(document).on('click', '.remove-image-function', function () {
  $(this).parents('div').find("input[type='file']").val('');
  $(this).parent('.image-area').empty();
});
$(document).on('change', 'input.imageID', function (event) {
  $(this).parent().find('.image-area').empty();
  var image_area = $(this).parent().find('.image-area');
  var file = event.target.files[0];
  if (file) {
    var reader = new FileReader();
    reader.onload = function (e) {
      image_area.append(`
            <a class="remove-image d-block remove-image-function">&#215;</a>
            <img src="${e.target.result}" class="img-thumbnail"  alt="Image preview...">
        `);
    };
    reader.readAsDataURL(this.files[0]);
  }
});


$(".js-login").on('click', function (event) {
  event.preventDefault();
  toastr.warning('Bạn cần đăng nhập');
})
$(window).bind("load", function () {
  jQuery("#status").fadeOut();
  jQuery("#loader").fadeOut();
});

$(".sku-variable-name").on("click", function () {
  $(".sku-variable-name").removeClass("active")
  var kichco = $(this).attr('title')
  $(this).addClass("active")
  var id = $('.muangay').data("id");
  $('.muangay').attr("href", "/add/" + id + "?type=1&kichco=" + kichco)
  $('.muatragop').attr("href", "/add/" + id + "?type=2&kichco=" + kichco)
})

$("ul.tab li").on('click', function (event) {
  $("li.active").removeClass("active");
  $(this).addClass("active");
  $(".type").val($(this).data("value"))
})

$('.qty').click(function () {
  let $this = $(this);
  let $input = $this.parent().prev();
  let number = parseInt($this.val());

  let price = $this.attr('data-price');
  let URL = $this.attr('data-url');
  let IdProduct = $this.attr('data-id-product');
  var total = $('#amount').val();
  $('#amount').val(parseInt(total) - parseInt(price));
  if (number <= 1) {
      toastr.warning("Số sản phẩm không được bằng 0");
      return false;
  }
  number = number - 1;

  $.ajax({
      url: URL,
      data: {
          qty: number,
          idProduct: IdProduct
      }
  }).done(function (results) {
      if (typeof results.totalMoney !== "underfined") {
          $input.val(number);
          $('.total').html(results.totalItem + "<span>VNĐ</span>");
          $(".number").text(results.number);
          $('#totalPrice').text(results.totalMoney);
      } else {
          $input.val(number + 1);
      }
  })
})
