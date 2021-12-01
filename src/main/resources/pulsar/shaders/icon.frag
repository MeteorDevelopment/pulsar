#version 330 core

out vec4 fragColor;

uniform sampler2D u_Texture;

in vec2 v_TexCoord;
in vec4 v_Color;

void main() {
    fragColor = vec4(v_Color.rgb, v_Color.a * texture(u_Texture, v_TexCoord).a);
}
