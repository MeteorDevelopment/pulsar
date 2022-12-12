package org.meteordev.pulsar.theme;

import java.util.*;

public class Styles {
    private final Map<String, Container> names = new HashMap<>(); // A map that contains all styles that contain the name
    private final Map<String, List<Style>> tags = new HashMap<>(); // A map that contains all styles that contain the tag AND do not have a name

    private final Set<Style> applied = new HashSet<>();
    private final List<Style> tagStateStyles = new ArrayList<>();
    private final List<Style> nameTagStateStyles = new ArrayList<>();

    public void add(Style style) {
        if (style.name != null) {
            Container container = names.computeIfAbsent(style.name, s -> new Container());

            if (style.tags == null) {
                container.set(style);
            }
            else {
                for (String tag : style.tags) {
                    List<Style> tagStyles = container.tags.computeIfAbsent(tag, s -> new ArrayList<>());
                    tagStyles.add(style);
                }
            }
        }
        else {
            for (String tag : style.tags) {
                List<Style> tagStyles = tags.computeIfAbsent(tag, s -> new ArrayList<>());
                tagStyles.add(style);
            }
        }
    }

    public Style compute(IStylable stylable) {
        Style style = new Style();

        // Apply name styles
        Container nameContainer = null;
        for (String name : stylable.names()) {
            Container container = names.get(name);
            if (container == null) continue;

            merge(style, container.normal);
            nameContainer = container;
        }

        // Apply tag styles
        applyTagStyles(style, stylable, tags, tagStateStyles);

        // Apply name tag styles
        if (nameContainer != null) {
            applyTagStyles(style, stylable, nameContainer.tags, nameTagStateStyles);
        }

        // Apply name state styles
        if (nameContainer != null) {
            if (stylable.isHovered()) merge(style, nameContainer.hovered);
            if (stylable.isPressed()) merge(style, nameContainer.pressed);
        }

        // Apply tag state styles
        if (stylable.isHovered()) applyStateStyles(style, tagStateStyles, Style.State.Hovered);
        if (stylable.isPressed()) applyStateStyles(style, tagStateStyles, Style.State.Pressed);

        // Apply name tag state styles
        if (stylable.isHovered()) applyStateStyles(style, nameTagStateStyles, Style.State.Hovered);
        if (stylable.isPressed()) applyStateStyles(style, nameTagStateStyles, Style.State.Pressed);

        // Clear temporary data and return style
        applied.clear();
        tagStateStyles.clear();
        nameTagStateStyles.clear();

        return style;
    }

    private void applyStateStyles(Style style, List<Style> styles, Style.State state) {
        for (Style stateStyle : styles) {
            if (stateStyle.state == state) merge(style, stateStyle);
        }
    }

    private void applyTagStyles(Style style, IStylable stylable, Map<String, List<Style>> tags, List<Style> stateStyles) {
        for (String tag : stylable.tags()) {
            List<Style> tagStyles = tags.get(tag);
            if (tagStyles == null) continue;

            for (Style tagStyle : tagStyles) {
                if (!stylable.tags().containsAll(tagStyle.tags)) continue;

                if (tagStyle.state == Style.State.Normal) style.merge(tagStyle);
                else stateStyles.add(tagStyle);
            }
        }
    }

    private void merge(Style target, Style style) {
        if (style == null) return;

        if (!applied.contains(style)) {
            target.merge(style);
            applied.add(style);
        }
    }

    private static class Container {
        public Style normal, hovered, pressed;
        public final Map<String, List<Style>> tags = new HashMap<>();

        public void set(Style style) {
            switch (style.state) {
                case Normal -> normal = style;
                case Hovered -> hovered = style;
                case Pressed -> pressed = style;
            }
        }
    }
}
