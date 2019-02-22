package com.appodeal.gdx.callbacks;

public interface RewardedVideoCallback {
    void onRewardedVideoLoaded(boolean b);
    void onRewardedVideoFailedToLoad();
    void onRewardedVideoShown();
    void onRewardedVideoFinished(double amount, String name);
    void onRewardedVideoClosed(boolean isFinished);
    void onRewardedVideoExpired();
}
