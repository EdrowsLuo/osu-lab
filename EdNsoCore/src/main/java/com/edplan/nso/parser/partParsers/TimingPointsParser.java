package com.edplan.nso.parser.partParsers;
import com.edplan.nso.OsuFilePart;
import com.edplan.nso.filepart.PartTimingPoints;
import com.edplan.nso.timing.TimingPoint;
import com.edplan.superutils.classes.strings.StringSpliter;
import com.edplan.superutils.U;
import com.edplan.nso.beatmapComponent.SampleSet;

public class TimingPointsParser extends PartParser<PartTimingPoints>
{
	private PartTimingPoints part;

	public TimingPointsParser(){
		part=new PartTimingPoints();
	}
	
	public TimingPoint parseTimingPoint(String l){
		TimingPoint t=new TimingPoint();
		StringSpliter sp=new StringSpliter(l,",");
		t.setTime((int)U.toDouble(sp.next()));
		t.setBeatLength(U.toDouble(sp.next()));
		t.setMeter(U.toInt(sp.next()));
		t.setSampleType(U.toInt(sp.next()));
		t.setSampleSet(SampleSet.parse(sp.next()));
		t.setVolume(U.toInt(sp.next()));
		t.setInherited(U.toBool(sp.next()));
		int eff=U.toInt(sp.next());
		t.setKiaiMode((eff&1)>0);
		t.setOmitFirstBarSignature((eff&8)>0);
		return t;
	}
	
	@Override
	public PartTimingPoints getPart(){

		return part;
	}

	@Override
	public boolean parse(String l){

		l=l.trim();
		if(l==null||l.length()==0){
			return true;
		}else{
			part.addTimingPoint(parseTimingPoint(l));
			return true;
		}
	}
}
