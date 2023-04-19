package logic;

import entity.ClusterElement;

import java.util.List;

public interface ClusteringAlgorithm {
    void compute();
    List<ClusterElement> getClusterElements();
}
