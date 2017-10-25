package com.fuyi.e3.service;

import java.util.List;

import com.fuyi.e3.common.vo.EasyUITreeNode;

/**
 * 商品类目service
 * @author Administrator
 *
 */
public interface ItemCatService {

	/**
	 * 获取商品类目
	 * @param parentId 父Node id
	 * @return
	 */
	public List<EasyUITreeNode> getItemCatList(Long parentId);
}
