package com.milky.misiklog.error;

//사용처 : 대상이 없어서 더 이상 진행할 수 없는 경우 사용하는 커스텀 예외
public class TargetNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public TargetNotFoundException() {
		super();
	}
	public TargetNotFoundException(String message) {
		super(message);
	}
}