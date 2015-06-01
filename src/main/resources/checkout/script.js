
// On load
$(document).ready(function() {
    var id = getUrlParameter('id');
    console.log("ID: " + id);
    $("#id").val(id);

    $.get("cart/checkout?id=" + id , function(cart){

        console.log(cart);
        console.log(cart.products);

        $("#hasCart").show();
        $("#info").hide();

        var table = "<table cellpadding='5px'>";
        table += "<tr><th>Name</th> <th>Price</th> <th>SUM</th><th>Unit</th></tr>";
        for(var i in cart.products){
            var product = cart.products[i];
            console.log(cart.products[i]);
            table += "<tr><td>" + product.product.name + "</td><td>" + product.product.price + "</td><td>" + product.amount + "</td><td>" + product.product.unit + "</td></tr>"
        }
        table += "</table>";
        $("#cart").html(table);
    });
});


//Button

function paid(){
    console.log("called paid!")

    $.post( "../cart/paid", { id: $("#id").val() } , function(data){
        console.log(data);
        $("#hasCart").hide();
        $("#info").show();
        $("#info").html("Warenkorb wurde bezahlt")
    }).fail(function() {
        alert("WARENKORB NICHT BEKANNT!");
    });
}

function cancelled(){
    console.log("cancelled!")
    $.post( "../cart/cancelled", { id: $("#id").val() } , function(data){
        console.log(data);
        $("#hasCart").hide();
        $("#info").show();
        $("#info").html("Warenkorb wurde verworfen")
    }).fail(function() {
        alert("WARENKORB NICHT BEKANNT!");
    });
}

//HELPER

function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam)
        {
            return sParameterName[1];
        }
    }
}