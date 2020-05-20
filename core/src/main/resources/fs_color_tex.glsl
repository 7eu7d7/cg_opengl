#version 330

uniform sampler2D tex;
uniform int type;

in vec4 color;
in vec2 vs_tex_coord;

out vec4 FragColor;

void main(){
    FragColor = color*texture(tex, vs_tex_coord);
    /*switch(type){
        case 0:{ //普通纹理叠加颜色
            FragColor=FragColor;
            break;
        }
        case 1:{ //去色
            float N=0;
            mat4 colmat = mat4(0.3086*(1-N) + N, 0.6094*(1-N)    , 0.0820*(1-N)    , 0,
                                0.3086*(1-N)   , 0.6094*(1-N) + N, 0.0820*(1-N)    , 0,
                                0.3086*(1-N)   , 0.6094*(1-N)    , 0.0820*(1-N) + N, 0,
                                0        , 0        , 0        , 1);
            FragColor=FragColor*colmat;
            break;
        }
        case 2:{ //调整饱和度
            float N=0.5;
            mat4 colmat = mat4(0.3086*(1-N) + N, 0.6094*(1-N)    , 0.0820*(1-N)    , 0,
            0.3086*(1-N)   , 0.6094*(1-N) + N, 0.0820*(1-N)    , 0,
            0.3086*(1-N)   , 0.6094*(1-N)    , 0.0820*(1-N) + N, 0,
            0        , 0        , 0        , 1);
            FragColor=FragColor*colmat;
            break;
        }
        case 3:{ //反色
            FragColor=1-FragColor;
            break;
        }
        case 4:{ //老照片效果
            float N=0.5;
            mat4 colmat = mat4(0.393,	0.769,	0.189,	0,
                                0.349,	0.686,	0.168,	0,
                                0.272,	0.534,	0.131,	0,
                                0     , 0     , 0     , 1);
            FragColor=FragColor*colmat;
            break;
        }
    }*/
}