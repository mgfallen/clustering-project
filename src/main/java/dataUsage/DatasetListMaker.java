package dataUsage;

import entity.Point;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface DatasetListMaker {
    List<Point> getListOfPoints(Dataset<Row> dataset);
}
