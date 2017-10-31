package com.fuyi.e3.common.vo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {

	private static final long serialVersionUID = -6132734614541981047L;

	private List<SearchItem> itemList;
	private Integer totalPages;
	private Integer recourdCount;
	
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public Integer getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(Integer recourdCount) {
		this.recourdCount = recourdCount;
	}
	
	
}
