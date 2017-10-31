package com.fuyi.e3.search.service;

import com.fuyi.e3.common.utils.TaotaoResult;
import com.fuyi.e3.common.vo.SearchResult;

public interface SearchItemService {

	TaotaoResult importAllItem();
	
	SearchResult search(String keyword, int pages, int rows) throws Exception ;
}
