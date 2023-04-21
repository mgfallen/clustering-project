package logic;

import dataUsage.DatasetListMaker;
import dataUsage.DatasetListMakerStreamImpl;
import entity.Metrics;
import entity.PointFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import utils.DistanceByChooseMetric;
import utils.DistanceCalculator;

public class PreprocessorDBScan implements Preprocessor{
    private final Dataset<Row> dataset;
    private final int minPoints;
    private final double eps;
    private final DistanceCalculator distanceCalculator;
    private final DatasetListMaker datasetListMaker;

    private PreprocessorDBScan(builder builder) {
        this.eps = builder.eps;
        this.dataset = builder.dataset;
        this.minPoints = builder.minPoints;


        PointFactory pointFactory = new PointFactory();
        this.datasetListMaker = new DatasetListMakerStreamImpl(pointFactory);
        Metrics metric = builder.metric;
        this.distanceCalculator = new DistanceByChooseMetric(metric);
    }

    @Override
    public ClusteringAlgorithm init() {
        return new DBScanSparkNaive.builder()
                .setPoints(datasetListMaker.getListOfPoints(dataset))
                .setEps(eps)
                .setMinPoints(minPoints)
                .setDistanceCalculator(distanceCalculator)
                .create();
    }

    public static class builder {
        private int minPoints;
        private Dataset<Row> dataset;
        private double eps;
        private Metrics metric;

        public builder() {
        }

        public builder setMinPoints(int minPoints) {
            this.minPoints = minPoints;
            return this;
        }

        public builder setEps(double eps) {
            this.eps = eps;
            return this;
        }

        public builder setMetric(Metrics metric) {
            this.metric = metric;
            return this;
        }

        public PreprocessorDBScan setDataSet(Dataset<Row> dataset) {
            this.dataset = dataset;
            return new PreprocessorDBScan(this);
        }
    }
}
