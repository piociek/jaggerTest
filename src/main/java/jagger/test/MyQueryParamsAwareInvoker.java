package jagger.test;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.griddynamics.jagger.engine.e1.Provider;
import com.griddynamics.jagger.invoker.InvocationException;
import com.griddynamics.jagger.invoker.Invoker;
import com.griddynamics.jagger.invoker.v2.DefaultHttpInvoker;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class MyQueryParamsAwareInvoker implements Provider<Invoker> {
    private static final Logger log = LoggerFactory.getLogger(MyQueryParamsAwareInvoker.class);

    private static Iterator<Map.Entry<String, String>> headerProviderIterator;

    public MyQueryParamsAwareInvoker() {
        ;
    }

    @Override
    public Invoker provide() {
        return new DefaultHttpInvoker() {
            @Override
            public JHttpResponse invoke(JHttpQuery query, JHttpEndpoint endpoint) throws InvocationException {

                // apply query params only for one of the queries
                if (query.getPath().contains("response-headers")) {
                    if (!areHeadersAvailable(headerProviderIterator)){
                        headerProviderIterator = new HeaderProvider().iterator();
                    }
                    Map.Entry<String, String> entry = headerProviderIterator.next();
                    query.queryParams(Collections.singletonMap(entry.getKey(), entry.getValue()));
                }

                return super.invoke(query, endpoint);
            }
        };
    }

    private boolean areHeadersAvailable(Iterator iterator){
        return iterator != null && iterator.hasNext();
    }
}

class HeaderProvider implements Iterable<Map.Entry<String, String>> {

    private final String FILE_NAME = "headers.csv";
    private Map<String, String> headers;

    {
        try {
            headers = Files.lines(Paths.get(FILE_NAME))
                    .map(line -> line.split(","))
                    .collect(Collectors.toMap(line -> line[0], line -> line[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return headers.entrySet().iterator();
    }
}

