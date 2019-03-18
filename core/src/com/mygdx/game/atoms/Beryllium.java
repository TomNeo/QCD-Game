package com.mygdx.game.atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

/**
 * Created by User on 2/8/2017.
 */

public class Beryllium extends Circles {

    private float aliveFor = 0f;
    private float initialLifespan;

    Beryllium(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.HELIUM_RADIUS;
        setColor(1, 0, 0, 1);
        this.lifeSpan = Variables.BERYLLIUM_LIFESPAN;
        initialLifespan = lifeSpan;

        calculateTimerPositions();
    }

    @Override
    public void act (float delta) {
        super.act(delta);

        if(lifeSpan<0 && !moving){
            kill = true;
        }
        radius =  Variables.HELIUM_RADIUS + (Variables.BERYLLIUM_RADIUS - Variables.HELIUM_RADIUS)*(aliveFor/initialLifespan);
        lifeSpan = lifeSpan - delta;
        aliveFor = aliveFor + delta;
        setColor((4 - lifeSpan)/4f, 0, lifeSpan/4f, 1);
    }

    protected void calculateTimerPositions(){
        this.timerX = this.pos.x + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.BERYLLIUM_LIFESPAN)));
        this.timerY = this.pos.y + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.BERYLLIUM_LIFESPAN)));
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        renderCircle();

        //Draw the main circle
        shapeRenderer.setColor(getColor());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.end();
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);
        shapeRenderer.end();
        if(game.highlightedCircle != null && game.highlightedCircle.getClass() == Helium.class && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x, pos.y, Variables.MATCH_INDICATOR_RADIUS);
            shapeRenderer.end();
        }

        Gdx.gl.glLineWidth(1f);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    public void collided(){
        if(!matchedCircle.kill) {
            Carbon c = new Carbon(game, pos.x, pos.y);
            game.stageShapeRenderer.addActor(c);
            game.addToCircles.add(c);
            matchedCircle.kill = true;
            this.kill = true;
            game.soundEffect.play();
        }
    }

}
