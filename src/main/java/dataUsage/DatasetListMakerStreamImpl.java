package dataUsage;

import entity.Point;
import entity.PointFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

@Singleton
public class DatasetListMakerStreamImpl implements DatasetListMaker {

    private final PointFactory pointFactory;

    public DatasetListMakerStreamImpl(PointFactory pointFactory) {
        this.pointFactory = pointFactory;
    }

    @Override
    public List<Point> getListOfPoints(Dataset<Row> dataset) {
        return dataset.select(col("CeramicName"), col("features"))
                .collectAsList()
                .stream()
                .map(row -> {
                    String name = row.getAs("CeramicName");
                    String nearlyParsedFeature = row.getAs("features").toString();
                    String[] rawFeatures = nearlyParsedFeature.substring(1, nearlyParsedFeature.length() - 1).split(",");
                    List<Double> features = new ArrayList<>(rawFeatures.length);
                    for(Object oneFeature : rawFeatures) {
                        features.add(Double.parseDouble(String.valueOf(oneFeature)));
                    }
                    Point point = pointFactory.createPoint();
                    point.setNameOfRow(name);
                    point.setValues(features);
                    return point;
                })
                .collect(Collectors.toList());
    }
}
