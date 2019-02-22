package com.appodeal.gdx.callbacks;

public interface BannerCallback {
    void onBannerLoaded(boolean isPrecache);
    void onBannerFailedToLoad();
    void onBannerShown();
    void onBannerClicked();
    void onBannerExpired();
}