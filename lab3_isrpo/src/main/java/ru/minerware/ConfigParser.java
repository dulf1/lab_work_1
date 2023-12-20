package ru.minerware;

import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ConfigParser {

    private final Core core;

    private Location lobbyRespawn;
    private Location playRespawn;

    private int playingTime;
    private int maxPlayers;
    private int yLimit;

    private List<Location> interactPoses;

    public ConfigParser (Core core)
    {
        this.core = core;
    }

    public boolean parseConfig ()
    {
        try
        {
            lobbyRespawn = parseLocation("lobby_respawn");
            playRespawn = parseLocation("play_respawn");

            playingTime = core.getConfig().getInt("playing_time");
            maxPlayers = core.getConfig().getInt("max_players");
            yLimit = core.getConfig().getInt("min_y_to_death");

            interactPoses = parseLocationList("interact_poses");

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке конфига");
            return false;
        }
    }

    private Location parseLocation0 (String[] rawCoord)
    {
        double[] coords = new double[3];

        for (int i = 0; i < 3; i++)
            coords[i] = Double.parseDouble(rawCoord[i]);

        return new Location(
                core.getWorld(),
                coords[0],
                coords[1],
                coords[2]
        );
    }

    private Location parseLocation (String path)
    {
        String[] rawCoords = core.getConfig().getString(path).split(",");

        return parseLocation0(rawCoords);
    }

    private List<Location> parseLocationList (String path)
    {
        List<Location> returnArray = new ArrayList<>();

        for (String coord : core.getConfig().getStringList(path))
        {
            returnArray.add(parseLocation0(coord.split(",")));
        }

        return returnArray;
    }
}
