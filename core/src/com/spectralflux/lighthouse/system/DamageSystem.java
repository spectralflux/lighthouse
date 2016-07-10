package com.spectralflux.lighthouse.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.spectralflux.lighthouse.component.DamageComponent;
import com.spectralflux.lighthouse.component.DeathComponent;
import com.spectralflux.lighthouse.component.EnemyComponent;
import com.spectralflux.lighthouse.component.PlayerComponent;

/**
 * System for calculating damage to entities that can receive it.
 *  
 */
public class DamageSystem extends IteratingSystem {

    private ComponentMapper<DamageComponent> dm = ComponentMapper.getFor(DamageComponent.class);
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);

    public DamageSystem () {
        super(Family.all(DamageComponent.class).get());
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        DamageComponent damage = dm.get(entity);
        PlayerComponent player = pm.get(entity);
        EnemyComponent enemy = em.get(entity);
        boolean hasDied = false;
        
        if (player != null) {
            player.sanity -= damage.dmg;
            if (player.sanity <= 0) {
                hasDied = true;
            }
        } else if (enemy != null) {
            enemy.hp -= damage.dmg;
            if (enemy.hp <= 0) {
                hasDied = true;
            }
        }
        
        // damage is done, remove the component
        entity.remove(DamageComponent.class);
        
        if (hasDied) {
            entity.add(new DeathComponent());
        }

    }
}
