#version 330

#define id_vPosition 0
#define id_vColor 1
#define id_tex_coord 2

layout (location = id_vPosition) in vec4 vPosition;

uniform mat4 mv;
uniform mat4 proj;

void main(){
    gl_Position = proj * mv * vPosition;
}