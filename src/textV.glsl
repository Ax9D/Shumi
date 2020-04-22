#version 400 core

layout (location=0) in vec2 pos;
layout (location=1) in vec2 tCoords;

uniform mat4 tmat;
uniform mat4 ratio_mat;

out vec2 tC;
void main()
{
    gl_Position=ratio_mat*tmat*vec4(pos, 0, 1.0);
    tC=tCoords;
}