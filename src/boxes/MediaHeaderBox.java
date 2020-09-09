package boxes;

public class MediaHeaderBox extends FullBox{
	public String creation_time; //64 32
	public String modification_time; //64 32
	public long timescale; //32 32
	public long duration; //64 32
	public long language;
	public long quality;
	
	public MediaHeaderBox(String boxtype, long v, long f) { //"mdhd", version, 0 version은 type 뒤에 바로 1바이트
		super(boxtype, v, f);
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\tmdhd\n"+
				"\t\t\t\tSize: "+size+
				"\n\t\t\t\tType: MediaHeaderBox"+
				"\n\t\t\t\tVersion: "+ version+
				"\n\t\t\t\tCreation Time: "+ creation_time+
				"\n\t\t\t\tModification Time: "+ modification_time+
				"\n\t\t\t\tTime Scale: "+timescale+
				"\n\t\t\t\tDuration: "+duration+
				"\n\t\t\t\tLanguage: "+language+
				"\n\t\t\t\tQuality: "+quality+"\n";
	}


}
