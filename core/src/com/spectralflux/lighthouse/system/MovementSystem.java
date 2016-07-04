package com.spectralflux.lighthouse.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.spectralflux.lighthouse.component.PositionComponent;
import com.spectralflux.lighthouse.component.VelocityComponent;

public class MovementSystem extends IteratingSystem {

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

	public MovementSystem () {
		super(Family.all(PositionComponent.class, VelocityComponent.class).get());
	}

	@Override
	public void processEntity (Entity entity, float deltaTime) {
		PositionComponent position = pm.get(entity);
		VelocityComponent velocity = vm.get(entity);

		position.pos.x += velocity.v.x * deltaTime;
		position.pos.y += velocity.v.y * deltaTime;
	}

}
