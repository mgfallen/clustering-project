package logic;

import entity.*;
import utils.DistanceCalculator;
import org.jboss.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

public class KmeansPP implements ClusteringAlgorithm {

    private final int numberOfClusters;
    private final List<Point> points;
    private final DistanceCalculator distanceCalculator;
    private final List<Point> centroids;

    public KmeansPP(builder builder) {
        this.numberOfClusters = builder.numberOfClusters;
        this.points = builder.points;
        this.distanceCalculator = builder.distanceCalculator;
        this.centroids = new ArrayList<>(numberOfClusters);
    }

    @Override
    public void compute() {
        // Select the first centroid randomly from points
        ThreadLocalRandom random = ThreadLocalRandom.current();
        centroids.add(points.get(random.nextInt(points.size())));

        while (centroids.size() < numberOfClusters) {
            double maxDistance = Double.NEGATIVE_INFINITY;
            Point nextCentroid = null;
            for (Point p : points) {
                double minDistance = Double.POSITIVE_INFINITY;
                for (Point c : centroids) {
                    double distance = distanceCalculator.getDistance(p, c);
                    minDistance = Math.min(minDistance, distance);
                }
                if (minDistance > maxDistance) {
                    maxDistance = minDistance;
                    nextCentroid = p;
                }
            }
            centroids.add(nextCentroid);
        }
        printClusterElements(getClusterElements());
    }

    @Override
    public List<ClusterElement> getClusterElements() {
        List<ClusterElement> clusters = new ArrayList<>();

        for (Point centroidPt : centroids) {
            Centroid centroid = new Centroid(centroidPt);
            clusters.add(centroid);
        }

        for (Point point : points) {
            double minDist = Double.MAX_VALUE;
            ClusterElement closestCentroid = null;

            for (ClusterElement centroid : clusters) {
                double dist = distanceCalculator.getDistance(point, centroid.getCenter());
                if (dist < minDist) {
                    minDist = dist;
                    closestCentroid = centroid;
                }
            }
            assert closestCentroid != null;
            closestCentroid.addPoint(point);
        }
        return clusters;
    }

    // #FIXME for debugging only
    private void printClusterElements(List<ClusterElement> centroids) {
        for (ClusterElement centroid : centroids) {
            System.out.println("Центр кластера лежит в: " + centroid.getCenter().getNameOfRow());
            for (Point point : centroid.getPoints()) {
                System.out.println(point.getNameOfRow());
            }
        }
    }

    protected static class builder {
        private int numberOfClusters;
        private List<Point> points;
        private DistanceCalculator distanceCalculator;

        public builder() {
        }

        protected builder setNumberOfClusters(int numberOfClusters) {
            this.numberOfClusters = numberOfClusters;
            return this;
        }

        protected builder setDistanceCalculator(DistanceCalculator distanceCalculator) {
            this.distanceCalculator = distanceCalculator;
            return this;
        }

        protected builder setDataSet(List<Point> points) {
            this.points = points;
            return this;
        }

        protected KmeansPP create(){
            return new KmeansPP(this);
        }
    }
}
