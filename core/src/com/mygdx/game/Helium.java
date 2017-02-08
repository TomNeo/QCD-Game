package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/7/2017.
 */

public class Helium extends Circles {
    public static float RADIUS = Proton.RADIUS * 2;
    public float lifeSpan = 12;

    public Helium(float x, float y){
        this.pos = new Vector2(x,y);
        this.radius = RADIUS;
        this.color = new float[] {.2f,1,.3f,1};
    }

    @Override
    public void tick(float deltaTime){
        boolean justOnce = false;
        if(lifeSpan<0){
            kill = true;
        }else if(lifeSpan < 1 && !justOnce){
            justOnce = true;
            color = new float[] {.4f, 1, .6f,1};
        }
        lifeSpan = lifeSpan - deltaTime;
    }

}
