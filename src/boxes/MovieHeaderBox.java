package boxes;

public class MovieHeaderBox extends FullBox{
	public long creation_time; //64 32
	public long modification_time; //64 32
	public long timescale; //32 32
	public long duration; //64 32
	public long rate=0x00010000; //32
	public long volume=0x0100; //16
	public long reserved=0; //16
//	public long reserved[2]=0; //32
	public long[] matrix= {0x00010000,0,0,0,0x00010000,0,0,0,0x40000000}; //32
//	public long[] pre_defined; //32
	public long next_track_ID; //32
	
	public MovieHeaderBox(String boxtype, long v, long f) { //"mvhd", version, 0 version은 type 뒤에 바로 1바이트
		super(boxtype, v, f);
		
		
	}


}
