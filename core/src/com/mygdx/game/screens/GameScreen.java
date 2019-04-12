package com.mygdx.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Variables;
import com.mygdx.game.atoms.Beryllium;
import com.mygdx.game.atoms.Carbon;
import com.mygdx.game.atoms.Circles;
import com.mygdx.game.atoms.Deuterium;
import com.mygdx.game.HealthBar;
import com.mygdx.game.atoms.Helion;
import com.mygdx.game.atoms.Helium;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.atoms.Proton;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    private float currentHealth = startingHealth;
    private long peakHealth = startingHealth;
    private long totalScored = 0;
    private long highestScore;
    private long Protons = 0;
    private Music bgMusic;
    private OrthographicCamera camera;
    private Texture img;
    private Vector3 initialPress = new Vector3();
    private Vector3 lastPress = new Vector3();
    private ArrayList<String> outputs = new ArrayList<String>();
    Variables variables = new Variables();

    private BufferedWriter writer = null;

    GameScreen(MyGdxGame gam, long highScore) {
        game = gam;
        highestScore = highScore;

        camera = new OrthographicCamera(width, height);
        camera.position.x=width/2;
        camera.position.y = height/2;
        camera.update();

        game.gameStage = new Stage(new StretchViewport(1920, 1080, camera));

        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        img = new Texture("Hex-grid-transparent.png");//"Hex-grid.png");

        game.allCircles = new ArrayList<Circles>();
        game.addToCircles = new ArrayList<Circles>();
        healthBar = new HealthBar(106,100);
        game.gameStage.addActor(healthBar);

        if (Gdx.app.getType() == Application.ApplicationType.Android ||
                Gdx.app.getType() == Application.ApplicationType.Desktop) {
            bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/mm5napalm.ogg"));
        } else if (Gdx.app.getType() == Application.ApplicationType.iOS) {
            bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/mm5napalm.mp3"));
        }
        if (bgMusic != null) {
            bgMusic.play();
        }
        outputs.add("TYPE,RUNTIME,INFO,DECREASE RATE");
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

        if(variables.Game_Score_Tier == 1) {
            r = (70f / 256f) * (colorTime / colorLength);
            g = (30f / 256f) * (colorTime / colorLength);
            b = (100f / 256f) * (colorTime / colorLength);
        }else if(variables.Game_Score_Tier == 2){
            r = (70f + (111f * (colorTime / colorLength))) / 256f;
            g = (30f + (66f * (colorTime / colorLength)))/ 256f;
            b = (100f - (26f * (colorTime / colorLength)))/ 256f;
        }else if(variables.Game_Score_Tier == 3){
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

        if (currentHealth > 0) {
            if (currentHealth != startingHealth) {
                runTime = runTime + delta;
            }
            newClick();

            game.batch.begin();
            game.batch.draw(img, 0, 0);
            game.batch.end();

            game.gameStage.act();
            tick(delta);
            game.gameStage.draw();


            //drawConnection();

            game.batch.begin();
            game.font.draw(game.batch, "Score: " + totalScored, 0, 20);
            game.batch.end();

            game.allCircles.addAll(game.addToCircles);
            game.addToCircles.clear();

            ArrayList<Circles> killList = new ArrayList<Circles>();
            for(Circles circle : game.allCircles){
                if(circle.kill){

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

        } else {
            outputs.add("End Game Rate Of Decrease," + runTime + "," + (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)));
                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Calendar.getInstance().getTime());
                    writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(timeStamp + ".csv"), "utf-8"));
                    for (String line : outputs) {
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (IOException ex) {
                    // Report
                } finally {
                    try {
                        writer.close();
                    } catch (Exception ex) {/*ignore*/}
                }

            game.highlightedCircle = null;
            bgMusic.stop();
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
                    outputs.add("New Proton," + runTime);
                    Proton p = new Proton(game, initialPress.x, initialPress.y);
                    game.allCircles.add(p);
                    game.gameStage.addActor(p);
                    currentHealth = currentHealth - variables.COST_OF_CREATING_PROTON;
                    Protons++;
                    if(game.highlightedCircle != null){
                        game.highlightedCircle.setHighlighted(false);
                        game.highlightedCircle = null;
                    }
                    // play sound for circle creation by touch
                    game.soundEffect.play();
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

    private void tick(float deltaTime) {
        if(currentHealth > peakHealth){
            peakHealth = (long)currentHealth;
            outputs.add("New peak health," + runTime + "," + peakHealth);
        }
        if(totalScored > highestScore){
            highestScore = totalScored;
        }
        currentHealth = (long)(currentHealth - (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)) * deltaTime);
        healthBar.setValue(currentHealth);
        healthBar.setZIndex(healthBar.getParent().getChildren().size + 1);
        variables.inputs(currentHealth, totalScored, runTime);
        if(variables.StarMan){
            currentHealth = Variables.MAX_HEALTH /2;
            variables.StarMan = false;
        }
    }

    private Circles inWhatCircle(Vector3 pos){
        float distance;
        Circles tempCircle = null;
        for(Circles circle: game.allCircles){
            distance = (float)Math.sqrt(Math.pow(pos.x - circle.getX(),2)+Math.pow(pos.y - circle.getY(),2));
            if(distance < circle.getRadius()){
                tempCircle = circle;// Using the tempCircle makes us return the top most rendered circle, instead of the bottom most.
            }
        }
        return tempCircle;
    }

    private void fuzePoints(Circles initialCircle, Circles lastCircle) {
        if(initialCircle != null && lastCircle != null && !initialCircle.equals(lastCircle)){
            if(initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Proton.class)){
                initialCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, initialCircle);
                currentHealth = currentHealth + Variables.PROTON_PROTON_SCORE;
                totalScored = totalScored + Variables.PROTON_PROTON_SCORE;
                outputs.add("Protons Fused," + runTime);
                outputs.add("Scored," + runTime + "," + Variables.PROTON_PROTON_SCORE + "," + (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * Variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)));
            }
            if((initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Deuterium.class))||(initialCircle.getClass().equals(Deuterium.class) && lastCircle.getClass().equals(Proton.class))){
                initialCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, initialCircle);
                currentHealth = currentHealth + Variables.PROTON_DEUTERIUM_SCORE;
                totalScored = totalScored + Variables.PROTON_DEUTERIUM_SCORE;
                outputs.add("Proton-Deuterium Fused," + runTime);
                outputs.add("Scored," + runTime + "," + Variables.PROTON_DEUTERIUM_SCORE+ "," + (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * Variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)));
            }
            if(initialCircle.getClass().equals(Helion.class) && lastCircle.getClass().equals(Helion.class)){
                initialCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, initialCircle);
                currentHealth = currentHealth + Variables.HELION_HELION_SCORE;
                totalScored = totalScored + Variables.HELION_HELION_SCORE;
                outputs.add("Helions Fused," + runTime);
                outputs.add("Scored," + runTime + "," + Variables.HELION_HELION_SCORE+ "," + (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * Variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)));
            }
            if(initialCircle.getClass().equals(Helium.class) && lastCircle.getClass().equals(Helium.class)){
                initialCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, initialCircle);
                currentHealth = currentHealth + Variables.HELIUM_HELIUM_SCORE;
                totalScored = totalScored + Variables.HELIUM_HELIUM_SCORE;
                outputs.add("Heliums Fused," + runTime);
                outputs.add("Scored," + runTime + "," + Variables.HELIUM_HELIUM_SCORE+ "," + (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * Variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)));
            }
            if((initialCircle.getClass().equals(Beryllium.class) && lastCircle.getClass().equals(Helium.class))||(initialCircle.getClass().equals(Helium.class) && lastCircle.getClass().equals(Beryllium.class))){
                initialCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, lastCircle);
                lastCircle.setTravelTo((initialCircle.getX() + lastCircle.getX())/2f,(initialCircle.getY() + lastCircle.getY())/2f, initialCircle);
                currentHealth = currentHealth + Variables.HELIUM_BERYLLIUM_SCORE;
                totalScored = totalScored + Variables.HELIUM_BERYLLIUM_SCORE;
                outputs.add("HELIUM-BERYLLIUM Fused," + runTime);
                outputs.add("Scored," + runTime + "," + Variables.HELIUM_BERYLLIUM_SCORE+ "," + (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * Variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)));
            }
            if((initialCircle.getClass().equals(Proton.class) && lastCircle.getClass().equals(Carbon.class))){
                initialCircle.setTravelTo(lastCircle.getX(),lastCircle.getY(), lastCircle);
                ((Carbon)lastCircle).matchedProtons.add((Proton)initialCircle);
                currentHealth = currentHealth + variables.PROTON_CARBON_SCORE((1 + ((Carbon)lastCircle).getProtons()));
                totalScored = totalScored + variables.PROTON_CARBON_SCORE((1 + ((Carbon)lastCircle).getProtons())) ;
                outputs.add("Carbon Absorbs Proton," + runTime);
                outputs.add("Scored," + runTime + "," + variables.PROTON_CARBON_SCORE((1 + ((Carbon)lastCircle).getProtons()))+ "," + (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * Variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)));
            }
            if((initialCircle.getClass().equals(Carbon.class) && lastCircle.getClass().equals(Proton.class))){
                lastCircle.setTravelTo(initialCircle.getX(),initialCircle.getY(), initialCircle);
                ((Carbon)initialCircle).matchedProtons.add((Proton)lastCircle);
                currentHealth = currentHealth + variables.PROTON_CARBON_SCORE((1 + ((Carbon)initialCircle).getProtons()));
                totalScored = totalScored + variables.PROTON_CARBON_SCORE((1 + ((Carbon)initialCircle).getProtons()));
                outputs.add("Carbon Absorbs Proton," + runTime);
                outputs.add("Scored," + runTime + "," + variables.PROTON_CARBON_SCORE((1 + ((Carbon)initialCircle).getProtons()))+ "," + (variables.HEALTH_DECREASE_CONSTANT() + (totalScored * variables.HEALTH_DECREASE_SCORE_MODIFIER()) + (Math.round(runTime) * Variables.HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER)));
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
        bgMusic.dispose();
        game.gameStage.dispose();
    }
}
