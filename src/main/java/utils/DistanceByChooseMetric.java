package utils;

import entity.Metrics;
import entity.Point;

import java.util.List;

import static java.lang.Math.*;

public class DistanceByChooseMetric implements DistanceCalculator {
    private final Metrics metric;

    public DistanceByChooseMetric(Metrics metric) {
        this.metric = metric;
    }

    @Override
    public double getDistance(Point from, Point to) {
        List<Double> fromInArray = from.getValues();
        List<Double> toInArray = to.getValues();
        assert fromInArray.size() == toInArray.size();
        double resultDistance = 0;

        switch (metric) {
            case EUCLID: {
                for (int i = 0; i < fromInArray.size(); i++) {
                    resultDistance += pow(fromInArray.get(i) - toInArray.get(i), 2);
                }
                resultDistance = sqrt(resultDistance);
                break;
            }
            case MANHATTAN: {
                for (int i = 0; i < fromInArray.size(); i++) {
                    resultDistance += abs(fromInArray.get(i) - toInArray.get(i));
                }
                break;
            }
            case CHEBYSHEV: {
                for (int i = 0; i < fromInArray.size(); i++) {
                    resultDistance = max(abs(fromInArray.get(i) - toInArray.get(i)), resultDistance);
                }
            }
            case POWER: {
                for (int i = 0; i < fromInArray.size(); i++) {
                    resultDistance = pow(fromInArray.get(i) - toInArray.get(i), 1 / 5);
                }
                resultDistance = sqrt(resultDistance);
            }
        }
        return resultDistance;
    }
}
