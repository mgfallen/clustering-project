package logic;

import entity.Cluster;
import entity.ClusterElement;
import entity.Point;
import scala.Tuple2;
import utils.DistanceCalculator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class DBScanSparkNaive implements ClusteringAlgorithm {

    private final List<Point> points;
    private final int minPoints;
    private final double eps;
    private final DistanceCalculator distanceCalculator;

    private final List<ClusterElement> clusters = new ArrayList<>();

    private DBScanSparkNaive(builder builder) {
        this.eps = builder.eps;
        this.points = builder.points;
        this.minPoints = builder.minPoints;
        this.distanceCalculator = builder.distanceCalculator;
    }

    @Override
    public void compute() {
        Set<Point> visitedPoints = new HashSet<>();
        int numberOfStartPoints = points.size();
        int clusterNumber = 0;

        while (points.size() > 0 && visitedPoints.size() < numberOfStartPoints - 1) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Point point = points.get(random.nextInt(points.size()));

            if (!visitedPoints.contains(point)) {
                visitedPoints.add(point);

                List<Point> neighbours = regionQuery(points, point);
                if (neighbours.size() < minPoints) {
                    // probably noise point, but it's not sure
                    point.setNoise();
                    continue;
                }

                ClusterElement cluster = new Cluster(clusterNumber);
                cluster.addPointsList(neighbours);
                visitedPoints.addAll(neighbours);
                clusterNumber++;

                for (Point neighPoint : neighbours) {
                    if (!visitedPoints.contains(neighPoint) || neighPoint.isNoise()) {
                        if (distanceCalculator.getDistance(neighPoint, point) >= minPoints) {
                            List<Point> neighOfNeighPoints = regionQuery(points, neighPoint);
                            cluster.addPointsList(neighOfNeighPoints);
                            visitedPoints.addAll(neighOfNeighPoints);
                        } else {
                            neighPoint.setNoise();
                        }
                    }
                }
                points.removeAll(cluster.getPoints());
                clusters.add(cluster);
            }
        }
    }

    @Override
    public Map<String, Integer> getNameAndClusterId() {
        Map<String, Integer> markedPoint = new HashMap<>();
        for(ClusterElement clusterElement : clusters) {
            int clusterId = clusterElement.getClusterId();
            for(Point point: clusterElement.getPoints()) {
                markedPoint.put(point.getNameOfRow(), clusterId);
            }
        }
        return markedPoint;
    }

    // This private function provide us to find neighbours points
    private List<Point> regionQuery(List<Point> points, Point point) {
        return points.stream()
                .filter(neighbour -> distanceCalculator.getDistance(point, neighbour) <= eps)
                .collect(Collectors.toList());
    }

    public static class builder {
        private int minPoints;
        private List<Point> points;
        private double eps;
        private DistanceCalculator distanceCalculator;

        public builder() {
        }

        protected builder setMinPoints(int minPoints) {
            this.minPoints = minPoints;
            return this;
        }

        protected builder setEps(double eps) {
            this.eps = eps;
            return this;
        }

        protected builder setPoints(List<Point> points) {
            this.points = points;
            return this;
        }

        protected builder setDistanceCalculator(DistanceCalculator distanceCalculator) {
            this.distanceCalculator = distanceCalculator;
            return this;
        }

        protected ClusteringAlgorithm create() {
            return new DBScanSparkNaive(this);
        }
    }
}
