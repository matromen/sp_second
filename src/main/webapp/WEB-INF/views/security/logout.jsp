<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
  <script src="/resources/vendor/jquery/jquery.min.js"></script>
</head>
<body>
	<form method="post" action="/logout">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token}">
	</form>
</body>


<script>
	$("document").ready(function(e){
		$("form").submit();
	});

</script>
</html>