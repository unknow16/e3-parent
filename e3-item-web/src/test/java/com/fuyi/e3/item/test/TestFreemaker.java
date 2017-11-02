package com.fuyi.e3.item.test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-freemaker.xml"})
public class TestFreemaker {

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Test
	public void genHtml() throws Exception {
		Configuration configuration = freeMarkerConfig.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "123");
		map.put("name", "fuyi");
		
		Writer out = new FileWriter(new File("F:/a.html"));
		
		template.process(map, out);
		
		out.close();
		
	}
}
