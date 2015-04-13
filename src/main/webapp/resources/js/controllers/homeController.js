myModule.controller("homeController", function ($scope) {
    initTree();
    $scope.compile =  function() {
        compileSources();
    };


});
var nodeSelected = null;
function initTree(){
    $('#jstree').jstree({
        "core" : {
            "animation" : 0,
            "check_callback" : true,
            "themes" : { "stripes" : true },
            'data' : {
                'url' : function (node) {
                    return  'getWebApps'
                },
                'data' : function (node) {
                    return { 'root' : node.id };
                }
            }
        },
        "types" : {
            "#" : {
                "max_children" : 1,
                "max_depth" : 4,
                "valid_children" : ["root"]
            },
            "root" : {
                "icon" : "/static/3.1.0/assets/images/tree_icon.png",
                "valid_children" : ["default"]
            },
            "default" : {
                "valid_children" : ["default","file"]
            },
            "type_directory" : {
                "valid_children" : ["default","file"]
            },
            "type_class" : {
                "icon" : "glyphicon glyphicon-file",
                "valid_children" : []
            },
            "type_jsp" : {
                "icon" : "glyphicon glyphicon-file",
                "valid_children" : []
            },
            "type_mf" : {
                "icon" : "glyphicon glyphicon-file",
                "valid_children" : []
            },
            "type_xml" : {
                "icon" : "glyphicon glyphicon-file",
                "valid_children" : []
            },
            "file" : {
                "icon" : "glyphicon glyphicon-file",
                "valid_children" : []
            }
        },
        "plugins" : [
            "contextmenu", "dnd", "search",
            "state", "types", "wholerow"
        ]
    });



    $('#jstree').on("select_node.jstree", function (e, data) {
        nodeSelected = data.selected;
        console.log(data.selected);
        $.ajax({
            url: "getContent",
            data: { urlpath: '' + data.selected}
        }).done(function(data) {
            $("#decompileText").val(data);
        });
    });
}

var compileSources = function(){
    $("#simcp-cloak").css("display", "block");
    $.ajax({
        type: "POST",
        url: "compileFile",
        data: { urlpath: '' + nodeSelected, content: $("#decompileText").val()}
    }).done(function(data) {
        $("#simcp-cloak").css("display", "none");
        if (data){
            $("#divError").css('display','inline');
            $("#divErrorInner").html("<strong>Error!</strong> " + data);
        } else {
            $("#divError").css('display','none');
        }
    });
}
