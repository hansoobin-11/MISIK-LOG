package com.milky.misiklog.configuration;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class EmptyStringToNullDeserializer extends JsonDeserializer<String>{
	
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		//1. 해석중인 값을 문자열로 불러온다.
		String value = p.getText();
		
		
		//2. 원래 null이었거나 불필요한 공백을 제거하고 나니 비어있는 경우 null로 치환
		if(value == null || value.strip().isEmpty()) {
			return null;
		}
		
		//3. 나머지 값들은 원래대로 반환
		return value;
	}
}
