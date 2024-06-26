#version 330 core

out vec4 color;
in vec2 tc;
uniform sampler2D tex;

void main()
{
    // color = vec4(1.0, 0.0, 0.0, 1.0);
    color = texture(tex, tc);
    if (color.w < 1.0)
        discard;
}