#version 330

uniform samplerCube tex;
uniform int type;

in vec3 vs_tex_coord;

out vec4 FragColor;

void main(){
    FragColor=texture(tex, vs_tex_coord);
}