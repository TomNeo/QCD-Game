package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by User on 2/7/2017.
 */

public class Helion extends Circles {

    public static float RADIUS =  Proton.RADIUS * (float)Math.sqrt(3);
    public float lifeSpan = 5;
    private float aliveFor = 0f;

    public Helion(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Deuterium.RADIUS;
        this.color = new float[] {0,0,.3f,1};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0){
            kill = true;
        }
        if(aliveFor <= 1f){
            this.radius = Deuterium.RADIUS + (RADIUS - Deuterium.RADIUS)*(aliveFor/1f);
        }else{
            this.radius = RADIUS;
        }
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
        color[2] = 1 - ((lifeSpan/5f) * .7f);
    }

    public void collided(){
        if(!matchedCircle.kill) {
            game.addToCircles.add(new Helium(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }
    }


}
