package utils;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.types.DataTypes.DoubleType;

public class CasterToDoubleValues implements Caster {

    private Dataset<Row> dataSet;
    private final List<String> columnNames;

    public CasterToDoubleValues(Dataset<Row> dataSet) {
        this.dataSet = dataSet;
        this.columnNames = Arrays.stream(dataSet.schema().fieldNames())
                .filter(colName -> !colName.equals("CeramicName") && !colName.equals("Part"))
                .collect(Collectors.toList());
    }

    @Override
    public Dataset<Row> getCastedData() {
        this.cast();
        return dataSet;
    }

    private void cast() {
        for (String columnName : columnNames) {
           dataSet = dataSet.withColumn(columnName, col(columnName).cast(DoubleType));
        }
    }
}
