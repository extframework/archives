package net.yakclient.mixin.registry.pool;

import java.net.URL;
import java.util.Objects;

public class ExternalLibLocation implements Location {
    private final URL url;

    public ExternalLibLocation(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalLibLocation that = (ExternalLibLocation) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

}
