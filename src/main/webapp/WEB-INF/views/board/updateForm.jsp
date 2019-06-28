<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>    


<%@ include file="../include/top.jsp" %>

<style>
	.bigPictureWrapper{
		position:absolute;
		display:none;
		justify-content:center;
		align-items:center;
		top:0%;
		width:100%;
		height:100%;
		background-color:gray;
		z-index:100;
		background:rgba(255,255,255,0.5);
	}
	
	.bigPicture{
		position:relative;
		display:flex;
		justify-content:center;
		align-itmes:center;
	}
	
	.bigPicture img{
		width:600px;
	}
	
</style>

<sec:authentication property="principal" var="mInfo"/>
<sec:authorize access="hasAuthority('admin')" var="isAdmin" />

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
				
					<!-- 이미지 첨부파일 클릭시 -->
					<div class="bigPictureWrapper">
						<div class="bigPicture">
						</div>
					</div>
					<!-- 이미지 첨부파일 클릭시 -->
					
									
					<!-- updateForm 시작 -->
					<form id="updateForm" method="post" action="/board/updatePro">
						<div class="form-group">
							<label>제목</label>
							<input class="form-control" name="title" value="${vo.title}">
						</div>
						
						<div class="form-group">
							<label>내용</label>
							<input class="form-control" name="content" value="${vo.content}">
						</div>						
			
						<div class="form-group">
							<label>작성자</label>
							<input class="form-control" value="${vo.userName}">
						</div>		
												
						<div class="form-group">
							<label>수정자</label>
							<input class="form-control" value='<sec:authentication property="principal.memberVO.username"/>' readonly>
						</div>
						
						<input type="hidden" name="register" value="${vo.register}"/>
						
						<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token}">
						
						
						<!-- file attach -->
						<div class="custom-file">
							<input type="file" class="custom-file-input" id="uploadFile" name="uploadFile" multiple>
						    <label class="custom-file-label" for="uploadFile">Choose file...</label>
						</div>
						<!-- file attach -->
											
						
						<!-- file attach -->
						<div class="panel-heading">Files</div>
						<div class="panel-body">
							<div class="uploadResult">
								<ul></ul>
							</div>
						</div>
						<!-- file attach -->	
											

						
						<input type="hidden" name="pageNum" value="${pbean.pageNum}">	
						<input type="hidden" name="pageAmount" value="${pbean.pageAmount}">
						<input type="hidden" name="searchType" value="${pbean.searchType}">
						<input type="hidden" name="searchValue" value="${pbean.searchValue}">
						<input type="hidden" name="searchValue0" value="${pbean.searchValue0}">											
					</form>	
					<!-- updateForm 끝 -->
					
					
					<c:if test="${mInfo.username eq vo.register || isAdmin}">				
					<button type="button" id="updateProButton" class="btn btn-default">수정 저장</button>
					<button type="button" id="deleteProButton" class="btn btn-default">삭제</button>
					</c:if>

					<button type="button" id="listButton" class="btn btn-default">취소</button>		
						
											
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
	$("#updateProButton").on("click", function(e){
		$("#updateForm").append("<input type='hidden' name='seq' value='${vo.seq}'>");
		
		
		var str = "";
		$(".uploadResult ul li").each(function(i, obj){
			var obj = $(obj);
			
			str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+obj.data('uuid')+"'>";
			str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+obj.data('uploadpath')+"'>";
			str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+obj.data('filename')+"'>";
			str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+obj.data('filetype')+"'>";			
		});
		
		$("#updateForm").append(str);

		$("#updateForm").submit();
	});
	
	$("#deleteProButton").on("click", function(e){
		$("#updateForm").append("<input type='hidden' name='seq' value='${vo.seq}'>");
		$("#updateForm").attr("action","/board/deletePro");
		
		$("#updateForm").submit();
		
	});
	
	$("#listButton").on("click", function(e){

		$("#listForm").submit();
	});	
	
	
	
	////////////////// file /////////////////////////
	// 웹페이지 로딩 후 첨부파일 표현
	$(document).ready(function(e){
		(
			function(){
				var seq= ${vo.seq};
				
				$.get("/attach/viewAttachList", {seq:seq}, function(resultArr){
					var str = "";
					
					$(resultArr).each(function(i, obj){
						if(obj.fileType == "i"){
							var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
							
							str += "<li data-uploadpath='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-filetype='"+obj.fileType+"'>";
							str += "<div><span>"+obj.fileName + "</span>";
							str += "<button type='button' data-file='"+fileCallPath+"' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
							str += "<img src='/attach/displayFiles?fileName="+fileCallPath+"' style='width:50px; height:50px;'>";
							str += "</div></li>"
						}else{
							var fileCallPath = encodeURIComponent(obj.uploadPath + "/_" + obj.uuid + "_" + obj.fileName);
							
							str += "<li data-uploadpath='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-filetype='"+obj.fileType+"'>";
							str += "<div><span>"+obj.fileName + "</span>";
							str += "<button type='button' data-file='"+fileCallPath+"' data-type='file' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
							str += "<img src='/resources/img/attach_images.png' style='width:50px; height:50px;'>";
							str += "</div></li>"							
						}
					});
					
					$(".uploadResult ul").html(str);
				});
			}
		)();
	});
	// 웹페이지 로딩 후 첨부파일 표현
	
	// 첨부파일 클릭시 이미지 화면 표현 및 일반파일 다운로드
	$(".uploadResult").on("click", "li", function(e){
		var obj = $(this);
		var fileCallPath = encodeURIComponent(obj.data("uploadpath") + "/" + obj.data("uuid") + "_" + obj.data("filename"));
		
		if(obj.data("filetype")=="i"){
			showImage(fileCallPath.replace(new RegExp(/\\/g), "/"));
		}else{
			self.location="/attach/downloadFile?fileName="+fileCallPath;
		}
	});
	
	function showImage(fileCallPath){
		$(".bigPictureWrapper").css("display", "flex");
		$(".bigPicture").html("<img src='/attach/displayFiles?fileName="+fileCallPath+"'>").animate({width:'100%', heigth:'100%', opacity:'0.8'}, "slow");
	}
	
	$(".bigPictureWrapper").on("click", function(e){
		$(".bigPicture").animate({width:'0%', heigth:'0%', opacity:'0.8'}, 1000);
		
		setTimeout(function(){
			$(".bigPictureWrapper").css("display", "none");
		}, 1000);
	});	
	// 첨부파일 클릭시 이미지 화면 표현 및 일반파일 다운로드
	
	// 첨부파일 삭제
	$(".uploadResult ul").on("click", "button", function(e){
		if(${minfo.username eq vo.register || isAdmin}){
			if(confirm("첨부파일을 삭제하시겠습니까?")){
				$(this).closest("li").remove();
				
				return;
			}else{
				return;
			}
		}else{
			return;
		}
	
	});
	// 첨부파일 삭제
	
	
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
	
	// 첨부파일 추가 및 표현
	var csrfHeaderName = "${_csrf.headerName}";
	var csrfTokenValue = "${_csrf.token}";
	
	$("input[type='file']").on("change",function(e){
		if(${minfo.username eq vo.register || isAdmin}){
			var formData = new FormData();
			var inputObject = $(this);
			var files = inputObject[0].files;
			
			for(var i=0; i<files.length; i++){
				if(checkExtension(files[0].name, files[0].size) == false){
					return;
				}
				formData.append("uploadFiles", files[i]);
			}
			
			
			$.ajax({
				type:"post",
				url:"/attach/uploadFiles",
				processData:false,
				contentType:false,
				
				data:formData,
				beforeSend:function(xhr){
					xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
				},
				
				dataType:"json",
				
				success:function(result){
					showUploadResult(result);
				}
			});
			
		}else{
			return;
		}
	});
	
	function showUploadResult(resultArr){
		if(resultArr.length==0){
			return;
		}
		
		var str = "";
		
		$(resultArr).each(function(i, obj){
			if(obj.image){
				var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
				
				str += "<li data-uploadpath='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-filetype='i'>";
				str += "<div><span>"+obj.fileName + "</span>";
				str += "<button type='button' data-file='"+fileCallPath+"' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
				str += "<img src='/attach/displayFiles?fileName="+fileCallPath+"' style='width:50px; height:50px;'>";
				str += "</div></li>"				
			}else{
				var fileCallPath = encodeURIComponent(obj.uploadPath + "/_" + obj.uuid + "_" + obj.fileName);
				
				str += "<li data-uploadpath='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-filetype='f'>";
				str += "<div><span>"+obj.fileName + "</span>";
				str += "<button type='button' data-file='"+fileCallPath+"' data-type='file' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
				str += "<img src='/resources/img/attach_images.png' style='width:50px; height:50px;'>";
				str += "</div></li>"				
			}
		});
		
		$(".uploadResult ul").append(str);
			
		
	}
	// 첨부파일 추가 및 표현
	////////////////// file /////////////////////////
</script>

				