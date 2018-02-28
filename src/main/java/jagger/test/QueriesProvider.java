package jagger.test;

import com.griddynamics.jagger.invoker.v2.JHttpQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueriesProvider  implements Iterable {
    @Override
    public Iterator iterator() {
        List<JHttpQuery> queries = new ArrayList<>();
        queries.add(new JHttpQuery()
                .get()
                .responseBodyType(String.class)
                .path("get"));
        queries.add(new JHttpQuery()
                .get()
                .responseBodyType(String.class)
                .path("xml"));
        queries.add(new JHttpQuery()
                .get()
                .responseBodyType(String.class)
                .path("response-headers"));

        return queries.iterator();
    }

}
