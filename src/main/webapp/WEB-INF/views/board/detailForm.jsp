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



					<!-- Modal for reply -->
					<div class="modal fade" id="replyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
									<h4 class="modal-title" id="myModalLabel">댓글등록</h4>
								</div>
								
								<div class="modal-body">
									<div class="form-group">
										<label>내용</label>
										<input class="form-control" name="content" value="">
									</div>
									
									<div class="form-group">
										<label>작성자</label>
										<input class="form-control" name="register" value="${mInfo.memberVO.username}(${mInfo.username})" readonly="readonly">
									</div>
								</div>
								
								
								<div class="modal-footer">
									<button id="replyModalModBtn" type="button" class="btn btn-warning">Modify</button>
									<button id="replyModalRemoveBtn" type="button" class="btn btn-danger">Remove</button>
									<button id="replyModalRegisterBtn" type="button" class="btn btn-primary">Register</button>
									<button id="replyModalCloseBtn" type="button" class="btn btn-default">Close</button>
								</div>
							</div>
						</div>
					</div>
					<!-- Modal for reply -->
						
					<!-- reply -->
					<div class="row">
						<div class="col-lg-12">
							<div class="panel">
							
								<div class="panel-heading">
									<i class="fa fa-comments fa-fw">댓글</i>
									<button id="addReplyBtn" class="btn btn-primary btn-xs pull-right">등록</button>
								</div>
								
								<div class="panel-body">
									<ul class="chat">
									
									</ul>
								</div>
								
								<div class="panel-footer">
									<ul class="pagination pull-right">
									
									</ul>
								</div>
							</div>
						</div>
					</div>
					<!-- reply 끝 -->	
					
					
					
					
					
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

<script type="text/javascript" src="/resources/js/reply.js"></script>

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
				
				$.get("/attach/viewAttachList", {seq: seq}, function(arr){
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
	
	
	
	
	
	
	
	
	//////////////////reply /////////////////////////
	let reply_pageNum = 1;
	var replyUL = $(".chat");
	
	//페이지 로딩 후 리스트 표현
	$(document).ready(function(e){
		showReplyList(1);
	});
	
	// 페이지 리스트 표현
	function showReplyList(page){
		if(page == -1){
			reply_pageNum = 1;
		}else{
			reply_pageNum = page;
		}
		
		
		var pobj = {rseq : "${vo.seq}", rpage:page};
		
		replyJsService.replyList(pobj, function(result){
			var list = result.listReply;
			
			if(list == null  || list.length == 0){
				replyUL.html("");
				
				showReplyListPage(result.listReplyPageConfig);
				
				return;
			}
			
			var str = "";
			
			$(result.listReply).each(function(i, obj){
				str += "<li class='left clearfix' data-rseq='"+obj.rseq+"' data-register='"+obj.register+"'>";
				str += "<div><div class='header><strong class='primary-font'>"+obj.userName+"("+obj.register+")</strong></div>";
				str += "<small class='pull-right text-muted'>" + obj.regDate + "</small></div>";
				str += "<p>" + obj.content + "</p></li>"
			});
			
			
			replyUL.html(str);
			
			showReplyListPage(result.listReplyPageConfig);
		});
	}
	
	//reply페이지 네비게이션
	function showReplyListPage(listReplyPageConfig){
		var str = "";
		
		if(listReplyPageConfig.prev){
			str += "<li call='page-item'><a class='page-link' href='"+(listReplyPageConfig.startPage-1)+"'>prev</a></li>";
		}

		for(var i=listReplyPageConfig.startPage; i<=listReplyPageConfig.endPage; i++){
			var active = reply_pageNum == i ? "active" : "";
			
			str += "<li call='page-item' "+active+"><a class='page-link' href='"+i+"'>"+i+"</a></li>";
		}
		
		if(listReplyPageConfig.next){
			str += "<li call='page-item'><a class='page-link' href='"+(listReplyPageConfig.endPage+1)+"'>next</a></li>";
		}
		
		$(".panel-footer .pagination").html(str);
	}
	
	// 네비게이션 클릭시 페이지 이동
	$(".panel-footer").on("click", "li a", function(e){
		e.preventDefault();
		
		showReplyList($(this).attr("href"));
	});
	
	
	
	
	//////  reply modal ///////
	
	
	var csrfHeaderName = "${_csrf.headerName}";
	var csrfTokenValue = "${_csrf.token}";
	
	// modal창 뛰우기
	$("#addReplyBtn").on("click", function(){
		$("#replyModal .modal-title").html("댓글등록");
		$("#replyModal").find("button").hide();
		$("#replyModal").find("button[id='replyModalRegisterBtn']").show();
		$("#replyModal").find("button[id='replyModalCloseBtn']").show();
		$("#replyModal").find("input[name='register']").val( "${mInfo.memberVO.username}(${mInfo.username})" );
		$("#replyModal").find("input[name='content']").val("");
		
		$("#replyModal").modal("show");
	});
	
	// modal창  등록하기
	$("#replyModalRegisterBtn").on("click", function(e){
		var replyJson = {content : $("#replyModal").find("input[name='content']").val(), register: "${mInfo.username}", seq : "${vo.seq}"};
		
		
		replyJsService.replyWritePro(replyJson, function(result){
			$("#replyModal").find("input[name='content']").val("");
			$("#replyModal").modal("hide");
			
			showReplyList(1);
		});
		
	});
	
	// modal창 뛰우기 수정/삭제 하기 위해
	$(replyUL).on("click", "li", function(e){
		if($(this).data("register") != "${mInfo.username}" && ${!isAdmin}){
			return;
		}
		
		var str = "";
		
		replyJsService.replyUpdateForm($(this).data("rseq"), function(result){
			$("#replyModal .modal-title").html("댓글수정");
			$("#replyModal").find("input[name='content']").val(result.content);
			$("#replyModal").find("input[name=register]").val( result.userName + "(" + result.register + ")");
			
			
			str += "<div class='form-group'>";
			str += "<label>최종 등록일</label>";
			str += "<input class='form-control' value='"+result.updateDate+"' readonly='readonly'>";
			str += "</div>";
			
			$("#replyModal .modal-body").append(str);
			
			$("#replyModal").find("button").show();
			$("#replyModal").find("button[id='replyModalRegisterBtn']").hide();
			
			$("#replyModal").data("rseq", result.rseq);
			$("#replyModal").data("seq", result.seq);
			$("#replyModal").data("register", result.register);
			
			
			$("#replyModal").modal("show");
		});
		
	});
	
	// modal창 수정하기
	var rpObj = $("#replyModal");
	
	$("#replyModalModBtn").on("click", function(e){
		var replyJson = {rseq:rpObj.data("rseq"), seq:rpObj.data("seq"), content:rpObj.find("input[name='content']").val(), register:rpObj.data("register")};
		
		replyJsService.replyUpdatePro(replyJson, function(result){
				
			$("#replyModal").find("input[name='content']").val("");
			$("#replyModal").modal("hide");			
			
			showReplyList(1);
		});

	});
	
	
	$("#replyModalRemoveBtn").on("click", function(e){
		var replyJson = {rseq:$("#replyModal").data("rseq"), seq:$("#replyModal").data("seq"), register:$("#replyModal").data("register")};
		
		replyJsService.replyDeletePro(replyJson, function(result){
			$("#replyModal").find("input[name='content']").val("");
			$("#replyModal").modal("hide");
			
			
			showReplyList(1);			
		});
	});
	
	
	$("#replyModalCloseBtn").on("click", function(e){
		rpObj.find("input[name='content']").val("");
		rpObj.find("input[name='register']").val("");
		rpObj.find("div[class='form-group']").eq(2).remove();
		
		rpObj.removeAttr("data-rseq");
		rpObj.removeAttr("data-seq");
		rpObj.removeAttr("data-register");
		
		rpObj.modal("hide");
	});
	
	// modal 창이 닫히면 발생하는 이벤트
	$("#replyModal").on("hidden.bs.modal", function(e){
		$("#replyModal").find("div[class='form-group']").eq(2).remove();
	});
	//////////////////reply /////////////////////////	
</script>

				