package main;

import configuration.SparkConfiguration;
import dataUsage.CeramicDataTransformer;
import entity.Metrics;
import logic.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import utils.StatisticsPrinter;

import java.util.List;
import java.util.stream.Collectors;

public class EntryPoint {
    public static void main(String[] args) {
        SparkSession spark = SparkConfiguration.getSpark();
        Logger.getLogger("org").setLevel(Level.ERROR);

        Dataset<Row> data = spark.read()
                .option("header", true)
                .csv("src/main/resources/chemical-composion-of-ceramic.csv").toDF();

        data = new CeramicDataTransformer(data).getTransformedDataset();
        data.show(false);

        //DBSCAN preparing
        int minPoints = 10;
        double eps = 250;
        ClusteringAlgorithm dbscan = new PreprocessorDBScan.builder()
                .setMinPoints(minPoints)
                .setEps(eps)
                .setMetric(Metrics.EUCLID)
                .setDataSet(data)
                .init();
        dbscan.compute();
        Dataset<Row> mDBscan = spark.createDataFrame(createDataFromProducedAlgorithm(dbscan), setSchema());
        Dataset<Row> dbscanData = union(data, mDBscan);

        //KMEANSPP preparing
        int numberOfClusters = 2;
        ClusteringAlgorithm kMeans = new PreprocessorKmeansPP.builder()
                .setNumberOfClusters(numberOfClusters)
                .setMetric(Metrics.EUCLID)
                .setDataSet(data)
                .init();
        kMeans.compute();
        Dataset<Row> mKMeans = spark.createDataFrame(createDataFromProducedAlgorithm(kMeans), setSchema());
        Dataset<Row> kMeansData = union(data, mKMeans);

        dbscanData.show(100, false);
        kMeansData.show(100, false);

        StatisticsPrinter printer = new StatisticsPrinter();
        printer.setDataset(kMeansData).setMethodName("kmeansPP").print();
        printer.setDataset(dbscanData).setMethodName("dbscan").print();

        spark.stop();
    }

    //#FIXME Smells here, need to fix. Maybe I need IoC container for not putting sparkContext everywhere and
    //#FIXME make a special class for this function
    private static List<Row> createDataFromProducedAlgorithm(ClusteringAlgorithm algorithm) {
        return algorithm.getNameAndClusterId().entrySet().stream()
                .map(entry -> RowFactory.create(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private static StructType setSchema() {
        return new StructType(new StructField[]{
                new StructField("Name", DataTypes.StringType, false, Metadata.empty()),
                new StructField("prediction", DataTypes.IntegerType, false, Metadata.empty())
        });
    }

    //In this Java version of Apache Spark I don't have union() method, so I do this
    private static Dataset<Row> union(Dataset<Row> data, Dataset<Row> markedDataset) {
        return data.join(markedDataset, data.col("CeramicName").equalTo(markedDataset.col("Name")), "left_outer")
                .withColumn("prediction", functions.coalesce(markedDataset.col("prediction"), functions.lit(-1)))
                .drop("Name");
    }
}