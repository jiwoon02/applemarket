package com.apple.common.vo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;

@Data
public class PageResponseDTO<E> {
	
	private List<E> dtoList; //boardList도 받을 수 있고 articleList도 받을 수 있으니 제네릭타입으로 <E>라고 쓴다.
	private List<Integer> pageNumList; //페이지번호를 여기서 구해서감. 반복할 바로가기 번호
	private PageRequestDTO pageRequestDTO;
	private boolean prev;
	private boolean next;
	private int totalCount; //토탈 레코드 수
	private int prevPage;
	private int nextPage; 
	private int totalPage; //토탈 페이지 수
	private int current;
	
	@Builder(builderMethodName = "withAll")
	public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
		
		this.dtoList = dtoList;
		this.pageRequestDTO = pageRequestDTO;
		this.totalCount = (int) totalCount;
		
		int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
		//pageRequestDTO.getPate() / 10.0 -> 현재 페이지를 10으로 나누어 몇번째 페이지에 속하는지 나타냄. 
		//Math.ceil()은 소수점을 올림함. 예를들어 15페이지를 10으로나누면 1.5가됨. 이를 2페이지로 지정
		int start = end - 9;
		int last = (int)(Math.ceil((totalCount / (double) pageRequestDTO.getSize())));
		
		end = end > last ? last : end;
		
		this.prev = start > 1;
		this.next = totalCount > end * pageRequestDTO.getSize();
		
		//for 문역할 수행 페이지 1번~10번까지 특정범위의 수가 필요.
		//boxed() - mapper 타입으로 변환
		//collect(Collectors.toList()) -> list 타입에 담아라 
		this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
		
		if(prev) {
			this.prevPage = start - 1;
		}
		
		if(next) {
			this.nextPage = end + 1;
		}
		
		this.totalPage = this.pageNumList.size();
		this.current = pageRequestDTO.getPage();
	}
}
