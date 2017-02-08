package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/7/2017.
 */

public class Helium extends Circles {

    public float lifeSpan = 12;

    public Helium(float x, float y){
        this.pos = new Vector2(x,y);
        this.radius = 80;
        this.color = new float[] {.2f,1,.3f,1};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[3] = lifeSpan/12f;
    }

}
