package com.bgkim.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bgkim.domain.board.SpBoardAttachVO;
import com.bgkim.mapper.SpBoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Component
@Log4j

public class FileCheckTask {
	@Setter(onMethod_ = @Autowired)
	private SpBoardAttachMapper attachMapper;
	
	public String getFoldYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		
		String str = sdf.format(cal.getTime());
		
		return str.replace("-", File.separator);
		
	}
	
	
	@Scheduled(cron = "0 0 2 * * * ")
	public void checkFile() throws Exception{
		List<SpBoardAttachVO> fileList = attachMapper.getYesterDayFiles();
		
		List<Path> fileListPath = fileList.stream().map(vo->Paths.get("c:\\upload",  vo.getUploadPath(), vo.getUuid()+"_"+vo.getFileName())).collect(Collectors.toList());
		fileList.stream().filter(vo-> vo.getFileType().equals("i") == true)
		.map(vo->Paths.get("c:\\upload",  vo.getUploadPath(), "s_"+vo.getUuid()+"_"+vo.getFileName())).forEach(p -> fileListPath.add(p));
		
		
		//File yRealDir = Paths.get("c:\\upload", getFoldYesterDay()).toFile();
		File yRealDir = new File("C:\\upload", getFoldYesterDay());
		File[] rmFiles = yRealDir.listFiles(file -> fileListPath.contains(file.toPath()) == false);
		
		for(File rmFile : rmFiles) {
			if(rmFile.exists()) {
				rmFile.delete();
			}
		}
				
	}
}
