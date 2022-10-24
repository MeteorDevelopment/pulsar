package meteordevelopment.pulsar.theme.fileresolvers;

import java.io.InputStream;

public interface IFileResolver {
    String resolvePath(String path);

    InputStream get(String path);
}
