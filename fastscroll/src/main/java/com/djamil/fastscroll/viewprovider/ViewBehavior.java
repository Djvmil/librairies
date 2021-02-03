package com.djamil.fastscroll.viewprovider;

/**
 *  @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 * Extending classes should use this interface to get notified about events that occur to the
 * fastscroller elements (handle and bubble) and react accordingly. See {@link DefaultBubbleBehavior}
 * for an example.
 */
public interface ViewBehavior {
    void onHandleGrabbed();
    void onHandleReleased();
    void onScrollStarted();
    void onScrollFinished();
}
