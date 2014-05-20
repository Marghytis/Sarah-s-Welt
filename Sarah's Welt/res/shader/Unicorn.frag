uniform sampler2D texSampler;
uniform vec3 color;

void main(void)
{
	gl_FragColor = vec4(gl_Color.rgb, gl_Color.a*(1 - texture2D(texSampler, gl_TexCoord[0]).a));
	vec4 texture = texture2D(texSampler, gl_TexCoord[0]);
	if(texture.r == 0 && texture.b == 0 && texture.g > 0){
		gl_FragColor = vec4(color.rgb*texture2D(texSampler, gl_TexCoord[0]).g, gl_Color.a);
	} else {
		gl_FragColor = gl_Color*texture2D(texSampler, gl_TexCoord[0]);
	}
}