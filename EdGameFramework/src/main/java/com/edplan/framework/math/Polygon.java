package com.edplan.framework.math;

public class Polygon
{
	/**
	 *ray-crossing算法
	 *通过射线法判断v是否在list所组成的多边形内，
	 *支持凹多边形
	 *这里用的是射向x轴正半轴的射线
	 */
	public static boolean inPolygon(Vec2 v,Vec2... list){
		if(list.length<3){
			return false;
		}
		int crossCount=0;
		Vec2 first=list[list.length-1];
		Vec2 second;
		int i=0;
		do{
			second=list[i];
			if(ifCross(v,first,second)){
				crossCount++;
			}
			i++;
		}while(i!=list.length-1);
		return crossCount%2==1;
	}
	
	private static boolean ifCross(Vec2 p,Vec2 l1,Vec2 l2){
		if(l1.y==l2.y){
			return false;
		}else{
			float crossPointX=(l2.x-l1.x)*(p.y-l2.y)/(l2.y-l1.y)+l1.x;
			if(crossPointX<=p.x){
				return false;
			}else{
				if(l1.x>l2.x){
					return FMath.inInterval(l2.x,l1.x,crossPointX);
				}else{
					return FMath.inInterval(l1.x,l2.x,crossPointX);
				}
			}
		}
	}
}
