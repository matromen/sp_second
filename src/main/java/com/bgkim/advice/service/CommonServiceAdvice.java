package com.bgkim.advice.service;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.bgkim.domain.board.SpBoardVO;

import lombok.extern.log4j.Log4j;

@Component
@Aspect
@Log4j

/**
 * 
 * @author �躴��
 *
 *	Aspect pure java�� @Service�� ������� advice�� ��.
 * 	��� �޼ҵ�(JoinPoint)�� pointCut(aspectj����) �Ͽ� advice��.
 * 	controller�� ControllerAdvice���� ��.
 */
public class CommonServiceAdvice {
	
	@Around("execution(* com.bgkim.service.board.SpBoardServiceImpl.writePro(..))")
	public Object boardWriteProAdvice(ProceedingJoinPoint pjp) throws Throwable {
		log.info("Target : " + pjp.getTarget());
		log.info("Args : " + Arrays.toString(pjp.getArgs()));
		
		
		SpBoardVO args = (SpBoardVO)pjp.getArgs()[0];
		
		//invoke method
		Object result = null;
		
		
		try {
			if(args.getTitle() == null || args.getContent() == null) {
				throw new IllegalArgumentException();
			}else {
				if(args.getTitle().isEmpty() || args.getContent().isEmpty()) {
					throw new IllegalAccessException();
				}
			}
			
			result = pjp.proceed();
		}catch(Throwable e) {
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}
	
	@Around("execution(* com.bgkim.service.board.SpBoardServiceImpl.updatePro(..))")
	public Object boardUpdateProAdvice(ProceedingJoinPoint pjp) throws Throwable {
		log.info("Target : " + pjp.getTarget());
		log.info("Args : " + Arrays.toString(pjp.getArgs()));
		
		SpBoardVO args = (SpBoardVO)pjp.getArgs()[0];
		
		//invoke method
		Object result = null;
		
		try {
			if(args.getTitle() == null || args.getContent() == null) {
				throw new IllegalArgumentException();
			}
			
			if(args.getTitle().isEmpty() || args.getContent().isEmpty()) {
				throw new IllegalArgumentException();
			}
			
			result = pjp.proceed();
		}catch(Throwable e) {
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}
	
	
	@Around("execution(* com.bgkim.service.board.SpBoardServiceImpl.deletePro(..))")
	public Object boardDeleteProAdvice(ProceedingJoinPoint pjp) throws Throwable {
		log.info("Target : " + pjp.getTarget());
		log.info("Args : " + Arrays.toString(pjp.getArgs()));
		
		SpBoardVO args = (SpBoardVO)pjp.getArgs()[0];

		//invoke method
		Object result = null;
		
		try {
			if(args.getSeq() == 0) {
				throw new IllegalArgumentException();
			}
			
			
			result = pjp.proceed();
		}catch(Throwable e) {
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}	
}
