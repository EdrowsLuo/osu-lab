package com.edplan.nso.parser.partParsers;
import com.edplan.nso.filepart.PartMetadata;
import com.edplan.superutils.U;
import com.edplan.nso.beatmapComponent.Tags;
import com.edplan.nso.OsuFilePart;

public class MetadataParser extends PartParser<PartMetadata>
{
	private PartMetadata part;
	
	public MetadataParser(){
		part=new PartMetadata();
	}

	@Override
	public PartMetadata getPart(){

		return part;
	}

	@Override
	public boolean parse(String l){

		if(l==null||l.trim().length()==0){
			return true;
		}else{
			String[] entry=U.divide(l,l.indexOf(":"));
			String v=entry[1];
			switch(entry[0]){
				case PartMetadata.Artist:
					part.setArtist(v);
					return true;
				case PartMetadata.ArtistUnicode:
					part.setArtistUnicode(v);
					return true;
				case PartMetadata.BeatmapID:
					part.setBeatmapID(U.toInt(v));
					return true;
				case PartMetadata.BeatmapSetID:
					part.setBeatmapSetID(U.toInt(v));
					return true;
				case PartMetadata.Creator:
					part.setCreator(v);
					return true;
				case PartMetadata.Source:
					part.setSource(v);
					return true;
				case PartMetadata.Tags:
					part.setTags(Tags.parse(l));
					return true;
				case PartMetadata.Title:
					part.setTitle(v);
					return true;
				case PartMetadata.TitleUnicode:
					part.setTitleUnicode(v);
					return true;
				case PartMetadata.Version:
					part.setVersion(v);
					return true;
				default:
					//key not find
					return false;
			}
		}
	}
}
