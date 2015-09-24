package dk.mrspring.updater.core;

import dk.mrspring.api.Function;
import dk.mrspring.api.Parameter;
import dk.mrspring.api.call.Call;
import dk.mrspring.api.call.SimpleCaller;
import dk.mrspring.api.type.LatestVersion;
import dk.mrspring.api.type.Mod;
import dk.mrspring.api.type.Versions;

/**
 * Created on 24-09-2015 for MrSpringUpdateChecker.
 */
public class ModInfoDownloadAction extends QueuedAction
{
    Callback callbackHandler;
    UpdatingMod source;
    Mod modResult = null;
    LatestVersion versionResult = null;
    Versions versionsResult = null;

    public ModInfoDownloadAction(UpdatingMod mod, Callback callback)
    {
        super(mod);
        this.callbackHandler = callback;
        this.source = mod;
    }

    @Override
    public void perform()
    {
        String modId = source.MOD_ID;
        String currentV = source.CURRENT_VERSION;

        SimpleCaller caller = new SimpleCaller(Function.GET_MOD_INFO);
        Call.ModInfoCall modCall = caller.makeCall(new Parameter("m", modId));
        this.modResult = modCall.getDecodedResult();
        caller.setFunction(Function.CHECK_UPDATE);
        Call.CheckUpdateCall updateCall = caller.makeCall(new Parameter("m", modId), new Parameter("v", currentV));
        this.versionResult = updateCall.getDecodedResult();
        caller.setFunction(Function.GET_MOD_VERSIONS);
        Call.GetVersionsCall versionsCall = caller.makeCall(new Parameter("m", modId), new Parameter("s", ""));
        this.versionsResult = versionsCall.getDecodedResult();

        callbackHandler.callback(this.modResult, this.versionResult, this.versionsResult);
    }

    public interface Callback
    {
        void callback(Mod simpleMod, LatestVersion version, Versions simpleVersions);
    }
}
