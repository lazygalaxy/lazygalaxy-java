package com.lazygalaxy.engine.load.jsoup;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lazygalaxy.engine.load.jsoup.TableJSoupElementProcessor.JSoupTable;
import com.lazygalaxy.engine.util.GeneralUtil;

public class TableJSoupElementProcessor implements JSoupElementProcessor<JSoupTable> {

	public class JSoupTable {
		protected List<String> fields = new ArrayList<String>();
		protected List<Element[]> data = new ArrayList<Element[]>();

		public int getDataRows() {
			return data.size();
		}

		public <T> T process(int row, String field, JSoupElementProcessor<T> processor) throws Exception {
			int index = fields.indexOf(GeneralUtil.alphanumerify(field));
			if (index == -1) {
				return processor.empty();
			}

			return processor.apply(data.get(row)[index]);
		}

		public Element getElement(int row, String field) {
			int index = fields.indexOf(GeneralUtil.alphanumerify(field));
			if (index == -1) {
				return null;
			}
			return data.get(row)[index];
		}
	}

	@Override
	public JSoupTable apply(Element element) throws Exception {
		JSoupTable table = new JSoupTable();

		Elements rows = element.select("tr");
		Elements cols = rows.remove(0).select("th,td");

		for (Element col : cols) {
			table.fields.add(GeneralUtil.alphanumerify(col.text()));
		}

		for (Element row : rows) {
			cols = row.select("th,td");
			// TODO: may skip some info if table contains a table
			if (cols.size() == table.fields.size()) {
				table.data.add(cols.toArray(new Element[cols.size()]));
			}
		}

		return table;
	}

	@Override
	public JSoupTable empty() {
		return new JSoupTable();
	}
}
