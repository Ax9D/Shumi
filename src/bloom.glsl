#version 400 core

in vec2 tC;

uniform sampler2D blurTexture;
uniform sampler2D colorTexture;

out vec4 color;
float exposure=0.5;
float gamma=2.2;
float bloomIntensity=1;
void main()
{
    vec3 result=texture(colorTexture, tC).rgb+texture(blurTexture, tC).rgb*bloomIntensity;

    result=result/(result+vec3(1));
    //result = vec3(1.0) - exp(-result * exposure);
    //result = pow(result, vec3(1.0 / gamma));

    color=vec4(result, 1);
}