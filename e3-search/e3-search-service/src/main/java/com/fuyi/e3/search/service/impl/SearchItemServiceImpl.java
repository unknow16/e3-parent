package com.fuyi.e3.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fuyi.e3.common.utils.TaotaoResult;
import com.fuyi.e3.common.vo.SearchItem;
import com.fuyi.e3.common.vo.SearchResult;
import com.fuyi.e3.search.dao.SearchDao;
import com.fuyi.e3.search.mapper.SearchItemMapper;
import com.fuyi.e3.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper searchItemMapper;

	@Autowired
	private SolrServer solrServer;
	
	@Autowired
	private SearchDao searchDao;
	
	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;

	@Override
	public TaotaoResult importAllItem() {
		try {
			List<SearchItem> itemList = searchItemMapper.getItemList();
			SolrInputDocument doc = null;
			for (SearchItem searchItem : itemList) {
				doc = new SolrInputDocument();
				doc.addField("id", searchItem.getId());
				doc.addField("item_category_name", searchItem.getCategory_name());
				doc.addField("item_image", searchItem.getImage());
				doc.addField("item_price", searchItem.getPrice());
				doc.addField("item_sell_point", searchItem.getSell_point());
				doc.addField("item_title", searchItem.getTitle());
				solrServer.add(doc);
			}
			solrServer.commit();
			
			return TaotaoResult.ok();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "solr导入发生异常");
		}
	}

	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		SolrQuery params = new SolrQuery();
		
		params.setQuery(keyword);
		
		params.setStart((page-1) * rows);
		params.setRows(rows);
		
		params.set("df", DEFAULT_FIELD);
		
		params.setHighlight(true);
		params.addHighlightField("item_title");
		params.setHighlightSimplePre("<em style=\"color:red\">");
		params.setHighlightSimplePost("</em>");

		SearchResult result = searchDao.search(params);
		
		//计算总页数
		Integer recourdCount = result.getRecourdCount();
		int pages = recourdCount / rows;
		if(recourdCount % rows > 0) pages++;
		
		result.setTotalPages(pages);
		return result;
	}

}
