#version 330 core

out vec4 fragColor;

uniform sampler2D u_Texture;

in vec2 v_TexCoord;
in vec4 v_Color;

void main() {
    vec4 color = texture(u_Texture, v_TexCoord);
    fragColor = color * v_Color;
}
