package ru.minerware.eventsystem;

import ru.minerware.eventsystem.events.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EventManager {

    private final List<Event> events = new ArrayList<>();
    private Event currentEvent;

    public EventManager ()
    {
        events.add(new StayOnDiamondEvent());
        events.add(new FloatingProblemsEvent());
        events.add(new CraftEvent());
        events.add(new ShearSheepEvent());
        events.add(new PvpEvent());
        events.add(new MilkTheCowEvent());
        events.add(new WaterDropEvent());
        events.add(new AnvilEvent());
        events.add(new PotionEvent());
        events.add(new SafeGroundEvent());
    }

    public List<Event> getEvents() {
        return events;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public boolean isEventStarting ()
    {
        return events.stream().anyMatch(Event::isStarting);
    }

    public Event getRandomEvent ()
    {
        return events.get(ThreadLocalRandom.current().nextInt(0, events.size()));
    }
}
