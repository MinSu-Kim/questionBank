<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
<title>Insert title here</title>
<style>
	div.question_wrap{
		border:1px solid gray;
		padding: 10px;
	}
</style>
</head>
<body>
	<jsp:include page="../include/header.jsp"></jsp:include>
	
	<form action="" method="post" id="wsm_testForm">
		<div class="container">
		</div>
		<button type="submit">제출</button>
	</form>
	
	<jsp:include page="../include/footer.jsp"></jsp:include>
	
	 <script>
	  function getPageList(){
		  $.ajax({
				url:"${pageContext.request.contextPath}/question/listJson",
				type:"get",
				dataType:"json",
				success:function(json){
					console.log(json);
					$(".container").empty();//안에만 비우기
					var source = $("#template1").html();
					var f = Handlebars.compile(source);  
					var result = f(json);
					$(".container").append(result);
				}
			})
	  }
	  
	  $(function(){
		  getPageList();
	  })
  </script>
	
	<script id="template1" type="text/x-handlebars-template"> 
	{{#each.}}
		<div class="question_wrap">
			<p>{{questionCode}}</p>
			<p>{{questionTitle}}</p>
			<p><input type="radio" name='answer' value='1'> {{choice1}}</p>
			<p><input type="radio" name='answer' value='2'> {{choice2}}</p>
			<p><input type="radio" name='answer' value='3'> {{choice3}}</p>
			<p><input type="radio" name='answer' value='4'> {{choice4}}</p>
			<p><input type="hidden" name='correct'> {{correct}}</p>
			<p><input type="hidden" name='customer'></p>
			<p><input type="hidden" name='test'>{{test}}</p>
		</div>
	{{/each}}
  	</script>
  	
  	
  	
</body>
</html>