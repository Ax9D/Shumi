#version 400 core
layout(location=0) in vec2 pos;
layout(location=1) in vec2 tCoords;

uniform mat4 tmat;
uniform mat4 cmat;
uniform mat4 ratio_mat;

out vec2 tC;
out vec2 posF;

void main()
{
    vec4 projected_pos=ratio_mat*cmat*tmat*vec4(pos, 0, 1.0);
    gl_Position=projected_pos;

    tC=tCoords;
    posF=(tmat*vec4(pos, 0, 1.0)).xy;
}