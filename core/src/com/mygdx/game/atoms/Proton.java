package com.mygdx.game.atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

/**
 * Created by User on 1/19/2017.
 */

public class Proton extends Circles{

    //public static float RADIUS = 50f;
    //boolean kill = false;

    public Proton(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.PROTON_RADIUS;
        setColor(0, 1, 0, 1);
        this.lifeSpan = Variables.PROTON_LIFESPAN;

        calculateTimerPositions();
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        renderCircle();

        //Draw outline
        shapeRenderer.setColor(0f, 0f, 0f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.end();
        //Draw main colored circle
        shapeRenderer.setColor(getColor());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);
        shapeRenderer.end();
        //Draw the timer line
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);
        shapeRenderer.end();

        if(game.highlightedCircle != null &&
                (game.highlightedCircle.getClass() == Proton.class ||
                        game.highlightedCircle.getClass() == Deuterium.class) &&
                !this.equals(game.highlightedCircle)) {
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],
                    Variables.MATCH_INDICATOR_COLOR[2], Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x, pos.y, Variables.MATCH_INDICATOR_RADIUS);
            shapeRenderer.end();
        }

        Gdx.gl.glLineWidth(1f);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    @Override
    public void act (float delta) {
        super.act(delta);

        if(lifeSpan<0 && !moving){
            kill = true;
        }
        lifeSpan = lifeSpan - delta;
        setColor((Variables.PROTON_LIFESPAN - lifeSpan)/Variables.PROTON_LIFESPAN,
                lifeSpan/Variables.PROTON_LIFESPAN, 0, 1);
    }

    protected void calculateTimerPositions(){
        this.timerX = this.pos.x + (float)(Variables.PROTON_RADIUS * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.PROTON_LIFESPAN)));
        this.timerY = this.pos.y + (float)(Variables.PROTON_RADIUS * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.PROTON_LIFESPAN)));
    }

    public void collided(){
        if(!matchedCircle.kill && matchedCircle.getClass().equals(Deuterium.class)) {
            Helion h = new Helion(game, pos.x, pos.y);
            game.stageShapeRenderer.addActor(h);
            game.addToCircles.add(h);
            matchedCircle.kill = true;
            this.kill = true;
        }else if(!matchedCircle.kill && matchedCircle.getClass().equals(Proton.class)){
            Deuterium d = new Deuterium(game, pos.x, pos.y);
            game.stageShapeRenderer.addActor(d);
            game.addToCircles.add(d);
            matchedCircle.kill = true;
            this.kill = true;
        }else if(!matchedCircle.kill && matchedCircle.getClass().equals(Carbon.class)){
            ((Carbon)matchedCircle).captureProton(this);
            this.kill = true;
        }
        game.soundEffect.play();
    }

}
