package com.mygdx.game.atoms;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Variables;

public class WavyBorder {


    public float radiusChangeRate = 10;
    public float radiusFluxMax = 5;
    public int waveFrequency = 5;
    public float radius;
    public float degreesChangeRate = 180;
    public float amplitude = 5;
    public float segments = 8;
    public Vector3 RGB = new Vector3(0f, 1f, 0f);

    private float currentDegrees = 0;
    private float radiusFlux = 0;
    private float nextDegrees;
    private Bezier<Vector2>[] circlePath;
    private Circles parent;


    public WavyBorder(Circles parent){
        this.parent = parent;
        calculate(0);
    }

    public void calculate(float deltaTime){
        radius = parent.radius;

        float radiusChange = (deltaTime * radiusChangeRate);
        if(radiusFlux < -radiusFluxMax){
            radiusChange = (deltaTime * radiusChangeRate);
        }
        if(radiusFlux > radiusFluxMax){
            radiusChange = -(deltaTime * radiusChangeRate);
        }
        radiusFlux = radiusFlux + radiusChange;

        float tempCureentDegrees = currentDegrees;
        currentDegrees = currentDegrees + (degreesChangeRate * deltaTime);

        circlePath = new Bezier[waveFrequency * 2];

        for(int i = 0; i < (waveFrequency * 2);i++){
            nextDegrees = tempCureentDegrees + (360/(waveFrequency * 2));
            Vector2[] currentPositions = new Vector2[3];
            currentPositions[0] = new Vector2((float)Math.cos(Math.toRadians(tempCureentDegrees))*radius+parent.getX(),(float)Math.sin(Math.toRadians(tempCureentDegrees))*radius+parent.getY());
            currentPositions[2] = new Vector2((float)Math.cos(Math.toRadians(nextDegrees))*radius+parent.getX(),(float)Math.sin(Math.toRadians(nextDegrees))*radius+parent.getY());

            float midPointX = (currentPositions[0].x + currentPositions[2].x)/2;
            float midPointY = (currentPositions[0].y + currentPositions[2].y)/2;
            float midPointDistance = (float)Math.sqrt((midPointX - parent.getX())*(midPointX - parent.getX()) + (midPointY - parent.getY())*(midPointY - parent.getY()));
            float bezierDistance = (radius - midPointDistance) + radius;
            if(i%2 == 0){
                bezierDistance = 2 * (radius - midPointDistance) + radius + amplitude;
            }else{
                bezierDistance = radius - (radius - midPointDistance)*2 - amplitude;
            }

            currentPositions[1] = new Vector2((float)Math.cos(Math.toRadians((tempCureentDegrees + nextDegrees)/2))*bezierDistance+parent.getX(),(float)Math.sin(Math.toRadians((tempCureentDegrees + nextDegrees)/2))*bezierDistance+parent.getY());
            circlePath[i] =  new Bezier<Vector2>(currentPositions);
            tempCureentDegrees = nextDegrees;
        }
        int breakPoint = 0;
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(RGB.x, RGB.y, RGB.z, 1);
        for(int i = 0; i < circlePath.length; i++){
            for(int j = 0; j < segments; ++j){
                float t = j /segments;
                Vector2 st = new Vector2();
                Vector2 end = new Vector2();
                circlePath[i].valueAt(st,t);
                circlePath[i].valueAt(end, t+1/(segments));
                shapeRenderer.begin();
                shapeRenderer.line(st.x, st.y, end.x, end.y);
                shapeRenderer.end();
            }
        }
    }

}
