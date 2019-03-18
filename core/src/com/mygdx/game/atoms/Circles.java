package com.mygdx.game.atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Variables;

import java.util.ArrayList;

/**
 * Created by User on 1/28/2017.
 */

public abstract class Circles extends Actor {

    private static float MOVE_RATE = 1000f;
    private static float EFFECTIVE_RANGE = 250f;
    private static float STANDARD_FORCE = .25f;

    protected MyGdxGame game;

    public boolean kill = false;
    public boolean moving = false;
    private boolean highlighted = false;

    Circles matchedCircle = null;

    float lifeSpan;
    float radius;
    float timerX = 0;
    float timerY = 0;
    private float travelToX = -1;
    private float travelToY = -1;

    //protected int colorInt;

    ShapeRenderer shapeRenderer;

    public Vector2 pos;
    private Vector2 velocity = new Vector2();

    public Circles(MyGdxGame main){
        game = main;
        shapeRenderer = game.shapeRenderer;
    }

    @Override
    public void act (float delta) {
        super.act(delta);

        if (moving) {
            move(delta);
        } else {
            applyAttraction(delta);
        }

        game.allCircles.addAll(game.addToCircles);
        game.addToCircles.clear();

        ArrayList<Circles> killList = new ArrayList<Circles>();
        for(Circles circle : game.allCircles){
            if(circle.kill){

                if (circle.getClass() == Carbon.class){
                    int breakpoint = 1;
                    int otherline = breakpoint + 1;
                }
                killList.add(circle);
                if(circle.getHighlighted()){
                    game.highlightedCircle = null;
                    circle.setHighlighted(false);
                }
            }
        }
        for(Circles circle : killList){
            circle.remove();
            game.allCircles.remove(circle);
        }
        killList.clear();

        calculateTimerPositions();
    }

    private boolean getHighlighted(){
        return highlighted;
    }

    public void setHighlighted(boolean value){
        highlighted = value;
    }
    public float getRadius(){
        return radius;
    }

    void renderCircle() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(Variables.CIRCLE_LINE_WIDTH);

        if(getHighlighted()) {
            shapeRenderer.setColor(0, .8f, .8f, .5f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(pos.x, pos.y, radius + 8);
            shapeRenderer.end();
        }
    }

    public void setTravelTo(float x, float y, Circles circle){
        moving = true;
        travelToX = x;
        travelToY = y;
        matchedCircle = circle;
    }

    private void move(float deltaTime){
        if(pos.x == travelToX && pos.y == travelToY){
            collided();
        }else{
            float h = (float)Math.sqrt((pos.x - travelToX)*(pos.x - travelToX)+(pos.y - travelToY)*(pos.y - travelToY));
            float translationRatio = deltaTime*MOVE_RATE/h;
            if(pos.x > travelToX){
                if(pos.x - ((pos.x - travelToX)*translationRatio) <= travelToX){
                    pos.x = travelToX;
                }else{
                    pos.x = pos.x - ((pos.x - travelToX)*translationRatio);
                }
            }else if(pos.x < travelToX){
                if(pos.x + ((travelToX - pos.x)*translationRatio) >= travelToX){
                    pos.x = travelToX;
                }else{
                    pos.x = pos.x + ((travelToX - pos.x)*translationRatio);
                }
            }

            if(pos.y > travelToY){
                if(pos.y - ((pos.y - travelToY)*translationRatio) <= travelToY){
                    pos.y = travelToY;
                }else{
                    pos.y = pos.y - ((pos.y - travelToY)*translationRatio);
                }
            }else if(pos.y < travelToY){
                if(pos.y + ((travelToY - pos.y)*translationRatio) >= travelToY){
                    pos.y = travelToY;
                }else{
                    pos.y = pos.y + ((travelToY - pos.y)*translationRatio);
                }
            }

        }
    }

    private void applyAttraction(float deltaTime){
        velocity.set(0,0);
        boolean didAnything = false;
        for(Circles otherCircle: game.allCircles){
            if(!this.equals(otherCircle)){
                float distanceFromEdges = (float) (Math.sqrt(Math.pow((double)Math.abs(otherCircle.pos.x - this.pos.x),2) + Math.abs(Math.pow((double)otherCircle.pos.y - this.pos.y,2))) - otherCircle.radius - this.radius);
                if(distanceFromEdges < Circles.EFFECTIVE_RANGE && distanceFromEdges > 0){
                    didAnything = true;
                    boolean attractive = ((this.getClass() == Proton.class && otherCircle.getClass() == Proton.class)
                            || (this.getClass() == Deuterium.class && otherCircle.getClass() == Proton.class)
                            || (otherCircle.getClass() == Deuterium.class && this.getClass() == Proton.class)
                            || (otherCircle.getClass() == Helion.class && this.getClass() == Helion.class)
                            || (otherCircle.getClass() == Helium.class && this.getClass() == Helium.class)
                            || (otherCircle.getClass() == Helium.class && this.getClass() == Beryllium.class)
                            || (otherCircle.getClass() == Beryllium.class && this.getClass() == Helium.class)
                            || (otherCircle.getClass() == Carbon.class && this.getClass() == Proton.class)
                            || (otherCircle.getClass() == Proton.class && this.getClass() == Carbon.class));
                    float potency = 1 - distanceFromEdges/Circles.EFFECTIVE_RANGE;
                    float vectorRatio = (potency * STANDARD_FORCE);
                    if(attractive){
                        velocity.x += (otherCircle.pos.x - this.pos.x) * vectorRatio;
                        velocity.y += (otherCircle.pos.y - this.pos.y) * vectorRatio;
                    }else{
                        velocity.x -= (otherCircle.pos.x - this.pos.x) * vectorRatio;
                        velocity.y -= (otherCircle.pos.y - this.pos.y) * vectorRatio;
                    }
                }else if(distanceFromEdges < 0){
                    if(this.getClass() != Carbon.class){
                        velocity.set(0, 0);
                        float BigSideA = otherCircle.pos.x - this.pos.x;
                        float BigSideB = otherCircle.pos.y - this.pos.y;
                        float BigSideC = (float) Math.sqrt(BigSideA * BigSideA + BigSideB * BigSideB);
                        float smallC = BigSideC - (this.radius + otherCircle.radius);
                        float smallA = smallC / BigSideC * BigSideA;
                        float smallB = smallC / BigSideC * BigSideB;
                        this.pos.x = this.pos.x + smallA;
                        this.pos.y = this.pos.y + smallB;
                    }
                }
            }
        }
        if(didAnything){
            this.pos.x = this.pos.x + (this.velocity.x * deltaTime);
            this.pos.y = this.pos.y + (this.velocity.y * deltaTime);
        }
    }

    protected abstract void collided();
    protected abstract void calculateTimerPositions();

}
