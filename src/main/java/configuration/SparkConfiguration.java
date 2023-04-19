package configuration;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SparkConfiguration {
    public static SparkSession getSpark() {
        System.setProperty("hadoop.home.dir", "C:\\java\\hadoop");

        SparkConf conf = new SparkConf()
                .setAppName("Java realization DBScan and KmeansPP")
                .setMaster("local");

        return SparkSession
                .builder()
                .config(conf)
                .getOrCreate();
    }
}
