package org.meteordev.pulsar.rendering;

import org.joml.Matrix4f;
import org.meteordev.juno.api.Juno;
import org.meteordev.juno.api.JunoProvider;
import org.meteordev.juno.api.pipeline.Pipeline;
import org.meteordev.juno.api.pipeline.PipelineInfo;
import org.meteordev.juno.api.pipeline.PrimitiveType;
import org.meteordev.juno.api.pipeline.state.WriteMask;
import org.meteordev.juno.api.pipeline.vertexformat.StandardFormats;
import org.meteordev.juno.api.shader.ShaderInfo;
import org.meteordev.juno.api.shader.ShaderType;
import org.meteordev.juno.api.utils.MeshBuilder;
import org.meteordev.pts.utils.ColorFactory;
import org.meteordev.pts.utils.IColor;
import org.meteordev.pulsar.widgets.Cell;
import org.meteordev.pulsar.widgets.Widget;

public class DebugRenderer {
    private static final IColor CELL_COLOR = ColorFactory.create(25, 225, 25, 255);
    private static final IColor WIDGET_COLOR = ColorFactory.create(25, 25, 225, 255);

    private static Pipeline pipeline;
    private static MeshBuilder mb;

    private static void lazyInit() {
        if (pipeline == null) {
            Juno juno = JunoProvider.get();

            pipeline = juno.findPipeline(new PipelineInfo()
                    .setPrimitiveType(PrimitiveType.LINES)
                    .setVertexFormat(StandardFormats.POSITION_2D_COLOR)
                    .setShaders(
                            ShaderInfo.resource(ShaderType.VERTEX, "/pulsar/shaders/basic.vert"),
                            ShaderInfo.resource(ShaderType.FRAGMENT, "/pulsar/shaders/basic.frag")
                    )
                    .setWriteMask(WriteMask.COLOR)
            );

            mb = new MeshBuilder(pipeline.getInfo().vertexFormat);
        }
    }

    public static void render(Widget widget, int windowWidth, int windowHeight) {
        Juno juno = JunoProvider.get();

        lazyInit();
        mb.begin();

        render(widget);

        juno.bind(pipeline);
        pipeline.getProgram().setUniform("u_Proj", new Matrix4f().ortho2D(0, windowWidth, windowHeight, 0));
        mb.draw();
    }

    private static void render(Widget widget) {
        lineBox(widget.x, widget.y, widget.width, widget.height, WIDGET_COLOR);

        for (Cell<?> cell : widget) {
            lineBox(cell.x, cell.y, cell.width, cell.height, CELL_COLOR);
            render(cell.widget());
        }
    }

    private static void lineBox(double x, double y, double width, double height, IColor color) {
        line(x, y, x + width, y, color);
        line(x + width, y, x + width, y + height, color);
        line(x, y, x, y + height, color);
        line(x, y + height, x + width, y + height, color);
    }

    private static void line(double x1, double y1, double x2, double y2, IColor color) {
        mb.line(
                mb.float2(x1, y1).color(color.getR(), color.getG(), color.getB(), color.getA()).next(),
                mb.float2(x2, y2).color(color.getR(), color.getG(), color.getB(), color.getA()).next()
        );
    }
}
