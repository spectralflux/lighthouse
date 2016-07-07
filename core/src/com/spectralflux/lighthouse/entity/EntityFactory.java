package com.spectralflux.lighthouse.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.spectralflux.lighthouse.World;
import com.spectralflux.lighthouse.component.ClickFlashComponent;
import com.spectralflux.lighthouse.component.HitboxComponent;
import com.spectralflux.lighthouse.component.MouseFollowComponent;
import com.spectralflux.lighthouse.component.PlayerComponent;
import com.spectralflux.lighthouse.component.PositionComponent;
import com.spectralflux.lighthouse.component.RenderComponent;
import com.spectralflux.lighthouse.component.VelocityComponent;

public class EntityFactory {

	public EntityFactory() {

	}

	public Entity newLighthouse(Texture tex) {
		Entity entity = new Entity();
		entity.add(new PositionComponent(new Vector2(World.GAME_AREA_X / 2, World.GAME_AREA_Y / 2), 70.0f));
		entity.add(new PlayerComponent());
		entity.add(new MouseFollowComponent());
		entity.add(new RenderComponent(tex));
		return entity;
	}

	public Entity newLighthouseBeam(Texture tex) {
		Entity entity = new Entity();
		entity.add(new PositionComponent(new Vector2(World.GAME_AREA_X / 2, World.GAME_AREA_Y / 2), 70.0f));
		entity.add(new RenderComponent(tex, new Color(1, 1, 1, 0.5f)));
		entity.add(new MouseFollowComponent());
		entity.add(new ClickFlashComponent());
		return entity;
	}

	// enemy makers

	private Entity newEnemy(Texture tex, int x, int y) {
		Entity entity = new Entity();
		entity.add(new PositionComponent(new Vector2(x, y), getEnemyInitialRotation(x, y)));
		entity.add(new RenderComponent(tex, new Color(1, 1, 1, 0.5f)));
		entity.add(new VelocityComponent(new Vector2(5, 5)));
		entity.add(new HitboxComponent(tex.getWidth()/2));
		return entity;
	}

	public Entity newSquidling(Texture tex, int x, int y) {
		return newEnemy(tex, x, y);
	}
	
	// TODO fill this in, not sure if working
	private float getEnemyInitialRotation(int x, int y) {
	    int centerX = World.GAME_AREA_X/2;
	    int centerY = World.GAME_AREA_Y/2;
	    
	    float angle = MathUtils.atan2(centerX - x, centerY - y);
        angle = angle * (180 / MathUtils.PI);
        
	    //return 315.f;
        return angle;
	}

}
