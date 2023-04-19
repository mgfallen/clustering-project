package dataUsage;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface DataTransformer {
    Dataset<Row> getTransformedDataset();
}
