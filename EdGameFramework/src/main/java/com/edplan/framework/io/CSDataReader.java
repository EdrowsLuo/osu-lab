package com.edplan.framework.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CSDataReader  
{
	private InputStream in;

	public CSDataReader(InputStream in){
		this.in=in;
	}

	public int readInt() throws IOException{
		return toInt(in.read(),in.read(),in.read(),in.read());
	}

	public byte readByte() throws IOException{
		return int2byte(in.read());
	}

	public int readUByte() throws IOException{
		return in.read();
	}

	public short readShort() throws IOException{
		return toShort(in.read(),in.read());
	}

	public long readLong() throws IOException{
		long l=0;
		for(int i=0;i<8;i++){
			l|=(((long)in.read())<<(i*8));
		}
		return l;
	}

	public static int toInt(int i1,int i2,int i3,int i4){
		return i1|(i2<<8)|(i3<<16)|(i4<<24);
	}

	public static short toShort(int i1,int i2){
		return (short)(i1|(i2<<8));
	}

	public byte[] readUnsignedLeb128() throws IOException {
        List<Byte> byteAryList = new ArrayList<Byte>();
        byte bytes =int2byte(in.read());
        byte highBit = (byte) (bytes & 0x80);
        byteAryList.add(bytes);
        while (highBit != 0) {
            bytes = int2byte(in.read());
            highBit = (byte) (bytes & 0x80);
            byteAryList.add(bytes);
        }
        byte[] byteAry = new byte[byteAryList.size()];
        for (int j = 0; j < byteAryList.size(); j++) {
            byteAry[j] = byteAryList.get(j);
        }
        return byteAry;
    }

	public int readULEB128Int() throws IOException{
		byte[] list=readUnsignedLeb128();
		return uleb1282int(list);
	}

	public String readString() throws IOException{
		int first=in.read();
		if(first==0x00){
			//System.out.println("0x00");
			return "";
		}else{
			int l=readULEB128Int();
			//System.out.println("length="+l);
			byte[] b=new byte[l];
			in.read(b);
			return new String(b,"UTF-8");
		}
	}

	public static int uleb1282int(byte... list){
		int n=0;
		for(int i=list.length-1;i>=0;i--){
			n=n<<7;
			n|=(list[i]&0x7f);
		}
		return n;
	}

	public static byte int2byte(int i){
		return (byte)(i&0xff);
	}

	public static int ubyte2int(byte b){
		return 0xff&b;
	}
}
