#version 400 core
#define MAX_NUM_POINT_LIGHTS 128

in vec2 tC;
in vec2 posF;

struct PointLight
{
    vec2 pos;
    vec3 color;
    float max_intensity; //Always less than 1
    float falloff;
};
struct EnvironmentLight
{
    float intensity;
    vec3 color;
};

vec3 computePointLight(PointLight pl,vec2 pos)
{
    float distSq=dot(pos-pl.pos,pos-pl.pos);

    float adj_factor=pl.max_intensity/(1+distSq*pl.falloff);

    return pl.color*adj_factor;
}

uniform PointLight ptLights[MAX_NUM_POINT_LIGHTS];
uniform int point_light_count;

uniform EnvironmentLight envLight;

uniform sampler2D biomeTex;

uniform int tileCount;

out vec4 color;
void main()
{
    vec4 base_col=texture(biomeTex,tC*tileCount);

    vec3 final_col=vec3(0,0,0);

    for(int i=0;i<point_light_count;i++)
    final_col+=computePointLight(ptLights[i],posF);

    //Compute environment light
    //final_col+=mix(base_col.rgb,envLight.color,envLight.intensity)*envLight.intensity;
    final_col+=envLight.color*envLight.intensity;

    final_col.r=min(final_col.r,200);
    final_col.g=min(final_col.g,200);
    final_col.b=min(final_col.b,200);

    color=vec4(final_col*base_col.rgb,base_col.a);
}