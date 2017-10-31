package com.fuyi.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fuyi.e3.common.utils.TaotaoResult;
import com.fuyi.e3.search.service.SearchItemService;

@Controller
public class SolrController {

	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public TaotaoResult importAllItem() {
		TaotaoResult result = searchItemService.importAllItem();
		return result;
	}
}
