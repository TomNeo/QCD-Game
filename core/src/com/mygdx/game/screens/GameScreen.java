package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Variables;
import com.mygdx.game.atoms.Beryllium;
import com.mygdx.game.atoms.Circles;
import com.mygdx.game.atoms.Deuterium;
import com.mygdx.game.HealthBar;
import com.mygdx.game.atoms.Helion;
import com.mygdx.game.atoms.Helium;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.atoms.Proton;

import java.util.ArrayList;

public class GameScreen implements Screen {

    private MyGdxGame game;
    private boolean colorTimeGoingUp = true;
    private boolean stillTouched = false;
    private float colorLength = 3;
    private float colorTime = 0;
    private float runTime = 0;
    private float width = 1920;
    private float height = 1080;
    private float r = 0;
    private float g = 0;
    private float b = 0;
    private HealthBar healthBar;
    private int colorCounter = 0;
    private int timesChanged = 0;
    private long startingHealth = Variables.STARTING_HEALTH;
    private long currentHealth = startingHealth;
    private long peakHealth = startingHealth;
    private long totalScored = 0;
    private long highestScore;
    private long Protons = 0;
    private OrthographicCamera camera;
    private Texture img;
    private Vector3 initialPress = new Vector3();
    private Vector3 lastPress = new Vector3();


    GameScreen(MyGdxGame gam, long highScore) {
        game = gam;
        highestScore = highScore;

        camera = new OrthographicCamera(width, height);
        camera.position.x=width/2;
        camera.position.y = height/2;
        camera.update();
        img = new Texture("Hex-grid-transparent.png");//"Hex-grid.png");

        game.allCircles = new ArrayList<Circles>();
        game.addToCircles = new ArrayList<Circles>();
        healthBar = new HealthBar(106,100);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(colorTimeGoingUp) {
            colorTime += delta;
        }else{
            colorTime -= delta;
        }
        if(colorTime >= colorLength){
            colorTimeGoingUp = false;
            colorCounter++;
        }else if(colorTime <= 0){
            colorTimeGoingUp = true;
            colorCounter++;
        }

        if(colorCounter == 5 && timesChanged == 0){
            timesChanged++;
            colorLength = 2.5f;
            colorTimeGoingUp = true;
            colorTime = 0;
        }else if(colorCounter == 12 && timesChanged == 1){
            timesChanged++;
            colorLength = 1.5f;
            colorTimeGoingUp = true;
            colorTime = 0;
        } if(colorCounter == 22 && timesChanged == 2){
            timesChanged++;
            colorLength = .9f;
            colorTimeGoingUp = true;
            colorTime = 0;
        }

        if(colorCounter < 5) {
            r = (70f / 256f) * (colorTime / colorLength);
            g = (30f / 256f) * (colorTime / colorLength);
            b = (100f / 256f) * (colorTime / colorLength);
        }else if(colorCounter < 12){
            r = (70f + (111f * (colorTime / colorLength))) / 256f;
            g = (30f + (66f * (colorTime / colorLength)))/ 256f;
            b = (100f - (26f * (colorTime / colorLength)))/ 256f;
        }else if(colorCounter < 22){
            r = (181f + (34f * (colorTime / colorLength))) / 256f;
            g = (96f + (66f * (colorTime / colorLength)))/ 256f;
            b = (74f - (34f * (colorTime / colorLength)))/ 256f;
        }else{
            r = (215f + (15f * (colorTime / colorLength))) / 256f;
            g = (162f + (93f * (colorTime / colorLength)))/ 256f;
            b = (40f - (14f * (colorTime / colorLength)))/ 256f;
        }

        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);

        if (currentHealth > 0) {
            if (currentHealth != startingHealth) {
                runTime = runTime + delta;
            }
            newClick();
            moveCircles(delta);
            addNewCircles();
            tick(delta);

            game.batch.begin();
            game.batch.draw(img, 0, 0);
            game.batch.end();

            drawCircles();
            //drawConnection();

            drawHealthBar();

            game.batch.begin();
            game.font.draw(game.batch, "Score: " + totalScored, 0, 20);
            game.batch.end();
        } else {
            game.highlightedCircle = null;
            game.setScreen(new GameOverScreen(
                    game, peakHealth, highestScore, totalScored, Protons, runTime));
            dispose();
        }

    }

    private void newClick(){
        if(Gdx.input.isTouched()){
            if(Gdx.input.justTouched()) {
                initialPress.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(initialPress);
                Circles temp = inWhatCircle(initialPress);
                if(temp == null){
                    game.allCircles.add(new Proton(game, initialPress.x, initialPress.y));
                    currentHealth = currentHealth - Variables.COST_OF_CREATING_PROTON;
                    Protons++;
                    if(game.highlightedCircle != null){
                        game.highlightedCircle.setHighlighted(false);
                        game.highlightedCircle = null;
                    }
                }else{
                    if(game.highlightedCircle == null){
                        game.highlightedCircle = temp;
                        temp.setHighlighted(true);
                    }else{
                        if(game.highlightedCircle != temp){
                            temp.setHighlighted(false);
                            game.highlightedCircle.setHighlighted(false);
                            fuzePoints(game.highlightedCircle,temp);
                            game.highlightedCircle = null;
                        }else{
                            game.highlightedCircle.setHighlighted(false);
                            game.highlightedCircle = null;
                        }
                    }
                }
            }
        }
    }

    private void addNewCircles() {
        for (Circles circle : game.addToCircles) {
            game.allCircles.add(circle);
        }
        game.addToCircles.clear();
    }

    private void moveCircles(float deltaTime) {
        for(Circles circle : game.allCircles){
            if(circle.moving){
                circle.move(deltaTime);
            }else{
                circle.applyAttraction(deltaTime);
            }
        }
    }

    private void tick(float deltaTime) {
        ArrayList<Circles> killList = new ArrayList<Circles>();
        for(Circles circle : game.allCircles){
            circle.tick(deltaTime);
            if(circle.kill){
                killList.add(circle);
                if(circle.getHighlighted()){
                    game.highlightedCircle = null;
                    circle.setHighlighted(false);
                }
            }
        }
        for(Circles circle : killList){
            game.allCircles.remove(circle);
        }
        killList.clear();
        if(currentHealth > peakHealth){
            peakHealth = currentHealth;
            if(totalScored > highestScore){
                highestScore = totalScored;
            }
        }
        currentHealth = (long)(currentHealth - (Variables.HEALTH_DECREASE_CONSTANT + (totalScored * Variables.HEALTH_DECREASE_SCORE_MODIFIER) + (Math.round(runTime) * Variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)) * deltaTime);
        healthBar.calculate(currentHealth);
    }

    private void drawHealthBar() {
        healthBar.draw(game.batch);
    }

    private void drawCircles(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(Variables.CIRCLE_LINE_WIDTH);
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        for (Circles circle : game.allCircles) {
            circle.renderCircle(game.shapeRenderer);
        }

        Gdx.gl.glLineWidth(1f);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private Circles inWhatCircle(Vector3 pos){
        float distance;
        Circles tempCircle = null;
        for(Circles circle: game.allCircles){
            distance = (float)Math.sqrt(Math.pow(pos.x - circle.getPosition().x,2)+Math.pow(pos.y - circle.getPosition().y,2));
            if(distance < circle.getRadius()){
                tempCircle = circle;// Using the tempCircle makes us return the top most rendered circle, instead of the bottom most.
            }
        }
        return tempCircle;
    }

    private void fuzePoints(Circles initialCircle, Circles lastCircle) {
        if(initialCircle != null && lastCircle != null && !initialCircle.equals(lastCircle)){
            if(initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Proton.class)){
                initialCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, initialCircle);
                currentHealth = currentHealth + Variables.PROTON_PROTON_SCORE;
                totalScored = totalScored + Variables.PROTON_PROTON_SCORE;
            }
            if((initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Deuterium.class))||(initialCircle.getClass().equals(Deuterium.class) && lastCircle.getClass().equals(Proton.class))){
                initialCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, initialCircle);
                currentHealth = currentHealth + Variables.PROTON_DEUTERIUM_SCORE;
                totalScored = totalScored + Variables.PROTON_DEUTERIUM_SCORE;
            }
            if(initialCircle.getClass().equals(Helion.class) && lastCircle.getClass().equals(Helion.class)){
                initialCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, initialCircle);
                currentHealth = currentHealth + Variables.HELION_HELION_SCORE;
                totalScored = totalScored + Variables.HELION_HELION_SCORE;
            }
            if(initialCircle.getClass().equals(Helium.class) && lastCircle.getClass().equals(Helium.class)){
                initialCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, initialCircle);
                currentHealth = currentHealth + Variables.HELIUM_HELIUM_SCORE;
                totalScored = totalScored + Variables.HELIUM_HELIUM_SCORE;
            }
            if((initialCircle.getClass().equals(Beryllium.class) && lastCircle.getClass().equals(Helium.class))||(initialCircle.getClass().equals(Helium.class) && lastCircle.getClass().equals(Beryllium.class))){
                initialCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, initialCircle);
                currentHealth = currentHealth + Variables.HELIUM_BERYLLIUM_SCORE;
                totalScored = totalScored + Variables.HELIUM_BERYLLIUM_SCORE;
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
