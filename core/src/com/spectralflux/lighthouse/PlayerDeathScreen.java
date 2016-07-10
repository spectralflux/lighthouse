package com.spectralflux.lighthouse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerDeathScreen implements Screen {

	Game game;
	SpriteBatch batch;
	BitmapFont eldergodsit60Font;
	BitmapFont eldergodsit45Font;

	public PlayerDeathScreen(Game game) {
		batch = new SpriteBatch();
		
		eldergodsit60Font = new BitmapFont(Gdx.files.internal("fonts/eldergodsit60.fnt"));
		eldergodsit60Font.setColor(Color.RED);
		
		eldergodsit45Font = new BitmapFont(Gdx.files.internal("fonts/eldergodsit45.fnt"));
		eldergodsit45Font.setColor(Color.WHITE);
		this.game = game;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		eldergodsit60Font.draw(batch, "You have gone mad! Ia! Ia!", 170, 450);
		eldergodsit45Font.draw(batch, "click to restart...", 300, 350);
		batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
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