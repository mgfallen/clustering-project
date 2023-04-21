package utils;

import org.apache.spark.ml.evaluation.ClusteringEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class StatisticsPrinter {
    private final ClusteringEvaluator evaluator = new ClusteringEvaluator();

    private Dataset<Row> data;
    private String methodName;

    public StatisticsPrinter setDataset(Dataset<Row> data) {
        this.data = data;
        return this;
    }

    public StatisticsPrinter setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public void print() {
        evaluator.setPredictionCol("prediction");
        double ari = evaluator.evaluate(data);

        System.out.println("======== " + methodName.toUpperCase() + " METRICS ============");
        System.out.println("ARI: " + ari);
    }
}
