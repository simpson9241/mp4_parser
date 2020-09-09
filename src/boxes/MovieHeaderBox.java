package boxes;

public class MovieHeaderBox extends FullBox{
	public String creation_time; //64 32
	public String modification_time; //64 32
	public long timescale; //32 32
	public long duration; //64 32
	public double rate=1.0; //32
	public double volume=1.0; //16
	public long reserved=0; //16
//	public long reserved[2]=0; //32
	public long[] matrix= {0x00010000,0,0,0,0x00010000,0,0,0,0x40000000}; //32
//	public long[] pre_defined; //32
	public long next_track_ID; //32
	
	public MovieHeaderBox(String boxtype, long v, long f) { //"mvhd", version, 0 version은 type 뒤에 바로 1바이트
		super(boxtype, v, f);
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\tmvhd\n"+
				"\t\tSize: "+size+
				"\n\t\tType: MovieHeaderBox"+
				"\n\t\tVersion: "+ version+
				"\n\t\tCreation Time: "+ creation_time+
				"\n\t\tModification Time: "+ modification_time+
				"\n\t\tTime Scale: "+timescale+
				"\n\t\tDuration: "+duration+
				"\n\t\tRate: "+rate+
				"\n\t\tVolume: "+volume+
				"\n\t\tNext Track ID: "+next_track_ID+"\n";
	}


}
