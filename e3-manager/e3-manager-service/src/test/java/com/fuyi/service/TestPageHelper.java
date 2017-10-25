package com.fuyi.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fuyi.e3.mapper.TbItemMapper;
import com.fuyi.e3.pojo.TbItem;
import com.fuyi.e3.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-dao.xml", "classpath:spring/applicationContext-service.xml", "classpath:spring/applicationContext-trans.xml"})
public class TestPageHelper {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Test
	public void testPage() throws Exception {
		PageHelper pageHelper = new PageHelper();
		pageHelper.startPage(1, 10);
		
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		
		PageInfo pageInfo = new PageInfo(list);
		
		Assert.assertEquals(10, pageInfo.getPageSize());
		Assert.assertEquals(934, pageInfo.getTotal());
		Assert.assertEquals(10, pageInfo.getList().size());
		
		System.out.println("================================================================================== size=" + pageInfo.getPageSize() + ", total=" + pageInfo.getTotal());
		
		List<TbItem> a = pageInfo.getList();
		for (TbItem tbItem : a) {
			System.out.println(tbItem.getSellPoint());
		}
	}

}
