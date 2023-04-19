package logic;

import dataUsage.DatasetListMaker;
import dataUsage.DatasetListMakerStreamImpl;
import entity.Metrics;
import factories.PointFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import utils.DistanceByChooseMetric;
import utils.DistanceCalculator;

public class PreprocessorKmeansPP implements Preprocessor{
    private final int numberOfClusters;
    private final Dataset<Row> dataset;
    private final DistanceCalculator distanceCalculator;
    private final DatasetListMaker datasetListMaker;

    public PreprocessorKmeansPP(builder builder) {
        this.numberOfClusters = builder.numberOfClusters;
        this.dataset = builder.dataset;

        // over special instrument we are creating here
        PointFactory pointFactory = new PointFactory();
        this.datasetListMaker = new DatasetListMakerStreamImpl(pointFactory);

        Metrics metric = builder.metric;
        this.distanceCalculator = new DistanceByChooseMetric(metric);
    }

    @Override
    public ClusteringAlgorithm init() {
        return new KmeansPP.builder()
                .setNumberOfClusters(numberOfClusters)
                .setDataSet(datasetListMaker.getListOfPoints(dataset))
                .setDistanceCalculator(distanceCalculator)
                .create();
    }

    public static class builder {
        private int numberOfClusters;
        private Dataset<Row> dataset;
        private Metrics metric;

        public builder() {
        }

        public builder setNumberOfClusters(int numberOfClusters) {
            this.numberOfClusters = numberOfClusters;
            return this;
        }

        public builder setMetric(Metrics metric) {
            this.metric = metric;
            return this;
        }

        public PreprocessorKmeansPP setDataSet(Dataset<Row> dataset) {
            this.dataset = dataset;
            return new PreprocessorKmeansPP(this);
        }
    }
}
