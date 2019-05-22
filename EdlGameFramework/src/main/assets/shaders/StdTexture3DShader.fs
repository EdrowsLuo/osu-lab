precision mediump float;

varying vec3 f_Position;
varying vec2 f_TexturePosition;
varying vec4 f_VaryingColor;
varying vec3 f_MaskPosition;

uniform sampler2D u_Texture;

void main(){
	vec4 t=texture2D(u_Texture,f_TexturePosition);
	vec4 c=f_VaryingColor*t;
	@include <FragmentFinal>
}
