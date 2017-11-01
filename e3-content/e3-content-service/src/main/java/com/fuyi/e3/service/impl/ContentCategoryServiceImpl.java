package com.fuyi.e3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyi.e3.common.jedis.JedisClient;
import com.fuyi.e3.common.utils.JsonUtils;
import com.fuyi.e3.common.vo.EasyUIResult;
import com.fuyi.e3.common.vo.EasyUITreeNode;
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
	
	@Autowired
	private JedisClient jedisClientPool;

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
		
		//*********************读取缓存  ********** 对内容信息做增删改操作后只需要把对应缓存删除即可。
		String json = jedisClientPool.hget("CONTENT_KEY", categoryId + "");
		if(json != null && json != "") {
			EasyUIResult result1 = JsonUtils.jsonToPojo(json, EasyUIResult.class);
			return result1;
		}
		//*********************
		
		PageHelper.startPage(page, rows);
		
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		
		EasyUIResult result = new EasyUIResult();
		result.setRows(pageInfo.getList());
		result.setTotal(pageInfo.getTotal());
		System.out.println("================================================================读数据库");
		
		//********************设置缓存
		jedisClientPool.hset("CONTENT_KEY", categoryId + "", JsonUtils.objectToJson(result));
		
		
		return result;
	}

}
