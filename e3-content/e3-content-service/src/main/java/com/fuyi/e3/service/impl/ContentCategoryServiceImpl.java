package com.fuyi.e3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyi.e3.common.EasyUIResult;
import com.fuyi.e3.common.EasyUITreeNode;
import com.fuyi.e3.mapper.TbContentCategoryMapper;
import com.fuyi.e3.mapper.TbContentMapper;
import com.fuyi.e3.pojo.TbContent;
import com.fuyi.e3.pojo.TbContentCategory;
import com.fuyi.e3.pojo.TbContentCategoryExample;
import com.fuyi.e3.pojo.TbContentCategoryExample.Criteria;
import com.fuyi.e3.pojo.TbContentExample;
import com.fuyi.e3.service.ContentCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Autowired
	private TbContentMapper contentMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		
		List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory cat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(cat.getId());
			node.setText(cat.getName());
			node.setState(cat.getIsParent()?"closed":"open");
			result.add(node);
		}
		return result;
	}

	@Override
	public EasyUIResult getContentByCategoryId(Long categoryId, Integer page, Integer rows) {
		
		PageHelper.startPage(page, rows);
		
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		
		EasyUIResult result = new EasyUIResult();
		result.setRows(pageInfo.getList());
		result.setTotal(pageInfo.getTotal());
		return result;
	}

}
