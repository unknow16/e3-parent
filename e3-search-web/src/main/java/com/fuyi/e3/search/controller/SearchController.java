package com.fuyi.e3.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fuyi.e3.common.vo.SearchResult;
import com.fuyi.e3.search.service.SearchItemService;

@Controller
public class SearchController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	@Value("${DEFAULT_ROWS}")
	private Integer DEFAULT_ROWS;

	@RequestMapping("/search")
	public String searchItem(String keyword, @RequestParam(defaultValue="1") Integer page, Model model) throws Exception {
		keyword = new String(keyword.getBytes("iso8859-1"), "UTF-8");
		SearchResult result = searchItemService.search(keyword, page, DEFAULT_ROWS);
		
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("recourdCount", result.getRecourdCount());
		model.addAttribute("page", page);
		model.addAttribute("itemList", result.getItemList());
		
		return "search";
	}
}
