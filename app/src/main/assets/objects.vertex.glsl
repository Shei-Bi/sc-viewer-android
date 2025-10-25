precision mediump float;

attribute vec2 aPos;
attribute vec2 aTexCoord;
attribute vec4 aColorMul;
attribute vec3 aColorAdd;

uniform mat4 pmv;

varying vec2 texCoord;
varying vec4 colorMul;
varying vec3 colorAdd;

void main() {
    gl_Position = pmv * vec4(aPos, 0.0, 1.0);
    texCoord = aTexCoord;
    colorMul = aColorMul;
    colorAdd = aColorAdd;
}
