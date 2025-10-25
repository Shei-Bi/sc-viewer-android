precision mediump float;

attribute vec2 aPos;
attribute vec2 aTexCoord;

uniform mat4 pmv;

varying vec2 texCoord;

void main() {
    gl_Position = vec4(aPos, 0.0, 1.0);
    texCoord = aTexCoord;
}
