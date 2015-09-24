package dk.mrspring.updater.core;

import dk.mrspring.api.type.Version;

import java.util.Arrays;

/**
 * Created on 24-09-2015 for MrSpringUpdateChecker.
 */
public class FullVersionDownloadAction extends QueuedAction
{
    Callback callbackHandler;
    Version source;

    public FullVersionDownloadAction(UpdatingMod mod, Version source, Callback callback)
    {
        super(mod);
        this.source = source;
        this.callbackHandler = callback;
    }

    @Override
    public void perform()
    {
        source.downloadFullVersion();
        callbackHandler.callback(source);
    }

    public interface Callback
    {
        void callback(Version updated);
    }
}
