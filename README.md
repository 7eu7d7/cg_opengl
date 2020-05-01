#计算机图形学实验代码及相关代码
----------
程序分为两部分，core为核心部分，实现对opengl的封装，提供一套较为直观的框架。<br>
src为主程序部分，所有实验具体实现代码都在这里。以core中的框架为基础实现。
<br>
#文件结构
<br>
```
project
│   build.gradle        //gradle脚本（自动配置环境）
│
└───core(module)        //绘图核心，实现对opengl的封装
│   │
│   └───java            //核心部分程序
│   │   │   geom            //基本几何体，实现对opengl绘图部分的封装
│   │   │   utils           //工具
│   │   │   view            //GUI组件实现
│   │
│   └───resources       //shader及资源文件
│
└───src(module)         //主程序模块及GUI
     │
     └───java            //绘制主程序
          │   Main            //主类
          │   ViewTest1       //实验1
```