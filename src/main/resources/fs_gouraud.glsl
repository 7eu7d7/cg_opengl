#version 330

uniform sampler2D tex;

in vec4 color;
in vec2 vs_tex_coord;

out vec4 FragColor;

void main(){
    FragColor=color;
}