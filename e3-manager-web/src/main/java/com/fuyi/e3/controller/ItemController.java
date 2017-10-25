package com.fuyi.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fuyi.e3.common.EasyUIResult;
import com.fuyi.e3.pojo.TbItem;
import com.fuyi.e3.service.ItemService;

@RequestMapping("/item")
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		return itemService.getItemById(itemId);
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIResult getItemList(Integer page, Integer rows) {
		return itemService.getItemList(page, rows);
	}
}
