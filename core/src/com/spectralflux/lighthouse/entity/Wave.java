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
    private Texture serpentTexture;
    private Texture flyerTexture;
    
    public Wave(Texture squidlingTexture, Texture serpentTexture, Texture flyerTexture) {
        this.squidlingTexture = squidlingTexture;
        this.serpentTexture = serpentTexture;
        this.flyerTexture = flyerTexture;
    }
    
    public List<Entity> createWave(int level) {
        List<Entity> waveEntities = new ArrayList<>();
        EntityFactory entityFactory = new EntityFactory();
        
        switch(level) {
        case 1:
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 0, 0));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, World.GAME_AREA_X, 0));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 100, 0));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 290, 0));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 100, World.GAME_AREA_Y));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 250, World.GAME_AREA_Y + 10));
            waveEntities.add(entityFactory.newSquidling(squidlingTexture, 400, World.GAME_AREA_Y + 20));
            
            waveEntities.add(entityFactory.newSerpent(serpentTexture, 400, 0));
            waveEntities.add(entityFactory.newSquidling(serpentTexture, 25, 0));
            waveEntities.add(entityFactory.newSquidling(serpentTexture, 400, World.GAME_AREA_Y + 20));
            
            waveEntities.add(entityFactory.newFlyer(flyerTexture, 400, World.GAME_AREA_Y + 20));
            waveEntities.add(entityFactory.newFlyer(flyerTexture, 340, 0));
            break;
        default:
            break;
        }
        
        return waveEntities;
        
    }

}
