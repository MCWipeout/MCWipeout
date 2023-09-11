package org.stormdev.mcwipeout.frame.obstacles;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.stormdev.mcwipeout.frame.game.CheckPoint;
import org.stormdev.mcwipeout.frame.team.WPoint;

import java.util.ArrayList;
import java.util.List;

public abstract class Obstacle {

    @Getter
    @Setter
    boolean enabled;

    @Getter
    @Setter
    private List<WPoint> points;

    @Getter
    @Setter
    private CheckPoint checkPoint;

    public Obstacle() {
        this.enabled = false;
        this.points = new ArrayList<>();
    }

    public abstract void handle(Event event);

    public abstract void run();

    public abstract void reset();

    public abstract void enable();

    public abstract void load();
}
