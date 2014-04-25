uniform sampler2D texSampler;

void main(void)
{
	gl_FragColor = gl_Color*(1 - texture2D(texSampler, gl_TexCoord[0]).a);
}