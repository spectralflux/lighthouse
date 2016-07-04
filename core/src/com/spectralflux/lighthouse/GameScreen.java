package com.spectralflux.lighthouse;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Family.Builder;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.spectralflux.lighthouse.component.MouseFollowComponent;
import com.spectralflux.lighthouse.component.PlayerComponent;
import com.spectralflux.lighthouse.component.PositionComponent;
import com.spectralflux.lighthouse.component.RenderComponent;
import com.spectralflux.lighthouse.entity.EntityFactory;
import com.spectralflux.lighthouse.system.MovementSystem;

public class GameScreen implements Screen, InputProcessor {
	SpriteBatch batch;
	Game game;
	Engine engine;

	Texture lighthouseBeamTx;
	Texture lighthouseTx;
	Texture squidlingTx;
	Texture[] oceanSquaresTx;

	// number of ocean textures to choose from
	private static final int NUM_OCEAN_TEXTURES = 1;

	BitmapFont eldergodsit45Font;
	BitmapFont eldergodsit60Font;

	public GameScreen(Game game) {
		this.game = game;
	}

	private void loadEngine() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		engine = new Engine();
	}

	private void loadInitialEntities() {
		EntityFactory entityFactory = new EntityFactory();

		engine.addEntity(entityFactory.newLighthouse(lighthouseTx));
		engine.addEntity(entityFactory.newLighthouseBeam(lighthouseBeamTx));

		// initial enemies
		engine.addEntity(entityFactory.newSquidling(squidlingTx));
	}

	private void loadSystems() {
		engine.addSystem(new MovementSystem());
	}

	private void loadTextures() {
		lighthouseBeamTx = new Texture("lighthouse-beam.png");
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

		eldergodsit45Font.draw(batch, "Sanity Meter!", World.WINDOW_X - World.SIDEBAR_WIDTH + 10, 45);
		eldergodsit60Font.draw(batch, "Innsmouth\nLighthouse\nKeeper", World.WINDOW_X - World.SIDEBAR_WIDTH + 2,
				World.WINDOW_Y - 16);

		for (Entity entity : engine.getEntitiesFor(family)) {
			PositionComponent position = entity.getComponent(PositionComponent.class);
			RenderComponent render = entity.getComponent(RenderComponent.class);
			drawEntity(position, render);
		}

		// player-specific rendering

		fb = Family.all(PlayerComponent.class);
		family = fb.get();

		for (Entity entity : engine.getEntitiesFor(family)) {
			PlayerComponent player = entity.getComponent(PlayerComponent.class);
			drawSanityMeter(player.sanity);
		}

		batch.end();
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
		
		//background rectangle
		batch.end();
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0.5f, 0, 0, 1));
		shapeRenderer.rect(meterStartX, meterStartY, meterLength, meterHeight);
		shapeRenderer.end();
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0, 0.8f, 0.3f, 1));
		shapeRenderer.rect(meterStartX, meterStartY, sanityRemaining*lengthMultiplier, meterHeight);
		shapeRenderer.end();
		batch.begin();
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
	public boolean mouseMoved(int screenX, int screenY) {
		boolean foundEntityToUpdate = false;

		// System.err.println("(" + screenX + "," + screenY + ")");

		Builder fb = Family.all(PositionComponent.class, MouseFollowComponent.class);
		Family family = fb.get();

		for (Entity entity : engine.getEntitiesFor(family)) {
			PositionComponent position = entity.getComponent(PositionComponent.class);

			float angle = MathUtils.atan2(screenX - position.pos.x, screenY - position.pos.y);
			angle = angle * (180 / MathUtils.PI);

			position.rotation = angle;
			foundEntityToUpdate = true;
		}
		return foundEntityToUpdate;
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
