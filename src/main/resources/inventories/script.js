


$(document).ready(function() {

    $.get("../../inventory/", function (response) {
        response.forEach(function(item) {
            console.log(item);
            $('#inventoryTable').find('tbody').append('<tr>' +
                    '<td>' + item.productName +'</td>' +
                    '<td>' + item.productId +'</td>' +
                    '<td>' + item.amount +'</td>' +
                    '<td>' + item.userName +'</td>' +
                    '<td>' + item.updated_at +'</td>' +
                '</tr>');
        });
    });
});




function deleteAllTouched(){
    if (confirm("Sicher das die gesamte Liste gelöscht werden soll?")) {
        console.log("del all")
    } else {
        console.log("cancelled")
    }
}