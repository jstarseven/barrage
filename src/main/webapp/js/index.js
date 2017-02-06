var socket = new WebSocket("ws://localhost:8080/init");

$(function () {
    listen();
});

function emit() {
    var text = encodeScript($("#msg").val());
    if (isNullParam(text))return;
    var msg = {
        "message": text,
        "color": "#2E8B57",
        "bubbleColor": "#2E2E2E",
        "fontSize": "12",
        "fontType": "黑体"
    };
    msg = JSON.stringify(msg);
    socket.send(msg);
    $("#content").append("<span style='color: " + "#2E8B57" + ";float: right; font-size: " + 12 + ";'>" + text + "</span><br/>");
    $("#msg").val("");
}

function listen() {
    socket.onopen = function () {
        $("#content").append("<span>Welcome ChatRoom!</span></br>");
    };
    socket.onmessage = function (evt) {
        var data = JSON.parse(evt.data);
        $("#content").append("<span style='color: #" + data.color + ";font-size: " + data.fontSize + ";margin-top: 10px;'>" + data.userName + " Say: " + data.message + "</span></br>");
    };
    socket.onclose = function (evt) {
        $("#content").append("<span>" + "Close!" + "</span></br>");
    }
    socket.onerror = function (evt) {
        $("#content").append("<span>" + "ERROR!" + "</span></br>");
    }
}

document.onkeydown = function (event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    if (e && e.keyCode == 13) { // enter 键
        emit();
    }
};

//判断参数值是否是空值
function isNullParam(param) {
    return !!(param == "" || null == param || param == undefined);
}