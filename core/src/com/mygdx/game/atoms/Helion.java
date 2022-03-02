package com.mygdx.game.atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

import java.util.Random;

/**
 * Created by User on 2/7/2017.
 */

public class Helion extends Circles {

    //public static float RADIUS = Variables.HELION_RADIUS;
    private float aliveFor = 0f;
    private Random random = new Random();

    Helion(MyGdxGame main, float x, float y){
        super(main);
        setPosition(x, y);
        this.radius = Variables.DEUTERIUM_RADIUS;
        setColor(0, 0, 0.3f, 1);
        this.lifeSpan = Variables.HELION_LIFESPAN;

        calculateTimerPositions();

        borders.add(new WavyBorder(this));
    }

    @Override
    public void act (float delta) {
        super.act(delta);

        if(lifeSpan<0 && !moving){
            kill = true;
        }
        if(aliveFor <= 1f){
            this.radius = Variables.DEUTERIUM_RADIUS + (Variables.HELION_RADIUS - Variables.DEUTERIUM_RADIUS)*(aliveFor/1f);
        }else{
            this.radius = Variables.HELION_RADIUS;
        }
        lifeSpan = lifeSpan - delta;
        aliveFor = aliveFor + delta;
        setColor(0, 0, 1 - ((lifeSpan/5f) * .7f), 1);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        renderCircle();

        //Draw outline
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
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
        if(game.highlightedCircle != null && game.highlightedCircle.getClass() == Helion.class && !this.equals(game.highlightedCircle)){
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
        this.timerX = this.getX() + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.HELION_LIFESPAN)));
        this.timerY = this.getY() + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.HELION_LIFESPAN)));
    }

    public void collided(){
        if(!matchedCircle.kill) {
            Helium tempHelium = new Helium(game, getX(), getY());
            Helium h = new Helium(game, getX(), getY());
            game.gameStage.addActor(h);
            game.addToCircles.add(h);
            float distance = tempHelium.radius + Variables.PROTON_RADIUS + 1;
            float angle = (float)(Math.random() * 90f);
            float tempX = (float)(Math.sin(Math.PI/180 * angle)* distance);
            if(random.nextBoolean()){
                tempX = -tempX;
            }
            float tempY = (float)(Math.sin(Math.PI/180 * (90 - angle))* distance);
            if(random.nextBoolean()){
                tempY = -tempY;
            }
            Proton p1 = new Proton(game,tempHelium.getX() + tempX,tempHelium.getY() + tempY);
            game.gameStage.addActor(p1);
            game.addToCircles.add(p1);
            angle = (float)(Math.random() * 90f);
            tempX = (float)(Math.sin(Math.PI/180 * angle)* distance);
            if(random.nextBoolean()){
                tempX = -tempX;
            }
            tempY = (float)(Math.sin(Math.PI/180 * (90 - angle))* distance);
            if(random.nextBoolean()){
                tempY = -tempY;
            }
            Proton p2 = new Proton(game,tempHelium.getX() + tempX,tempHelium.getY() + tempY);
            game.gameStage.addActor(p2);
            game.addToCircles.add(p2);
            matchedCircle.kill = true;
            this.kill = true;
            //game.soundEffect.play();
        }
    }

}
