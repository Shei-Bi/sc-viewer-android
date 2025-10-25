precision mediump float;

uniform sampler2D TEX_SAMPLER;

varying vec2 texCoord;
varying vec4 colorMul;
varying vec3 colorAdd;

void main() {
    vec4 sample = texture2D(TEX_SAMPLER, texCoord);
    vec4 color = sample * colorMul;
    color.rgb += colorAdd * color.a;
    gl_FragColor = vec4(color.rgb * colorMul.a, color.a);
}
