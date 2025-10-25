precision mediump float;

uniform sampler2D TEX_SAMPLER;
varying vec2 texCoord;

void main() {
    gl_FragColor = texture2D(TEX_SAMPLER, texCoord);
}
