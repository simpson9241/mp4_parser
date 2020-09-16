package boxes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import main.Util;

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
	
	public void SetMDHDBox(InputStream fis,Calendar cal) throws Exception {
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
		byte[] language=new byte[2];
		fis.read(language);
		this.language=Util.ByteArrayToLong(language);
		
		byte[] quality=new byte[2];
		fis.read(quality);
		this.quality=Util.ByteArrayToLong(quality);
		
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
