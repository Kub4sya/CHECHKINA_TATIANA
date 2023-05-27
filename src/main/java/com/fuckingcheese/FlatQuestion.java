package com.fuckingcheese;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;

public class FlatQuestion {
//    public double geometricMean(ArrayList<Double> x) {
//        int n = x.size();
//        double GM_log = 0.0;
//        for (int i = 0; i < n; i++) {
//            if (x.get(i) == 0) {
//                return 0.0;
//            }
//            GM_log += Math.log(Math.abs(x.get(i)));
//
//        }
//        return Math.exp(GM_log / n);
//    }
//    public double simpleMean(ArrayList<Double> x) {
//        int n = x.size();
//        double SM = 0.0;
//        for (int i = 0; i < n; i++) {
//            SM += x.get(i);
//        }
//        return Math.exp(SM/n);
//    }

    public DescriptiveStatistics MakeStatistic (ArrayList<Double> s){
        DescriptiveStatistics makeStatistic = new DescriptiveStatistics();
        for (double v : s) {
            makeStatistic.addValue(v);
        }
        return makeStatistic;
    }

    public ArrayList<Double> calculate(ArrayList<Double> data)
    {
        DescriptiveStatistics ms = MakeStatistic(data);
        ArrayList<Double> XResult = new ArrayList<>();
        //1
        XResult.add(ms.getGeometricMean());
        //2
        XResult.add(ms.getMean());
        //3
        XResult.add(ms.getStandardDeviation());
        //4
        XResult.add(ms.getMax()-ms.getMin());
        //6
        XResult.add(Double.valueOf(ms.getN()));
        //7
        XResult.add(ms.getVariance());
        //9
        XResult.add((ms.getStandardDeviation()/ Math.abs(ms.getMean())));
        //10
        XResult.add(ms.getMax());
        XResult.add(ms.getMin());
        //8
        TDistribution t = new TDistribution(data.size()-1);
        double tlevel = t.inverseCumulativeProbability(0.05);
        double conf = tlevel*ms.getStandardDeviation()/Math.sqrt(data.size());
        XResult.add(ms.getMean()-conf);
        XResult.add(ms.getMean()+conf);

        return XResult;
    }
}
