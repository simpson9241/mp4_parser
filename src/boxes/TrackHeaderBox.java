package boxes;

import java.io.InputStream;
import java.util.Calendar;

import main.Util;

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

	public void SetTKHDBox(InputStream fis,Calendar cal) throws Exception {
		long creation_time_temp;
		long modification_time_temp;
		
		if(this.version==1) {
			byte[] creation_time_byte=new byte[8];
			fis.read(creation_time_byte);
			creation_time_temp=Util.ByteArrayToLong(creation_time_byte);
			byte[] modification_time_byte=new byte[8];
			fis.read(modification_time_byte);
			modification_time_temp=Util.ByteArrayToLong(modification_time_byte);
			byte[] track_ID=new byte[4];
			fis.read(track_ID);
			this.track_ID=Util.ByteArrayToLong(track_ID);
			//reserved 4bytes for use by Apple. Set to 0
			fis.skip(4);
			byte[] duration=new byte[8];
			fis.read(duration);
			this.duration=Util.ByteArrayToLong(duration);
		}else {
			byte[] creation_time_byte=new byte[4];
			fis.read(creation_time_byte);
			creation_time_temp=Util.ByteArrayToLong(creation_time_byte);
			byte[] modification_time_byte=new byte[4];
			fis.read(modification_time_byte);
			modification_time_temp=Util.ByteArrayToLong(modification_time_byte);
			byte[] track_ID=new byte[4];
			fis.read(track_ID);
			this.track_ID=Util.ByteArrayToLong(track_ID);
			//reserved 4bytes for use by Apple. Set to 0
			fis.skip(4);
			byte[] duration=new byte[4];
			fis.read(duration);
			this.duration=Util.ByteArrayToLong(duration);
		}
		//reserved 4bytes for use by Apple. Set to 0
		fis.skip(8);
		byte[] layer=new byte[2];
		fis.read(layer);
		this.layer=Util.ByteArrayToLong(layer);
		
		byte[] alternate_group=new byte[2];
		fis.read(alternate_group);
		this.alternate_group=Util.ByteArrayToLong(alternate_group);
		
		byte[] volume_1=new byte[1];
		fis.read(volume_1);
		byte[] volume_2=new byte[1];
		fis.read(volume_2);
		this.volume=(double)Util.ByteArrayToLong(volume_1)+((double)Util.ByteArrayToLong(volume_2))*0.1;
		
		//Reserved 2 bytes and Matrix Structure 36bytes
		fis.skip(38);
		
		byte[] track_width_1=new byte[2];
		fis.read(track_width_1);
		byte[] track_width_2=new byte[2];
		fis.read(track_width_2);
		this.track_width=(double)Util.ByteArrayToLong(track_width_1)+((double)Util.ByteArrayToLong(track_width_2))*0.01;
		
		byte[] track_height_1=new byte[2];
		fis.read(track_height_1);
		byte[] track_height_2=new byte[2];
		fis.read(track_height_2);
		this.track_height=(double)Util.ByteArrayToLong(track_height_1)+((double)Util.ByteArrayToLong(track_height_2))*0.01;

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
