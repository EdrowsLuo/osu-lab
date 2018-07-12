precision highp float;

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
uniform vec4 u_RoundedInner;
uniform float u_Ridaus;
uniform float u_GlowFactor;
uniform vec4 u_GlowColor;

bool outOfRect(vec2 p,vec4 r){
	return p.x<r.x||p.x>r.z||p.y<r.y||p.y>r.w;
}

float distanceFromRoundedRect(){
	vec2 tlo=u_RoundedInner.xy-f_RectPosition;
	vec2 bro=f_RectPosition-u_RoundedInner.zw;
	
	vec2 dis=max(bro,tlo);
	
	float mDis=max(dis.x,dis.y);
	
	if(mDis<=0.0){
		return mDis-u_Ridaus;
	}else{
		return length(max(vec2(0.0),dis))-u_Ridaus;
	}
}

void main(){
	float dis=distanceFromRoundedRect();
	float u_GlowWidth=10.0;
	float r_alpha=dis>0.0?max(pow(u_GlowFactor,dis)*(1.0-dis/u_GlowWidth),0.0):1.0;
	vec4 t=texture2D(u_Texture,f_TexturePosition);
	float edgeFactor=1.0-pow(0.2,dis);
	
	vec4 c=u_MixColor*t;
	
	if(dis>0.0){
		c=mix(c,u_GlowColor,u_GlowColor.a*edgeFactor);
	}
	
	
	float w=2.0; 
	if(dis<w&&dis>-w){
		c.a=mix(c.a,1.0,((w-dis)/w/2.0));
	}
	
	c*=r_alpha*u_FinalAlpha;
	if(c.a<0.001)discard;
	gl_FragColor=c;
}
