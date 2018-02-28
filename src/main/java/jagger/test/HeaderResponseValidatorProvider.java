package jagger.test;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidatorProvider;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HeaderResponseValidatorProvider implements ResponseValidatorProvider {

    public HeaderResponseValidatorProvider() {}

    @Override
    public ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse> provide(String sessionId,
                                                                               String taskId,
                                                                               NodeContext kernelContext) {

        return new ResponseValidator<JHttpQuery, JHttpEndpoint, JHttpResponse>(taskId, sessionId, kernelContext) {
            @Override
            /* This name will be displayed in the reports */
            public String getName() {
                return "Header response validator";
            }

            @Override
            public boolean validate(JHttpQuery query, JHttpEndpoint endpoint, JHttpResponse result, long duration) {
                // validate only if query parameters were provided
                return !query.getPath().contains("response-headers") || query.getQueryParams().keySet().stream().filter(result.getHeaders()::containsKey).count() != 0;
            }
        };
    }
}
