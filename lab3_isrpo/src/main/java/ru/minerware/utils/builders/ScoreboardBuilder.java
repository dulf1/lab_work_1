package ru.minerware.utils.builders;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import ru.minerware.utils.U;

public class ScoreboardBuilder {

    private final Scoreboard scoreboard;
    private final Objective objective;

    private int lastScore;

    public ScoreboardBuilder (String title)
    {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("stats", "dummy");

        objective.setDisplayName(U.color(title));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public ScoreboardBuilder addLine (String format, Object... args)
    {
        objective.getScore(U.color(String.format(format, args))).setScore(lastScore--);

        return this;
    }

    public ScoreboardBuilder addLine (String text)
    {
        objective.getScore(U.color(text)).setScore(lastScore--);

        return this;
    }

    public Scoreboard build ()
    {
        return scoreboard;
    }
}
