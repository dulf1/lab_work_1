package ru.minerware.data;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.minerware.Core;
import ru.minerware.GamePhase;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerInfo {

    public static final List<PlayerInfo> PLAYERS = new ArrayList<>();

    private final Player player;
    private int score;

    public PlayerInfo (Player player)
    {
        this.player = player;
        this.score = 0;

        PLAYERS.add(this);
    }

    public static PlayerInfo get (Player player)
    {
        return PLAYERS.stream()
                .filter(info -> info.getPlayer().equals(player))
                .findFirst()
                .orElse(null);
    }

    public static PlayerInfo get (String name)
    {
        return PLAYERS.stream()
                .filter(info -> info.getPlayer().getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void addScore(int score) {
        this.score += score;
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1); // NOTE_PIANO
    }

    public void respawn ()
    {
        player.spigot().respawn();

        if (Core.getInstance().getGamePhase() == GamePhase.PLAY)
        {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(Core.getInstance().getConfigParser().getPlayRespawn());
        }
        else
        {
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(Core.getInstance().getConfigParser().getLobbyRespawn());
        }
    }
}
