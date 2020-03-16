#version 400 core

in vec2 tC;

out vec4 color;
uniform sampler2D texSamp;
uniform vec3 textColor;

void main()
{

    vec4 sampled = vec4(1.0, 1.0, 1.0, texture(texSamp, vec2(tC.x,-tC.y)).r);
    color = vec4(textColor, 1.0) * sampled;
    /*if(brightness==0)
        discard;*/
}