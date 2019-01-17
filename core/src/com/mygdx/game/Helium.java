package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/7/2017.
 */

public class Helium extends Circles {
    public static float RADIUS = Proton.RADIUS * 2;
    public float lifeSpan = 12;
    private float aliveFor = 0f;

    public Helium(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = RADIUS;
        this.color = new float[] {.2f,1,.3f,1};
    }

    @Override
    public void tick(float deltaTime){
        boolean justOnce = false;
        if(lifeSpan<0 && !moving){
            kill = true;
        }else if(lifeSpan < 1 && !justOnce){
            justOnce = true;
            color = new float[] {.4f, 1, .6f,1};
        }
        if(aliveFor <= 12f) {
            this.radius = Helion.RADIUS + (RADIUS - Helion.RADIUS) * (aliveFor / 12f);
        }
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        super.renderCircle(shapeRenderer);
        shapeRenderer.setColor(0f, 0f, 0f, 1f);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);
    }


    public void collided(){
            if(!matchedCircle.kill) {
                game.addToCircles.add(new Carbon(game, pos.x, pos.y));
                matchedCircle.kill = true;
                this.kill = true;
            }
        }

}
