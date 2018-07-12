precision mediump float;

varying vec3 f_Position;
varying vec2 f_TexturePosition;
varying vec4 f_VaryingColor;
varying vec3 f_MaskPosition;
varying vec2 f_RectPosition;

uniform float u_FinalAlpha;
uniform vec4 u_MixColor;
uniform sampler2D u_Texture;
uniform vec4 u_DrawingRect;
uniform vec4 u_InnerRect;
uniform vec4 u_Padding;

bool outOfRect(vec2 p,vec4 r){
	return p.x<r.x||p.x>r.z||p.y<r.y||p.y>r.w;
}

void main(){
	if(outOfRect(f_RectPosition,u_InnerRect)){
		gl_FragColor=vec4(0.0,0.0,0.0,0.0);
		return;
	}
	vec4 t=texture2D(u_Texture,f_TexturePosition);
	vec4 c=u_MixColor*t;
	

	
	c*=u_FinalAlpha;
	gl_FragColor=c;
}
