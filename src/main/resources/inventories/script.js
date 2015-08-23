$(document).ready(function() {
    $.get("../../inventory/", function (response) {
        $("#loading").hide()
        if(response.length == 0) {
            $("#empty").show()
        }else {
            response.forEach(function (item) {
                $("#inventoryTable").show()
                console.log(item);
                $('#inventoryTable').find('tbody').append('<tr>' +
                    '<td>' + item.productName + '</td>' +
                    '<td>' + item.productId + '</td>' +
                    '<td>' + item.amount + '</td>' +
                    '<td>' + item.userName + '</td>' +
                    '<td>' + item.updated_at + '</td>' +
                    '</tr>');
            });
        }
    });
});

