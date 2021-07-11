<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="false" %>
<html>
<head>
<title>Home</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>div{border: 1px solid orange; min-height: 50px; width: 200px;}</style>
</head>

<body>
	<ol>
		<li>
			서버 쪽 컨트롤러로 값 보내기<button id="test1">테스트</button>
			<script>
				$('#test1').on('click', function(){
					$.ajax({
						url: 'test1.do',
						data: {name:'강건강', age:20},
						success: function(data){
							console.log(data);
							if(data == 'ok'){
								alert('본인 인증 성공');
							} else {
								alert('본인 인증 실패');
							}
						}
					});
				});
			</script>
		</li>
		
		<li>
			컨트롤러에서 리턴하는 JSON객체 받아 출력하기
			<button id="test2">테스트</button>
			<div id="d2"></div>
			<script>
				$('#test2').on('click', function(){
					$.ajax({
						url: 'test2.do',
						dataType: 'json',
						success: function(data){
							console.log(data);
							$('#d2').html('번호 : ' + data.no + "<br>"
									+ '제목 : ' + data.title + "<br>"
									+ '작성자 : ' + data.writer + "<br>"
									+ '내용 : ' + data.content)
						}
					});
				});
			</script>
		</li>
		
		<li>
			컨트롤러에서 리턴하는 JSON배열을 받아서 출력하기
			<button id="test3">테스트</button>
			<div id="d3"></div>
			<script>
				$('#test3').on('click', function(){
					$.ajax({
						url: 'test3.do',
						dataType: 'json',
						success: function(data){
							console.log(data);
							var values = $('#d3').html();
							
							for(var i in data){
								values += data[i].userId + ", "
										+ data[i].userPwd + ", "
										+ data[i].userName + ", "
										+ data[i].age + ", "
										+ data[i].email + ", "
										+ data[i].phone + "<br>";
							}
							$('#d3').html(values);
						}
					});
				});
			</script>
		</li>
	</ol>
</body>
</html>
