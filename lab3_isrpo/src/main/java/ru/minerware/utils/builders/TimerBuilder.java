package ru.minerware.utils.builders;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class TimerBuilder {

    public interface TickCallback
    {
        void update (int currentTime);
    }

    public interface FinishCallback
    {
        void finish ();
    }

    private final JavaPlugin plugin;

    /* Каждые msPerTick тиков майнкрафта будет проходить один цикл таймера */
    private final long ticksPerTick;
    private final int maxTime;
    private int time;
    private BukkitTask timerTask;

    /* Вверх или вниз будет идти таймер */
    private final boolean up;

    private TickCallback tickCallback = val -> {};
    private FinishCallback finishCallback = () -> {};

    public TimerBuilder(JavaPlugin plugin, int maxTime, long serverTicksPerTick, boolean up)
    {
        this.plugin = plugin;
        this.ticksPerTick = serverTicksPerTick;
        this.maxTime = maxTime;
        this.up = up;

        this.time = up ? 0 : maxTime;
    }

    public TimerBuilder tick (TickCallback callback)
    {
        this.tickCallback = callback;
        return this;
    }

    public TimerBuilder finish (FinishCallback callback)
    {
        this.finishCallback = callback;
        return this;
    }

    private void finishTimer ()
    {
        finishCallback.finish();
        timerTask.cancel();
    }

    public TimerBuilder start ()
    {
        timerTask = Bukkit.getScheduler().runTaskTimer(plugin, () ->
        {
            if (up)
                time++;
            else
                time--;

            if (!up && time <= 0)
                finishTimer();

            tickCallback.update(time);

            if (up && time >= maxTime)
                finishTimer();

        }, ticksPerTick, ticksPerTick);

        return this;
    }

    public TimerBuilder stop ()
    {
        timerTask.cancel();

        return this;
    }
}
