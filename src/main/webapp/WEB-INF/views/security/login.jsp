<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>SB Admin 2 - Login</title>

  <!-- Custom fonts for this template-->
  <link href="/resources/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

  <!-- Custom styles for this template-->
  <link href="/resources/css/sb-admin-2.min.css" rel="stylesheet">

</head>

<body class="bg-gradient-primary">

  <div class="container">

    <!-- Outer Row -->
    <div class="row justify-content-center">

      <div class="col-xl-10 col-lg-12 col-md-9">

        <div class="card o-hidden border-0 shadow-lg my-5">
          <div class="card-body p-0">
            <!-- Nested Row within Card Body -->
            <div class="row">
              <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
              <div class="col-lg-6">
                <div class="p-5">
                  <div class="text-center">
                    <h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
                  </div>
                  
                  <form class="user" role="form" method="post" action="/login">
                    <div class="form-group">
                      <input type="text" name="username" class="form-control form-control-user" id="exampleInputEmail" aria-describedby="emailHelp" placeholder="Enter user name">
                    </div>
                    <div class="form-group">
                      <input type="password" name="password" class="form-control form-control-user" id="exampleInputPassword" placeholder="Password">
                    </div>
                    
                    <div class="form-group form-check">
                    	<label class="form-echeck-label">
                    		<input class="form-check-input" type="checkbox" name="sp-param">Remember ME
                    	</label>
                    </div>
              
<!--                     <div class="form-group">
                      <div class="custom-control custom-checkbox small">
                        <input type="checkbox" class="custom-control-input" id="customCheck">
                        <label class="custom-control-label" for="customCheck">Remember Me</label>
                      </div>
                    </div> -->
                    
                    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token}">
                    
                                        
                    <a href="index.html" class="btn btn-primary btn-user btn-block">
                      Login
                    </a>
                  </form>
                  
                  <hr>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>

    </div>

  </div>

  <!-- Bootstrap core JavaScript-->
  <script src="/resources/vendor/jquery/jquery.min.js"></script>
  <script src="/resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <!-- Core plugin JavaScript-->
  <script src="/resources/vendor/jquery-easing/jquery.easing.min.js"></script>

  <!-- Custom scripts for all pages-->
  <script src="/resources/js/sb-admin-2.min.js"></script>

<script>
	$(".btn-user").on("click", function(e){
		
		e.preventDefault();
		
		$("form").submit();
	});
	
	
	$("input").keydown(function(e){
		if(e.which==13){
/* 			$("form input").each(function(i, obj){
				if(i>=1){
					if($.trim(obj.value)==""){
						alert("공백");
						return;
					}
				}
			}); */
			
			if(valid()){
				$("form").submit();
			}
		}
	});
	
	function valid(){
		if($.trim( $("input[name='username']").val() ) != "" && $.trim( $("input[name='password']").val() ) != "" ){
			return true;
		}else{
			alert("입력 값이 부족 합니다.");
			
			return false;
		}
	}
</script>

</body>

</html>




