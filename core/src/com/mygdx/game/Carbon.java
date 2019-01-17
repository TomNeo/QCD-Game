package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/8/2017.
 */

public class Carbon extends Circles {

    public static float RADIUS =  Proton.RADIUS / .1547f;
    public float lifeSpan = 6;
    private float aliveFor = 0f;

    public Carbon(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Helium.RADIUS;
        this.color = new float[] {1,0,0,1};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        radius =  Helium.RADIUS + (RADIUS - Helium.RADIUS)*(aliveFor/6f);
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
        color[2] = lifeSpan/4f;
        color[0] = (4 - lifeSpan)/4f;
    }


    @Override
    public void renderCircle(ShapeRenderer shapeRenderer) {
        super.renderCircle(shapeRenderer);
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius);
    }

    public void collided(){
    }

}
