package com.bgkim.controller.board;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.bgkim.domain.board.SpBoardAttachDTO;
import com.bgkim.domain.board.SpBoardAttachVO;
import com.bgkim.service.board.SpBoardService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
@RequestMapping("/attach")

public class SpBoardFileController {
	
	@Setter(onMethod_ = @Autowired)
	private SpBoardService service;
	
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	private boolean checkImageType(File file) throws Exception{
		String contentType = Files.probeContentType(file.toPath());
		log.info("Files.probeContentType(file.toPath()) : " + contentType);
		
		return contentType.startsWith("image");
	}
	
	// 파일 업로드 및 파일 정보 표현
	@PostMapping(value="/uploadFiles", produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<SpBoardAttachDTO>> uploadAjaxPost(MultipartFile[] uploadFiles){
		log.info("uploadFiles controller");
		List<SpBoardAttachDTO> list = new ArrayList<>();
		
		String uploadFolder = "c:\\upload";
		String uploadFolderPath = getFolder();
		
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
		}
		
		
		
		for(MultipartFile multipartFile : uploadFiles) {
			String uploadFileName = multipartFile.getOriginalFilename();
			
			//IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("uploadFileName : " + uploadFileName);
			
			
			UUID uuid = UUID.randomUUID();
			
			try {
				SpBoardAttachDTO spBoardAttachDTO = new SpBoardAttachDTO();
				
				File saveFile = new File(uploadPath, uuid.toString()+"_"+uploadFileName);
				
				multipartFile.transferTo(saveFile);
				
				if(checkImageType(saveFile)) {
					spBoardAttachDTO.setImage(true);
					
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"+uuid.toString()+"_"+uploadFileName));
					Thumbnailator.createThumbnail(new FileInputStream(saveFile), thumbnail, 100, 100);
					
					thumbnail.close();
				}
				
				spBoardAttachDTO.setUploadPath(uploadFolderPath);
				spBoardAttachDTO.setUuid(uuid.toString());
				spBoardAttachDTO.setFileName(uploadFileName);
				
				list.add(spBoardAttachDTO);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	// 썸네일 이미지 표현 (등록 직후 및 뷰페이지 등 에서 사용)
	@GetMapping("/displayFiles")
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("/displayFiles : " + fileName);
		
		File file = new File("c:\\upload\\" + fileName);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type",  Files.probeContentType(file.toPath()));
			
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	// 단일 선택 파일 삭제
	@PostMapping(value="/deleteFile", produces= {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> deleteFile(String fileName, String type){
		File file;
		
		try {
			file = new File("c:\\upload\\"+ URLDecoder.decode(fileName, "UTF-8"));
			file.delete();
			
			if(type.equals("image")){
				String defaultFileName = file.getAbsolutePath().replace("s_", "");

				file = new File(defaultFileName);
				file.delete();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>("Delete SUCCESS", HttpStatus.OK);
	}
	
	// 보기 및 수정 페이지, 삭제 등 에서 파일 리스트 정보
	@GetMapping(value="/viewAttachList", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<SpBoardAttachVO>> viewAttachList(long seq){
		return new ResponseEntity<>(service.getAttachViewList(seq), HttpStatus.OK);
	}
	
	// 이미지 외 파일다운로드
	@GetMapping(value="/downloadFile", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public ResponseEntity<Resource> download(@RequestHeader("User-Agent") String userAgent, String fileName){
		Resource resource = new FileSystemResource("c:\\upload\\"+fileName);
		
		if(resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String resourceName = resource.getFilename();
		String resourceFileName = resourceName.substring(resourceName.indexOf("_")+1);
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			String downloadName = null;
			
			if(userAgent.contains("Trident")) {
				log.info("IE Browser");
				downloadName = URLEncoder.encode(resourceFileName, "UTF-8").replaceAll("\\", " ");
			}else if(userAgent.contains("Edge")) {
				log.info("Edge Browser");
				downloadName = URLEncoder.encode(resourceFileName, "UTF-8");
			}else {
				log.info("etc Browser");
				downloadName = new String(resourceFileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			headers.add("Content-Disposition", "attachment; filename=" + downloadName); 
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
				
	}
}
