package com.edplan.framework.media.bass;
import com.un4seen.bass.BASS;
import java.util.Map;
import java.util.TreeMap;
import android.util.Log;

public class BassException extends Exception
{
	public static final String[] errString=new String[]{
		"BASS_OK             : all is OK",
		"BASS_ERROR_MEM      : memory error",
		"BASS_ERROR_FILEOPEN : can't open the file",
		"BASS_ERROR_DRIVER   : can't find a free/valid driver",
		"BASS_ERROR_BUFLOST  : the sample buffer was lost",
		"BASS_ERROR_HANDLE   : invalid handle",
		"BASS_ERROR_FORMAT   : unsupported sample format",
		"BASS_ERROR_POSITION : invalid position",
		"BASS_ERROR_INIT     : BASS_Init has not been successfully called",
		"BASS_ERROR_START    : BASS_Start has not been successfully called",
		"BASS_ERROR_SSL      : SSL/HTTPS support isn't available",
		"BASS_ERROR_ALREADY  : already initialized/paused/whatever",
		"BASS_ERROR_NOCHAN   : can't get a free channel",
		"BASS_ERROR_ILLTYPE  : an illegal type was specified",
		"BASS_ERROR_ILLPARAM : an illegal parameter was specified",
		"BASS_ERROR_NO3D     : no 3D support",
		"BASS_ERROR_NOEAX    : no EAX support",
		"BASS_ERROR_DEVICE   : illegal device number",
		"BASS_ERROR_NOPLAY   : not playing",
		"BASS_ERROR_FREQ     : illegal sample rate",
		"BASS_ERROR_NOTFILE  : the stream is not a file stream",
		"BASS_ERROR_NOHW     : no hardware voices available",
		"BASS_ERROR_EMPTY    : the MOD music has no sequence data",
		"BASS_ERROR_NONET    : no internet connection could be opened",
		"BASS_ERROR_CREATE   : couldn't create the file",
		"BASS_ERROR_NOFX     : effects are not available",
		"BASS_ERROR_NOTAVAIL : requested data is not available",
		"BASS_ERROR_DECODE   : the channel is a \"decoding channel\"",
		"BASS_ERROR_DX       : a sufficient DirectX version is not installed",
		"BASS_ERROR_TIMEOUT  : connection timedout",
		"BASS_ERROR_FILEFORM : unsupported file format",
		"BASS_ERROR_SPEAKER  : unavailable speaker",
		"BASS_ERROR_VERSION  : invalid BASS version (used by add-ons)",
		"BASS_ERROR_CODEC    : codec is not available/supported",
		"BASS_ERROR_ENDED    : the channel/file has ended",
		"BASS_ERROR_BUSY     : the device is busy",
		"BASS_ERROR_UNKNOWN  : some other mystery problem",
		"BASS_ERROR_CLASS    : java object class problem",
		"JAVA_ERROR_NOINDEX  : index not found"
	};
	
	public static final int[] indexAry=new int[]{
		BASS.BASS_OK,
		BASS.BASS_ERROR_MEM,
		BASS.BASS_ERROR_FILEOPEN,
		BASS.BASS_ERROR_DRIVER,
		BASS.BASS_ERROR_BUFLOST,
		BASS.BASS_ERROR_HANDLE,
		BASS.BASS_ERROR_FORMAT,
		BASS.BASS_ERROR_POSITION,
		BASS.BASS_ERROR_INIT,
		BASS.BASS_ERROR_START,
		BASS.BASS_ERROR_SSL,
		BASS.BASS_ERROR_ALREADY,
		BASS.BASS_ERROR_NOCHAN,
		BASS.BASS_ERROR_ILLTYPE,
		BASS.BASS_ERROR_ILLPARAM,
		BASS.BASS_ERROR_NO3D,
		BASS.BASS_ERROR_NOEAX,
		BASS.BASS_ERROR_DEVICE,
		BASS.BASS_ERROR_NOPLAY,
		BASS.BASS_ERROR_FREQ,
		BASS.BASS_ERROR_NOTFILE,
		BASS.BASS_ERROR_NOHW,
		BASS.BASS_ERROR_EMPTY,
		BASS.BASS_ERROR_NONET,
		BASS.BASS_ERROR_CREATE,
		BASS.BASS_ERROR_NOFX,
		BASS.BASS_ERROR_NOTAVAIL,
		BASS.BASS_ERROR_DECODE,
		BASS.BASS_ERROR_DX,
		BASS.BASS_ERROR_TIMEOUT,
		BASS.BASS_ERROR_FILEFORM,
		BASS.BASS_ERROR_SPEAKER,
		BASS.BASS_ERROR_VERSION,
		BASS.BASS_ERROR_CODEC,
		BASS.BASS_ERROR_ENDED,
		BASS.BASS_ERROR_BUSY,
		BASS.BASS_ERROR_UNKNOWN,
		BASS.BASS_ERROR_JAVA_CLASS
	};
	
	public static final Map<Integer,Integer> indexMap=new TreeMap<Integer,Integer>();
	
	static{
		Log.v("excp Check",""+(-indexAry.length+errString.length));
		for(int i=0;i<indexAry.length;i++){
			indexMap.put(indexAry[i],i);
		}
	}
	
	
	
	public BassException(int errCode){
		this(errCode,"no message");
	}
	
	public BassException(int errCode,String message){
		super("<"+errcodeToString(errCode)+"> "+message);
	}
	
	public BassException(String message){
		this(BASS.BASS_ErrorGetCode(),message);
	}
	
	public static String errcodeToString(int errCode){
		int i=getErrIndex(errCode);
		if(i>=0&&i<errString.length)
			return errString[i];
		else
			return String.format("errCode[%s]IndexNotFound!",errCode);
	}
	
	private static int getErrIndex(int code){
		Integer c=indexMap.get(code);
		if(c==null){
			return errString.length-1;
		}else{
			return c;
		}
	}
	
	public static void check(){
		int c=getErrIndex(BASS.BASS_ErrorGetCode());
		if(c!=errString.length-1){
			//throw new BassException();
		}
	}
	
	
}
