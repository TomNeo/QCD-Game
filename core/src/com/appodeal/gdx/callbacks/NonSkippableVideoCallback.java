package com.appodeal.gdx.callbacks;

public interface NonSkippableVideoCallback {
    void onNonSkippableVideoLoaded(boolean b);
    void onNonSkippableVideoFailedToLoad();
    void onNonSkippableVideoShown();
    void onNonSkippableVideoFinished();
    void onNonSkippableVideoClosed(boolean b);
    void onNonSkippableVideoExpired();
}
