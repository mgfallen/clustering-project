package logic;

import java.util.Map;

public interface ClusteringAlgorithm {
    void compute();
    Map<String, Integer> getNameAndClusterId();
}
