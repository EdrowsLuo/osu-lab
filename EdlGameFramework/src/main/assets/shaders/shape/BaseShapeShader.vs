uniform mat4 u_MVPMatrix;
uniform mat4 u_MaskMatrix;

attribute vec3 a_Position;
attribute vec4 a_VaryingColor;

varying vec3 f_Position;
varying vec4 f_VaryingColor;
varying vec3 f_MaskPosition;

void main(){
	f_Position=a_Position;
	f_MaskPosition=(u_MaskMatrix*vec4(a_Position,1.0)).xyz;
	f_VaryingColor=a_VaryingColor;
	gl_Position=u_MVPMatrix*vec4(a_Position,1.0);
}
