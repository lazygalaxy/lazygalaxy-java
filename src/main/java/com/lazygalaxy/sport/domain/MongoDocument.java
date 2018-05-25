package com.lazygalaxy.sport.domain;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lazygalaxy.sport.utils.GeneralUtil;

public abstract class MongoDocument {
	private String id;
	private String name;
	private Set<String> labels;

	public MongoDocument() {
	}

	public MongoDocument(String id, String name, String[] labels) {
		this.id = id.toLowerCase();
		this.name = name;

		this.labels = new TreeSet<String>();
		addLabel(name);
		for (String label : labels) {
			addLabel(label);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getLabels() {
		return labels;
	}

	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}

	public void addLabel(String label) {
		this.labels.add(GeneralUtil.simplify(label));
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
