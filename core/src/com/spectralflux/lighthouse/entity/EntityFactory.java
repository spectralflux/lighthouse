package com.spectralflux.lighthouse.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.spectralflux.lighthouse.World;
import com.spectralflux.lighthouse.component.BeamComponent;
import com.spectralflux.lighthouse.component.ClickFlashComponent;
import com.spectralflux.lighthouse.component.EnemyComponent;
import com.spectralflux.lighthouse.component.HitboxComponent;
import com.spectralflux.lighthouse.component.MouseFollowComponent;
import com.spectralflux.lighthouse.component.PlayerComponent;
import com.spectralflux.lighthouse.component.PositionComponent;
import com.spectralflux.lighthouse.component.RenderComponent;
import com.spectralflux.lighthouse.component.VelocityComponent;

/**
 * Factory class for Ashley entities for use by the game.
 *  
 */
public class EntityFactory {

	public EntityFactory() {

	}

	public Entity newLighthouse(Texture tex) {
		Entity entity = new Entity();
		entity.add(new PositionComponent(new Vector2(World.GAME_AREA_X / 2, World.GAME_AREA_Y / 2), 70.0f));
		entity.add(new PlayerComponent());
		entity.add(new MouseFollowComponent());
		entity.add(new RenderComponent(tex));
		entity.add(new HitboxComponent(16));
		return entity;
	}

	public Entity newLighthouseBeam(Texture tex) {
		Entity entity = new Entity();
		entity.add(new PositionComponent(new Vector2(World.GAME_AREA_X / 2, World.GAME_AREA_Y / 2), 70.0f));
		entity.add(new RenderComponent(tex, new Color(1, 1, 1, 0.5f)));
		entity.add(new MouseFollowComponent());
		entity.add(new ClickFlashComponent());
		entity.add(new BeamComponent());
		return entity;
	}

	// enemy makers

	private Entity newEnemy(Texture tex, int x, int y) {
		Entity entity = new Entity();
		entity.add(new PositionComponent(new Vector2(x, y), getEnemyInitialRotation(x, y)));
		entity.add(new RenderComponent(tex, new Color(1, 1, 1, 0.5f)));
		entity.add(new HitboxComponent(tex.getWidth()/2));
		
		return entity;
	}

	public Entity newSquidling(Texture tex, int x, int y) {
		Entity e = newEnemy(tex, x, y);
		e.add(new EnemyComponent(1, 10, 20));
		e.add(new VelocityComponent(getEnemyInitialVelocity(x, y, 10)));
		return e;
	}
	
	public Entity newSerpent(Texture tex, int x, int y) {
		Entity e = newEnemy(tex, x, y);
		e.add(new EnemyComponent(1, 20, 15));
		e.add(new VelocityComponent(getEnemyInitialVelocity(x, y, 20)));
		return e;
	}
	
	public Entity newFlyer(Texture tex, int x, int y) {
		Entity e = newEnemy(tex, x, y);
		e.add(new EnemyComponent(1, 50, 10));
		e.add(new VelocityComponent(getEnemyInitialVelocity(x, y, 100)));
		return e;
	}
	
	private float getEnemyInitialRotation(int x, int y) {
	    int centerX = World.GAME_AREA_X/2;
	    int centerY = World.GAME_AREA_Y/2;
	    
	    float angle = MathUtils.atan2(centerX - x, centerY - y);
        angle = 360.0f - (angle * (180 / MathUtils.PI));
        
        return angle;
	}
	
	private Vector2 getEnemyInitialVelocity(int x, int y, float speed) {
		int centerX = World.GAME_AREA_X/2;
	    int centerY = World.GAME_AREA_Y/2;
	    
	    float diffX = centerX - x;
	    float diffY = centerY - y;
	    
	    Vector2 v = (new Vector2(diffX, diffY)).nor().scl(speed);
	    
		return v;
	}

}
