package com.lazygalaxy.engine.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.util.GeneralUtil;

public abstract class MongoDocument {
	public static final List<String> EXCLUDE_FIELDS = Arrays.asList("updateDateTime");

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
			this.id = GeneralUtil.alphanumerify(id, "_", "");
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
		this.updateDateTime = LocalDateTime.now();
	}

	public void addLabel(String label) {
		this.labels.add(GeneralUtil.alphanumerify(label));
	}

	public void removeLabel(String label) {
		this.labels.remove(GeneralUtil.alphanumerify(label));
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, EXCLUDE_FIELDS);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, EXCLUDE_FIELDS);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
