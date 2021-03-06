package com.fuyi.e3.service.impl;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.fuyi.e3.common.jedis.impl.JedisClientPool;
import com.fuyi.e3.common.utils.IDUtils;
import com.fuyi.e3.common.utils.JsonUtils;
import com.fuyi.e3.common.utils.TaotaoResult;
import com.fuyi.e3.common.vo.EasyUIResult;
import com.fuyi.e3.mapper.TbItemDescMapper;
import com.fuyi.e3.mapper.TbItemMapper;
import com.fuyi.e3.pojo.TbItem;
import com.fuyi.e3.pojo.TbItemDesc;
import com.fuyi.e3.pojo.TbItemExample;
import com.fuyi.e3.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination topicDestination;
	
	@Autowired
	private JedisClientPool jedisClientPool;
	
	/*@Value("${ITEM_INFO_PRE}")*/
	private String ITEM_INFO_PRE = "ITEM_INFO";
	
	/*@Value("${ITEM_INFO_EXPIRE}")*/
	private Integer ITEM_INFO_EXPIRE = 30000;
	
	@Override
	public TbItem getItemById(Long itemId) {
		TbItem item = null;
		//读缓存，存在返回，不存在查数据库
		String json = jedisClientPool.get(ITEM_INFO_PRE + ":" + itemId + ":BASE");
		if(json != null && !"".equals(json)) {
			item = JsonUtils.jsonToPojo(json, TbItem.class);
			return item;
		}
		
		//查数据库
		item = itemMapper.selectByPrimaryKey(itemId);
		
		//存入缓存
		jedisClientPool.set(ITEM_INFO_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
		jedisClientPool.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
		return item;
	}
	
	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		TbItemDesc itemDesc = null;
		String json = jedisClientPool.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
		if(json != null && !"".equals(json)) {
			itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
			return itemDesc;
		}
		
		itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		jedisClientPool.set(ITEM_INFO_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
		jedisClientPool.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
		return itemDesc;
	}

	@Override
	public EasyUIResult getItemList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		
		TbItemExample example = new TbItemExample();
		List<TbItem> resultList = itemMapper.selectByExample(example);
		
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(resultList);
		
		EasyUIResult result = new EasyUIResult();
		result.setRows(pageInfo.getList());
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		
		//1.生成商品id
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		
		itemMapper.insert(item);
		
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		
		itemDescMapper.insert(itemDesc);
		
		//发送一个商品添加消息, 因为其他很多功能模块都对添加商品感兴趣，所以用topic，如，同步solr索引库
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		
		return TaotaoResult.ok();
	}

	
	
}
