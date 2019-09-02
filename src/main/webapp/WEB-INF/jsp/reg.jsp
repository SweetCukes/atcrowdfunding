<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
	<link rel="stylesheet" href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
	<link rel="stylesheet" href="${APP_PATH}/css/login.css">
	<style>

	</style>
  </head>
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <div><a class="navbar-brand" href="${APP_PATH}/index.htm" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
      </div>
    </nav>

    <div class="container">

      <form id="loginForm" action="${APP_PATH}/doreg.htm"  method="post" class="form-signin" role="form">
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户注册</h2>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" id="loginacct" name="loginacct" placeholder="请输入登录账号" autofocus>
			<span class="glyphicon glyphicon-user form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control"  id="userpswd" name="userpswd" placeholder="请输入登录密码" style="margin-top:10px;">
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" id="email" name="email" placeholder="请输入邮箱地址" style="margin-top:10px;">
			<span class="glyphicon glyphicon glyphicon-envelope form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名称" style="margin-top:10px;">
			<span class="glyphicon glyphicon glyphicon-envelope form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<select class="form-control" >
                <option>会员</option>
                <option>管理</option>
            </select>
		  </div>
        <div class="checkbox">
          <label>
            忘记密码
          </label>
          <label style="float:right">
            <a href="${APP_PATH}/login.html">我有账号</a>
          </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="doreg()" > 注册</a>
      </form>
    </div>
   <script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
    <script src="${APP_PATH}/script/layer/layer.js"></script>
    <script type="text/javascript">
    </script>
    <script>
    	function doreg(){
    		var loginacct =$("#loginacct");
    			if( loginacct.val() == "" ){
    			layer.msg("登录账号为空重新输入",{time:2000, icon:5, shitf:6},function(){
     				loginacct.focus();
    			});
    			return;
    		}    
    		var userpswd =$("#userpswd");
    		if( userpswd.val() == "" ){
    			layer.msg("登录密码为空重新输入",{time:2000, icon:5, shitf:6},function(){
     				userpswd.focus();
    			});
    			return;
    		} 
    		var email =$("#email");
    		if( email.val() == "" ){
    			layer.msg("登录邮箱为空重新输入",{time:2000, icon:5, shitf:6},function(){
    				email.focus();
    			});
    			return;
    		} 
    		var username =$("#username");
			if( username.val() == "" ){
			layer.msg("用户名称为空重新输入",{time:2000, icon:5, shitf:6},function(){
 				loginacct.focus();
			});
			return;
		}    
    		
    		var loadingIndex = -1;
    		$.ajax({
    			url : "${APP_PATH}/doreg.do",
    			type : "POST",
    			//dataType : "json",
    			data : {
    				"loginacct"  :  loginacct.val(),
    				"userpswd" :  userpswd.val(),
    				"email"  		 :	email.val(),
    				"username":	username.val()
    			},
    			beforeSend : function(){
    				 loadingIndex = layer.load(2,{time : 10*1000});
    			},
    			success : function(result){
    				layer.close(loadingIndex);
    				if (result.success) {
    					window.location.href ="${APP_PATH}/manage/main.htm";
    				}else{
    					alert("注册失败");
    				}
    			},
    			error : function(){
    				alert("用户注册失败");
    				
    			}
    		});
    		
    	}
    
    </script>
  </body>
</html>