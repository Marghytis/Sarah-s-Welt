uniform vec2 lightLocation;
uniform float lightStrength;

void main() {
	float distance = length(lightLocation - gl_FragCoord.xy);
	float attenuation = 1 - (lightStrength*distance);

	gl_FragColor = vec4(attenuation, attenuation, attenuation, 1);
}