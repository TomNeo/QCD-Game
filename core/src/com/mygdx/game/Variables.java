package com.mygdx.game;


public class Variables {

    /** Circles Radii **/

    public static float PROTON_RADIUS = 50f;
    public static float DEUTERIUM_RADIUS =  PROTON_RADIUS * (float)Math.sqrt(2);
    public static float HELION_RADIUS = PROTON_RADIUS * (float)Math.sqrt(3);
    public static float HELIUM_RADIUS = PROTON_RADIUS * 2;
    public static float BERYLLIUM_RADIUS = PROTON_RADIUS / .1547f;
    public static float CARBON_RADIUS = PROTON_RADIUS / .13f;

    /** Circles Lifespans **/

    public static float PROTON_LIFESPAN = 10f;
    public static float DEUTERIUM_LIFESPAN =  10f;
    public static float HELION_LIFESPAN = 5f;
    public static float HELIUM_LIFESPAN = 12f;
    public static float BERYLLIUM_LIFESPAN = 6f;
    public static float CARBON_LIFESPAN = 8f;
    public static float PROTON_CAPTURE_LIFESPAN_BUMP = .8f;

    /** Game play Mechanics **/

    public static long STARTING_HEALTH = 5000;
    public long COST_OF_CREATING_PROTON = 150;
    public float HEALTH_DECREASE_CONSTANT(){
        if(Game_Score_Tier == 1){
            return 100f;
        }else if(Game_Score_Tier == 2){
            return 150f;
        }else if(Game_Score_Tier == 3){
            return 225f;
        }if(Game_Score_Tier == 4){
            return 350f;
        }
        return  150;
    }

    public float HEALTH_DECREASE_SCORE_MODIFIER(){
        if(Game_Score_Tier == 1){
            return .0025f;
        }else if(Game_Score_Tier == 2){
            return .0025f;
        }else if(Game_Score_Tier == 3){
            return 0f;
        }
        return .0025f;
    }
    public static float HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER = 0;
    public int Game_Score_Tier = 1;
    public boolean StarMan = false;

    /** Combination Score Bonuses **/

    public static long PROTON_PROTON_SCORE = 400;
    public static long PROTON_DEUTERIUM_SCORE = 500;
    public static long HELION_HELION_SCORE = 750;
    public static long HELIUM_HELIUM_SCORE = 1000;
    public static long HELIUM_BERYLLIUM_SCORE = 2000;
    public long PROTON_CARBON_SCORE(int numberOfProtons ){
        if(Game_Score_Tier == 1){
            return (COST_OF_CREATING_PROTON /3) * numberOfProtons;
        }else if(Game_Score_Tier == 2){
            return COST_OF_CREATING_PROTON / 2 * numberOfProtons;
        }else if(Game_Score_Tier == 3){
            return 75 * numberOfProtons;
        }
        return  COST_OF_CREATING_PROTON * numberOfProtons;
    }

    /** Other Visual things **/

    public static float MATCH_INDICATOR_RADIUS = 6;
    public static float[] MATCH_INDICATOR_COLOR = new float[] {1,1,1,1};
    public static final float CIRCLE_LINE_WIDTH = 5f;


    /** Health Bar Variables **/

    public static float HEALTH_TIER_1_VALUE = 1000;
    public static float HEALTH_TIER_2_VALUE = 2000;
    public static float HEALTH_TIER_3_VALUE = 3000;
    public static float HEALTH_TIER_4_VALUE = 3000;
    public static float HEALTH_TIER_5_VALUE = 3000;
    public static float MAX_HEALTH = HEALTH_TIER_1_VALUE + HEALTH_TIER_2_VALUE + HEALTH_TIER_3_VALUE + HEALTH_TIER_4_VALUE + HEALTH_TIER_5_VALUE;

    /** sfx paths **/
    //public static final String SFX_CREATE_ATOM = "sfx/MegaBuster";

    public void inputs(float currentHealth, long totalScored, float runTime) {
        if(totalScored >= 6000){
            if(totalScored >= 20000){
                if(totalScored >= 50000){
                    COST_OF_CREATING_PROTON = 10;
                    Game_Score_Tier = 4;
                }else{
                    COST_OF_CREATING_PROTON = 50;
                    Game_Score_Tier = 3;
                }
            }else{
                COST_OF_CREATING_PROTON = 100;
                Game_Score_Tier = 2;
            }
        }
        if(currentHealth > MAX_HEALTH && !StarMan){
            StarMan = true;
        }
    }
}
