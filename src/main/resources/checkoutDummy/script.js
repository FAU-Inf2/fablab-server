//TODO ZYKLISCHES ABFRAGEN VON CART --> Problem Async muss aufrufen wenn ergebniss da ist --> ergeniss nicht getestet...


// On load
var id;
var cart;
$(document).ready(function() {
    id = getUrlParameter('id');
    if(id == undefined){
        id = Math.floor((Math.random() * 1000000) + 1);
        generateQRCode();
    }
    getCart();
    console.log("ID: " + id);
});



function getCart(){
    console.log("Trying to get Cart for id")
    $.get("cart/checkout?id=" + id , function(obj){
        cart = obj;
        console.log(cart);
        console.log(cart.products);

        $("#hasCart").show();
        $("#qrcode").hide();

        var table = "<table cellpadding='5px'>";
        table += "<tr><th>Name</th> <th>Price</th> <th>SUM</th><th>Unit</th></tr>";
        for(var i in cart.products){
            var product = cart.products[i];
            console.log(cart.products[i]);
            table += "<tr><td>" + product.product.name + "</td><td>" + product.product.price + "</td><td>" + product.amount + "</td><td>" + product.product.unit + "</td></tr>"
        }
        table += "</table>";
        $("#cart").html(table);
    }).done(function() {
        alert("DONE");
    });
}

function generateQRCode(){
    console.log("Generating QR")
    var qrcode = new QRCode(document.getElementById("qrcode"), {
        width : 300,
        height : 300
    });    

qrcode.makeCode(3453425);
    //qrcode.makeCode(id);
}

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