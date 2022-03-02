package com.mygdx.game.atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

import java.util.ArrayList;
import java.util.Random;

public class Carbon extends Circles {

    private float aliveFor = 0f;
    private float initialLifespan;
    private int protons = 0;
    public ArrayList<Proton> matchedProtons = new ArrayList<Proton>();
    private Random random = new Random();

    Carbon(MyGdxGame main, float x, float y){
        super(main);
        setPosition(x, y);
        this.radius = Variables.BERYLLIUM_RADIUS;
        setColor(1, 0, 1, 0.5f);
        this.lifeSpan = Variables.CARBON_LIFESPAN;
        initialLifespan = lifeSpan;

        calculateTimerPositions();

        borders.add(new WavyBorder(this));
    }

    @Override
    public void act (float delta) {
        super.act(delta);

        if(lifeSpan<0 && matchedProtons.size() == 0){
            kill = true;
        }
        if(aliveFor > 1) {
            radius = Variables.CARBON_RADIUS;
        }else {
            radius =  Variables.BERYLLIUM_RADIUS + ((Variables.CARBON_RADIUS - Variables.BERYLLIUM_RADIUS)*(aliveFor/1));
        }
        lifeSpan = lifeSpan - delta;
        aliveFor = aliveFor + delta;
    }

    protected void calculateTimerPositions(){
        this.timerX = this.getX() + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.CARBON_LIFESPAN)));
        this.timerY = this.getY() + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.CARBON_LIFESPAN)));
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        renderCircle();

        shapeRenderer.setColor(getColor());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(getX(), getY(), radius);
        shapeRenderer.end();
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(getX(), getY(),this.timerX,this.timerY);
        shapeRenderer.end();
        if(protons >= 1){
            shapeRenderer.setColor(1, 1, 1, .4f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(getX(), getY() + radius/2, Variables.PROTON_RADIUS);
            shapeRenderer.end();
        }
        if (protons >= 2){
            shapeRenderer.setColor(1, 1, 1, .4f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(getX() - radius/ 4 * (float)Math.sqrt(2), getY() - radius / 4 *(float)Math.sqrt(2), Variables.PROTON_RADIUS);
            shapeRenderer.end();
        }
        if (protons >= 3){
            shapeRenderer.setColor(1, 1, 1, .4f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(getX() + radius/ 4 * (float)Math.sqrt(2), getY() - radius / 4 *(float)Math.sqrt(2), Variables.PROTON_RADIUS);
            shapeRenderer.end();
        }
        if(game.highlightedCircle != null && (game.highlightedCircle.getClass() == Proton.class) && !this.equals(game.highlightedCircle)){
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
    }

    void captureProton(Proton capturedProton){
        matchedProtons.remove(capturedProton);
        protons++;
        if(lifeSpan + Variables.PROTON_CAPTURE_LIFESPAN_BUMP > Variables.CARBON_LIFESPAN){
            lifeSpan = Variables.CARBON_LIFESPAN;
        }else{
            lifeSpan = lifeSpan + Variables.PROTON_CAPTURE_LIFESPAN_BUMP;
        }
        if(protons >= 4){
            float distance = this.radius + Variables.HELIUM_RADIUS + 1;
            float angle = (float)(Math.random() * 90f);
            float tempX = (float)(Math.sin(Math.PI/180 * angle)* distance);
            if(random.nextBoolean()){
                tempX = -tempX;
            }
            float tempY = (float)(Math.sin(Math.PI/180 * (90 - angle))* distance);
            if(random.nextBoolean()){
                tempY = -tempY;
            }
            Helium h = new Helium(game,this.getX() + tempX,this.getY() + tempY);
            game.gameStage.addActor(h);
            game.addToCircles.add(h);
            //game.soundEffect.play();
            protons = 0;
        }
    }

    public int getProtons(){
        return protons;
    }
}
