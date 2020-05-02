#version 330

#define id_vPosition 0
#define id_vColor 1
#define id_tex_coord 2

layout (location = id_vPosition) in vec4 vPosition;
layout (location = id_tex_coord) in vec2 tex_coord;

uniform mat4 mv;
uniform mat4 proj;

out vec2 vs_tex_coord;

void main(){
    gl_Position = proj * mv * vPosition;
    vs_tex_coord=tex_coord;
}