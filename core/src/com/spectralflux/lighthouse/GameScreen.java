package com.spectralflux.lighthouse;

import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Family.Builder;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.spectralflux.lighthouse.component.BeamComponent;
import com.spectralflux.lighthouse.component.ClickFlashComponent;
import com.spectralflux.lighthouse.component.DamageComponent;
import com.spectralflux.lighthouse.component.DeathComponent;
import com.spectralflux.lighthouse.component.EnemyComponent;
import com.spectralflux.lighthouse.component.MouseFollowComponent;
import com.spectralflux.lighthouse.component.PlayerComponent;
import com.spectralflux.lighthouse.component.PositionComponent;
import com.spectralflux.lighthouse.component.RenderComponent;
import com.spectralflux.lighthouse.entity.EntityFactory;
import com.spectralflux.lighthouse.entity.Wave;
import com.spectralflux.lighthouse.system.DamageSystem;
import com.spectralflux.lighthouse.system.MovementSystem;

public class GameScreen implements Screen, InputProcessor {
	SpriteBatch batch;
	SpriteBatch sidebarBatch;
	Game game;
	Engine engine;

	Texture lighthouseBeamTx;
	int lighthouseBeamWidth;
	int lighthouseBeamLength;
	Texture lighthouseTx;
	Texture squidlingTx;
	Texture[] oceanSquaresTx;

	// number of ocean textures to choose from
	private static final int NUM_OCEAN_TEXTURES = 1;

	BitmapFont eldergodsit45Font;
	BitmapFont eldergodsit60Font;

	// lighthouse shot
	private static final int FLASH_LENGTH = 50;
	private static final int SHOT_COUNTDOWN = 80;
	int shotCountdownRemaining = 0;
	int flashRemaining = 0;

	public GameScreen(Game game) {
		this.game = game;
	}

	private void loadEngine() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		engine = new Engine();
	}

	private void loadInitialEntities() {
		EntityFactory entityFactory = new EntityFactory();
		Wave enemyWave = new Wave(squidlingTx);
		List<Entity> initialWave = enemyWave.createWave(1);

		engine.addEntity(entityFactory.newLighthouse(lighthouseTx));
		engine.addEntity(entityFactory.newLighthouseBeam(lighthouseBeamTx));

		// initial enemies
		for (Entity entity: initialWave) {
		    engine.addEntity(entity);
		}
		
	}

	private void loadSystems() {
		engine.addSystem(new MovementSystem());
		engine.addSystem(new DamageSystem());
	}

	private void loadTextures() {
		lighthouseBeamTx = new Texture("lighthouse-beam.png");
		lighthouseBeamWidth = lighthouseBeamTx.getHeight();
		lighthouseBeamLength = lighthouseBeamTx.getWidth();
		
		lighthouseTx = new Texture("lighthouse.png");
		squidlingTx = new Texture("squidling.png");

		oceanSquaresTx = new Texture[1];
		for (int i = 1; i <= 1; i++) {
			oceanSquaresTx[i - 1] = new Texture("ocean" + i + ".png");
		}

		eldergodsit45Font = new BitmapFont(Gdx.files.internal("fonts/eldergodsit45.fnt"));
		eldergodsit45Font.setColor(Color.WHITE);

		eldergodsit60Font = new BitmapFont(Gdx.files.internal("fonts/eldergodsit60.fnt"));
		eldergodsit60Font.setColor(Color.WHITE);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		sidebarBatch = new SpriteBatch();

		// load textures up front
		loadTextures();

		loadEngine();
		loadSystems();
		loadInitialEntities();

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		// update all systems
		engine.update(Gdx.graphics.getDeltaTime());

		Builder fb = Family.all(PositionComponent.class, RenderComponent.class);
		Family family = fb.get();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		for (int x = 0; x < World.GAME_AREA_X; x = x + 32) {
			for (int y = 0; y < World.GAME_AREA_Y; y = y + 32) {
				batch.draw(oceanSquaresTx[0], x, y);
			}
		}

		for (Entity entity : engine.getEntitiesFor(family)) {
			PositionComponent position = entity.getComponent(PositionComponent.class);
			RenderComponent render = entity.getComponent(RenderComponent.class);
			ClickFlashComponent clickFlash = entity.getComponent(ClickFlashComponent.class);

			if (clickFlash != null) {
				render.setColor(new Color(1, 0.0f + (float) flashRemaining / FLASH_LENGTH, 0,
						(float) shotCountdownRemaining / SHOT_COUNTDOWN));
			}

			drawEntity(position, render);
		}

		batch.end();

		// sidebar background
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.1f, 1));
		shapeRenderer.rect(World.GAME_AREA_X, 0, World.SIDEBAR_WIDTH, World.GAME_AREA_Y);
		shapeRenderer.end();

		sidebarBatch.begin();
		eldergodsit45Font.draw(sidebarBatch, "Sanity Meter!", World.WINDOW_X - World.SIDEBAR_WIDTH + 10, 45);
		eldergodsit60Font.draw(sidebarBatch, "Innsmouth\nLighthouse\nKeeper", World.WINDOW_X - World.SIDEBAR_WIDTH + 2,
				World.WINDOW_Y - 16);

		// player-specific rendering
		fb = Family.all(PlayerComponent.class);
		family = fb.get();
		for (Entity entity : engine.getEntitiesFor(family)) {
			PlayerComponent player = entity.getComponent(PlayerComponent.class);
			drawSanityMeter(player.sanity);
		}

		sidebarBatch.end();

		// count down last lighthouse firing flash.
		if (flashRemaining > 0) {
			flashRemaining -= delta;
		}
		if (shotCountdownRemaining > 0) {
			shotCountdownRemaining -= delta;
		}
		
		//destroy all killed entities
		fb = Family.all(DeathComponent.class);
		family = fb.get();
		for (Entity entity : engine.getEntitiesFor(family)) {
			engine.removeEntity(entity);
		}

	}

	private void drawEntity(PositionComponent position, RenderComponent render) {
		batch.setColor(render.color());
		batch.draw(render.img(), position.pos.x - render.originX(), position.pos.y - render.originY(), render.originX(),
				render.originY(), render.width(), render.height(), render.scaleX(), render.scaleY(), position.rotation,
				render.srcX(), render.srcY(), render.srcWidth(), render.srcHeight(), render.flipX(), render.flipY());
		batch.setColor(1, 1, 1, 1);
	}

	private void drawSanityMeter(int sanityRemaining) {
		float lengthMultiplier = 1.8f;
		float meterLength = World.TOTAL_SANITY * lengthMultiplier;
		int meterHeight = 25;
		int meterStartX = World.WINDOW_X - World.SIDEBAR_WIDTH + 15;
		int meterStartY = 50;

		// background rectangle
		sidebarBatch.end();
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0.5f, 0, 0, 1));
		shapeRenderer.rect(meterStartX, meterStartY, meterLength, meterHeight);
		shapeRenderer.end();

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0, 0.8f, 0.3f, 1));
		shapeRenderer.rect(meterStartX, meterStartY, sanityRemaining * lengthMultiplier, meterHeight);
		shapeRenderer.end();
		sidebarBatch.begin();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if (button == Input.Buttons.LEFT && shotCountdownRemaining <= 0) {
			Builder fb = Family.all(BeamComponent.class, RenderComponent.class);
	        Family family = fb.get();
	        Entity beamEntity = engine.getEntitiesFor(family).first();
	        RenderComponent beamRender = beamEntity.getComponent(RenderComponent.class);
	        PositionComponent beamPosition = beamEntity.getComponent(PositionComponent.class);
	        float[] beamVertices = new float[12];
	        //lighthouseBeamWidth
	        
	        float angle = MathUtils.atan2(screenX - beamPosition.pos.x, screenY - beamPosition.pos.y);
	        float opposingAngle = angle + MathUtils.PI;
			
	        float arc = 3f;
	        
	        beamVertices[0] = (float)(lighthouseBeamLength/2 * MathUtils.cos(angle+arc));
	        beamVertices[1] = (float)(lighthouseBeamLength/2 * MathUtils.sin(angle+arc));
	        beamVertices[2] = (float)(lighthouseBeamLength/2 * MathUtils.cos(angle-arc));
	        beamVertices[3] = (float)(lighthouseBeamLength/2 * MathUtils.sin(angle-arc));
	        
	        beamVertices[4] = 3f;
	        beamVertices[5] = 3f;
	        
	        beamVertices[6] = (float)(lighthouseBeamLength/2 * MathUtils.cos(opposingAngle-arc));
	        beamVertices[7] = (float)(lighthouseBeamLength/2 * MathUtils.sin(opposingAngle-arc));
	        
	        beamVertices[8] = (float)(lighthouseBeamLength/2 * MathUtils.cos(opposingAngle+arc));
	        beamVertices[9] = (float)(lighthouseBeamLength/2 * MathUtils.sin(opposingAngle+arc));
	        beamVertices[10] = -3f;
	        beamVertices[11] = -3f;

	        
	        
	        
	        
	        
	        //beamVertices[0] = beamPosition.pos.x - beamRender.originX();
	        Polygon beamPolygon = new Polygon(beamVertices);
	        
	        
		    fb = Family.all(PositionComponent.class, EnemyComponent.class);
	        family = fb.get();
	        
	        for (Entity entity : engine.getEntitiesFor(family)) {
	            // TODO check collision with beam, if collide give entity a damage component
	            PositionComponent position = entity.getComponent(PositionComponent.class);
	            Polygon entPoly = new Polygon(new float[]{position.pos.x+16f, position.pos.y+16f, position.pos.x-16f, position.pos.y+16f, position.pos.x-16f, position.pos.y-16f, position.pos.x+16f, position.pos.y-16f});
	            boolean isCollision = Intersector.intersectPolygons(beamPolygon, entPoly, null);
	            if (isCollision) {
	            	System.err.println("hit!");
	            	entity.add(new DamageComponent(World.LIGHTHOUSE_BEAM_DAMAGE));
	            }
	        }
			// shoot
			flashRemaining = FLASH_LENGTH;
			shotCountdownRemaining = SHOT_COUNTDOWN;
			return true;
		}
		return false;
	}
	
	public boolean isBeamCollision(Entity entity, Polygon beamPolygon) {
		//make polygon for entity
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		boolean foundEntityToUpdate = false;

		Builder fb = Family.all(PositionComponent.class, MouseFollowComponent.class);
		Family family = fb.get();

		for (Entity entity : engine.getEntitiesFor(family)) {
			ClickFlashComponent clickFlash = entity.getComponent(ClickFlashComponent.class);
			PositionComponent position = entity.getComponent(PositionComponent.class);
			boolean updatePosition = true;

			float angle = MathUtils.atan2(screenX - position.pos.x, screenY - position.pos.y);
			angle = angle * (180 / MathUtils.PI);

			// only update position for mouse followers that arent a recently
			// fired lighthouse beam
			if (clickFlash != null && shotCountdownRemaining > 0) {
				updatePosition = false;
			}

			if (updatePosition) {
				position.rotation = angle;
			}
			foundEntityToUpdate = true;
		}

		return foundEntityToUpdate;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
