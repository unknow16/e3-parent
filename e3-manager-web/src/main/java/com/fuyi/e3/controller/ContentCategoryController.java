package com.fuyi.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fuyi.e3.common.vo.EasyUIResult;
import com.fuyi.e3.common.vo.EasyUITreeNode;
import com.fuyi.e3.service.ContentCategoryService;

@Controller
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value="id", defaultValue="0")Long parentId) {
		return contentCategoryService.getContentCategoryList(parentId);
	}
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIResult getContentByCategoryId(Long categoryId, Integer page, Integer rows) {
		return contentCategoryService.getContentByCategoryId(categoryId, page, rows);
	}
}
