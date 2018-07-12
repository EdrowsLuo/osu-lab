precision mediump float;

uniform vec2 org;
uniform float radius;

varying vec3 f_Position;
varying vec4 f_VaryingColor;
varying vec3 f_MaskPosition;

uniform float u_FinalAlpha;
uniform vec4 u_MixColor;

float shapeColor(){
	float r=min(2.0,length(org-f_Position)-radius);
	return smoothstep(1.0,0.0,r);
}

void main(){
	vec4 c=u_MixColor*f_VaryingColor;
	c*=u_FinalAlpha*shapeColor;
	if(c.a<0.001)discard;
	gl_FragColor=c;
}
