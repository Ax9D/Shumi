#version 400 core

uniform sampler2D texSamp;
uniform vec4 solidColor;

in vec2 tC;


out vec4 color;
void main()
{
    if (solidColor.a==0)
    color=vec4(texture(texSamp, tC).rgb, 1);
    else
    color=solidColor;
}