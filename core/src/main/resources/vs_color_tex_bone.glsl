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

layout (location = id_bones) in vec4 BoneIDs;
layout (location = id_weights) in vec4 Weights;

uniform mat4 mv;
uniform mat4 proj;
uniform mat4 gBones[100];

out vec4 color;
out vec3 normal;
out vec4 position;
out vec2 vs_tex_coord;

void main(){
    //mat4 BoneTransform = gBones[int(BoneIDs[0])];
    mat4 BoneTransform = gBones[int(BoneIDs[0])] * Weights[0];
    BoneTransform += gBones[int(BoneIDs[1])] * Weights[1];
    BoneTransform += gBones[int(BoneIDs[2])] * Weights[2];
    BoneTransform += gBones[int(BoneIDs[3])] * Weights[3];

    gl_Position = proj * mv * (BoneTransform * vPosition); //加上骨骼变换
    position = mv * vPosition;
    color = vColor;

    mat4 matNoral =  transpose(inverse(mv));
    normal = normalize((matNoral * vec4(vNormal, 1.0)).xyz);
    //normal = normalize(vNormal);
    //normal = normalize((mv * vec4(vNormal, 0.0)).xyz);
    vs_tex_coord=tex_coord;
}