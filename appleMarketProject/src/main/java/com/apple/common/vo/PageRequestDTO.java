package com.apple.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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

	//검색조건
	private String search = "";
	private String keyword = "";

	private LocalDate startDate;
	private LocalDate endDate;
}
