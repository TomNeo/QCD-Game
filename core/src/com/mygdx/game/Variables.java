package com.mygdx.game;


public class Variables {

    /** Circles Radii **/

    public static float PROTON_RADIUS = 50f;
    public static float DEUTERIUM_RADIUS =  PROTON_RADIUS * (float)Math.sqrt(2);
    public static float HELION_RADIUS = PROTON_RADIUS * (float)Math.sqrt(3);;
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

    public static long STARTING_HEALTH = 3000;
    public static long COST_OF_CREATING_PROTON = 150;
    public static float HEALTH_DECREASE_CONSTANT = 150;
    public static float HEALTH_DECREASE_SCORE_MODIFIER = .01f;
    public static float HEALTH_DECREASE_COMPOUNDING_TIME_MODIFIER = 0;

    /** Combination Score Bonuses **/

    public static long PROTON_PROTON_SCORE = 400;
    public static long PROTON_DEUTERIUM_SCORE = 500;
    public static long HELION_HELION_SCORE = 750;
    public static long HELIUM_HELIUM_SCORE = 1000;
    public static long HELIUM_BERYLLIUM_SCORE = 1200;
    public static long PROTON_CARBON_SCORE = COST_OF_CREATING_PROTON/3;

    /** Other Visual things **/

    public static float MATCH_INDICATOR_RADIUS = 6;
    public static float[] MATCH_INDICATOR_COLOR = new float[] {1,1,1,1};


    /** Health Bar Variables **/

    public static float TIER_1_VALUE = 1000;
    public static float TIER_2_VALUE = 2000;
    public static float TIER_3_VALUE = 3000;
    public static float TIER_4_VALUE = 3000;
    public static float TIER_5_VALUE = 3000;

    /** sfx paths **/
    public static final String SFX_CREATE_ATOM = "sfx/MegaBuster";

}
