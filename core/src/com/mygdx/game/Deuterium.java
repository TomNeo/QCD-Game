package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/7/2017.
 */

public class Deuterium extends Circles {

    public static float RADIUS =  Proton.RADIUS * (float)Math.sqrt(2);
    public float lifeSpan = 5;

    public Deuterium(float x, float y){
        this.pos = new Vector2(x,y);
        this.radius = RADIUS;
        this.color = new float[] {1,.2f,.5f,0};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[1] = 1 - ((lifeSpan / 5f) * .8f);
        color[2] = 1 - ((lifeSpan / 5f) * .5f);

    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.setColor(.1f, .1f, .1f, 1f);
        shapeRenderer.circle(pos.x, pos.y, Proton.RADIUS);
    }


}
