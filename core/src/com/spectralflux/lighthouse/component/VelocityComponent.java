package com.spectralflux.lighthouse.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
	
	public Vector2 v;
	
	public VelocityComponent(Vector2 v) {
		this.v = v;
	}

}
