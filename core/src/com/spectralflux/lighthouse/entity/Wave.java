package com.spectralflux.lighthouse.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.spectralflux.lighthouse.World;

/**
 * Helper class for creating a wave of enemies
 */
public class Wave
{
    private Texture squidlingTexture;
    
    public Wave(Texture squidlingTexture) {
        this.squidlingTexture = squidlingTexture;
    }
    
    public List<Entity> createWave(int level) {
        List<Entity> waveEntities = new ArrayList<>();
        EntityFactory entityFactory = new EntityFactory();
        
        switch(level) {
        case 1:
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 0, 0));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, World.GAME_AREA_X, 0));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 100, World.GAME_AREA_Y));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 250, World.GAME_AREA_Y + 10));
            break;
        default:
            break;
        }
        
        return waveEntities;
        
    }

}
