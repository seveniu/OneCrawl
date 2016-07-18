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
                    <div id="summaryTest" class="summary-wrap"></div>
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
    pagurian.call(["modules/summary/app"], function (app) {
        var getColumns = function (data) {
            var columns = [];
            data.forEach(function (v) {
                columns.push({
                    "cName": v.consumerName,
                    "title": v.consumerName
                })
            });

            return columns;
        };

        var getRows = function () {
            var rows = [];
            rows.push({
                "dataName": "title",
                "isTitle": true,
                "klass": "summary-span-title"
            });
            rows.push({
                "dataName": "waitSpiderNum",
                "tpl": "等待:{0}"
            });
            rows.push({
                "dataName": "runningSpiderNum",
                "tpl": "运行:{0}"
            });
            rows.push({
                "dataName": "runThreadNum",
                "tpl": "线程数:{0}"
            });

            return rows;
        };


        pagurian.lib.api = "/api/spider-monitor";
        pagurian.lib.api.summary = pagurian.lib.api + "/all";
        $.get(pagurian.lib.api + "/all", {}, function (result) {
            result.result.push({
                "consumerName": "哈哈哈",
                "waitSpiderNum": 12341234,
                "runningSpiderNum": 11,
                "runThreadNum": 22
            });
            result.result.forEach(function (v) {
                v.cName = v.consumerName;
            });
            console.log(result)
            var option = {
                //列
                "allColumns": getColumns(result.result),
                //行
                "allRows": getRows(),
                //数据源
                "dataSource": function (param, dataProcess) {
                    dataProcess(result)
                }
            };
            var summary = $p.summary("#summaryTest", option);
        })
    })
</script>

</body>
</html>
