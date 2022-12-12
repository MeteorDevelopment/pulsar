package org.meteordev.pulsar.theme.fileresolvers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NormalFileResolver implements IFileResolver {
    private final String root;

    public NormalFileResolver(String root) {
        if (root.isEmpty() || root.equals("/")) this.root = "";
        else {
            if (root.startsWith("/")) root = root.substring(1);
            if (!root.endsWith("/")) root = root + "/";

            this.root = root;
        }
    }

    @Override
    public String resolvePath(String path) {
        return root + (path.startsWith("/") ? path.substring(1) : path);
    }

    @Override
    public InputStream get(String path) {
        try {
            return new FileInputStream(resolvePath(path));
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
