package org.meteordev.pts.utils;

public interface IColor {
    IColor WHITE = new ColorImpl(255, 255, 255, 255);
    IColor BLACK = new ColorImpl(0, 0, 0, 255);

    int getR();
    int getG();
    int getB();
    int getA();
}
