package entity;

import java.util.ArrayList;
import java.util.List;

public class Centroid implements ClusterElement {
    private final Point center;
    private final List<Point> neighboursPoints;

    public Centroid(Point center) {
        this.center = center;
        this.neighboursPoints = new ArrayList<>();
    }

    @Override
    public Point getCenter() {
        return center;
    }

    @Override
    public List<Point> getPoints() {
        return neighboursPoints;
    }

    @Override
    public void addPoint(Point point) {
        neighboursPoints.add(point);
    }

    @Override
    public void addPointsList(List<Point> pointsCentroid) {
        neighboursPoints.addAll(pointsCentroid);
    }

    @Override
    public int getClusterId() {
        throw new UnsupportedOperationException();
    }

}
