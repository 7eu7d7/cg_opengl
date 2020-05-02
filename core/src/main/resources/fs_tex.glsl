#version 330

uniform sampler2D tex;

in vec2 vs_tex_coord;

out vec4 FragColor;

void main(){
    FragColor = texture2D(tex, vs_tex_coord).rgba;
}