package jagger.test;

import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EndpointsProvider implements Iterable {
    private List<JHttpEndpoint> endpoints = new ArrayList<>();

    public EndpointsProvider() {
        JHttpEndpoint httpEndpoint = new JHttpEndpoint(URI.create("http://httpbin.org"));
//        JHttpEndpoint httpEndpoint = new JHttpEndpoint(URI.create("http://localhost:5000"));
        endpoints.add(httpEndpoint);
    }

    @Override
    public Iterator<JHttpEndpoint> iterator() {
        return endpoints.iterator();
    }
}
