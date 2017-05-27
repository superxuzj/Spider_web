<%@ page contentType="text/html; charset=UTF-8"  %>
<style>
#timeshow{
	font-family:宋体;
	font-size:15px;
	margin-left:700px;
}
</style>
<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="btn btn-navbar" data-toggle="collapse"
					data-target=".nav-collapse"> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> </a> <a
					class="brand" href="#">爬虫后台管理系统</a>
					<a class="brand" href="#" ></a>
				 <a class="brand" href="#" ><span id="timeshow"></span></a>
					
				<!-- <div class="nav-collapse collapse">
					<ul class="nav pull-right">
						<li class="dropdown"><a href="#" role="button"
							class="dropdown-toggle" data-toggle="dropdown"> <i
								class="icon-user"></i> ${ sessionScope.username} <i class="caret"></i> </a>
							<ul class="dropdown-menu">
								<li><a tabindex="-1" href="#" onclick="changepwd(${ sessionScope.userid})">修改密码</a>
								</li>
								<li class="divider"></li>
								<li><a tabindex="-1" href="#" onclick="loginout()">退出</a>
								</li>
							</ul>
						</li>
				</div> -->
			</div>
		</div>
</div>
 <script language="javascript">
     var t = null;
    t = setTimeout(time,1000);//开始执行
    function time()
    {
       clearTimeout(t);//清除定时器
       dt = new Date();
       var year = dt.getFullYear();
       var month = dt.getMonth()+1;
       var day = dt.getDate();
       var h=dt.getHours();
       var m=dt.getMinutes();
       var s=dt.getSeconds();
       $("#timeshow").text(""+year+"年"+month+"月"+day+"日"+h+"时"+m+"分"+s+"秒");
       t = setTimeout(time,1000); //设定定时器，循环执行             
    } 
  </script>
<script type="text/javascript">
	function changepwd(id){
		window.location.href="<%=request.getContextPath()%>/changepwd?id="+id;
	}
	function loginout(){
		window.location.href="<%=request.getContextPath()%>/loginout";
	}
	
	</script>