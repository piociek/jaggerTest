package jagger.test;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;

public class BodyResponseValidatorProvider implements ResponseValidatorProvider {

    private final String url;
    private final String expectedInBody;

    public BodyResponseValidatorProvider(String url, String expectedInBody) {
        this.url = url;
        this.expectedInBody = expectedInBody;
    }

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String sessionId,
                                                                               String taskId,
                                                                               NodeContext kernelContext) {

        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(taskId, sessionId, kernelContext) {
            @Override
            public String getName() {
                return "Body response validator";
            }

            @Override
            public boolean validate(JHttpQuery query, JHttpEndpoint endpoint, JHttpResponse result, long duration) {
                return !query.getPath().contains(url) || result.getBody().toString().contains(expectedInBody);
            }
        };
    }
}
