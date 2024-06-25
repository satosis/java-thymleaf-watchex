$(document).on('click', '.remove-image-function', function () {
    $(this).parents('div').find("input[type='file']").val('');
    $(this).parent('.image-area').empty();
  });
$(document).on('change', 'input.imageID', function (event) {
  $(this).parent().find('.image-area').empty();
  var image_area = $(this).parent().find('.image-area');
  var file = event.target.files[0];
  if (file){
    var reader = new FileReader();
    reader.onload = function (e) {
        image_area.append(`
            <a class="remove-image d-block remove-image-function">&#215;</a>
            <img src="${e.target.result}" class="img-circle" alt="Image preview...">
        `);
    };
    reader.readAsDataURL(this.files[0]);
    }
});
