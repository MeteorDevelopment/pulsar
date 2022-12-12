package org.meteordev.pulsar.theme;

import java.util.List;

public interface IStylable {
    /** @return all names of this object. */
    String[] names();

    /** @return all tags of this object. */
    List<String> tags();

    /** @return true if this object is hovered. */
    boolean isHovered();

    /** @return true if this object is pressed. */
    boolean isPressed();
}
