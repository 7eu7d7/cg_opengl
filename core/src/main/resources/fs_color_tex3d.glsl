#version 330

uniform sampler3D tex3d;
uniform sampler2D tex;
uniform int type;

in vec4 color;
in vec3 normal;
in vec4 position;
in vec2 vs_tex_coord;
in vec3 vs_tex3d_coord;

out vec4 FragColor;

//---------Light---------
struct LightProperties {
    bool isEnabled;
    bool isLocal;
    bool isSpot;
    vec3 ambient;
    vec3 color;
    vec3 position;
    vec3 halfVector;
    vec3 coneDirection;
    float spotCosCutoff;
    float spotExponent;
    float constantAttenuation;
    float linearAttenuation;
    float quadraticAttenuation;
};
// the set of lights to apply, per invocation of this shader
const int MaxLights = 10;
uniform LightProperties Lights[MaxLights];
uniform float Shininess;
uniform float Strength;
uniform vec3 EyeDirection;
//---------Light---------

//-------Material--------
struct MaterialProperties {
    vec3 emission; // light produced by the material
    vec3 ambient; // what part of ambient light is reflected
    vec3 diffuse; // what part of diffuse light is scattered
    vec3 specular; // what part of specular light is scattered
    float shininess; // exponent for sharpening specular reflection
// other properties you may desire
};
uniform MaterialProperties material;
//-------Material--------

void main(){
    switch(type){
        case 0:{ //普通纹理叠加颜色
            FragColor=color*texture(tex, vs_tex_coord);
            break;
        }
        case 1:{ //光照+材质
            vec3 scatteredLight = vec3(0.0); // or, to a global ambient light
            vec3 reflectedLight = vec3(0.0);
            // loop over all the lights
            for (int light = 0; light < MaxLights; ++light) {
                if (! Lights[light].isEnabled)
                    continue;
                vec3 halfVector;
                vec3 lightDirection = Lights[light].position;
                float attenuation = 1.0;
                // for local lights, compute per-fragment direction,
                // halfVector, and attenuation
                if (Lights[light].isLocal) {
                    lightDirection = lightDirection - vec3(position);
                    float lightDistance = length(lightDirection);
                    lightDirection = lightDirection / lightDistance;
                    attenuation = 1.0 / (Lights[light].constantAttenuation+ Lights[light].linearAttenuation * lightDistance
                                + Lights[light].quadraticAttenuation * lightDistance* lightDistance);
                    if (Lights[light].isSpot) {
                        float spotCos = dot(lightDirection, -Lights[light].coneDirection);
                        if (spotCos < Lights[light].spotCosCutoff)
                            attenuation = 0.0;
                        else
                            attenuation *= pow(spotCos, Lights[light].spotExponent);
                    }
                    halfVector = normalize(lightDirection + EyeDirection);
                } else {
                    halfVector = Lights[light].halfVector;
                }
                float diffuse = max(0.0, dot(normal, lightDirection));
                float specular = max(0.0, dot(normal, halfVector));
                //float diffuse =1;
                //float specular =1;
                if (diffuse == 0.0)
                    specular = 0.0;
                else
                    specular = pow(specular, Shininess) * Strength;
                // Accumulate all the lights’ effects
                scatteredLight += Lights[light].ambient * material.ambient * attenuation +
                                Lights[light].color * material.diffuse * diffuse * attenuation;
                reflectedLight += Lights[light].color * material.specular * specular * attenuation;
            }
            vec3 rgb = min(material.emission+color.rgb * scatteredLight + reflectedLight, vec3(1.0));
            FragColor = vec4(rgb, color.a)*texture(tex, vs_tex_coord);
            break;
        }
        case 2:{
            FragColor=color*texture(tex3d, vs_tex3d_coord);
            break;
        }
    }
}