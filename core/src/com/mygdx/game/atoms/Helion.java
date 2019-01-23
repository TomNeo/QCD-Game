package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;

import java.util.Random;

/**
 * Created by User on 2/7/2017.
 */

public class Helion extends Circles {

    public static float RADIUS =  Proton.RADIUS * (float)Math.sqrt(3);
    public float lifeSpan = 5;
    private float aliveFor = 0f;
    private Random random = new Random();

    public Helion(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Deuterium.RADIUS;
        this.color = new float[] {0,0,.3f,1};
    }

    @Override
    public void tick(float deltaTime){
        if(lifeSpan<0 && !moving){
            kill = true;
        }
        if(aliveFor <= 1f){
            this.radius = Deuterium.RADIUS + (RADIUS - Deuterium.RADIUS)*(aliveFor/1f);
        }else{
            this.radius = RADIUS;
        }
        lifeSpan = lifeSpan - deltaTime;
        aliveFor = aliveFor + deltaTime;
        color[2] = 1 - ((lifeSpan/5f) * .7f);
    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        super.renderCircle(shapeRenderer);
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.circle(pos.x, pos.y, radius);
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);
        if(game.highlightedCircle != null && game.highlightedCircle.getClass() == Helion.class && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(1f, 1f, 1f, 1f);
            shapeRenderer.circle(pos.x, pos.y, 6);
        }
    }


    public void collided(){
        if(!matchedCircle.kill) {
            Helium tempHelium = new Helium(game, pos.x, pos.y);
            game.addToCircles.add(new Helium(game, pos.x, pos.y));
            float distance = tempHelium.radius + Proton.RADIUS + 1;
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
