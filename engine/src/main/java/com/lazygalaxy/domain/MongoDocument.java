package com.lazygalaxy.domain;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.util.GeneralUtil;

public abstract class MongoDocument {

	@Deprecated
	protected static String buildId(String seperator, String... parts) {
		for (Object part : parts) {
			if (part == null) {
				return null;
			}
		}
		return String.join(seperator, parts);
	}

	public String id;
	public String name;
	public Set<String> labels;
	public LocalDateTime updateDateTime;

	public MongoDocument() {
	}

	public MongoDocument(String id, String name, String[] labels) throws Exception {
		if (!StringUtils.isBlank(id)) {
			this.id = GeneralUtil.alphanumerify(id);
		} else {
			this.id = UUID.randomUUID().toString();
		}
		this.name = name;

		if (labels != null) {
			this.labels = new TreeSet<String>();
			addLabel(name);
			for (String label : labels) {
				addLabel(label);
			}
		}
	}

	public void addLabel(String label) {
		this.labels.add(GeneralUtil.alphanumerify(label));
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "updateDateTime");
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, "updateDateTime");
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
