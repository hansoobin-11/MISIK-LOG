package com.milky.misiklog.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ErrorRestControllerAdvice {
	
	@ExceptionHandler(value = {TargetNotFoundException.class, NoResourceFoundException.class})
	public ResponseEntity<String> notFound(Exception e) {
		return ResponseEntity.notFound().build();//404
	}
	@ExceptionHandler(UnauthorizationException.class)
	public ResponseEntity<String> unauthorize(UnauthorizationException e) {
		return ResponseEntity.status(401).build();
	}
	
	@ExceptionHandler(NeedPermissionException.class)
	public ResponseEntity<String> needPermission(NeedPermissionException e) {
		return ResponseEntity.status(403).build();
	}
	
	@ExceptionHandler(ReservationConflictException.class)
	public ResponseEntity<String> reservationConflict(ReservationConflictException e){
		return ResponseEntity.status(409).build();
	}
	
	//나머지 모든 예외
	//- 사용자에게는 별거 아닌 것처럼, 개발자에게는 아주 상세한 정보를 남긴다
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> all(Exception e) {
		//e.printStackTrace();
		log.error("예외 발생", e);
		return ResponseEntity.internalServerError().build();
	}
}