#version 400 core
#define MAX_NUM_POINT_LIGHTS 128

layout (location=0) out vec4 brightnessCol;
layout (location=1) out vec4 fragCol;

in vec2 tC;
in vec2 posF;

uniform sampler2D texSamp;
uniform vec4 solidColor;

struct PointLight
{
    vec2 pos;
    vec3 color;
    float max_intensity;//Always less than 1
    float falloff;
};
struct EnvironmentLight
{
    float intensity;
    vec3 color;
};

vec3 computePointLight(PointLight pl, vec2 pos)
{

    float distSq=dot(pos-pl.pos, pos-pl.pos);
    //float adj_factor=clamp(1-distance(pos,pl.pos)/10,0,1);

    float adj_factor=pl.max_intensity/(1+distSq*pl.falloff);

    return pl.color*adj_factor;
}

uniform PointLight ptLights[MAX_NUM_POINT_LIGHTS];
uniform int point_light_count;

uniform EnvironmentLight envLight;

out vec4 color;

void main()
{
    vec4 base_col;

    if (solidColor.a==0)
    base_col=texture(texSamp, tC);
    else
    base_col=solidColor;

    vec3 light_color=vec3(0, 0, 0);



    for (int i=0;i<point_light_count;i++)
    light_color+=computePointLight(ptLights[i], posF);

    light_color+=envLight.color*envLight.intensity;

    fragCol=vec4(light_color*base_col.rgb, base_col.a);

    float brightness = dot(fragCol.rgb, vec3(0.2126, 0.7152, 0.0722));
    //float brightness = (fragCol.r+fragCol.g+fragCol.b)/3;
    if (brightness > 1.2)
    brightnessCol = fragCol.rgba;
    else
    brightnessCol = vec4(0.0, 0.0, 0.0, fragCol.a);
}