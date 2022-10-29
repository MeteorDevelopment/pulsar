package meteordevelopment.pulsar.rendering;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record CharData(float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1, float xAdvance) {
}
