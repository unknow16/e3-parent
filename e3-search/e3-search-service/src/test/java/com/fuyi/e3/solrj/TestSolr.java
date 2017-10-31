package com.fuyi.e3.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolr {

	@Test
	public void addDocument() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr-4.10.3");
		
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		
		solrInputDocument.addField("id", "2");
		solrInputDocument.addField("item_title", "测试商品");
		solrInputDocument.addField("item_price", "200");
		
		solrServer.add(solrInputDocument);
		solrServer.commit();
	}
	
	@Test
	public void deleteDocumentById() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr-4.10.3");
		
		//solrServer.deleteById("test001");
		solrServer.deleteByQuery("item_title:商品");
		
		solrServer.commit();
	}
	
	@Test
	public void queryDocument() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr-4.10.3");
		
		SolrQuery params = new SolrQuery();
		params.setQuery("*:*");
		
		QueryResponse response = solrServer.query(params);
		SolrDocumentList results = response.getResults();
		
		System.out.println("查询结果总数： " + results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
	}
	
	/**
	 * 带高亮查询结果
	 * @throws Exception
	 */
	@Test
	public void queryDocWithHighLignting() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr-4.10.3");
		
		SolrQuery params = new SolrQuery();
		
		//搜索条件
		params.setQuery("测试");
		
		/**
		 * 	指定默认搜索域
			<field name="item_keywords" type="text_ik" indexed="true" stored="false" multiValued="true"/>
			<copyField source="item_title" dest="item_keywords"/>
			<copyField source="item_sell_point" dest="item_keywords"/>
			<copyField source="item_category_name" dest="item_keywords"/>
		 */
		params.set("df", "item_keywords");
		//开启高亮
		params.setHighlight(true);
		//高亮的字段
		params.addHighlightField("item_title");
		params.setHighlightSimplePre("<font color='red'>");
		params.setHighlightSimplePost("</font>");
		
		QueryResponse response = solrServer.query(params);
		SolrDocumentList results = response.getResults();
		
		System.out.println("查询结果总数： " + results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			List<String> list = map.get("item_title");
			String itemTitle = null;
			if(list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = solrDocument.get("item_title").toString();
			}
			System.out.println(itemTitle);
			
			System.out.println(solrDocument.get("item_price"));
		}
	}
	
	@Test
	public void queryDocWithHighLignting1() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr-4.10.3");
		SolrQuery params = new SolrQuery();
		
		//设置查询条件
		params.set("q", "阿尔卡特");
		//params.setQuery("阿尔卡特");
		
		//设置分页
		params.setStart(0);
		params.setRows(5);
		
		//高亮
		params.setHighlight(true);
		params.addHighlightField("item_title");
		params.setHighlightSimplePre("<font color='red'>");
		params.setHighlightSimplePost("</font>");
		
		//设置默认搜索域
		params.set("df", "item_keywords");
		
		QueryResponse response = solrServer.query(params);
		SolrDocumentList results = response.getResults();
		
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		System.out.println("查询结果总数： " + results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			List<String> list = map.get("item_title");
			String itemTitle = null;
			if(list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = solrDocument.get("item_title").toString();
			}
			System.out.println(itemTitle);
			
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}
}
