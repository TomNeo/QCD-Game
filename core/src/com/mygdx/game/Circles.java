package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 1/28/2017.
 */

public abstract class Circles {


    protected float radius;
    protected Vector2 pos;
    //protected int colorInt;
    protected float[] color = {0,0,0,0};
    public boolean kill = false;

    public void tick(float deltaTime){

    }

    public float getRadius(){
        return radius;
    }

    public Vector2 getPosition(){
        return pos;
    }

    /***
     *
     * @param shapeRenderer
     */
    public void renderCircle(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius);
    }

}
