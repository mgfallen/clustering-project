package entity;

import java.util.List;

public interface ClusterElement {
    int getClusterId();
    List<Point> getPoints();
    void addPoint(Point point);
    void addPointsList(List<Point> pointList);
    Point getCenter();

}
