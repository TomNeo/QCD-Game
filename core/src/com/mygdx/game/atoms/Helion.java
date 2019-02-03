package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

import java.util.Random;

/**
 * Created by User on 2/7/2017.
 */

public class Helion extends Circles {

    //public static float RADIUS = Variables.HELION_RADIUS;
    public float lifeSpan = Variables.HELION_LIFESPAN;
    private float aliveFor = 0f;
    private Random random = new Random();

    public Helion(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.DEUTERIUM_RADIUS;
        this.color = new float[] {0,0,.3f,1};
    }

    @Override
    public void tick(float deltaTime){
        super.tick(deltaTime);
        if(lifeSpan<0 && !moving){
            kill = true;
        }
        if(aliveFor <= 1f){
            this.radius = Variables.DEUTERIUM_RADIUS + (Variables.HELION_RADIUS - Variables.DEUTERIUM_RADIUS)*(aliveFor/1f);
        }else{
            this.radius = Variables.HELION_RADIUS;
        }
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
        color[2] = 1 - ((lifeSpan/5f) * .7f);
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        super.renderCircle(shapeRenderer);
        //Draw outline
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.circle(pos.x, pos.y, radius);
        //Draw main colored circle
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);
        //If the highlighted circle is a match, draw the indicator circle
        if(game.highlightedCircle != null && game.highlightedCircle.getClass() == Helion.class && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.circle(pos.x, pos.y, Variables.MATCH_INDICATOR_RADIUS);
        }
    }


    protected void calculateTimerPositions(){
        this.timerX = this.pos.x + (float)(radius * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.HELION_LIFESPAN)));
        this.timerY = this.pos.y + (float)(radius * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.HELION_LIFESPAN)));
    }


    public void collided(){
        if(!matchedCircle.kill) {
            Helium tempHelium = new Helium(game, pos.x, pos.y);
            game.addToCircles.add(new Helium(game, pos.x, pos.y));
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
            game.addToCircles.add(new Proton(game,tempHelium.pos.x + tempX,tempHelium.pos.y + tempY));
            angle = (float)(Math.random() * 90f);
            tempX = (float)(Math.sin(Math.PI/180 * angle)* distance);
            if(random.nextBoolean()){
                tempX = -tempX;
            }
            tempY = (float)(Math.sin(Math.PI/180 * (90 - angle))* distance);
            if(random.nextBoolean()){
                tempY = -tempY;
            }
            game.addToCircles.add(new Proton(game,tempHelium.pos.x + tempX,tempHelium.pos.y + tempY));
            matchedCircle.kill = true;
            this.kill = true;
        }
    }


}
