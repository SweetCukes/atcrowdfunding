<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
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
          <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
      </div>
    </nav>

    <div class="container">
		<h1>${errorMsg}</h1>
      <form id="loginForm" action="${APP_PATH}/dologin.htm"  method="post" class="form-signin" role="form">
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户登录</h2>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" id="loginacct" name="loginacct" value="zs" placeholder="请输入登录账号" autofocus>
			<span class="glyphicon glyphicon-user form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<input type="password" class="form-control" id="userpswd" name="userpswd" value="123" placeholder="请输入登录密码" style="margin-top:10px;">
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<select id="userType"class="form-control" >
                <option value="member" selected>会员</option>
                <option value="user">管理</option>
            </select>
		  </div>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> 记住我
          </label>
          <br>
          <label>
            忘记密码
          </label>
          <label style="float:right">
            <a href="${APP_PATH}/reg.htm">我要注册</a>
          </label>
        </div>
        <a class="btn btn-lg btn-success btn-block"  onclick="dologin()" >登录</a>
      </form>
    </div>
   	<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
    <script src="${APP_PATH}/script/layer/layer.js"></script>
    <script type="text/javascript">
    </script>
    <script>
    function dologin(){
    	var loginacct =$("#loginacct");
		if( loginacct.val() == "" ){
			//alert("登录账号不为空！请输入！");
			layer.msg("登录账号为空重新输入",{time:2000, icon:5, shitf:6},function(){
 				loginacct.focus();
			});
			return;
		}    
		var userpswd =$("#userpswd");
		if( userpswd.val() == "" ){
			//alert("登录密码不能为空！请输入!");
			layer.msg("登录密码为空重新输入",{time:2000, icon:5, shitf:6},function(){
 				userpswd.focus();
			});
			return;
		}    	
		//使用AJAx线程提交数据
		//{}  ==> 声明对象
		//JavaScriptObjectNotation对象 ==>
		//[{属性名称:属性值，属性名称1：属性值1}]
		var url = "dologin.do";
		
		var jsonData = {
				"loginacct"  :  loginacct.val(),
				"userpswd" :  userpswd.val()
		};
		
		if( $("#userType").val() == "member" ){
			url = "doMemberLogin.do";
			jsonData.memberpswd = userpswd.val();
		}
		
		var loadingIndex = -1;
		
		$.ajax({
			url : "${APP_PATH}/"+url ,
			type : "POST",
			//dataType : "json",
			data : 	jsonData	,
			beforeSend : function(){
				 loadingIndex = layer.load(2,{time : 10*1000});
			},
			success : function(result){
				layer.close(loadingIndex);
				if (result.success) {
					if( $("#userType").val() == "member" ){
						window.location.href ="${APP_PATH}/member.htm";
					}else{
						window.location.href ="${APP_PATH}/manage/main.htm";
					}
						
				}else{
					alert("密码错误，登录失败");
				}
			},
			error : function(){
				alert("用户登录失败");
				
			}
		});
    }
    </script>
  </body>
</html>