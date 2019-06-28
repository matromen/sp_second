<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>    


<%@ include file="../../include/top.jsp" %>

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
            
				
				<p>principal(CustomUser) : <sec:authentication property="principal"/> </p>
				<p>principal : <sec:authentication property="principal.username"/> </p>
				<p>principal : <sec:authentication property="principal.password"/> </p>
				<p>principal : <sec:authentication property="principal.authorities"/> </p>	
				<p>MemberVO : <sec:authentication property="principal.memberVO"/> </p>
				<p>MemberVO : <sec:authentication property="principal.memberVO.userid"/> </p>
				<p>MemberVO : <sec:authentication property="principal.memberVO.userpw"/> </p>
				<p>MemberVO : <sec:authentication property="principal.memberVO.username"/> </p>
				<p>MemberVO : <sec:authentication property="principal.memberVO.authList"/> </p>



            </div>
          </div>

        </div>
        <!-- /.container-fluid -->

<%@ include file="../../include/bottom.jsp" %>