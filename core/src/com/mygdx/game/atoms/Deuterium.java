package com.mygdx.game.atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        setPosition(x, y);
        this.radius = Variables.PROTON_RADIUS;
        setColor(1, 0.2f, 0.5f, 1);
        this.lifeSpan = Variables.DEUTERIUM_LIFESPAN;

        calculateTimerPositions();

        borders.add(new WavyBorder(this));
    }

    @Override
    public void act (float delta) {
        super.act(delta);

        if(lifeSpan<0 && !moving) {
            kill = true;
        }
        if(aliveFor <= 1.5f){
            this.radius = Variables.PROTON_RADIUS + (Variables.DEUTERIUM_RADIUS - Variables.PROTON_RADIUS)*(aliveFor/1.5f);
        }else{
            this.radius = Variables.DEUTERIUM_RADIUS;
        }
        lifeSpan = lifeSpan - delta;
        aliveFor = aliveFor + delta;
        setColor(1, 1 - ((lifeSpan / 5f) * .8f), 1 - ((lifeSpan / 5f) * .5f), 1);
    }

    protected void calculateTimerPositions(){
        this.timerX = this.getX() + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.DEUTERIUM_LIFESPAN)));
        this.timerY = this.getY() + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.DEUTERIUM_LIFESPAN)));
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        renderCircle();

        //Draw the outline
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX(), getY(), radius);
        shapeRenderer.end();
        //Draw the colored pink part
        shapeRenderer.setColor(getColor());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX(), getY(), radius-2f);
        shapeRenderer.end();
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(getX(), getY(),this.timerX,this.timerY);
        shapeRenderer.end();
        //Draw the blank void meant to represent where a proton goes
        shapeRenderer.setColor(.1f, .1f, .1f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX(), getY(), radius * (Variables.PROTON_RADIUS/Variables.DEUTERIUM_RADIUS));
        shapeRenderer.end();
        //If the highlighted circle is a match, draw the indicator circle
        if(game.highlightedCircle != null && game.highlightedCircle.getClass() == Proton.class && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(getX(), getY(), Variables.MATCH_INDICATOR_RADIUS);
            shapeRenderer.end();
        }

        Gdx.gl.glLineWidth(1f);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    public void collided(){
        if(!matchedCircle.kill) {
            Helion h = new Helion(game, getX(), getY());
            game.gameStage.addActor(h);
            game.addToCircles.add(h);
            matchedCircle.kill = true;
            this.kill = true;
            game.soundEffect.play();
        }
    }

}
