<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<title>Wooooo's QuestionBank</title> 
<style>
	/*bx slider 파트*/
	.main_banner_wrap{
		height: 263px;
		position: relative;
		width:100%;
		height: 447px;
		margin:10px auto;
		border-radius: 10px;
	}
	#main_banner{
		position: relative;
	}
	#main_banner img{
		width: 1200px;
		height: 447px;
	}
	#main_banner h1{
		position: absolute;
		top: 40%;
		left: 45%;
		color:white;
	}
	.touch_left_btn{
		position: absolute;
		top:50%;
		left: 30px;
		margin-top: -25px;
		cursor:pointer;
	}
	.touch_right_btn{
		position: absolute;
		top:50%;
		right: 30px;
		margin-top: -25px;
		cursor:pointer;
	}
	/*홈 화면 파트*/
	.home_container2{
		width:100%;
		height: 210px;  
		margin:5px auto;
	}
	.home_container2 article{  
		width:295px;
		height:200px;
		float:left;
		margin:2px;
		background-color:#A3918F;/* 코코아 */
		position: relative;
		transition:all 1s;
	}
	.home_container2 article:hover{
		background-color:#5B4149;
	}
	.home_container2 img{
		width:70px;
		margin-left: 60px;
		margin-top: 60px;
	}
	.home_container2 article div{
		color:#F6EFEC; 
		width:120px;
		position: absolute;
		right: 20px;
		top: 60px; 
	}
	.home_container3{
		width:100%;
		height:400px;
		margin-bottom: 40px;
		border:1px solid #A3918F; 
		position: relative;
	}
	.home_container3>div{
		height: 400px;
	}
	.home_container3>div:first-child{
		background-color:#444;
		position: relative;
	}
	.home_container3>div:first-child div{
		width:290px; 
		text-align: center;
		color:white;
		position: absolute;
		left: 0;
		top:150px;
	}
	.home_container3>div:last-child{
		position: relative;
	}
	.home_container3>div:last-child div{
		width:300px;
		position: absolute;
		right:200px;s
		top:150px;
	}
	.home_container3>img{
		width:333px;
		position: absolute;
		top: 0;
		left: 300px;
	}
	.home_container>div#color1{
		background-color:#5B4149;/* 진갈색  */
		background-color:#F6EFEC; /* 연베이지 */
		background-color:#A3918F;/* 코코아 */
		background-color:#F3C2BA; /* 연핑크 */
		background-color:#F28683; /* 진핑크 */
	}

</style>
</head>
<body>
	<jsp:include page="include/header.jsp"></jsp:include>
	<%-- <div class="home_container1">
		<img src="${pageContext.request.contextPath}/resources/images/main.jpg">
	</div> --%>
	<div class="main_banner_wrap">
		<div id="main_banner">
			<!-- bx슬라이더, section은 main.js에 넣어야함, css는 필요없음 -->
			<div>
				<a href="#"><img src="${pageContext.request.contextPath}/resources/upload/main.jpg" alt=""></a>
				<h1>1 page</h1>
			</div>
			<div>
				<a href="#"><img src="${pageContext.request.contextPath}/resources/upload/main2.png" alt=""></a>
				<h1>2 Page</h1>
			</div>
			<div>
				<a href="#"><img src="${pageContext.request.contextPath}/resources/upload/main3.png" alt=""></a>
				<h1>3 Page</h1> 
			</div>
		</div>
		<img src="${pageContext.request.contextPath}/resources/upload/cocoa_btn_left.png" alt="이전배너" class="touch_left_btn"> 
		<img src="${pageContext.request.contextPath}/resources/upload/cocoa_btn_right.png" alt="이후배너" class="touch_right_btn">
	</div>
	<div class="home_container2">
		<article>
			<img src="${pageContext.request.contextPath}/resources/upload/solve.png">
			<div>클릭하면<br>문제풀러가기</div>
		</article>
		<article>
			<img src="${pageContext.request.contextPath}/resources/upload/phone.png">
			<div>모바일<br>어쩌구저쩌구 소개</div>
		</article>
		<article>
			<img src="${pageContext.request.contextPath}/resources/upload/incorrect.png">
			<div>틀린문제<br>다시풀러가기</div>
		</article>
		<article>
			<img src="${pageContext.request.contextPath}/resources/upload/timer.png">
			<div>문제풀리는데<br>걸리는 시간 측정가능</div>
		</article>
	</div>
	
	<div class="home_container3">
		<div class="col-sm-5">
			<div>
				<p>블라블라 설명<br>마우스올리면<br>이미지 옆으로 밀기</p>
			</div>
		</div>
		<div class="col-sm-7"> 
			<div>
				<p>블라블라 설명<br>마우스올리면<br>이미지 옆으로 밀기</p>
			</div>
		</div>
		<img src="${pageContext.request.contextPath}/resources/upload/career_bg.jpg">
	</div>
	
	<script>
	$(function(){
		/*---Section , bxSlider---*/
		var $main_banner = $("#main_banner").bxSlider({//반환하는 객체를 받아서 사용
	  	  	slideWidth: 1200,
	  	  	auto:true,
	  	  	autoControls:false,//control을 안보이게 할 것
	  	  	controls:false,//control을 안보이게 할 것
	  	  	pager:false,//페이지 표시 안보이게 할 것
	  	  	pause:2000,
	  	  	stopAutoOnClick:true//클릭됬을때 자동을 멈추게 하는 장치
		});
		
		//버튼으로 bxslider 조정
		$(".touch_left_btn").click(function(){
			$main_banner.goToPrevSlide();
		})
		$(".touch_right_btn").click(function(){
			$main_banner.goToNextSlide();
		})
	})
	</script>
	
	<jsp:include page="include/footer.jsp"></jsp:include>
</body>
</html>
