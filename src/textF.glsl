#version 400 core

in vec2 tC;

out vec4 color;
uniform sampler2D texSamp;
uniform vec4 textColor;

void main()
{

    float sampled = texture(texSamp, vec2(tC.x, -tC.y)).r;
    color = vec4(textColor.rgb,textColor.a*sampled);
    /*if(brightness==0)
        discard;*/
}