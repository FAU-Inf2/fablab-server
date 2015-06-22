
/*
  On load / init simulator
 */

var url = "../../checkout/", code, cart, requestRepeater;
$(document).ready(function() {
    //Dummy can be used to display Carts, just use the url ?code=XXX for displaying
    code = getUrlParameter('code');
    if(code == undefined){
        code = Math.floor((Math.random() * 1000000) + 1);
        generateQRCode();
    }else{
        $("#hasCart").hide();
        $("#qrcode").hide();
        $("#waiting").show();
        $("#waitingCode").val("Warenkorb [mit Code:"+ code +"]  nicht vorhanden")
    }
    getCart();
    console.log("code: " + code);
});


/*
     Request cart from server
 */
function getCart(){
    clearTimeout(requestRepeater);
    $.get(url + code , function(obj) {
        if (obj == undefined) {
            requestRepeater = setTimeout(getCart, 500)
        }else {
            cart = obj;
            //console.log(cart);

            $("#hasCart").show();
            $("#qrcode").hide();
            $("#waiting").hide();

            $("#code").val(code);
            $("#status").val(cart.status);
            switch (cart.status){
                case "PENDING": $("#status").css("background-color", "orange");  break;
                case "PAID": $("#status").css("background-color", "green"); break;
                case "CANCELLED": $("#status").css("background-color", "red"); break;
                default: $("#status").css("background-color", "gray"); break;
            }


            var table = "<table border='0' cellpadding='5' cellspacing='0' id='productTable'>";
            table += "<tr><th></th> <th>OpenERP</th> <th>Amount</th></tr>";
            for (var i in cart.items) {
                var item = cart.items[i];
                table += "<tr><td>" + item.id + "</td><td>" + item.productId + "</td><td>" + item.amount + "</td></tr>"
            }
            table += "</table>";
            $("#cart").html(table);
        }
    });
}

function updateCartInformation() {

    console.log("updateCartInformation")
    $.get(url + code, function (obj) {
        console.log("OBJ:")
        console.log(obj)
        if (obj == undefined) {
            $("#hasCart").hide();
            $("#qrcode").hide();
            $("#waiting").show();
            $("#waitingCode").val("Warenkorb [mit Code:"+ code +"]  nicht vorhanden")
        } else {
            $("#status").val(cart.status);
            switch (cart.status) {
                case "PENDING":
                    $("#status").css("background-color", "orange");
                    break;
                case "PAID":
                    $("#status").css("background-color", "green");
                    break;
                case "CANCELLED":
                    $("#status").css("background-color", "red");
                    break;
                default:
                    $("#status").css("background-color", "gray");
                    break;
            }
        }
    });
}


/*
        if no ID is set -> generate QR
 */

function generateQRCode(){
    $("#qrcode").empty();
    var qrcode = new QRCode(document.getElementById("qrcode"), {
        width : 300,
        height : 300,
        text: "" + code, //QRCode generator needs a string, so give him one
    });
}


/*
       functions of the buttons
 */

function paidButtonTouched(){
    $.post( url + "paid/" + code, function(statusChanged) {
        if(statusChanged == "true")
            alert("Warenkorb wurde bezahlt");
        else
            alert("Kann nicht mehr durchgeführt werden")
        updateCartInformation();
    }).fail(function() {
        alert("WARENKORB NICHT BEKANNT!");
        updateCartInformation();
    });
}

function cancelledButtonTouched(){
    $.post( url + "cancelled/" + code, function(statusChanged){
        if(statusChanged == "true")
            alert("Warenkorb wurde verworfen");
        else
            alert("Kann nicht mehr durchgeführt werden")
        updateCartInformation();
    }).fail(function() {
        alert("WARENKORB NICHT BEKANNT!");
        updateCartInformation();
    });
}

function showCartForCodeButtonTouched(){
    code = $("#setCode").val();
    $("#hasCart").hide();
    $("#qrcode").hide();
    $("#waiting").show();
    $("#waitingCode").val("Warenkorb [mit Code:"+ code +"]  nicht vorhanden")
    getCart();
}



function newQRCode(){
    code = Math.floor((Math.random() * 1000000) + 1);
    $("#hasCart").hide();
    $("#qrcode").show();
    $("#waiting").hide();
    getCart();
    generateQRCode();
    console.log("code: " + code);
}

/*
        Helper functions
 */

function getUrlParameter(sParam){
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++){
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam)
            return sParameterName[1];
    }
}

