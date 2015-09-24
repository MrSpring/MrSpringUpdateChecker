package dk.mrspring.updater.core;

/**
 * Created on 24-09-2015 for MrSpringUpdateChecker.
 */
public class QueuedActionRunner extends Thread
{
    final QueuedAction action;
    public boolean finished = false;

    public QueuedActionRunner(final QueuedAction action)
    {
        if (action == null) throw new IllegalArgumentException("Action cannot be null!");
        this.action = action;
    }

    @Override
    public void run()
    {
        super.run();
        try
        {
            action.perform();
        } catch (Exception e)
        {
            System.err.printf("Action for mod %s threw an exception:", action.getModId());
            e.printStackTrace();
        } finally
        {
            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finished = true;
            ModUpdater.getInstance().queuedActionCallback(this.action);
        }
    }
}
