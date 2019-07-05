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
			pbean.setTransSearchValue0(memberMapper.useridSelect(pbean.getSearchValue0())); //�̸����� ���� id list�� ������ �ͼ� pbean�� ����.
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
			
			int cgubun = isRegisterSeqSpBoardCount(seq, userId); //������ �� �� ��� Ȯ��, ������ ������ �並 �ߴ��� 
			
			if(cgubun<=0) { 
				addRegisterSeqSpBoardCount(seq, userId);  // ó�� ������ �� �� ��� ���
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
		
		//controller : ���� id �� jsp���� ���޵� �Ķ���� ������ ��, @PreAuthorize("principal.username == #vo.register || hasAuthority('admin')")
		//service : ���� db ����� ���� ������ ���� ������ �ͼ� ���� id�� ��
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
		
		// ���� ���� �� ���� ���� ���� ������  ���� ����
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
		
		// ���� �����ϰ� ���� �������� ���� ���Ͽ� �������������� �� �������� ���ٸ� ȭ��� ������ ���� �ش� ������ ����
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
		// ���� �����ϰ� ���� �������� ���� ���Ͽ� �������������� �� �������� ���ٸ� ȭ��� ������ ���� �ش� ������ ����
		
		deleteFiles(deleteAttachList);
		
		
		return 1;
	}

	@Override
	@Transactional
	public int deletePro(SpBoardVO vo) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//CustomUser user = (CustomUser)authentication.getPrincipal();
		
		//controller : ���� id �� jsp���� ���޵� �Ķ���� ������ ��, @PreAuthorize("principal.username == #vo.register || hasAuthority('admin')")
		//service : ���� db ����� ���� ������ ���� ������ �ͼ� ���� id�� ��	
		
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
		
		// ������ �ϰ� ������ ���� ���� ����
		List<SpBoardAttachVO> attachList = getAttachViewList(vo.getSeq());
		
		attachMapper.seqAllAttachDelete(vo.getSeq());
		replyMapper.remReplyAllDelete(vo.getSeq());
		mapper.remSpBoardCountAllDelete(vo.getSeq());
		
		mapper.deleteDelete(vo.getSeq());
		
		
		// ������ �ϰ� ����
		deleteFiles(attachList);
		
		return 1;
	}
	
	@Override
	public List<SpBoardAttachVO> getAttachViewList(long seq){
		return attachMapper.attachViewListSelect(seq);
	}
	
	
	// collection ���� ����
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
