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

vec3 computePointLight(PointLight pl,vec2 pos,vec3 icol)
{
    float distSq=dot(pos-pl.pos,pos-pl.pos);

    float factor=pl.max_intensity/(1+distSq*pl.falloff);
    float adj_factor=factor>0.01?factor:0;

    return mix(icol,pl.color,adj_factor)*adj_factor;
}

uniform PointLight ptLights[MAX_NUM_POINT_LIGHTS];
uniform int point_light_count;

uniform sampler2D biomeTex;

uniform int tileCount;

out vec4 color;
void main()
{
    vec4 base_col=texture(biomeTex,tC*tileCount);

    vec3 final_col=vec3(0,0,0);

    for(int i=0;i<point_light_count;i++)
    final_col+=computePointLight(ptLights[i],posF,base_col.rgb);

    color=vec4(final_col,base_col.a);
}