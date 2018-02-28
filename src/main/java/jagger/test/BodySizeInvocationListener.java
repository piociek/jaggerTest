package jagger.test;

import com.griddynamics.jagger.engine.e1.Provider;
import com.griddynamics.jagger.engine.e1.collector.MetricDescription;
import com.griddynamics.jagger.engine.e1.collector.SumMetricAggregatorProvider;
import com.griddynamics.jagger.engine.e1.collector.invocation.InvocationInfo;
import com.griddynamics.jagger.engine.e1.collector.invocation.InvocationListener;
import com.griddynamics.jagger.engine.e1.services.ServicesAware;
import com.griddynamics.jagger.invoker.InvocationException;

public class BodySizeInvocationListener extends ServicesAware implements Provider<InvocationListener> {
    private final String metricName = "body-size";

    @Override
    protected void init() {
        getMetricService().createMetric(new MetricDescription(metricName).
                displayName("Response body size").
                showSummary(true).
                plotData(true).
                addAggregator(new SumMetricAggregatorProvider()));
    }

    @Override
    public InvocationListener provide() {
        return new InvocationListener() {
            @Override
            public void onStart(InvocationInfo invocationInfo) {
            }

            @Override
            public void onSuccess(InvocationInfo invocationInfo) {
                if (invocationInfo.getResult() != null){
                    getMetricService().saveValue(metricName, invocationInfo.getResult().toString().length());
                }
            }

            @Override
            public void onFail(InvocationInfo invocationInfo, InvocationException e) {
            }

            @Override
            public void onError(InvocationInfo invocationInfo, Throwable error) {
            }
        };
    }
}
