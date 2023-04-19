package utils;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface Caster {
    Dataset<Row> getCastedData();
}
