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
              <h6 class="m-0 font-weight-bold text-primary">admin Page ${result}11</h6>
            </div>
            <div class="card-body">
            
				<div class="table-responsive">
					
					<!-- searchForm 시작 -->
					<div class="row">
						<div class="col-lg-12">
							<form role="form" id="searchForm" action="/board/list" method="get">
								<select name="searchType">
									<option value="999" ${pbean.searchType=='999' ? 'selected':''}>전체</option>
									<option value="000" ${pbean.searchType=='000' ? 'selected':''}>제목</option>
									<option value="111" ${pbean.searchType=='111' ? 'selected':''}>내용</option>
									<option value="000,111" ${pbean.searchType=='000,111' ? 'selected':''}>제목+내용</option>
								</select>
								
								<input type="text" name="searchValue" value="${pbean.searchValue}">
								 &nbsp;
								작성자 : <input type="text" name="searchValue0" value="${pbean.searchValue0}">
								<button type="submit">찾기</button>
								 
								<input type="hidden" name="pageNum" value="${pbean.pageNum}">
								<input type="hidden" name="pageAmount" value="${pbean.pageAmount}">
							</form>
						</div>
					</div>
					<!-- searchForm 끝 -->
					
					<!-- list 시작 -->
					<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
						<thead>
							<tr>
								<th>순번</th>
								<th>제목</th>
								<th>작성자</th>
								<th>작성일</th>
								<th>수정일</th>
								<th>조회수</th>
							</tr>
						</thead>
						
						<tbody>
							<c:set var="number" value="${listPageConfig.pageNumber}"/>
							
							<c:forEach var="vo" items="${voList}">
							<tr>
								<td>
									<c:out value="${number}"/>
									<c:set var="number" value="${number-1}"/>
								</td>
								<td><a class="move" href="${vo.seq}"><c:out value="${vo.title}"/></a></td>
								<td><c:out value="${vo.userName}" /></td>
								<td><fmt:formatDate  pattern="yyyy:MM:dd" value="${vo.rdate}"/></td>
								<td><fmt:formatDate  pattern="yyyy:MM:dd" value="${vo.udate}"/></td>								
							</tr>
							</c:forEach>
							
							
						</tbody>
					</table>
					<!-- list 끝 -->		
					
					<!-- paging 시작 -->
					<div class="pull-right">
						<ul class="pagination">
							<c:if test="${listPageConfig.prev}">
							<li class="paginate_button previous"><a href="${listPageConfig.startPage-1}">이전</a></li>
							</c:if>
							
							<c:forEach var="paginum" begin="${listPageConfig.startPage}" end="${listPageConfig.endPage}">
							<li class="paginate_button"><a href="${paginum}">${paginum}</a></li>
							</c:forEach>
							
							<c:if test="${listPageConfig.next}">
							<li class="paginate_button next"><a href="${listPageConfig.endPage+1}">다음</a></li>
							</c:if>
						</ul>
					</div>
					<!-- paging 끝 -->
					
					
					<div class="row">
						<div class="col-lg=12" style="text-align:center;">
							<button id="writeButton" type="button" class="btn btn-primary btn-lg">등록하기</button>
						</div>
					</div>
					
					<form role="form" id="listForm" method="get" action="/board/list">
						<input type="hidden" name="pageNum" value="${pbean.pageNum}">
						<input type="hidden" name="pageAmount" value="${pbean.pageAmount}">
						<input type="hidden" name="searchType" value="${pbean.searchType}">
						<input type="hidden" name="searchValue" value="${pbean.searchValue}">
						<input type="hidden" name="searchValue0" value="${pbean.searchValue0}">
					</form>			
					
					
					
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
	
	$("#searchForm button").on("click", function(e){
		if(!$("#searchForm").find("option:selected")){
			alert("검색종류가 없습니다.");
			return false;
		}
		
		if($.trim( $("#searchForm").find("input[name='searchValue']").val() ) ==""){
			alert("검색어를 넣어 주세요.")
			return false;
		}
		
		$("#searchForm").find("input[name='pageNum']").val("1");
		
		$("searchForm").submit();
	});
	
	
	$(".paginate_button a").on("click", function(e){
		e.preventDefault();
		
		$("#listForm").find("input[name='pageNum']").val($(this).attr("href"));
		$("#listForm").submit();
	});
	
	$("#writeButton").on("click", function(e){
		$("#listForm").attr("action", "/board/writeForm");
		$("#listForm").submit();
	});
	
	$(".move").on("click", function(e){
		e.preventDefault();
		
		$("#listForm").append("<input type='hidden' name='seq' value='"+$(this).attr('href')+"'>");
		$("#listForm").attr("action", "/board/detailForm");
		
		$("#listForm").submit();
	});
</script>