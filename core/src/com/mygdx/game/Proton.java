package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 1/19/2017.
 */

public class Proton extends Circles{

    public float lifeSpan = 3;
    //boolean kill = false;

    public  Proton(float x, float y){
        this.pos = new Vector2(x,y);
        this.radius = 15f;
        this.color = new float[] {0,1,0,1};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[1] = lifeSpan/3f;
    }

}
