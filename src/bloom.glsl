#version 400 core

in vec2 tC;

uniform sampler2D blurTexture;
uniform sampler2D colorTexture;

out vec4 color;
float exposure=0.5;
void main()
{
    color=vec4(texture(blurTexture,tC).rgb+texture(colorTexture,tC).rgb,1);


    vec3 result = vec3(1.0) - exp(-color.rgb * exposure);

    color=vec4(result,1);
}