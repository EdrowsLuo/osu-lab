precision mediump float;

varying vec3 f_Position;
varying vec4 f_VaryingColor;
varying vec3 f_MaskPosition;

void main(){
	vec4 c=f_VaryingColor;
	@include <FragmentFinal>
}
