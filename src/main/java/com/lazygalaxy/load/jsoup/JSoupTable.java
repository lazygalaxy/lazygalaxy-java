package com.lazygalaxy.load.jsoup;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.utils.GeneralUtil;

public class JSoupTable {
	private List<String> fields = new ArrayList<String>();
	private List<Element[]> data = new ArrayList<Element[]>();

	public JSoupTable(Element table) {
		Elements rows = table.select("tr");

		Elements cols = rows.remove(0).select("th,td");

		for (Element col : cols) {
			fields.add(GeneralUtil.alphanumerify(col.text()));
		}

		for (Element row : rows) {
			cols = row.select("th,td");
			data.add(cols.toArray(new Element[cols.size()]));
		}
	}

	public int getDataRows() {
		return data.size();
	}

	public <T> T process(int row, String field, JSoupElementProcessor<T> processor) {
		int index = fields.indexOf(GeneralUtil.alphanumerify(field));
		if (index == -1) {
			return processor.empty();
		}

		return processor.apply(data.get(row)[index]);
	}
}
