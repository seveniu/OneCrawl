<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->

<head>
    <title>列表</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <meta name="MobileOptimized" content="320">
    <link rel="shortcut icon" href="favicon.ico"/>
    <link href="/static/pagurain/resources/css/public.css" rel="stylesheet" type="text/css"/>
    <link id="style_themes" href="/static/pagurain/resources/css/themes-green.css" rel="stylesheet" type="text/css"/>

    <link rel="stylesheet" href="/static/css/highlight.default.min.css">

    <!--[if lt IE 9]>
    <script src="/static/js/es5-shim.min.js"></script>
    <![endif]-->
    <!--[if !IE]><!-->
    <script src="/static/js/highlight.min.js"></script>
    <!--<![endif]-->


</head>
<body>

<#include "../header.ftl">
<div class="clearfix"></div>

<!-- BEGIN CONTAINER -->
<div class="page-container">
<#include "../left.ftl">


    <!-- BEGIN CONTENT -->
    <div class="page-content-wrapper">
        <div class="page-content">

            <div class="row">
                <div class="col-md-12">
                    <div class="table-toolbar">
                        <button id="add" type="button" class="btn btn-primary"><i class="fa fa-plus"></i> 新建</button>
                        <button id="del" type="button" class="btn btn-danger"> 删除</button>
                        <button id="edit" type="button" class="btn btn-info"> 编辑</button>
                        <span class="label ml10 mr10"> 状态 :</span>
                        <div class="btn-group btn-dropdown btn-select" data-type='select'>
                            <button data-toggle="dropdown" type="button"
                                    class="btn btn-default  dropdown-toggle w-unset">全部 <i class="fa fa-angle-down"></i>
                            </button>
                            <ul role="menu" class="dropdown-menu chooseStatus">
                                <li><a data-id="0" href="javascript:;">全部</a></li>
                                <li><a data-id="1" href="javascript:;">启用</a></li>
                                <li><a data-id="2" href="javascript:;">禁用</a></li>
                            </ul>
                        </div>
                    <#--<span class="label ml10 mr10"> 状态 :</span>-->
                    <#--<div class="btn-group btn-radio">-->
                    <#--<button  type="button" class="btn btn-default btn-active">全部</button>-->
                    <#--<button  type="button" class="btn btn-default ">启用</button>-->
                    <#--<button  type="button" class="btn btn-default ">禁用</button>-->
                    <#--</div>-->
                        <div class="input-icon input-search right">
                            <span class="label">|</span>
                            <button id="exportToExcel" type="button" class="btn btn-default ">报告导出</button>
                        </div>

                    <#--<div class="input-icon input-search right">-->
                    <#--<i class="fa fa-search"></i>-->
                    <#--<input placeholder="查询关键字" id="txt_search" class="form-control" maxlength="1024" type="text">-->
                    <#--</div>-->

                    </div>
                    <table id="my_table"></table>
                </div>
            </div>
        </div>

    </div>
    <!-- END CONTENT -->
</div>
<!-- END CONTAINER -->
<div id="descrView"></div>

<script src="/static/pagurain/pagurian.js"></script>

<!--[if lt IE 9]>
<script src="/static/js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">
    //    window.CONFIG = {
    //        appId: "uploader"
    //    };
</script>
<script type="text/javascript">
//    require('/static/js/moment');
    var dataTable;
    pagurian.call(["modules/datatable/app","/static/js/moment"], function (app) {
        console.log(moment(1318781876406));
        pagurian.path.api = "/api/task";
        pagurian.lib.apiPostfix = "";
        dataTable = $p.dataTable("#my_table", {
            "fnDataSource": function (params, callback) {
                pagurian.lib.service.get("/list", params, callback);
            },
            "sClass": "table-fixed",
            "aaSorting": [
                [0, "asc"]
            ],
            "aLengthMenu":[ [10,20, 30], [10,20, 30],[10, 20, 30] ],
            "fnParams": function () {
                return {};
            },
            "aoColumns": [{
                "sClass": "t-a-l w100",
                "sTitle": "ID",
                "mData": "id"
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "任务",
                "mData": "name"
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "员工",
                "mData": "createEmployeeId"
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "状态",
                "mData": "status",
                mRender: function (data, type, full) {
                    //1: HTML 源文本, 2：跳转链接,3：下一页链接, 4：文本链接（链接文本，以及链接），5： 纯文本;
                    if (data == 1) {
                        return '未运行'
                    } else if (data == 2) {
                        return '正在运行'
                    } else if (data == 3) {
                        return '禁用'
                    }
                    return data;
                }
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "周期",
                "mData": "cycle",
                mRender: function (data,type,full) {
                    return data/3600;
                }
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "链接",
                "mData": "url"
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "模板id",
                "mData": "templateId"
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "线程数",
                "mData": "threadNum"
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "代理",
                "mData": "proxy",
                mRender: function (data, type, full) {
                    //1: HTML 源文本, 2：跳转链接,3：下一页链接, 4：文本链接（链接文本，以及链接），5： 纯文本;
                    if (data == -1) {
                        return '不使用'
                    } else if (data == 1) {
                        return '使用'
                    }
                    return data;
                }
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "JS引擎",
                "mData": "javascript",
                mRender: function (data, type, full) {
                    //1: HTML 源文本, 2：跳转链接,3：下一页链接, 4：文本链接（链接文本，以及链接），5： 纯文本;
                    if (data == -1) {
                        return '不使用'
                    } else if (data == 1) {
                        return '使用'
                    }
                    return data;
                }
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "下次运行时间",
                "mData": "nextRunTime",
                mRender: function (data) {
                    return moment(data).format("YYYY-MM-DD HH:mm");
                }
            }, {
                "bSortable": false,
                "sClass": "t-a-l",
                "sTitle": "默认值",
                "mData": "defaultValue"
            }
            ]
        });
        $p.dialog("#del", {
            title: "提示",
            body: "你确定要删除吗？",
            submit: function (modal, data, params, callback) {
                var ids = [];
                var error = false;
                $("table.dataTable tr.selected").each(function (i, v) {
                    var id = $(v).find("td").first().html();
                    try {
                        ids.push(parseInt(id));
                    } catch (e) {
                        $p.alert("错误: id 不是数字");
                        error = true;
                    }
                });
                if (!error) {

                    $.ajax({
                        url: pagurian.path.api + "/del?ids=" + ids.join(","),
                        type: 'DELETE',
                        success: function (result) {
                            // Do something with the result
                            if (result.code == "200000") {
                                $p.alert("删除成功");
                                window.location.reload()
//                                modal.hide();
                            } else {
                                $p.alert("错误 : " + result.code, "error");
                            }
                        }
                    });
                }
            }
        });
        $('#my_table tbody').on('click', 'tr', function () {
            $(this).toggleClass('selected');
        });
        $("#exportToExcel").click(function (e) {
            window.location = pagurian.path.api + "/excel";
        });
        $(".chooseStatus a").click(function (e) {
            var value = $(this).data("id");
            if(value == 0) {
                value = null;
            }
            dataTable.aApiParams["status"] = value;
            dataTable.update();
            console.log("hahaha")
        });
    })
</script>

</body>
</html>
