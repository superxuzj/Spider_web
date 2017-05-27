<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
       <jsp:include page="common/top.jsp" flush="true"/> 
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span3" id="sidebar">
                    <ul class="nav nav-list bs-docs-sidenav nav-collapse collapse">
                        <li class="active">
                            <a href=""><i class="icon-chevron-right"></i> 菜单</a>
                        </li>
                        <li class="activeli">
                            <a href="<%=request.getContextPath()%>/"><i class="icon-chevron-right"></i> 爬虫管理</a>
                        </li>
                        <li>
                            <a href="<%=request.getContextPath()%>/info"><i class="icon-chevron-right"></i> 数据管理</a>
                        </li>
                         <li>
                            <a href="<%=request.getContextPath()%>/spider/all"><i class="icon-chevron-right"></i> 数据爬取</a>
                        </li>
                       <!--  <li>
                            <a href="#"><i class="icon-chevron-right"></i> 通告管理</a>
                        </li> -->
                        
                    </ul>
                </div>
                <!--/span-->
                <div class="span9" id="content">
                    <div class="row-fluid">
                        <div class="block">
                            <div class="navbar navbar-inner block-header">
                                <div class="muted pull-left">爬虫抓取列表</div>
                                
                            </div>
                            <div class="block-content collapse in">
                                <div class="span12">
  									<table class="table table-striped">
						              <thead>
						                <tr>
						                  <th>序号</th>
						                  <th>编码</th>
						                  <th>网站名称</th>
						                  <th>抓取板块</th>
						                  <th>抓取链接</th>
						                  <th>操作</th>
						                </tr>
						              </thead>
						              <tbody>
						              <c:forEach items="${websiteList }" var="website">
							               <tr>
							                  <td>${website.id} </td>
							                  <td>${website.code}</td>
							                  <td>${website.name}</td>
							                  <td>${website.section}</td>
							                  <td>
							                   <a href="${website.link}" title="${website.link}" target="_blank">
							                  ${fn:substring(website.link,0,42)}</a></td>
							                  <td>
							                  	<button class="btn btn-danger" onclick="doSpider('${website.code}')">爬取</button>
							                  </td>
							                </tr>
									   </c:forEach>
						              </tbody>
						            </table>
                            </div>
                        </div>
                        <!-- /block -->
                    </div>


                </div>
            </div>
            <hr>
           
        </div>
        <!--/.fluid-container-->
<jsp:include page="common/bottom.jsp" flush="true"/> 
<script src="<%=request.getContextPath()%>/callstatic/vendors/jquery-1.9.1.js"></script>
<script src="<%=request.getContextPath()%>/callstatic/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/callstatic/assets/scripts.js"></script>
<script src="<%=request.getContextPath()%>/callstatic/assets/DT_bootstrap.js"></script>
<script>
function doSpider(code){
	alert(code);
	window.location.href="<%=request.getContextPath()%>/spider/"+code;
}
</script>
</body>
</html>