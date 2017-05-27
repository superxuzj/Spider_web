<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <head>
        <title>搜救中心财务报销预约后台管理系统</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <!-- Bootstrap -->
        <link href="<%=request.getContextPath()%>/callstatic/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <link href="<%=request.getContextPath()%>/callstatic/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" media="screen">
        <link href="<%=request.getContextPath()%>/callstatic/assets/styles.css" rel="stylesheet" media="screen">
        <link href="<%=request.getContextPath()%>/callstatic/assets/DT_bootstrap.css" rel="stylesheet" media="screen">
<!--[if lt IE 9]>
<script src="<%=request.getContextPath()%>/callstatic/vendors/html5shiv.min.js"></script> 
<script src="<%=request.getContextPath()%>/callstatic/vendors/respond.min.js"></script> 
<![endif]-->
<style type="text/css">
.activeli{
        background-color: #e0e0d1;
    }
</style>
    </head>
    
    <body>
       <jsp:include page="../common/top.jsp" flush="true"/> 
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span3" id="sidebar">
                    <ul class="nav nav-list bs-docs-sidenav nav-collapse collapse">
                        <li class="active">
                            <a href=""><i class="icon-chevron-right"></i> 菜单</a>
                        </li>
                        <li class="activeli">
                            <a href="/manage"><i class="icon-chevron-right"></i> 用户管理</a>
                        </li>
                       <li>
                            <a href="<%=request.getContextPath()%>/dictionary"><i class="icon-chevron-right"></i> 内容管理</a>
                        </li><!-- <li>
                            <a href="#"><i class="icon-chevron-right"></i> 业务管理</a>
                        </li>
                        <li>
                            <a href="#"><i class="icon-chevron-right"></i> 通告管理</a>
                        </li> -->
                        
                    </ul>
                </div>
                <!--/span-->
                <div class="span9" id="content">
                    <div class="row-fluid">
                        <div class="block">
                            <div class="block-content collapse in">
                            <div class="span12">
                                 <form class="form-horizontal" action="<%=request.getContextPath()%>/manage/saveuser" method="post" id="saveForm">
                                      <fieldset>
                                        <legend>新增用户</legend>
                                        <div class="control-group">
                                          <label class="control-label">用户姓名</label>
                                          <div class="controls">
                                           <input class="input-xlarge focused" name="name" type="text" value="">
                                          </div>
                                        </div>
                                        <div class="control-group">
                                          <label class="control-label">用户手机(登录名)</label>
                                          <div class="controls">
                                           <input class="input-xlarge focused" name="phone" type="text" value="" >
                                          </div>
                                        </div>
                                        <div class="control-group">
                                          <label class="control-label" for="focusedInput">用户部门</label>
                                          <div class="controls">
                                            <input class="input-xlarge focused" name="department"  type="text" value="" >
                                          </div>
                                        </div>
                                        <div class="control-group">
                                          <label class="control-label" for="focusedInput">用户类型</label>
                                          <div class="controls">
                                           <select class="span4 m-wrap" name="type">
		  										<option value="1">中心员工</option>
		  										<option value="2">横向</option>
		  										<option value="3">纵向</option>
		  										<option value="4">横纵</option>
		  										<option value="5">管理员</option>
		  									</select>
                                          </div>
                                        </div>
                                        <div class="control-group warning">
                                          <label class="control-label" for="inputError">用户密码</label>
                                          <div class="controls">
                                            <input type="text" class="input-xlarge focused" name="password" value="">
                                          </div>
                                        </div>
                                        <div class="form-actions">
                                          <button onclick="submitForm()" class="btn btn-primary">保存修改</button>
                                          <button onclick="gohis()" class="btn">取消</button>
                                        </div>
                                      </fieldset>
                                    </form>
                            </div>
                            </div>
                        </div>
                        <!-- /block -->
                    </div>


                </div>
            </div>
            <hr>
         <jsp:include page="../common/bottom.jsp" flush="true"/> 
        </div>
        <!--/.fluid-container-->

<script src="<%=request.getContextPath()%>/callstatic/vendors/jquery-1.9.1.js"></script>
<script src="<%=request.getContextPath()%>/callstatic/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/callstatic/assets/scripts.js"></script>
<script src="<%=request.getContextPath()%>/callstatic/assets/DT_bootstrap.js"></script>
<script>
function submitForm(){
	$("#searchForm").submit();
}
function gohis(){
	history.go(-1);
}
function test(userid){
	alert(userid);
}
//第一页
function firstPage(currentPage,totalPage){
	if(currentPage == 1){
		alert("已经是第一页数据");
		return false;
	}else{
		window.location.href="<%=request.getContextPath()%>/manage?pageNo=1";
		return true;
	}
}
//下一页
function nextPage(currentPage,totalPage){
	if(currentPage == totalPage){
		alert("已经是最后一页数据");
		return false;
	}else{
		window.location.href="<%=request.getContextPath()%>/manage?pageNo=" + (parseInt(currentPage)+1);
		return true;
	}
}
//上一页
function previousPage(currentPage,totalPage){
	if(currentPage == 1){
		alert("已经是第一页数据");
		return false;
	}else{
		window.location.href = "<%=request.getContextPath()%>/manage?pageNo=" + (currentPage-1);
		return true;
	}
}

// 尾页
function lastPage(currentPage,totalPage){
	
	if(currentPage == totalPage){
		alert("已经是最后一页数据");
		return false;
	}else{
		window.location.href = "<%=request.getContextPath()%>/manage?pageNo=" +totalPage;
		return true;
	}
}
$(function() {
    
});
</script>
</body>
</html>