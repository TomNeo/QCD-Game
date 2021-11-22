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

public class Helium extends Circles {
    //public static float RADIUS = Variables.HELIUM_RADIUS;
    private float aliveFor = 0f;

    Helium(MyGdxGame main, float x, float y){
        super(main);
        setPosition(x, y);
        this.radius = Variables.HELIUM_RADIUS;
        setColor(0.2f, 1, 0.3f, 1);
        this.lifeSpan = Variables.HELIUM_LIFESPAN;

        calculateTimerPositions();
        borders.add(new WavyBorder(this));
        midiChannel = 3;
        midiNote = 71;
        noteVelocity = 100;

        game.midiTools.startMidiChannel(midiChannel, midiNote,noteVelocity, game.midiTools.changeInsturment(1));
        game.midiTools.startMidiChannel(3, 71,100,game.midiTools.changeInsturment(15));
    }

    @Override
    public void act (float delta) {
        super.act(delta);

        boolean justOnce = false;
        if(lifeSpan<0 && !moving){
            kill = true;
        }else if(lifeSpan < 1 && !justOnce){
            justOnce = true;
            setColor(0.4f, 1, 0.6f, 1);
        }
        if(aliveFor <= 12f) {
            this.radius = Variables.HELION_RADIUS + (Variables.HELIUM_RADIUS - Variables.HELION_RADIUS) * (aliveFor / 12f);
        }
        lifeSpan = lifeSpan - delta;
        aliveFor = aliveFor + delta;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        renderCircle();

        //Draw outline
        shapeRenderer.setColor(0f, 0f, 0f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX(), getY(), radius);
        shapeRenderer.end();
        //Draw main colored circle
        shapeRenderer.setColor(getColor());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX(), getY(), radius-2f);
        shapeRenderer.end();
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(getX(), getY(),this.timerX,this.timerY);
        shapeRenderer.end();
        //If the highlighted circle is a match, draw the indicator circle
        if(game.highlightedCircle != null && (game.highlightedCircle.getClass() == Helium.class || game.highlightedCircle.getClass() == Beryllium.class) && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(getX(), getY(), Variables.MATCH_INDICATOR_RADIUS);
            shapeRenderer.end();
        }

        Gdx.gl.glLineWidth(1f);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
    }

    protected void calculateTimerPositions(){
        this.timerX = this.getX() + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.HELIUM_LIFESPAN)));
        this.timerY = this.getY() + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.HELIUM_LIFESPAN)));
    }

    public void collided(){
        if(!matchedCircle.kill && matchedCircle.getClass().equals(Helium.class)) {
            Beryllium b = new Beryllium(game, getX(), getY());
            game.gameStage.addActor(b);
            game.addToCircles.add(b);
            matchedCircle.kill = true;
            this.kill = true;
        }else if(!matchedCircle.kill && matchedCircle.getClass().equals(Beryllium.class)){
            Carbon c = new Carbon(game, getX(), getY());
            game.gameStage.addActor(c);
            game.addToCircles.add(c);
            matchedCircle.kill = true;
            this.kill = true;
        }
    }

}
