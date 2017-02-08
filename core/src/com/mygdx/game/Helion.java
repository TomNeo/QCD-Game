package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/7/2017.
 */

public class Helion extends Circles {


    public float lifeSpan = 5;

    public Helion(float x, float y){
        this.pos = new Vector2(x,y);
        this.radius = 60;
        this.color = new float[] {0,0,.5f,1};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[3] = lifeSpan/5f;
    }

}
