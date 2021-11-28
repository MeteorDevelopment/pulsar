#version 330 core

out vec4 fragColor;

in vec2 v_LocalPos;
flat in vec2 v_Size;
flat in float v_Pixel;
flat in vec4 v_Radius;
flat in uint v_Background;
in vec4 v_BackgroundColor;
in vec4 v_OutlineColor;
flat in float v_OutlineSize;

float sdRoundedBox(vec2 p, vec2 b, vec4 r) {
    r.xy = (p.x > 0.0) ? r.xy : r.zw;
    r.x  = (p.y > 0.0) ? r.x : r.y;
    vec2 q = abs(p) - b + r.x;
    return min(max(q.x, q.y),0.0) + length(max(q, 0.0)) - r.x;
}

float opOnion(float s, float r) {
    return abs(s) - r;
}

void main() {
    float distance = sdRoundedBox(v_LocalPos, v_Size, v_Radius * v_Pixel);

    // Background
    if (v_Background != uint(0)) {
        float a = smoothstep(v_Pixel, -v_Pixel, distance);

        fragColor = vec4(v_BackgroundColor.rgb, v_BackgroundColor.a * a);
    }

    // Outline
    if (v_OutlineSize != 0.0) {
        float distance2 = opOnion(distance, v_OutlineSize * v_Pixel);
        distance = max(distance, distance2);

        float a = smoothstep(v_Pixel, -v_Pixel, distance);

        if (v_Background == uint(0)) fragColor = vec4(v_OutlineColor.rgb, a);
        else fragColor.rgb = mix(fragColor.rgb, v_OutlineColor.rgb, a);
    }
}
