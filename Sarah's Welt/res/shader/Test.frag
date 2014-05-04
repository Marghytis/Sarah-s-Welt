uniform sampler2D texSampler;

void main(void)
{
	gl_FragColor = vec4(gl_Color.rgb, gl_Color.a*(1 - texture2D(texSampler, gl_TexCoord[0]).a));
}