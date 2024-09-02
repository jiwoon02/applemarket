package com.apple.common.vo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;

@Data
public class PageResponseDTO<E> {

	private List<E> dtoList; // 페이징된 데이터 리스트
	private List<Integer> pageNumList; // 페이지 번호 리스트
	private PageRequestDTO pageRequestDTO; // 요청 정보 (검색 조건 포함)
	private boolean prev; // 이전 페이지 존재 여부
	private boolean next; // 다음 페이지 존재 여부
	private int totalCount; // 전체 레코드 수
	private int prevPage; // 이전 페이지 번호
	private int nextPage; // 다음 페이지 번호
	private int totalPage; // 전체 페이지 수
	private int current; // 현재 페이지 번호

	@Builder(builderMethodName = "withAll")
	public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
		this.dtoList = dtoList;
		this.pageRequestDTO = pageRequestDTO;
		this.totalCount = (int) totalCount;

		int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
		int start = end - 9;
		int realEnd = (int) (Math.ceil(totalCount / (double) pageRequestDTO.getSize()));

		if (realEnd < end) {
			end = realEnd;
		}

		this.prev = start > 1;
		this.next = end < realEnd;
		this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
		this.current = pageRequestDTO.getPage();
		this.prevPage = start - 1;
		this.nextPage = end + 1;
		this.totalPage = realEnd;
	}
}
