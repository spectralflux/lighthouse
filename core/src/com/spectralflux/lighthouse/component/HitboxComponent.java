package com.spectralflux.lighthouse.component;

import com.badlogic.ashley.core.Component;

public class HitboxComponent implements Component {
	
	public float radius;
	
	public HitboxComponent(float radius) {
		this.radius = radius;
	}

}
