uniform vec2 location;
uniform vec4 color;
uniform float radius;

void main() {
	float distance = length(location - gl_FragCoord.xy);
	float attenuation = distance/radius;
	
	if(distance < radius){
		gl_FragColor = vec4(attenuation,attenuation,attenuation,attenuation)*color;
	} else {
		gl_FragColor = vec4(0, 0, 0, 0);
	}
}