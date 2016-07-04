package com.spectralflux.lighthouse.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.spectralflux.lighthouse.GdxGame;
import com.spectralflux.lighthouse.World;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Innsmouth Lighthouse Keeper";
		
		config.width = World.WINDOW_X;
		config.height = World.WINDOW_Y;
		new LwjglApplication(new GdxGame(), config);
	}
}
