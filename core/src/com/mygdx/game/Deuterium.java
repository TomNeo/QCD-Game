package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/7/2017.
 */

public class Deuterium extends Circles {

    public float lifeSpan = 5;

    public Deuterium(float x, float y){
        this.pos = new Vector2(x,y);
        this.radius = 45;
        this.color = new float[] {1,.2f,.5f,0};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[3] = lifeSpan/10f;
    }

}
