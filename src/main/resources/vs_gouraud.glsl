#version 330

#define id_vPosition 0
#define id_vColor 1
#define id_vNormal 2
#define id_tex_coord 3
#define id_bones 4
#define id_weights 5

layout (location = id_vPosition) in vec4 vPosition;
layout (location = id_vNormal) in vec3 vNormal;
layout (location = id_vColor) in vec4 vColor;
layout (location = id_tex_coord) in vec2 tex_coord;


struct PositionalLight
{
    vec3 ambient;
    vec3 position;
};
struct Material
{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};
uniform vec3 globalAmbient;
uniform PositionalLight light;
uniform Material material;

uniform mat4 mv;
uniform mat4 proj;

out vec4 color;
out vec2 vs_tex_coord;

void main(){
    gl_Position = proj * mv * vPosition;
    color = vColor;
    vs_tex_coord=tex_coord;

    //Light
    mat4 matNoral =  transpose(inverse(mv));

    vec4 P = mv * vec4(vPosition.xyz,1.0);
    vec3 N = normalize((matNoral * vec4(vNormal,1.0)).xyz);
    vec3 L = normalize(light.position - P.xyz);
    vec3 V = normalize(-P.xyz);
    vec3 R = reflect(-L,N);

    vec3 ambient = (globalAmbient * material.ambient) + (light.ambient * material.ambient);
    vec3 diffuse = light.ambient * material.diffuse * max(dot(N,L), 0.0);
    vec3 specular = material.specular * light.ambient * pow(max(dot(R,V), 0.0f), material.shininess);
    color = vColor*vec4((ambient + diffuse + specular), 1.0);
}