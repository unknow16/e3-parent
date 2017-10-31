package com.fuyi.e3.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fuyi.e3.common.vo.SearchItem;
import com.fuyi.e3.common.vo.SearchResult;

@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery params) throws Exception {
		SearchResult searchResult = new SearchResult();
		
		QueryResponse response = solrServer.query(params);
		SolrDocumentList results = response.getResults();
		
		searchResult.setRecourdCount((int)results.getNumFound());
		
		List<SearchItem> itemList = new ArrayList<>();
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument solrDocument : results) {
			//取商品信息
			SearchItem searchItem = new SearchItem();
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			
			//取高亮结果
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(itemTitle);
			itemList.add(searchItem);
		}
		searchResult.setItemList(itemList);
		return searchResult;
	}
	
}
