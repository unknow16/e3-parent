package com.fuyi.e3.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fuyi.e3.search.service.SearchItemService;

public class ItemChangeListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemChangeListener.class);
	
	@Autowired SearchItemService searchItemService;

	@Override
	public void onMessage(Message message) {
		try {
			if(message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				String itemId = textMessage.getText();
				
				//添加商品到索引库
				searchItemService.addDocument(Long.parseLong(itemId));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("商品添加时，加入索引库失败", e);
		}
	}

}
