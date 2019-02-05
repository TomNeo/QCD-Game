package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

/**
 * Created by User on 2/7/2017.
 */

public class Helium extends Circles {
    //public static float RADIUS = Variables.HELIUM_RADIUS;
    public float lifeSpan = Variables.HELIUM_LIFESPAN;
    private float aliveFor = 0f;

    public Helium(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.HELIUM_RADIUS;
        this.color = new float[] {.2f,1,.3f,1};
    }

    @Override
    public void tick(float deltaTime){
        super.tick(deltaTime);
        boolean justOnce = false;
        if(lifeSpan<0 && !moving){
            kill = true;
        }else if(lifeSpan < 1 && !justOnce){
            justOnce = true;
            color = new float[] {.4f, 1, .6f,1};
        }
        if(aliveFor <= 12f) {
            this.radius = Variables.HELION_RADIUS + (Variables.HELIUM_RADIUS - Variables.HELION_RADIUS) * (aliveFor / 12f);
        }
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        super.renderCircle(shapeRenderer);
        //Draw outline
        shapeRenderer.setColor(0f, 0f, 0f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.end();
        //Draw main colored circle
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);
        shapeRenderer.end();
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);
        shapeRenderer.end();
        //If the highlighted circle is a match, draw the indicator circle
        if(game.highlightedCircle != null && (game.highlightedCircle.getClass() == Helium.class || game.highlightedCircle.getClass() == Beryllium.class) && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x, pos.y, Variables.MATCH_INDICATOR_RADIUS);
            shapeRenderer.end();
        }
    }


    protected void calculateTimerPositions(){
        this.timerX = this.pos.x + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.HELIUM_LIFESPAN)));
        this.timerY = this.pos.y + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.HELIUM_LIFESPAN)));
    }

    public void collided(){
        if(!matchedCircle.kill && matchedCircle.getClass().equals(Helium.class)) {
            game.addToCircles.add(new Beryllium(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }else if(!matchedCircle.kill && matchedCircle.getClass().equals(Beryllium.class)){
            game.addToCircles.add(new Carbon(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }
        }

}
