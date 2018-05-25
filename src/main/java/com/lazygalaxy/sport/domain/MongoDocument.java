package com.lazygalaxy.sport.domain;

import java.util.HashSet;
import java.util.Set;

import com.lazygalaxy.sport.utils.GeneralUtil;

public abstract class MongoDocument {
	private String id;
	private String name;
	private Set<String> labels;

	public MongoDocument() {
	}

	public MongoDocument(String id, String name, String[] labels) {
		this.id = id;
		this.name = name;

		this.labels = new HashSet<String>();
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
		this.labels.add(GeneralUtil.normalize(label));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((labels == null) ? 0 : labels.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MongoDocument other = (MongoDocument) obj;
		if (labels == null) {
			if (other.labels != null)
				return false;
		} else if (!labels.equals(other.labels))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id + " " + name + " " + labels;
	}

}
