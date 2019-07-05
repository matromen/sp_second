package com.bgkim.service.board;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bgkim.domain.board.SpBoardAttachVO;
import com.bgkim.domain.board.SpBoardParam;
import com.bgkim.domain.board.SpBoardVO;
import com.bgkim.mapper.MemberMapper;
import com.bgkim.mapper.SpBoardAttachMapper;
import com.bgkim.mapper.SpBoardMapper;
import com.bgkim.mapper.SpBoardReplyMapper;
import com.bgkim.security.CustomUser;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j

public class SpBoardServiceImpl implements SpBoardService{
	@Setter(onMethod_ = @Autowired)
	private SpBoardMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private MemberMapper memberMapper;
	
	@Setter(onMethod_ = @Autowired)
	private SpBoardAttachMapper attachMapper;
	
	@Setter(onMethod_ = @Autowired)
	private SpBoardReplyMapper replyMapper;
	
	@Override
	public int getCount(SpBoardParam pbean) {
		if(!(pbean.getSearchValue0().trim()).isEmpty()) {
			pbean.setTransSearchValue0(memberMapper.useridSelect(pbean.getSearchValue0())); //이름으로 관련 id list를 가지고 와서 pbean에 셋함.
		}
		
		return mapper.totalSelect(pbean);
	}
	
	@Override
	public List<SpBoardVO> getList(SpBoardParam pbean){
		return mapper.listSelect(pbean);
	}

	@Override
	@Transactional
	public int writePro(SpBoardVO vo) {

		mapper.writeInsert(vo);
		
		if(vo.getAttachList()!=null) {
			if(vo.getAttachList().size()>0) {
				
				vo.getAttachList().forEach(attach -> {
														attach.setSeq(vo.getSeq());
														attachMapper.attachInsert(attach);
													});
				
			}
		}
		return 1;
	}

	@Override
	public SpBoardVO getDetail(long seq, char flag) {
		
		if(flag == 'd') {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String userId = authentication.getName();
			
			int cgubun = isRegisterSeqSpBoardCount(seq, userId); //페이지 뷰 일 경우 확인, 기존에 페이지 뷰를 했는지 
			
			if(cgubun<=0) { 
				addRegisterSeqSpBoardCount(seq, userId);  // 처음 페이지 뷰 일 경우 기록
				updateSpBoardCount(seq);
			}
		}
		
		
		return mapper.detailSelect(seq);
	}

	@Override
	@Transactional
	public int updatePro(SpBoardVO vo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUser user = (CustomUser)authentication.getPrincipal();
		
		//controller : 현재 id 와 jsp에서 전달된 파라미터 값으로 비교, @PreAuthorize("principal.username == #vo.register || hasAuthority('admin')")
		//service : 실제 db 사용자 변경 직전의 값을 가지고 와서 헌재 id와 비교
//		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
//			log.info("not admin..............................");
//			String register = mapper.registerSelect(vo.getSeq());
//
//			if(!register.equals(user.getUsername())) throw new AccessDeniedException(user.getUsername());
//		}
		
	
		if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
			log.info("not admin.....................");
			
			String register = mapper.registerSelect(vo.getSeq());
			
			if(!register.equals(authentication.getName())) {
				throw new AccessDeniedException(authentication.getName());
			}
		}
		
		log.info("admin.............................");
		
		// 삭제 직전 현 정보 수집 기존 실파일  삭제 위해
		List<SpBoardAttachVO> attachList = getAttachViewList(vo.getSeq());
		
		mapper.updateUpdate(vo);

		attachMapper.seqAllAttachDelete(vo.getSeq());
		
		if(vo.getAttachList() != null) {
			if(vo.getAttachList().size()>0) {
				
				vo.getAttachList().forEach(attach -> {
														attach.setSeq(vo.getSeq());
														attachMapper.attachInsert(attach);
													});
			}
		}
		
		// 이전 실파일과 현재 수정파일 정보 비교하여 수정파일정보에 기 실파일이 없다면 화면상 삭제로 보아 해당 실파일 삭제
		List<SpBoardAttachVO> deleteAttachList = new ArrayList<>();
		int flag = 0;
		for(SpBoardAttachVO attachD : attachList) {
			flag = 0;
			for(SpBoardAttachVO attachN : vo.getAttachList()) {
				if(attachD.getUuid().equals(attachN.getUuid())){
					flag += 1;
				}
			}
			
			if(flag == 0) deleteAttachList.add(attachD);		
		}
		// 이전 실파일과 현재 수정파일 정보 비교하여 수정파일정보에 기 실파일이 없다면 화면상 삭제로 보아 해당 실파일 삭제
		
		deleteFiles(deleteAttachList);
		
		
		return 1;
	}

	@Override
	@Transactional
	public int deletePro(SpBoardVO vo) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//CustomUser user = (CustomUser)authentication.getPrincipal();
		
		//controller : 현재 id 와 jsp에서 전달된 파라미터 값으로 비교, @PreAuthorize("principal.username == #vo.register || hasAuthority('admin')")
		//service : 실제 db 사용자 변경 직전의 값을 가지고 와서 헌재 id와 비교	
		
//		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
//			log.info("not admin..............................");
//			String register = mapper.registerSelect(vo.getSeq());
//
//			if(!register.equals(user.getUsername())) throw new AccessDeniedException(user.getUsername());
//		}
				
		if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
			log.info("not admin.............................");
			
			String register = mapper.registerSelect(vo.getSeq());
			
			if(!register.equals(authentication.getName())) {
				throw new AccessDeniedException(authentication.getName());
			}
		}
		
		log.info("admin.............................");
		
		// 실파일 일괄 삭제를 위해 정보 수집
		List<SpBoardAttachVO> attachList = getAttachViewList(vo.getSeq());
		
		attachMapper.seqAllAttachDelete(vo.getSeq());
		replyMapper.remReplyAllDelete(vo.getSeq());
		mapper.remSpBoardCountAllDelete(vo.getSeq());
		
		mapper.deleteDelete(vo.getSeq());
		
		
		// 실파일 일괄 삭제
		deleteFiles(attachList);
		
		return 1;
	}
	
	@Override
	public List<SpBoardAttachVO> getAttachViewList(long seq){
		return attachMapper.attachViewListSelect(seq);
	}
	
	
	// collection 파일 삭제
	public void deleteFiles(List<SpBoardAttachVO> attachList) {
		
		if(attachList != null) {
			if(attachList.size()>0) {
				log.info("delete attachList files............................");
				
				attachList.forEach(attach -> {
					try {
						Path path = Paths.get("c:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());
						
						Files.deleteIfExists(path);
						log.info(path.toString());
						
						if(Files.probeContentType(path).startsWith("image")) {
							Path thumbnail = Paths.get("c:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
							
							Files.deleteIfExists(thumbnail);
							log.info(thumbnail.toString());
							
						}
					}catch(Exception e) {
						log.error(e.getMessage());
					}
				});
				
			}
		}
	}	
	
	public int isRegisterSeqSpBoardCount(long seq, String userId) {
		return mapper.getCountRegisterSeq(seq, userId);
	}
	
	public void addRegisterSeqSpBoardCount(long seq, String userId) {
		mapper.writeSpBoardCountInsert(seq, userId);
	}
	
	public void updateSpBoardCount(long seq) {
		
	}
}
