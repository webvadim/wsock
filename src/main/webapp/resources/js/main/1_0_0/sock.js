/* 
 *  Copyright 2015 Vadims Zemlanojs <vadim@tenplanets.net>. All Rights Reserved.  
 */



if (typeof (tenplanets) === "undefined") {
    tenplanets = {};
}



tenplanets.months = new Array("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
tenplanets.webSocket = null;
tenplanets.webSocketUrl = null;


tenplanets.isOpen = function () {
    return  (tenplanets.webSocket && (tenplanets.webSocket.readyState !== WebSocket.CLOSED));
}

tenplanets.openSocket = function () {

    if (tenplanets.isOpen()) {
        return;
    }
    tenplanets.message("Connection is openning...");



    tenplanets.webSocket = new WebSocket(tenplanets.webSocketUrl);
    tenplanets.webSocket.onopen = function (event) {

        tenplanets.message("Connection is opened.");
        if (event.data === undefined) {
            return;
        }
        tenplanets.message(event.data);
    };
    tenplanets.webSocket.onmessage = function (event) {
        tenplanets.message(event.data);
    };
    tenplanets.webSocket.onclose = function (event) {
        tenplanets.message("Connection is closed.");
    };
    tenplanets.webSocket.onerror = function (event) {
        tenplanets.message("Error.");
    };
}

tenplanets.closeSocket = function () {

    if (tenplanets.isOpen()) {
        tenplanets.webSocket.close();
    }
}
tenplanets.reopenSocket = function () {


    if (tenplanets.isOpen()) {
        tenplanets.webSocket.onclose = function (event) {
            tenplanets.openSocket();
        };
        tenplanets.closeSocket();
    } else {
        tenplanets.openSocket();
    }

}



tenplanets.message = function (text, clazz) {

    if (clazz) {
        document.getElementById("messages").innerHTML += ('<br/>' + '<span class="' + clazz + '">' + text + '</span>');
    } else {
        document.getElementById("messages").innerHTML += "<br/>" + text;
    }

}

tenplanets.clearMessages = function () {
    document.getElementById("messages").innerHTML = "";

}

