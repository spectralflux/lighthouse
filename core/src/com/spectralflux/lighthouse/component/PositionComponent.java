package com.spectralflux.lighthouse.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
	
	public Vector2 pos;
	public float rotation;
	
	public PositionComponent(Vector2 pos, float rotation) {
		this.pos = pos;
		this.rotation = rotation;
	}

}
