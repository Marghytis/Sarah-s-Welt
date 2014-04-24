uniform sampler2D texSampler;
uniform vec4 particleColor;

void main(void)
{
	gl_FragColor = particleColor*texture2D(texSampler, vec2(gl_TexCoord[0])).r;
}