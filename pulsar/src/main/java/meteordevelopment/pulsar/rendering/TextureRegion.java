package meteordevelopment.pulsar.rendering;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record TextureRegion(double x1, double y1, double x2, double y2) {
}
