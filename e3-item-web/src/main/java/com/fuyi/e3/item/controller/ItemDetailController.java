package com.fuyi.e3.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.fuyi.e3.item.domain.Item;
import com.fuyi.e3.pojo.TbItem;
import com.fuyi.e3.pojo.TbItemDesc;
import com.fuyi.e3.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class ItemDetailController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@RequestMapping("/item/{itemId}")
	public String itemDetail(@PathVariable Long itemId, Model model) {
		
		TbItem tbItem = itemService.getItemById(itemId);
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		
		Item item = new Item(tbItem);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		
		return "item";
	}
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception {
		Configuration configuration = freeMarkerConfig.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "123");
		map.put("name", "fuyi");
		
		Writer out = new FileWriter(new File("F:/a.html"));
		
		template.process(map, out);
		
		out.close();
		return "ok";
	}
}
