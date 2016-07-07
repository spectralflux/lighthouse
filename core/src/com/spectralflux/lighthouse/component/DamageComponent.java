package com.spectralflux.lighthouse.component;

import com.badlogic.ashley.core.Component;

public class DamageComponent implements Component {
    
    public int dmg;
    
    public DamageComponent(int dmg) {
        this.dmg = dmg;
    }
}
