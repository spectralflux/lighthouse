package com.spectralflux.lighthouse;

import com.badlogic.gdx.Game;

public class GdxGame extends Game {
	
	
	@Override
	public void create () {
		this.setScreen(new GameScreen(this));
		
	}

	@Override
	public void render () {
		super.render();
	}
}
