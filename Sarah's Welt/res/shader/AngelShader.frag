uniform vec2 lightLocation;
uniform vec3 lightColor;

void main() {
	float distance = length(lightLocation - gl_FragCoord.xy);
	float attenuation = 1.0 / distance;
	
	gl_FragColor = vec4(attenuation,attenuation,attenuation,1.0)*vec4(lightColor, 1.0);
}