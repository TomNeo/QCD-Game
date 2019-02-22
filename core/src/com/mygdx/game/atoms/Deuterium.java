package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

/**
 * Created by User on 2/7/2017.
 */

public class Deuterium extends Circles {

    //public static float RADIUS = Variables.DEUTERIUM_RADIUS;
    private float aliveFor = 0f;

    Deuterium(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.PROTON_RADIUS;
        this.color = new float[] {1,.2f,.5f,1};
        this.lifeSpan = Variables.DEUTERIUM_LIFESPAN;
    }

    @Override
    public void tick(float deltaTime){
        super.tick(deltaTime);
        if(lifeSpan<0 && !moving) {
            kill = true;
        }
        if(aliveFor <= 1.5f){
            this.radius = Variables.PROTON_RADIUS + (Variables.DEUTERIUM_RADIUS - Variables.PROTON_RADIUS)*(aliveFor/1.5f);
        }else{
            this.radius = Variables.DEUTERIUM_RADIUS;
        }
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
        color[1] = 1 - ((lifeSpan / 5f) * .8f);
        color[2] = 1 - ((lifeSpan / 5f) * .5f);
    }

    protected void calculateTimerPositions(){
        this.timerX = this.pos.x + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.DEUTERIUM_LIFESPAN)));
        this.timerY = this.pos.y + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.DEUTERIUM_LIFESPAN)));
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        super.renderCircle(shapeRenderer);

        //Draw the outline
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.end();
        //Draw the colored pink part
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);
        shapeRenderer.end();
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);
        shapeRenderer.end();
        //Draw the blank void meant to represent where a proton goes
        shapeRenderer.setColor(.1f, .1f, .1f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius * (Variables.PROTON_RADIUS/Variables.DEUTERIUM_RADIUS));
        shapeRenderer.end();
        //If the highlighted circle is a match, draw the indicator circle
        if(game.highlightedCircle != null && game.highlightedCircle.getClass() == Proton.class && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x, pos.y, Variables.MATCH_INDICATOR_RADIUS);
            shapeRenderer.end();
        }
    }


    public void collided(){
        if(!matchedCircle.kill) {
            game.addToCircles.add(new Helion(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
            game.soundEffect.play();

        }
    }

}
