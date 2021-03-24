package fr.insa.lyon.ifa1.models.request;

import fr.insa.lyon.ifa1.models.map.Intersection;

public class DurationPassagePoint extends PassagePoint {

    private int duration;
    private PassagePointType type;

    public DurationPassagePoint(Intersection intersection, int duration, PassagePointType type) {
        super(intersection);
        this.duration = duration;
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public PassagePointType getType() {
        return type;
    }

}
