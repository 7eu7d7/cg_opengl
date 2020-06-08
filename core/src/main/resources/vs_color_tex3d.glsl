#version 330

#define id_vPosition 0
#define id_vColor 1
#define id_vNormal 2
#define id_tex_coord 3
#define id_bones 4
#define id_weights 5
#define id_tex3d_coord 10

layout (location = id_vPosition) in vec4 vPosition;
layout (location = id_vNormal) in vec3 vNormal;
layout (location = id_vColor) in vec4 vColor;
layout (location = id_tex_coord) in vec2 tex_coord;
layout (location = id_tex3d_coord) in vec3 tex3d_coord;

uniform mat4 mv;
uniform mat4 proj;

out vec4 color;
out vec3 normal;
out vec4 position;
out vec2 vs_tex_coord;
out vec3 vs_tex3d_coord;

void main(){
    gl_Position = proj * mv * vPosition;
    position = mv * vPosition;
    color = vColor;

    mat4 matNoral =  transpose(inverse(mv));
    normal = normalize((matNoral * vec4(vNormal, 1.0)).xyz);
    //normal = normalize(vNormal);
    //normal = normalize((mv * vec4(vNormal, 0.0)).xyz);
    vs_tex_coord=tex_coord;
    vs_tex3d_coord=tex3d_coord;
}