package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 1/28/2017.
 */

public abstract class Circles {


    private static float MOVE_RATE = 1000f;
    protected MyGdxGame game;
    protected float radius;
    protected Vector2 pos;
    //protected int colorInt;
    protected float[] color = {0,0,0,0};
    public boolean kill = false;
    public boolean moving = false;
    private float travelToX = -1;
    private float travelToY = -1;
    protected Circles matchedCircle = null;

    public Circles(MyGdxGame main){
        game = main;
    }

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

    public void setTravelTo(float x, float y, Circles circle){
        moving = true;
        travelToX = x;
        travelToY = y;
        matchedCircle = circle;
    }

    public void move(float deltaTime){
        if(pos.x == travelToX && pos.y == travelToY){
            collided();
        }else{
            float h = (float)Math.sqrt((pos.x - travelToX)*(pos.x - travelToX)+(pos.y - travelToY)*(pos.y - travelToY));
            float translationRatio = deltaTime*MOVE_RATE/h;
            if(pos.x > travelToX){
                if(pos.x - ((pos.x - travelToX)*translationRatio) <= travelToX){
                    pos.x = travelToX;
                }else{
                    pos.x = pos.x - ((pos.x - travelToX)*translationRatio);
                }
            }else if(pos.x < travelToX){
                if(pos.x + ((travelToX - pos.x)*translationRatio) >= travelToX){
                    pos.x = travelToX;
                }else{
                    pos.x = pos.x + ((travelToX - pos.x)*translationRatio);
                }
            }

            if(pos.y > travelToY){
                if(pos.y - ((pos.y - travelToY)*translationRatio) <= travelToY){
                    pos.y = travelToY;
                }else{
                    pos.y = pos.y - ((pos.y - travelToY)*translationRatio);
                }
            }else if(pos.y < travelToY){
                if(pos.y + ((travelToY - pos.y)*translationRatio) >= travelToY){
                    pos.y = travelToY;
                }else{
                    pos.y = pos.y + ((travelToY - pos.y)*translationRatio);
                }
            }

        }
    }

    protected abstract void collided();

}
