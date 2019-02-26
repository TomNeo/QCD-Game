package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mygdx.game.Variables.PROTON_CAPTURE_LIFESPAN_BUMP;

public class Carbon extends Circles {

    private float aliveFor = 0f;
    private float initialLifespan;
    private int protons = 0;
    private Random random = new Random();

    Carbon(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.BERYLLIUM_RADIUS;
        this.color = new float[] {1,0,1,.5f};
        this.lifeSpan = Variables.CARBON_LIFESPAN;
        initialLifespan = lifeSpan;
    }

    @Override
    public void tick(float deltaTime){
        super.tick(deltaTime);
        if(lifeSpan<0){
            kill = true;
        }
        if(aliveFor > 1) {
            radius = Variables.CARBON_RADIUS;
        }else {
            radius =  Variables.BERYLLIUM_RADIUS + ((Variables.CARBON_RADIUS - Variables.BERYLLIUM_RADIUS)*(aliveFor/1));
        }
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
    }


    protected void calculateTimerPositions(){
        this.timerX = this.pos.x + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.CARBON_LIFESPAN)));
        this.timerY = this.pos.y + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.CARBON_LIFESPAN)));
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer) {
        super.renderCircle(shapeRenderer);
        //Draw the main circle
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.end();
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);
        shapeRenderer.end();
        if(protons >= 1){
            shapeRenderer.setColor(1, 1, 1, .4f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x, pos.y + radius/2, Variables.PROTON_RADIUS);
            shapeRenderer.end();
        }
        if (protons >= 2){
            shapeRenderer.setColor(1, 1, 1, .4f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x - radius/ 4 * (float)Math.sqrt(2), pos.y - radius / 4 *(float)Math.sqrt(2), Variables.PROTON_RADIUS);
            shapeRenderer.end();
        }
        if (protons >= 3){
            shapeRenderer.setColor(1, 1, 1, .4f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x + radius/ 4 * (float)Math.sqrt(2), pos.y - radius / 4 *(float)Math.sqrt(2), Variables.PROTON_RADIUS);
            shapeRenderer.end();
        }
        if(game.highlightedCircle != null && (game.highlightedCircle.getClass() == Proton.class) && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x, pos.y, Variables.MATCH_INDICATOR_RADIUS);
            shapeRenderer.end();
        }
    }

    public void collided(){
    }

    void captureProton(){
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
            game.addToCircles.add(new Helium(game,this.pos.x + tempX,this.pos.y + tempY));
            game.soundEffect.play();
            protons = 0;
        }
    }

    public int getProtons(){
        return protons;
    }
}
