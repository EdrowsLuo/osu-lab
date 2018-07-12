package com.edplan.nso.parser.partParsers;
import com.edplan.nso.OsuFilePart;
import com.edplan.nso.filepart.PartEvents;
import com.edplan.superutils.U;

public class EventsParser extends PartParser<PartEvents>
{
	private PartEvents events;
	
	public EventsParser(){
		events=new PartEvents();
	}
	
	@Override
	public boolean parse(String l){

		return true;
	}

	@Override
	public PartEvents getPart(){

		return events;
	}

}
