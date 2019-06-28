package com.bgkim.service.board;

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
	public SpBoardVO getDetail(long seq) {
		return mapper.detailSelect(seq);
	}

	@Override
	@Transactional
	public int updatePro(SpBoardVO vo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUser user = (CustomUser)authentication.getPrincipal();
		
		
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
			log.info("not admin..............................");
			String register = mapper.registerSelect(vo.getSeq());

			if(!register.equals(user.getUsername())) throw new AccessDeniedException(user.getUsername());
		}
		log.info("admin.............................");
		
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
		return 1;
	}

	@Override
	@Transactional
	public int deletePro(SpBoardVO vo) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUser user = (CustomUser)authentication.getPrincipal();
		
	
		if(!user.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
			log.info("not admin..............................");
			String register = mapper.registerSelect(vo.getSeq());

			if(!register.equals(user.getUsername())) throw new AccessDeniedException(user.getUsername());
		}
		
		log.info("admin.............................");
		
		attachMapper.seqAllAttachDelete(vo.getSeq());
		mapper.deleteDelete(vo.getSeq());
		
		return 1;
	}
	
	@Override
	public List<SpBoardAttachVO> getAttachViewList(long seq){
		return attachMapper.attachViewListSelect(seq);
	}
}
