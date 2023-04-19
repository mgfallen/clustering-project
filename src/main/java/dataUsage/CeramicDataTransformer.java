package dataUsage;

import utils.CasterToDoubleValues;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Arrays;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.when;

public class CeramicDataTransformer implements DataTransformer{
    private Dataset<Row> dataset;

    public CeramicDataTransformer(Dataset<Row> dataset) {
        this.dataset = dataset;
    }
    @Override
    public Dataset<Row> getTransformedDataset() {
        dataset = new CasterToDoubleValues(dataset).getCastedData();

        return new VectorAssembler()
                .setInputCols(Arrays.stream(dataset.columns())
                        .filter(row -> !row.equals("Part") && !row.equals("CeramicName"))
                        .toArray(String[]::new))
                .setOutputCol("features")
                .transform(dataset)
                .select(col("CeramicName"), col("features"), when(col("Part").equalTo("Body"), "0").otherwise("1").as("PartBinary"));
    }
}
