package com.fuyi.e3.item.domain;

import com.fuyi.e3.pojo.TbItem;

public class Item extends TbItem {

	private static final long serialVersionUID = 1249000568325592174L;

	public Item(TbItem tbItem) {
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}

	public String[] getImages() {
		String images = this.getImage();
		if (images != null && !"".equals(images)) {
			String[] split = images.split(",");
			return split;
		}
		return null;
	}
}
