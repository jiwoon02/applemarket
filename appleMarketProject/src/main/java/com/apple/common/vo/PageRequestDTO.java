package com.apple.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
	
	//페이지번호
	@Builder.Default
	private int page = 1;
	
	//한페이지에 10개씩 보여주기
	@Builder.Default
	private int size = 16;
	
}
