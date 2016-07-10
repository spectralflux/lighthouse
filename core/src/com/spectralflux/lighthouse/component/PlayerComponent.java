package com.spectralflux.lighthouse.component;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.spectralflux.lighthouse.World;

public class PlayerComponent implements Component {
	
	public int sanity;
	public int score;
	public List<PowerupComponent> powerups;
	
	public PlayerComponent() {
		sanity = World.TOTAL_SANITY;
		score = 0;
		powerups = new ArrayList<>();
	}

}
