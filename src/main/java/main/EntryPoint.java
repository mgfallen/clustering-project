package main;

import configuration.SparkConfiguration;
import dataUsage.CeramicDataTransformer;
import entity.Metrics;
import factories.PointFactory;
import logic.*;

import org.apache.spark.ml.evaluation.ClusteringEvaluator;
import org.apache.spark.sql.*;

public class EntryPoint {
    public static void main(String[] args) {
        SparkSession spark = SparkConfiguration.getSpark();

        Dataset<Row> data = spark.read()
                .option("header", true)
                .csv("src/main/resources/chemical-composion-of-ceramic.csv").toDF();

        data = new CeramicDataTransformer(data).getTransformedDataset();
        data.show(false);
        PointFactory pointFactory = new PointFactory();

        int minPoints = 15;
        double eps = 150;
        ClusteringAlgorithm dbscan = new PreprocessorDBScan.builder()
                .setMinPoints(minPoints)
                .setEps(eps)
                .setMetric(Metrics.EUCLID)
                .setDataSet(data)
                .init();
        dbscan.compute();

        int numberOfClusters = 2;
        ClusteringAlgorithm KMeans = new PreprocessorKmeansPP.builder()
                .setNumberOfClusters(numberOfClusters)
                .setMetric(Metrics.EUCLID)
                .setDataSet(data)
                .init();
        KMeans.compute();

        ClusteringEvaluator evaluator = new ClusteringEvaluator();

//        double ari = evaluator.evaluate(data);
//        double ami = evaluator.setMetricName("silhouette").evaluate(data);
//        double vMeasure = evaluator.setMetricName("vMeasure").evaluate(data);

//        System.out.println("ARI: " + ari);
//        System.out.println("AMI: " + ami);
//        System.out.println("vMeasure: " + vMeasure);

        spark.stop();
    }
}
