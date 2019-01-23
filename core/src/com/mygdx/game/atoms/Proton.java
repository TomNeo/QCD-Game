package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;

/**
 * Created by User on 1/19/2017.
 */

public class Proton extends Circles{

    public static float RADIUS = 50f;
    public float lifeSpan = 10;
    //boolean kill = false;

    public Proton(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = RADIUS;
        this.color = new float[] {0,1,0,1};
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        super.renderCircle(shapeRenderer);
        shapeRenderer.setColor(0f, 0f, 0f, 1f);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);

        if(game.highlightedCircle != null && (game.highlightedCircle.getClass() == Proton.class || game.highlightedCircle.getClass() == Deuterium.class) && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(1f, 1f, 1f, 1f);
            shapeRenderer.circle(pos.x, pos.y, 6);
        }
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0 && !moving){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[1] = lifeSpan/10f;
        color[0] = (10 - lifeSpan)/10f;
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
