package com.lazygalaxy.canvas.common;

public class Chromosome {
	private Gene[] genes;

	public Chromosome(Gene... genes) {
		this.genes = genes;
	}

	public Object[] getSequence() {
		Object[] objects = new Object[genes.length];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = genes[i].getValue();
		}
		return objects;
	}

	@Override
	public String toString() {
		String string = "";
		for (int i = 0; i < genes.length; i++) {
			string += genes[i].toString() + ", ";
		}
		return string;
	}
}
