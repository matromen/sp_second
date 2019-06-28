package com.bgkim.security;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml", "file:src/main/webapp/WEB-INF/spring/security-context.xml"})
@Log4j
public class MemberTests {
	@Setter(onMethod_ = @Autowired)
	private DataSource ds;
	
	@Setter(onMethod_ = @Autowired)
	private PasswordEncoder passEncoder;
	
	
//	  @Test public void testInsert() { Connection conn = null; PreparedStatement
//	  pstmt = null;;
//	  
//	  String sql =
//	  "insert into sp_member (userid, userpw, username) values (?,?,?)";
//	  
//	  try {
//	  
//	  conn = ds.getConnection(); pstmt = conn.prepareStatement(sql);
//	  
//	  pstmt.setString(1, "admin"); pstmt.setString(2, passEncoder.encode("123"));
//	  pstmt.setString(3, "admin");
//	  
//	  pstmt.executeUpdate();
//	  
//	  }catch(Exception e) { e.printStackTrace(); }finally {
//	  if(pstmt!=null){try{pstmt.close();}catch(Exception e) {}}
//	  if(conn!=null){try{conn.close();}catch(Exception e) {}} } }
//	 
//	
	
	@Test
	public void testAuthInsert() {
		Connection conn = null;
		PreparedStatement pstmt = null;;
		
		String sql = "insert into sp_member_auth (userid, auth) values (?,?)";
		
		try {
			
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, "admin");
			pstmt.setString(2, "staff");
			
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null){try{pstmt.close();}catch(Exception e) {}}
			if(conn!=null){try{conn.close();}catch(Exception e) {}}
		}
	}	
}
