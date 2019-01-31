package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

/**
 * Created by User on 2/8/2017.
 */

public class Beryllium extends Circles {

    public float lifeSpan = Variables.BERYLLIUM_LIFESPAN;
    private float aliveFor = 0f;
    private float initialLifespan;

    public Beryllium(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.HELIUM_RADIUS;
        this.color = new float[] {1,0,0,1};
        initialLifespan = lifeSpan;
    }

    @Override
    public void tick(float deltaTime){
        super.tick(deltaTime);
        if(lifeSpan<0){
            kill = true;
        }
        radius =  Variables.HELIUM_RADIUS + (Variables.BERYLLIUM_RADIUS - Variables.HELIUM_RADIUS)*(aliveFor/initialLifespan);
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
        color[2] = lifeSpan/4f;
        color[0] = (4 - lifeSpan)/4f;
    }


    protected void calculateTimerPositions(){
        this.timerX = this.pos.x + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.BERYLLIUM_LIFESPAN)));
        this.timerY = this.pos.y + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.BERYLLIUM_LIFESPAN)));
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer) {
        super.renderCircle(shapeRenderer);
        //Draw the main circle
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius);
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);
        if(game.highlightedCircle != null && game.highlightedCircle.getClass() == Helium.class && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.circle(pos.x, pos.y, Variables.MATCH_INDICATOR_RADIUS);
        }
    }

    public void collided(){
        if(!matchedCircle.kill) {
            game.addToCircles.add(new Carbon(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }
    }

}
