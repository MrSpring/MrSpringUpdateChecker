package dk.mrspring.updater.core;

/**
 * Created by MrSpring on 07-07-2015 for MC Music Player.
 */
public interface QueuedAction
{
    public void perform();

    public String id();
}
