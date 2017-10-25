package com.fuyi.e3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyi.e3.common.EasyUITreeNode;
import com.fuyi.e3.mapper.TbItemCatMapper;
import com.fuyi.e3.pojo.TbItemCat;
import com.fuyi.e3.pojo.TbItemCatExample;
import com.fuyi.e3.pojo.TbItemCatExample.Criteria;
import com.fuyi.e3.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
		
		List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
		for (TbItemCat tbItemCat : itemCatList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			result.add(node);
		}
		return result;
	}

}
