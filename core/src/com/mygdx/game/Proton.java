package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 1/19/2017.
 */

public class Proton extends Circles{

    public static float RADIUS = 50f;
    public float lifeSpan = 3;
    //boolean kill = false;

    public  Proton(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = RADIUS;
        this.color = new float[] {0,1,0,1};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[1] = lifeSpan/3f;
        color[0] = (3 - lifeSpan)/3f;
    }

    public void collided(){
        if(!matchedCircle.kill && matchedCircle.getClass().equals(Deuterium.class)) {
            game.addToCircles.add(new Helion(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }else if(!matchedCircle.kill && matchedCircle.getClass().equals(Proton.class)){
            game.addToCircles.add(new Deuterium(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }
    }

}
