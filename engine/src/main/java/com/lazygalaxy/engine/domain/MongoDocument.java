package com.lazygalaxy.engine.domain;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.engine.util.GeneralUtil;

public abstract class MongoDocument {
	public static final String EXCLUDE_FIELD = "updateDateTime";

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
		this(id, true, name, labels);
	}

	public MongoDocument(String id, boolean normalizeId, String name, String[] labels) throws Exception {
		if (!StringUtils.isBlank(id)) {
			if (normalizeId) {
				this.id = GeneralUtil.alphanumerify(id, "_", "");
			}
		} else {
			this.id = UUID.randomUUID().toString();
		}
		this.name = name;
		addLabel(name);

		if (labels != null) {
			for (String label : labels) {
				addLabel(label);
			}
		}
		this.updateDateTime = LocalDateTime.now();
	}

	public void addLabel(String label) {
		if (!StringUtils.isBlank(label)) {
			if (this.labels == null) {
				this.labels = new TreeSet<String>();
			}
			this.labels.add(GeneralUtil.alphanumerify(label));
		}
	}

	public void removeLabel(String label) {
		this.labels.remove(GeneralUtil.alphanumerify(label));
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, EXCLUDE_FIELD);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, EXCLUDE_FIELD);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
