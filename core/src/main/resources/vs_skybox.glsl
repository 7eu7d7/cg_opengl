#version 330

#define id_vPosition 0
#define id_vColor 1
#define id_vNormal 2
#define id_tex_coord 3
#define id_bones 4
#define id_weights 5
#define id_tex3d_coord 10

layout (location = id_vPosition) in vec4 vPosition;
layout (location = id_tex3d_coord) in vec3 tex3d_coord;

uniform mat4 mv;
uniform mat4 proj;

out vec3 vs_tex_coord;

void main(){
    /*vec4 pos=vec4(vPosition.xyz-0.5f, 1.0);
    //pos.xyz-=pos.xyz/2;
    //pos.xyz*=5;

    mat4 mv1 = mat4(mat3(mv));
    pos=proj*mv1*pos;

    gl_Position = pos.xyww;

    vs_tex_coord=vPosition.xyz;*/

    vs_tex_coord = tex3d_coord;
    mat4 mv1 = mat4(mat3(mv));

    vec4 pos=proj * mv1 * vec4(vPosition.xyz, 1.0);
    gl_Position = pos.xyww;

}