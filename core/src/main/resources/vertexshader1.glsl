#version 330

layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec4 vColor;

uniform mat4 transform;
uniform mat4 mv;
uniform mat4 proj;

out vec4 color;

void main(){
    gl_Position = proj * mv * vPosition;
    color = vColor;
}