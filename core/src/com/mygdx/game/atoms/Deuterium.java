package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;

/**
 * Created by User on 2/7/2017.
 */

public class Deuterium extends Circles {

    public static float RADIUS =  Proton.RADIUS * (float)Math.sqrt(2);
    public float lifeSpan = 10;
    private float aliveFor = 0f;

    public Deuterium(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Proton.RADIUS;
        this.color = new float[] {1,.2f,.5f,0};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0 && !moving) {
            kill = true;
        }
        if(aliveFor <= 1.5f){
            this.radius = Proton.RADIUS + (RADIUS - Proton.RADIUS)*(aliveFor/1.5f);
        }else{
            this.radius = RADIUS;
        }
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
        color[1] = 1 - ((lifeSpan / 5f) * .8f);
        color[2] = 1 - ((lifeSpan / 5f) * .5f);
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        super.renderCircle(shapeRenderer);
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);
        shapeRenderer.setColor(.1f, .1f, .1f, 1f);
        shapeRenderer.circle(pos.x, pos.y, radius * (Proton.RADIUS/RADIUS));
        if(game.highlightedCircle != null && game.highlightedCircle.getClass() == Proton.class && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(1f, 1f, 1f, 1f);
            shapeRenderer.circle(pos.x, pos.y, 6);
        }
    }


    public void collided(){
        if(!matchedCircle.kill) {
            game.addToCircles.add(new Helion(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }
    }

}
