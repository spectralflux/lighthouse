package com.spectralflux.lighthouse.component;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
	
	public int hp;
	
	public EnemyComponent(int hp) {
		this.hp = hp;
	}

}
