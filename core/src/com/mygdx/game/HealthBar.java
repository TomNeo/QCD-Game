package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class HealthBar extends Actor {

    private Texture sourceFile = new Texture("health-bars.png");

    private VisualComponent backEnd;
//    private TextureRegion leadingEdge = new TextureRegion(sourceFile, 17,0,17,33);
    private TextureRegion green25 = new TextureRegion(sourceFile, 26,0,25,25);
    private TextureRegion yellow25 = new TextureRegion(sourceFile, 51,0,25,25);
    private TextureRegion red25 = new TextureRegion(sourceFile, 0,33,25,25);
    private TextureRegion blue25 = new TextureRegion(sourceFile, 26,25,25,25);
    private TextureRegion purple25 = new TextureRegion(sourceFile, 51,25,25,25);
    private TextureRegion top = new TextureRegion(sourceFile, 51,50,25,4);
    private TextureRegion bottom = new TextureRegion(sourceFile, 51,54,25,4);

    private ArrayList<VisualComponent> visualsList;

    private float value;

    public HealthBar(float x, float y){
        setPosition(x, y);
        setWidth(1700);

        backEnd = new VisualComponent(new TextureRegion(sourceFile, 0,0,17,33),x,y);
        visualsList = new ArrayList<VisualComponent>();
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(VisualComponent component: visualsList){
            batch.draw(component.visual, component.pos.x, component.pos.y);
        }
    }

    @Override
    public void act (float delta) {
        super.act(delta);
        int borderOffset = 4;

        visualsList.clear();

        if(value < Variables.HEALTH_TIER_1_VALUE) {
            for (int i = 0; i < ((value/Variables.HEALTH_TIER_1_VALUE) * getWidth() / red25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(red25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else if(value < (Variables.HEALTH_TIER_1_VALUE + Variables.HEALTH_TIER_2_VALUE)){
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(red25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
            for (int i = 0; i < ((value - Variables.HEALTH_TIER_1_VALUE) / Variables.HEALTH_TIER_2_VALUE * getWidth() / yellow25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(yellow25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else if(value < (Variables.HEALTH_TIER_1_VALUE + Variables.HEALTH_TIER_2_VALUE + Variables.HEALTH_TIER_3_VALUE)){
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(yellow25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }for (int i = 0; i < ((value - (Variables.HEALTH_TIER_1_VALUE + Variables.HEALTH_TIER_2_VALUE)) / Variables.HEALTH_TIER_3_VALUE * getWidth() / green25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(green25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else if(value < Variables.HEALTH_TIER_1_VALUE + Variables.HEALTH_TIER_2_VALUE + Variables.HEALTH_TIER_3_VALUE + Variables.HEALTH_TIER_4_VALUE){
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(green25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }for (int i = 0; i < ((value - (Variables.HEALTH_TIER_1_VALUE + Variables.HEALTH_TIER_2_VALUE + Variables.HEALTH_TIER_3_VALUE)) / Variables.HEALTH_TIER_4_VALUE * getWidth() / blue25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(blue25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else if(value < Variables.HEALTH_TIER_1_VALUE + Variables.HEALTH_TIER_2_VALUE + Variables.HEALTH_TIER_3_VALUE + Variables.HEALTH_TIER_4_VALUE + Variables.HEALTH_TIER_5_VALUE){
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(blue25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }for (int i = 0; i < ((value - (Variables.HEALTH_TIER_1_VALUE + Variables.HEALTH_TIER_2_VALUE + Variables.HEALTH_TIER_3_VALUE + Variables.HEALTH_TIER_4_VALUE)) / Variables.HEALTH_TIER_5_VALUE * getWidth() / purple25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(purple25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else{
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(purple25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }
        for(int i = 0; i < 68; i++){
            VisualComponent topBorder = new VisualComponent(top, backEnd.pos.x + backEnd.visual.getRegionWidth() + (i * top.getRegionWidth()),backEnd.pos.y + backEnd.visual.getRegionHeight()-borderOffset);
            VisualComponent bottomBorder = new VisualComponent(bottom, backEnd.pos.x + backEnd.visual.getRegionWidth()+ (i * top.getRegionWidth()), backEnd.pos.y);
            visualsList.add(topBorder);
            visualsList.add(bottomBorder);
        }
        visualsList.add(backEnd);
    }

//    public void calculate(long value){
        /*
        visualsList.clear();

        if(value < 1700) {
            for (int i = 0; i < (value / red25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(red25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else if(value < 3400){
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(red25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
            for (int i = 0; i < ((value - 1700)/ yellow25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(yellow25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else if(value < 5100){
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(yellow25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }for (int i = 0; i < ((value - 3400)/ green25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(green25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else if(value < 6800){
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(green25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }for (int i = 0; i < ((value - 5100)/ blue25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(blue25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else if(value < 8500){
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(blue25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }for (int i = 0; i < ((value - 6800)/ purple25.getRegionWidth()); i++) {
                VisualComponent coloredSection = new VisualComponent(purple25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }else{
            for (int i = 0; i < 68; i++) {
                VisualComponent coloredSection = new VisualComponent(purple25, backEnd.pos.x + borderOffset + (i * red25.getRegionWidth()), backEnd.pos.y + borderOffset);
                visualsList.add(coloredSection);
            }
        }
        for(int i = 0; i < 68; i++){
            VisualComponent topBorder = new VisualComponent(top, backEnd.pos.x + backEnd.visual.getRegionWidth() + (i * top.getRegionWidth()),backEnd.pos.y + backEnd.visual.getRegionHeight()-borderOffset);
            VisualComponent bottomBorder = new VisualComponent(bottom, backEnd.pos.x + backEnd.visual.getRegionWidth()+ (i * top.getRegionWidth()), backEnd.pos.y);
            visualsList.add(topBorder);
            visualsList.add(bottomBorder);
        }
        visualsList.add(backEnd);
*/
//    }

    @Override
    public boolean remove () {
        sourceFile.dispose();
        return super.remove();
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
