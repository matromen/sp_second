<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>    


<%@ include file="../include/top.jsp" %>

        <!-- Begin Page Content -->
        <div class="container-fluid">

          <!-- Page Heading -->
          <h1 class="h3 mb-2 text-gray-800">Tables</h1>
          <p class="mb-4">DataTables is a third party plugin that is used to generate the demo table below. For more information about DataTables, please visit the <a target="_blank" href="https://datatables.net">official DataTables documentation</a>.</p>

          <!-- DataTales Example -->
          <div class="card shadow mb-4">
            <div class="card-header py-3">
              <h6 class="m-0 font-weight-bold text-primary">admin Page</h6>
            </div>
            <div class="card-body">
            
				<div class="table-responsive">
				
					<!-- writeForm 시작 -->
				
					<form role="form" id="writeForm" method="post" action="/board/writePro">
						<div class="form-group">
							<label>제목</label>
							<input class="form-control" name="title">
						</div>
						
						<div class="form-group">
							<label>내용</label>
							<input class="form-control" name="content">
						</div>						
						
						<div class="form-group">
							<label>작성자</label>
							<input class="form-control" value='<sec:authentication property="principal.memberVO.username"/>' readonly>
						</div>

						<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token}">
												
						
						<input type="hidden" name="pageNum" value="${pbean.pageNum}">	
						<input type="hidden" name="pageAmount" value="${pbean.pageAmount}">
						<input type="hidden" name="searchType" value="${pbean.searchType}">
						<input type="hidden" name="searchValue" value="${pbean.searchValue}">
						<input type="hidden" name="searchValue0" value="${pbean.searchValue0}">
											
					</form>
				
					<!-- writeForm 끝 -->
					
					<!-- file attach -->
					<div class="custom-file">
						<input type="file" class="custom-file-input" id="uploadFile" name="uploadFile" multiple>
					    <label class="custom-file-label" for="uploadFile">Choose file...</label>
					</div>
					<!-- file attach -->
					
					<div class="uploadResult">
						<ul></ul>
					</div>
					
					<button type="button" id="writeProButton" class="btn btn-default">등록하기</button>
					<button type="button" id="listButton" class="btn btn-default">리스트</button>
					
											
				
					<!-- listForm 시작 -->
							
					<form id="listForm" method="get" action="/board/list">	
						<input type="hidden" name="pageNum" value="${pbean.pageNum}">	
						<input type="hidden" name="pageAmount" value="${pbean.pageAmount}">
						<input type="hidden" name="searchType" value="${pbean.searchType}">
						<input type="hidden" name="searchValue" value="${pbean.searchValue}">
						<input type="hidden" name="searchValue0" value="${pbean.searchValue0}">
					</form>
				
					<!-- listForm 끝 -->				
									
				</div>
	
            </div>
          </div>

        </div>
        <!-- /.container-fluid -->

<%@ include file="../include/bottom.jsp" %>

<script>
	$("#writeProButton").on("click", function(e){
		var _form = $("#writeForm");
		
		if( $.trim( _form.find("input[name='title']").val() ) == ""){
			alert("제목을 넣어주세요.");
			return false;
		}
		
		if( $.trim( _form.find("input[name='content']").val() ) == ""){
			alert("내용을 넣어주세요.");
			return false;
		}
		
		var str = "";
		$(".uploadResult ul li").each(function(i, obj){
			var obj2 = $(obj);
			
			
 			str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+obj2.data("path")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].uuid'       value='"+obj2.data("uuid")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].fileName'   value='"+obj2.data("filename")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].fileType'   value='"+obj2.data("filetype")+"'>"; 

	
		});
		
		_form.append(str);
		
		_form.submit();
	});
	
	$("#listButton").on("click", function(e){
		$("#listForm").submit();
	});
	
	
	////////////////////////files//////////////////////////
	//첨부파일 체크
	var regex = new RegExp("(.*?)\.(exe|sh)$");
	var maxSize = 5242880; //5MB
	
	function checkExtension(fileName, fileSize){
		if(fileSize > maxSize){
			alert("파일 사이즈 초과");
			
			return false;
		}
		
		if(regex.test(fileName)){
			alert("해당 종류의 파일은 업로드 할 수 없습니다.");
			
			return false;
		}
		
		return true;
	}
	
	//파일 선택시 업로드 및 화면 표현
	var csrfHeaderName = "${_csrf.headerName}";
	var csrfTokenValue = "${_csrf.token}";
	
	$("input[name='uploadFile']").on('change', function(e){
		var formData = new FormData();
		var inputFile = $("input[name='uploadFile']");
		var files = inputFile[0].files;
		
		for(var i=0; i<files.length; i++){
			if(!checkExtension(files[i].name, files[i].size)){
				return false;
			}
			
			formData.append("uploadFiles", files[i]);
		}
		
		$.ajax({
			type:'post',
			url:'/attach/uploadFiles',
			processData:false,
			contentType:false,
			data:formData,
				
			beforeSend:function(xhr){
				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
			},
			
			dataType:'json',
			success:function(result){
				console.log(result);
				
				showUploadResult(result);
			}
		});
	});
	
	
	function showUploadResult(uploadResultArr){
		if(!uploadResultArr || uploadResultArr.length==0) return;
		
		var uploadUL = $(".uploadResult ul");
		var str = "";
		
		$(uploadResultArr).each(function(i, obj){
			if(obj.image){
				var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
				
				str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-filetype='i'>";
				str += "<div><span>" + obj.fileName + "</span>";
				str += "<button type='button' data-file = '"+ fileCallPath +"' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button>";
				str += "<img src='/attach/displayFiles?fileName="+fileCallPath+"'>";
				str += "</div></li>";
			}else{
				var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
				
				str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName+ "' data-filetype='f'>";
				str += "<div><span>" + obj.fileName + "</span>";
				str += "<button type='button' data-file = '"+ fileCallPath+"' data-type='file' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button>";
				str += "</div></li>";
			}
		});
		
		uploadUL.append(str);
	}
	//파일 선택시 업로드 및 화면 표현
	
	//파일 삭제 및 화면 표현
	$(".uploadResult").on("click", "button", function(e){
		
		var targetFile = $(this).data("file");
		var type = $(this).data("type");
		var targetLi = $(this).closest("li");
		
		alert(targetLi);
		$.ajax({
			type:"post",
			url:"/attach/deleteFile",
			data:{fileName:targetFile, type:type},
			
			beforeSend:function(xhr){
				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
			},
			
			dataType:"text",
			success:function(result){
				alert(result);
				targetLi.remove();
			}
		}); 
	});
	//파일 삭제 및 화면 표현	
	
	////////////////////////files//////////////////////////
 	

</script>

				