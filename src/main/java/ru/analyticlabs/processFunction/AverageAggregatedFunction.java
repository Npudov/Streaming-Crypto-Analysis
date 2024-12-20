package ru.analyticlabs.processFunction;

import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import ru.analyticlabs.dataModel.CryptoAggregatedData;
import ru.analyticlabs.dataModel.CryptoData;

public class AverageAggregatedFunction implements WindowFunction<CryptoData, CryptoAggregatedData, String, TimeWindow> {
    @Override
    public void apply(String key, TimeWindow window, Iterable<CryptoData> values, Collector<CryptoAggregatedData> out) throws Exception {
        Double sum = 0.0;
        int count = 0;
        for (CryptoData value: values) {
            sum += value.getClose();
            count++;
        }

        Double avg_price = sum / count;

        CryptoAggregatedData result = new CryptoAggregatedData();
        result.setSymbol(key);
        result.setEvent_time(window.getEnd());
        result.setAverage_price(avg_price);
        out.collect(result);
    }
}
