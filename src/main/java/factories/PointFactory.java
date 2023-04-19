package factories;

import entity.Point;

import javax.inject.Singleton;

@Singleton
public class PointFactory {

    public Point createPoint() {
        return new Point();
    }
}
