
$(function () {
    $.getJSON("../data/list-data.json", function (data) {
        $.each(data, function (index, list) {
            $("#goods-list").append(
                "<li class='yui3-u-1-4'><div class='list-wrap' ><div class='p-img'><img src='" + list["img"] + "' alt=''></div><div class='price'><strong><em>$</em> <i>" + list["n-price"] + "</i></strong></div>"
                + "<div class='attr'><em>" + list["desc"] + "</em></div><div class='cu'><em><span></span>"+ list["cu"] +"</em></div>"
                + "<div class='operate'><a href='success-cart.html' target='blank' class='sui-btn btn-bordered btn-danger'>Buy Now</a>"
                + "<a href='javascript:void(0);' class='sui-btn btn-bordered'>Comparison</a>"
                + "<a href='javascript:void(0);' class='sui-btn btn-bordered'>Price reduction notification</a>"
                + "</div></div></li >"
            );
           
        })
    })
})

