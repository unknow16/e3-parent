package com.fuyi.e3.service;

import com.fuyi.e3.common.utils.TaotaoResult;
import com.fuyi.e3.common.vo.EasyUIResult;
import com.fuyi.e3.pojo.TbItem;
import com.fuyi.e3.pojo.TbItemDesc;

/**
 * 商品管理service
 * @author Administrator
 *
 */
public interface ItemService {

	/**
	 * 根据商品id查询
	 * @param itemId
	 * @return
	 */
	public TbItem getItemById(Long itemId);
	
	public TbItemDesc getItemDescById(Long itemId);
	
	/**
	 * 分页查询商品
	 * @param page 当前页数
	 * @param rows 显示条数
	 * @return
	 */
	public EasyUIResult getItemList(Integer page, Integer rows);
	
	/**
	 * 添加商品
	 * @param item
	 * @param desc
	 * @return
	 */
	public TaotaoResult addItem(TbItem item, String desc);
}
