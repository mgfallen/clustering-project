package entity;

import java.util.ArrayList;
import java.util.List;

public class Cluster implements ClusterElement{
    private final int clusterId;
    private final List<Point> points;

    public Cluster(int clusterId) {
        this.clusterId = clusterId;
        this.points = new ArrayList<>();
    }

    @Override
    public void addPoint(Point point) {
        points.add(point);
    }

    @Override
    public void addPointsList(List<Point> pointList) {
        points.addAll(pointList);
    }

    @Override
    public Point getCenter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public int getClusterId() {
        return clusterId;
    }
}
