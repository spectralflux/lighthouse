package com.spectralflux.lighthouse.component;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
	
	public int hp;
	public int points;
	public int dmg;
	
	public EnemyComponent(int hp, int points, int dmg) {
		this.hp = hp;
		this.points = points;
		this.dmg = dmg;
	}

}
