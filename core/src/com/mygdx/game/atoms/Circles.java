package com.mygdx.game.atoms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
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
    protected int midiChannel;
    protected int midiNote;
    protected int noteVelocity;

    Circles matchedCircle = null;

    float lifeSpan;
    float radius;
    float timerX = 0;
    float timerY = 0;
    private float travelToX = -1;
    private float travelToY = -1;
    protected ArrayList<WavyBorder> borders = new ArrayList<WavyBorder>();

    //protected int colorInt;

    ShapeRenderer shapeRenderer;

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

        calculateTimerPositions();
        calculateWavyBorders(delta);
    }

    public boolean getHighlighted(){
        return highlighted;
    }
    public int getMidiChannel(){
        return midiChannel;
    }
    public int getNote(){
        return midiNote;
    }
    public int getNoteVelocity(){
        return noteVelocity;
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
            shapeRenderer.circle(getX(), getY(), radius + 8);
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
        if(getX() == travelToX && getY() == travelToY){
            collided();
        }else{
            float h = (float)Math.sqrt((getX() - travelToX)*(getX() - travelToX)+(getY() - travelToY)*(getY() - travelToY));
            float translationRatio = deltaTime*MOVE_RATE/h;
            if(getX() > travelToX){
                if(getX() - ((getX() - travelToX)*translationRatio) <= travelToX){
                    setX(travelToX);
                }else{
                    setX(getX() - ((getX() - travelToX)*translationRatio));
                }
            }else if(getX() < travelToX){
                if(getX() + ((travelToX - getX())*translationRatio) >= travelToX){
                    setX(travelToX);
                }else{
                    setX(getX() + ((travelToX - getX())*translationRatio));
                }
            }

            if(getY() > travelToY){
                if(getY() - ((getY() - travelToY)*translationRatio) <= travelToY){
                    setY(travelToY);
                }else{
                    setY(getY() - ((getY() - travelToY)*translationRatio));
                }
            }else if(getY() < travelToY){
                if(getY() + ((travelToY - getY())*translationRatio) >= travelToY){
                    setY(travelToY);
                }else{
                    setY(getY() + ((travelToY - getY())*translationRatio));
                }
            }

        }
    }

    private void applyAttraction(float deltaTime){
        velocity.set(0,0);
        boolean didAnything = false;
        for(Circles otherCircle: game.allCircles){
            if(!this.equals(otherCircle)){
                float distanceFromEdges = (float) (Math.sqrt(Math.pow((double)Math.abs(otherCircle.getX() - this.getX()),2) + Math.abs(Math.pow((double)otherCircle.getY() - this.getY(),2))) - otherCircle.radius - this.radius);
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
                        velocity.x += (otherCircle.getX() - this.getX()) * vectorRatio;
                        velocity.y += (otherCircle.getY() - this.getY()) * vectorRatio;
                    }else{
                        velocity.x -= (otherCircle.getX() - this.getX()) * vectorRatio;
                        velocity.y -= (otherCircle.getY() - this.getY()) * vectorRatio;
                    }
                }else if(distanceFromEdges < 0){
                    if(this.getClass() != Carbon.class){
                        velocity.set(0, 0);
                        float BigSideA = otherCircle.getX() - this.getX();
                        float BigSideB = otherCircle.getY() - this.getY();
                        float BigSideC = (float) Math.sqrt(BigSideA * BigSideA + BigSideB * BigSideB);
                        float smallC = BigSideC - (this.radius + otherCircle.radius);
                        float smallA = smallC / BigSideC * BigSideA;
                        float smallB = smallC / BigSideC * BigSideB;
                        setX(this.getX() + smallA);
                        setY(this.getY() + smallB);
                    }
                }
            }
        }
        if(didAnything){
            setX(this.getX() + (this.velocity.x * deltaTime));
            setY(this.getY() + (this.velocity.y * deltaTime));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        for (WavyBorder border : borders) {
            border.draw(shapeRenderer);
        }

    }

    protected void calculateWavyBorders(float deltaTime){
        for (WavyBorder border : borders) {
            border.calculate(deltaTime);
        }
    }

    protected abstract void collided();
    protected abstract void calculateTimerPositions();

}
