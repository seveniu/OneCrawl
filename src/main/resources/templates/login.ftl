<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
    <title>登录</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <meta name="MobileOptimized" content="320">
    <link rel="shortcut icon" href="favicon.ico" />
    <link href="/static/pagurain/resources/css/public.css" rel="stylesheet" type="text/css"/>
    <link href="/static/pagurain/resources/css/themes-green.css" rel="stylesheet" type="text/css"/>
    <link href="/static/pagurain/resources/css/page-login.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<div class="header navbar navbar-inverse navbar-fixed-top">

    <div class="header-inner">
        <a class="navbar-brand logo" href="theme-color.html">
            <img src="../resources/css/img/logo.png" />
        </a>
    </div>

</div>
<div class="clearfix"></div>

<!-- BEGIN LOGIN -->
<div class="login action-before">
    <div class="content">
        <!-- BEGIN LOGIN FORM -->
        <form  id="form_login" method="post" action="login">

            <div class="form-group has-error">
                <div class="input-icon">
                    <i class="fa fa-user"></i>
                    <div class="input-cut"></div>
                    <input class="form-control placeholder-no-fix {required:true}"
                           maxlength="255" type="text"  autocomplete="off" placeholder="用户名"
                           name="username" tabindex="1" />
                </div>
                <span class="help-block" for="username"></span>
            </div>
            <div class="form-group has-error">
                <div class="input-icon">
                    <i class="fa fa-lock"></i>
                    <div class="input-cut"></div>
                    <input
                            class="form-control placeholder-no-fix {required:true,rangelength:[6,30]}"
                            maxlength="30" type="password" autocomplete="off"
                            placeholder="密码" name="password" tabindex="2" />
                </div>
                <span for="password" class="help-block"></span>
            </div>
            <div class="form-group has-error">
                <div class="input-icon">
                    <i class="fa fa-shield"></i>
                    <div class="input-cut"></div>
                    <input
                            class="form-control placeholder-no-fix captcha {required:true}"
                            type="text" maxlength="4" autocomplete="off" placeholder="验证码"
                            id="txt_captcha" tabindex="3" />
                    <div class="captcha-wrap">
                        <a href="javascript:;" id="captcha" title="点击刷新验证码"> <img
                                id="img_captcha" src="../resources/img/loading.gif" />
                        </a>
                    </div>
                </div>
                <span for="txt_captcha" class="help-block"></span>
            </div>
            <div class="form-actions">
                <label class="checkbox">
                    <input type="checkbox" name="remember_me" value="1" tabindex="4" /> 记住密码
                </label>
                <a class="a-forgot" href="javascript:;">忘记密码？</a>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn btn-primary btn-block"
                        tabindex="5">登 录</button>
            </div>
        </form>
    </div>
</div>
<!-- END LOGIN -->


</body>
<!-- END BODY -->

<script src="/static/pagurain/pagurian.js" ></script>
<script>
    pagurian.call("modules/page/app", function(app) {
        app.page.login();
    });
</script>

</html>
