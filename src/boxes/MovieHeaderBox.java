package boxes;

import java.io.InputStream;
import java.util.Calendar;

import main.Util;

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
	
	public void SetMVHDBox(InputStream fis,Calendar cal) throws Exception {
		long creation_time_temp;
		long modification_time_temp;
		
		if(this.version==1) {
			byte[] creation_time_byte=new byte[8];
			fis.read(creation_time_byte);
			creation_time_temp=Util.ByteArrayToLong(creation_time_byte);
			byte[] modification_time_byte=new byte[8];
			fis.read(modification_time_byte);
			modification_time_temp=Util.ByteArrayToLong(modification_time_byte);
			byte[] duration=new byte[8];
			byte[] timescale=new byte[4];
			fis.read(timescale);
			this.timescale=Util.ByteArrayToLong(timescale);
			fis.read(duration);
			this.duration=Util.ByteArrayToLong(duration);
		}else {
			byte[] creation_time_byte=new byte[4];
			fis.read(creation_time_byte);
			creation_time_temp=Util.ByteArrayToLong(creation_time_byte);
			byte[] modification_time_byte=new byte[4];
			fis.read(modification_time_byte);
			modification_time_temp=Util.ByteArrayToLong(modification_time_byte);
			byte[] timescale=new byte[4];
			fis.read(timescale);
			this.timescale=Util.ByteArrayToLong(timescale);
			byte[] duration=new byte[4];
			fis.read(duration);
			this.duration=Util.ByteArrayToLong(duration);
		}
		byte[] rate_1=new byte[2];
		fis.read(rate_1);
		byte[] rate_2=new byte[2];
		fis.read(rate_2);
		this.rate=(double)Util.ByteArrayToLong(rate_1)+((double)Util.ByteArrayToLong(rate_2))*0.01;
		
		byte[] volume_1=new byte[1];
		fis.read(volume_1);
		byte[] volume_2=new byte[1];
		fis.read(volume_2);
		this.volume=(double)Util.ByteArrayToLong(volume_1)+((double)Util.ByteArrayToLong(volume_2))*0.1;
		//Reserved 10 bytes Matrix Structure 36 bytes Preview time 4 bytes Preview duration 4 bytes Poster time 4 bytes Selection time 4 bytes Selection duration 4 bytes Current time 4 bytes
		fis.skip(70);
		byte[] next_track_id=new byte[4];
		fis.read(next_track_id);
		this.next_track_ID=Util.ByteArrayToLong(next_track_id);
		
		cal.set(1970, 0, 1, 0, 0, 0);
		cal.add(Calendar.SECOND, -2082844800);
		cal.add(Calendar.SECOND, (int)creation_time_temp);
		this.creation_time=cal.getTime().toString();
		
		cal.set(1970, 0, 1, 0, 0, 0);
		cal.add(Calendar.SECOND, -2082844800);
		cal.add(Calendar.SECOND, (int)modification_time_temp);
		this.modification_time=cal.getTime().toString();
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
