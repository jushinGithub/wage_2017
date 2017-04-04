package com.zhugx.salary.pojo.common;

import java.util.HashMap;
import java.util.List;

public class DataGridProductData {
	int total;
	List<HashMap> rows;
	List footer;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<HashMap> getRows() {
		return rows;
	}

	public void setRows(List<HashMap> rows) {
		this.rows = rows;
	}

	public List getFooter() {
		return footer;
	}

	public void setFooter(List footer) {
		this.footer = footer;
	}

}
