package dk.mrspring.updater.core;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created on 24-09-2015 for MrSpringUpdateChecker.
 */
public class FileDownloadAction extends QueuedAction
{
    File destination;
    URL downloading;
    private boolean isDownloading = false, hasStarted = false;

    public FileDownloadAction(UpdatingMod mod, File destination, URL downloadFrom)
    {
        super(mod);

        this.destination = destination;
        this.downloading = downloadFrom;
    }

    @Override
    public void perform()
    {
        try
        {
            hasStarted = true;
            isDownloading = true;
            FileUtils.copyURLToFile(downloading, destination);
            isDownloading = false;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isDownloading()
    {
        return isDownloading;
    }

    public boolean hasStarted()
    {
        return hasStarted;
    }
}
