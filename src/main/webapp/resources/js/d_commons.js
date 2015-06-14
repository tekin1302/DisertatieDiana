/*
$(document).ready(function() {
    $( window ).resize(function() {
        resize();
    });
resize();
});
function resize() {
    var paddingTop = $("#header").height();
    var paddingBotton = $("#footer").height();

    $("#mainIn").css("padding-top", paddingTop);
    $("#mainIn").css("padding-bottom", paddingBotton);
    }
*/
var tCacheMap = new Object();


function getScreenshot(exceptionId) {
    html2canvas($('body'), {
        onrendered: function (canvas) {
            var imgString = canvas.toDataURL();
            $.ajax({
                type: "POST",
                url: ctx + "/exception",
                data: {
                    id: exceptionId,
                    img: imgString
                },
                success: function() {
                    alert("A fost inregistrata eroarea cu numarul " + exceptionId + "!");
                }
            });
        }
    });
}

$(document).ajaxError(function myErrorHandler(event, xhr, ajaxOptions, thrownError) {
    handleException(xhr.responseText);
});

function handleException(id) {
    if (!isNaN(id)) {
        getScreenshot(id);
    }
}
var tDateFormatter = function (cellval, opts) {
    var date = new Date(cellval);
    var d = date.getDate();
    if (d < 10) d = "0" + d;
    var m =  date.getMonth();
    m += 1;  // JavaScript months are 0-11
    if (m < 10) m = "0" + m;
    var y = date.getFullYear();

    return d + "-" + m + "-" + y;
};
function tDateFormatter2(cellvalue, options, rowObject) {

    // parseExact just returns 'null' if the date you are trying to
    // format is not in the exact format specified
    var parsedDate = Date.parseExact(cellvalue, "yyyy-MM-ddTHH:mm:ss");
    if(parsedDate == null )
        parsedDate = new Date(cellvalue);

    // if parsed date is null, just used the passed cell value; otherwise,
    // transform the date to desired format
    var formattedDate = parsedDate ? parsedDate.toString("yyyy-MM-dd HH:mm:ss") : cellvalue;

    return formattedDate;
}

function tlog(s) {
    console.log(s)
}

errs = {required: 'Camp obligatoriu!',
    invalidEmail: 'Adresa de e-mail invalida!',
    passwordsDontMatch: 'Parolele nu sunt identice!',
    emailExists: 'E-mail existent!',
    maxLength100: "Maxim 100 de caractere!",
    maxLength2000: "Maxim 2000 de caractere!",
    floatPattern: "Numar cu maxim 10 cifre si 2 zecimale!",
    floatInvalid: "Numar invalid"};

floatRegex = '^\d{0,10}(\.\d{0,2}){0,1}$';
var WARNING = 0;
var ERROR = 1;

var alertDiv = $('#alert');
function tAlert(message, type) {
    $('#alert #msg').html(message);
    alertDiv.removeClass();
    if (type != undefined && type == WARNING) {
        alertDiv.addClass("alert-warning");
    } else if (type == ERROR) {
        alertDiv.addClass("alert-danger");
    }else {
        alertDiv.addClass("alert-success");
    }
    alertDiv.dialog({
        modal: true,
        buttons: {
            Ok: function() {
                $( this ).dialog( "close" );
            }
        }
    });
}

function tConfirmation(message, func) {
    $( "#confirm #msgConfirm").html(message);
    $("#confirm").dialog({
            modal: true,
            buttons: {
                "Da": function() {
                    func();
                    $( this ).dialog( "close" );
                },
                "Nu": function() {
                    $( this ).dialog( "close" );
                }
            }
        });
}
var imgExt = ['gif','png','jpg','jpeg', 'bmp'];
function fileIsNotImage (img) {
    if (img == null || img == undefined) {
        return false;
    }
    if(img != null && img.name != null) {
        var ext = img.name.split('.').pop().toLowerCase();
        if($.inArray(ext, imgExt) != -1) {
            return false;
        }
    }
    return true;
}

function formContainsErrors(formId) {
    var hasErrors = false;
    $("#" + formId + " .error").each(function(index, elem) {
        if($(elem).css("display") != "none") {
            hasErrors = true;
        }
    });
    return hasErrors;
}

function tProgressBar(options) {
    if (options.elemId) {
        var elem = $("#" + options.elemId);
        elem.addClass("progressBar");
//        elem.append('<div class="innerProgressBar"></div>')

        var innerPB = $("#" + options.elemId + " .innerProgressBar");
        var tfontSize = 10;
        var theight = 13;
        var twidth = 180;
        if (options.fontSize) {
            tfontSize = options.fontSize;
        }
        if (options.height) {
            theight = options.height;
        }
        if (options.width) {
            twidth = options.width;
        }

        elem.css("fontSize", tfontSize);
        elem.css("height", theight);
        elem.css("width", twidth);
        innerPB.css("height", theight);
//        innerPB.css("width", options.proc + "%");

        var resultObject = {
            innerProgressBar: innerPB,
            val: function(tval) {
                innerPB.css("width", options.proc + "%");
            }
        };
        return resultObject;
    } else {
        alert("Please provide the element id for the progressbar");
    }
}

function exists(variable) {
    if (variable != undefined & variable != null) {
        return true;
    }
    return false;
}
function getDialogMaxHeight() {
    var winHeight = $(window.top).height() - 100;
    tlog("winHeight: " + winHeight);
    return winHeight;
}

function dateFromString (str) {
    str = str.split("-");
    return new Date(str[2], str[1]-1, str[0]);
}
function safeApply(scope) {
    var scopePhase = scope.$root.$$phase;
    if(scopePhase==null||(scopePhase != '$apply' && scopePhase != '$digest')) {
        scope.$apply();
    }
}
