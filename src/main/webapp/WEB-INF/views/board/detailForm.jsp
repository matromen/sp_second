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
									
				
					<div class="form-group">
						<label>제목</label>
						<input class="form-control" value="${vo.title}">
					</div>
					
					<div class="form-group">
						<label>내용</label>
						<input class="form-control" value="${vo.content}">
					</div>						
					
					<div class="form-group">
						<label>작성자</label>
						<input class="form-control" value='${vo.userName}'>
					</div>
						
					<!-- file attach -->
					<div class="panel-heading">Files</div>
					<div class="panel-body">
						<div class="uploadResult">
							<ul></ul>
						</div>
					</div>
					<!-- file attach -->	

					<!-- listForm 시작 -->
							
					<form id="listForm" method="get" action="/board/list">	
						<input type="hidden" name="pageNum" value="${pbean.pageNum}">	
						<input type="hidden" name="pageAmount" value="${pbean.pageAmount}">
						<input type="hidden" name="searchType" value="${pbean.searchType}">
						<input type="hidden" name="searchValue" value="${pbean.searchValue}">
						<input type="hidden" name="searchValue0" value="${pbean.searchValue0}">

						
						<button type="button" id="updateFormButton" class="btn btn-default">수정하기</button>
						<button type="button" id="listButton" class="btn btn-default">리스트</button>						
											
					</form>
				
					<!-- listForm 끝 -->
									
				</div>
	
            </div>
          </div>

        </div>
        <!-- /.container-fluid -->

<%@ include file="../include/bottom.jsp" %>

<script>
	$(document).ready(function(e){
		try{
			var result = '<c:out value="${result}"/>';
			if(result=="")result=false;
			if(result){
				alert("정상 등록 되었습니다.")
			}	
		}catch(e){}
	});


	$("#updateFormButton").on("click", function(e){
		$("#listForm").append("<input type='hidden' name='seq' value='${vo.seq}'>");
		
		$("#listForm").attr("action", "/board/updateForm");
		$("#listForm").submit();
	});
	
	$("#listButton").on("click", function(e){
		$("#listForm").attr("action", "/board/list");
		$("#listForm").submit();
	});	
	
	////////////////// file /////////////////////////
	// 웹페이지 로딩 후 첨부파일 표현
	$(document).ready(function(e){
		(
			function(){
				var seq = "${vo.seq}";
				
				$.get("/attach/viewAttachList", {seq, seq}, function(arr){
					var str = "";
					
					$(arr).each(function(i, obj){
						if(obj.fileType == "i"){
							var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
							
							str += "<li data-uuid='"+obj.uuid+"' data-uploadpath='"+obj.uploadPath+"' data-filename='"+obj.fileName+"' data-filetype='"+obj.fileType+"'>";
							str += "<div><img src='/attach/displayFiles?fileName="+fileCallPath+"'>";
							str += "</div></li>";
						}else{
							str += "<li data-uuid='"+obj.uuid+"' data-uploadpath='"+obj.uploadPath+"' data-filename='"+obj.fileName+"' data-filetype='"+obj.fileType+"'>";
							str += "<div><img src='/resources/img/attach_images.png' style='width:50px; height:50px;'><span>"+obj.fileName+"</span>";
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
	$(".uploadResult ul").on("click", "li", function(e){
		var li_Object = $(this);
		var fileCallPath = encodeURIComponent(li_Object.data('uploadpath') + "/" + li_Object.data('uuid') + "_" + li_Object.data('filename'));
		
		if(li_Object.data('filetype') == "i"){
			showImage(fileCallPath.replace(new RegExp(/\\/g), "/"));
		}else{
			self.location="/attach/downloadFile?fileName="+fileCallPath;
		}
	});
	// 첨부파일 클릭시 이미지 화면 표현 및 일반파일 다운로드
	
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
	//////////////////file /////////////////////////
</script>

				