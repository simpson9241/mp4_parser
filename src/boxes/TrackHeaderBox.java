package boxes;

public class TrackHeaderBox extends FullBox{
	public String creation_time; //64 32
	public String modification_time; //64 32
	public long track_ID;
	public long duration; //64 32
	public long layer;
	public long alternate_group;
	public double volume=1.0; //16
	public long[] matrix= {0x00010000,0,0,0,0x00010000,0,0,0,0x40000000}; //32
	public double track_width;
	public double track_height;
	public long reserved=0; //16
//	public long reserved[2]=0; //32
//	public long[] pre_defined; //32
	
	
	public TrackHeaderBox(String boxtype, long v, long f) { //tkhd version flags
		super(boxtype, v, f);
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\ttkhd\n"+
				"\t\t\tSize: "+size+
				"\n\t\t\tType: TrackHeaderBox"+
				"\n\t\t\tVersion: "+ version+
				"\n\t\t\tFlags: "+flags+
				"\n\t\t\tCreation Time: "+ creation_time+
				"\n\t\t\tModification Time: "+ modification_time+
				"\n\t\t\tTrack ID: "+track_ID+
				"\n\t\t\tDuration: "+duration+
				"\n\t\t\tLayer: "+layer+
				"\n\t\t\tAlternate Group: "+alternate_group+
				"\n\t\t\tVolume: "+volume+
				"\n\t\t\tTrack Width: "+track_width+
				"\n\t\t\tTrack Height: "+track_height+"\n";
	}
}
