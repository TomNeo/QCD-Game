package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/8/2017.
 */

public class Carbon extends Circles {

    public static float RADIUS =  Proton.RADIUS / .1547f;
    public float lifeSpan = 4;

    public Carbon(float x, float y){
        this.pos = new Vector2(x,y);
        this.radius = RADIUS;
        this.color = new float[] {1,0,0,1};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[2] = lifeSpan/4f;
        color[0] = (4 - lifeSpan)/4f;
    }
}
