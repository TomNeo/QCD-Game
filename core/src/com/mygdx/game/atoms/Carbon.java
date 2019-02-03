package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

public class Carbon extends Circles {

    public float lifeSpan = Variables.CARBON_LIFESPAN;
    private float aliveFor = 0f;
    private float initialLifespan;

    public Carbon(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.BERYLLIUM_RADIUS;
        this.color = new float[] {1,0,1,.5f};
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
        shapeRenderer.circle(pos.x, pos.y, radius);
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);
    }

    public void collided(){
    }

}
