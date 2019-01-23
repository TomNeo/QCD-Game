package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
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
    //private Circles highlightedCircle = null;
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
    private long startingScore = 3000;
    private long currentScore = startingScore;
    private long highScore = startingScore;
    private long highestScore = startingScore;
    private long totalScored = 0;
    private long Protons = 0;
    private OrthographicCamera camera;
    private Texture img;
    private Vector3 initialPress = new Vector3();
    private Vector3 lastPress = new Vector3();

    //------ UNUSED? ---------//
    float touchedFor = 0f;
    static float TOUCH_TIME_OUT = .3f;
    boolean pastFirstScreen = false;

    GameScreen(MyGdxGame gam) {
        game = gam;

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

        if (currentScore > 0) {
            if (currentScore != startingScore) {
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
            drawConnection();

            drawHealthBar();

            game.batch.begin();
            game.font.draw(game.batch, "Score: " + currentScore, 0, 20);
            game.batch.end();
        } else {
            game.highlightedCircle = null;
            game.setScreen(new GameOverScreen(
                    game, highScore, highestScore, totalScored, Protons, runTime));
            dispose();
        }

    }

    private void newClick(){
        /** This is the code for the drawing a line to fuze circles
         if(Gdx.input.isTouched()){
         touchedFor = touchedFor + deltaTime;
         if(Gdx.input.justTouched()){
         touchedFor = 0f;
         stillTouched = true;
         initialPress.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(initialPress);
         }
         lastPress.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(lastPress);
         }else{
         if(stillTouched){
         stillTouched = false;
         if(touchedOpenSpace(lastPress) && touchedFor < TOUCH_TIME_OUT) {
         allCircles.add(new Proton(this,lastPress.x, lastPress.y));
         currentScore = currentScore - 150;
         Protons++;
         }else{
         fuzePoints(initialPress,lastPress);
         }
         touchedFor = 0;
         }
         }
         **/

        if(Gdx.input.isTouched()){
            if(Gdx.input.justTouched()) {
                initialPress.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(initialPress);
                Circles temp = inWhatCircle(initialPress);
                if(temp == null){
                    game.allCircles.add(new Proton(game, initialPress.x, initialPress.y));
                    currentScore = currentScore - 150;
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
            }
        }
        for(Circles circle : killList){
            game.allCircles.remove(circle);
        }
        killList.clear();
        if(currentScore > highScore){
            highScore = currentScore;
            if(highScore > highestScore){
                highestScore = highScore;
            }
        }
        currentScore = (long)(currentScore - (150 + (totalScored*.01f)) * deltaTime);
        healthBar.calculate(currentScore);
    }

    private void drawHealthBar() {
        healthBar.draw(game.batch);
    }

    private void drawConnection() {
        if(stillTouched){
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            game.shapeRenderer.setProjectionMatrix(camera.combined);
            game.shapeRenderer.setColor(1,1,0,.3f);
            game.shapeRenderer.line(initialPress.x,initialPress.y,lastPress.x,lastPress.y);
            game.shapeRenderer.end();
        }
    }

    private void drawCircles(){
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        for (Circles circle : game.allCircles) {
            circle.renderCircle(game.shapeRenderer);
        }
        game.shapeRenderer.end();
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
                currentScore = currentScore + 400;
                totalScored = totalScored + 400;
            }
            if((initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Deuterium.class))||(initialCircle.getClass().equals(Deuterium.class) && lastCircle.getClass().equals(Proton.class))){
                initialCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, initialCircle);
                currentScore = currentScore + 500;
                totalScored = totalScored + 500;
            }
            if(initialCircle.getClass().equals(Helion.class) && lastCircle.getClass().equals(Helion.class)){
                initialCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, initialCircle);
                currentScore = currentScore + 750;
                totalScored = totalScored + 750;
            }
            if(initialCircle.getClass().equals(Helium.class) && lastCircle.getClass().equals(Helium.class)){
                initialCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.pos.x + lastCircle.pos.x)/2f,(initialCircle.pos.y + lastCircle.pos.y)/2f, initialCircle);
                currentScore = currentScore + 1000;
                totalScored = totalScored + 1000;
            }
        }
    }

    private void fuzePoints(Vector3 initialPress, Vector3 lastPress) {
        Circles initialCircle = inWhatCircle(initialPress);
        Circles lastCircle = inWhatCircle(lastPress);
        if(initialCircle != null && lastCircle != null && !initialCircle.equals(lastCircle)){
            if(initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Proton.class)){
                initialCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, initialCircle);
                currentScore = currentScore + 400;
                totalScored = totalScored + 400;
            }
            if((initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Deuterium.class))||(initialCircle.getClass().equals(Deuterium.class) && lastCircle.getClass().equals(Proton.class))){
                initialCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, initialCircle);
                currentScore = currentScore + 500;
                totalScored = totalScored + 500;
            }
            if(initialCircle.getClass().equals(Helion.class) && lastCircle.getClass().equals(Helion.class)){
                initialCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, initialCircle);
                currentScore = currentScore + 750;
                totalScored = totalScored + 750;
            }
            if(initialCircle.getClass().equals(Helium.class) && lastCircle.getClass().equals(Helium.class)){
                initialCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, lastCircle);
                lastCircle.setTravelTo((initialPress.x + lastPress.x)/2f,(initialPress.y + lastPress.y)/2f, initialCircle);
                currentScore = currentScore + 1000;
                totalScored = totalScored + 1000;
            }
        }
    }

    private boolean touchedOpenSpace(Vector3 pos){
        float distance = 0;
        for(Circles circle: game.allCircles){
            distance = (float)Math.sqrt(Math.pow(pos.x - circle.getPosition().x,2)+Math.pow(pos.y - circle.getPosition().y,2));
            if(distance < (Proton.RADIUS + circle.getRadius())){
                return false;
            }
        }
        return true;
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
