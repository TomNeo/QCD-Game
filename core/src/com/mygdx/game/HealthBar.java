package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class HealthBar {

    private int borderOffset = 4;
    private Vector2 pos = new Vector2();
    private Texture sourceFile = new Texture("health-bars.png");//"Hex-grid.png");

    VisualComponent backEnd;
    private TextureRegion leadingEdge = new TextureRegion(sourceFile, 17,0,17,33);
    private TextureRegion green25 = new TextureRegion(sourceFile, 26,0,25,25);
    private TextureRegion yellow25 = new TextureRegion(sourceFile, 51,0,25,25);
    private TextureRegion red25 = new TextureRegion(sourceFile, 0,33,25,25);
    private TextureRegion blue25 = new TextureRegion(sourceFile, 26,25,25,25);
    private TextureRegion purple25 = new TextureRegion(sourceFile, 51,25,25,25);

    ArrayList<VisualComponent> visualsList;

    public HealthBar(float x, float y){
        pos.x = x;
        pos.y = y;
        backEnd = new VisualComponent(new TextureRegion(sourceFile, 0,0,17,33),x,y);
        visualsList = new ArrayList<VisualComponent>();
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        for(VisualComponent component: visualsList){
            batch.draw(component.visual, component.pos.x, component.pos.y);
        }
        batch.end();
    }

    public void calculate(long value){
        visualsList.clear();
        for(int i = 0; i < (value / red25.getRegionWidth()); i++){
            VisualComponent coloredSection = new VisualComponent(red25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
            visualsList.add(coloredSection);
        }
        visualsList.add(backEnd);
    }

    class VisualComponent{

        TextureRegion visual;
        Vector2 pos = new Vector2();

        VisualComponent(TextureRegion img, float x, float y){
            visual = img;
            pos.x = x;
            pos.y = y;
        }
    }
}
