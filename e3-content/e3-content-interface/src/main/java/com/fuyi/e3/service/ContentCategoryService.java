package com.fuyi.e3.service;

import java.util.List;

import com.fuyi.e3.common.EasyUIResult;
import com.fuyi.e3.common.EasyUITreeNode;

/**
 * 内容管理分类service
 * @author Administrator
 *
 */
public interface ContentCategoryService {

	/**
	 * 根据父id查询其子分类
	 * @param parentId
	 * @return
	 */
	public List<EasyUITreeNode> getContentCategoryList(Long parentId);
	
	/**
	 * 根据内容分类id查询内容
	 * @param categoryId
	 * @return
	 */
	public EasyUIResult getContentByCategoryId(Long categoryId, Integer page, Integer rows);
}
