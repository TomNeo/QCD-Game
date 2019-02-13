package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;
import com.sun.org.apache.xpath.internal.operations.Variable;

/**
 * Created by User on 1/19/2017.
 */

public class Proton extends Circles{

    //public static float RADIUS = 50f;
    public float lifeSpan = Variables.PROTON_LIFESPAN;

    //boolean kill = false;

    public Proton(MyGdxGame main, float x, float y){
        super(main);
        this.pos = new Vector2(x,y);
        this.radius = Variables.PROTON_RADIUS;
        this.color = new float[] {0,1,0,1};

    }

    @Override
    public void renderCircle(ShapeRenderer shapeRenderer){
        super.renderCircle(shapeRenderer);
        //Draw outline
        shapeRenderer.setColor(0f, 0f, 0f, 1f);
        shapeRenderer.circle(pos.x, pos.y, radius);
        //Draw main colored circle
        shapeRenderer.setColor(color[0], color[1], color[2], color[3]);
        shapeRenderer.circle(pos.x, pos.y, radius-2f);
        //Draw the timer line
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.line(pos.x, pos.y,this.timerX,this.timerY);

        if(game.highlightedCircle != null && (game.highlightedCircle.getClass() == Proton.class || game.highlightedCircle.getClass() == Deuterium.class || game.highlightedCircle.getClass() == Carbon.class) && !this.equals(game.highlightedCircle)){
            shapeRenderer.setColor(Variables.MATCH_INDICATOR_COLOR[0], Variables.MATCH_INDICATOR_COLOR[1],Variables.MATCH_INDICATOR_COLOR[2],Variables.MATCH_INDICATOR_COLOR[3]);
            shapeRenderer.circle(pos.x, pos.y, Variables.MATCH_INDICATOR_RADIUS);
        }
    }

    @Override
    public void tick(float deltaTime){
        super.tick(deltaTime);
        if(lifeSpan<0 && !moving){
            kill = true;
        }
        lifeSpan = lifeSpan - deltaTime;
        color[1] = lifeSpan/Variables.PROTON_LIFESPAN;
        color[0] = (Variables.PROTON_LIFESPAN - lifeSpan)/Variables.PROTON_LIFESPAN;
    }

    protected void calculateTimerPositions(){
        this.timerX = this.pos.x + (float)(Variables.PROTON_RADIUS * Math.sin(Math.toRadians(360) * (lifeSpan/Variables.PROTON_LIFESPAN)));
        this.timerY = this.pos.y + (float)(Variables.PROTON_RADIUS * Math.cos(Math.toRadians(360) * (lifeSpan/Variables.PROTON_LIFESPAN)));
    }

    public void collided(){
        if(!matchedCircle.kill && matchedCircle.getClass().equals(Deuterium.class)) {
            game.addToCircles.add(new Helion(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }else if(!matchedCircle.kill && matchedCircle.getClass().equals(Proton.class)){
            game.addToCircles.add(new Deuterium(game, pos.x, pos.y));
            matchedCircle.kill = true;
            this.kill = true;
        }else if(!matchedCircle.kill && matchedCircle.getClass().equals(Carbon.class)){
            ((Carbon)matchedCircle).captureProton();
            this.kill = true;
        }
        game.soundEffect.play();

    }

}
