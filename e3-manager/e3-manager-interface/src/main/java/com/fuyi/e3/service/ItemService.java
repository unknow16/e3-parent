package com.fuyi.e3.service;

import com.fuyi.e3.common.EasyUIResult;
import com.fuyi.e3.pojo.TbItem;

public interface ItemService {

	/**
	 * 根据商品id查询
	 * @param itemId
	 * @return
	 */
	public TbItem getItemById(Long itemId);
	
	/**
	 * 分页查询商品
	 * @param page 当前页数
	 * @param rows 显示条数
	 * @return
	 */
	public EasyUIResult getItemList(Integer page, Integer rows);
}
