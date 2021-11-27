#version 330 core

layout (location = 0) in vec4 pos;
layout (location = 1) in vec2 localPos;
layout (location = 2) in vec2 size;
layout (location = 3) in vec4 radius;
layout (location = 4) in uint background;
layout (location = 5) in vec4 backgroundColor;
layout (location = 6) in vec4 outlineColor;
layout (location = 7) in float outlineSize;

uniform mat4 u_Proj;

out vec2 v_LocalPos;
flat out vec2 v_Size;
flat out float v_Pixel;
flat out vec4 v_Radius;
flat out uint v_Background;
out vec4 v_BackgroundColor;
out vec4 v_OutlineColor;
flat out float v_OutlineSize;

void main() {
    gl_Position = u_Proj * pos;

    float bigger = max(size.x, size.y);

    v_LocalPos = localPos - (size - bigger) / bigger;
    v_Size = size / bigger;
    v_Pixel = 1.0 / (bigger);
    v_Radius = radius * 2.0;
    v_Background = background;
    v_BackgroundColor = backgroundColor;
    v_OutlineColor = outlineColor;
    v_OutlineSize = outlineSize;
}
