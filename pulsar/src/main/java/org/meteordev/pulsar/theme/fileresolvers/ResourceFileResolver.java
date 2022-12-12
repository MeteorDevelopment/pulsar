package org.meteordev.pulsar.theme.fileresolvers;

import java.io.InputStream;

public class ResourceFileResolver implements IFileResolver {
    private final String root;

    public ResourceFileResolver(String root) {
        if (!root.startsWith("/")) root = "/" + root;
        if (!root.endsWith("/")) root += "/";

        this.root = root;
    }

    @Override
    public String resolvePath(String path) {
        return root + (path.startsWith("/") ? path.substring(1) : path);
    }

    @Override
    public InputStream get(String path) {
        return ResourceFileResolver.class.getResourceAsStream(resolvePath(path));
    }
}
