package com.lazygalaxy.canvas.common;

import com.lazygalaxy.canvas.main.WireFrameCreate;

public class FloatGene extends Gene<Float> {
	final private float minValue;
	final private float maxValue;

	public FloatGene(String name, float minValue, float maxValue) {
		super(name);

		this.minValue = minValue;
		this.maxValue = maxValue;

		this.value = randomize();
	}

	public Float randomize() {
		return (WireFrameCreate.RANDOM.nextFloat() * (maxValue - minValue)) + minValue;
	}

	public Float getValue() {
		return value;
	}
}
