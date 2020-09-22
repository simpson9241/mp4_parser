package boxes;

import java.io.IOException;
import java.io.InputStream;

import main.Util;

public class TrackExtendsBox extends FullBox {
	public TrackExtendsBox(String boxtype, long v, long f) { //trex
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	public int sample_depends_on;
	public int sample_is_depended_on;
	public int sample_has_redundancy;
	public int sample_padding_value;
	public int sample_is_difference_sample;
	public int sample_degradation_priority;
	public long track_ID;
	public long default_sample_description_index;
	public long default_sample_duration;
	public long default_sample_size;
	
	public void SetTREXBox(InputStream fis) throws IOException {
		
		
		byte[] track_ID= new byte[4];
		fis.read(track_ID);
		this.track_ID=Util.ByteArrayToLong(track_ID);
		byte[] default_sample_description_index= new byte[4];
		fis.read(default_sample_description_index);
		this.default_sample_description_index=Util.ByteArrayToLong(default_sample_description_index);
		byte[] default_sample_duration= new byte[4];
		fis.read(default_sample_duration);
		this.default_sample_duration=Util.ByteArrayToLong(default_sample_duration);
		byte[] default_sample_size= new byte[4];
		fis.read(default_sample_size);
		this.default_sample_size=Util.ByteArrayToLong(default_sample_size);
		
		byte[] sample_flags_1= new byte[1];
		fis.read(sample_flags_1);
		this.sample_depends_on=(int) (Util.ByteArrayToLong(sample_flags_1)&3);
		
		byte[] sample_flags_2= new byte[1];
		fis.read(sample_flags_2);
		this.sample_is_depended_on=(int) (Util.ByteArrayToLong(sample_flags_2)>>6);
		this.sample_has_redundancy=(int) ((Util.ByteArrayToLong(sample_flags_2)>>4)&3);
		this.sample_padding_value=(int) ((Util.ByteArrayToLong(sample_flags_2)>>1)&7);
		this.sample_is_difference_sample=(int) (Util.ByteArrayToLong(sample_flags_2)&1);
		byte[] sample_flags_3= new byte[2];
		fis.read(sample_flags_3);
		this.sample_degradation_priority=(int) Util.ByteArrayToLong(sample_flags_3);
	}
	
	@Override
	public String toString() {
		return "\t\ttrex\n"+
				"\t\t\tSize: "+size+
				"\n\t\t\tType: TrackExtendsBox"+
				"\n\t\t\tSample Depends On: "+sample_depends_on+
				"\n\t\t\tSample Is Depended On: "+sample_is_depended_on+
				"\n\t\t\tSample Has Redundancy: "+sample_has_redundancy+
				"\n\t\t\tSample Padding Value: "+sample_padding_value+
				"\n\t\t\tSample Is Difference Sample: "+sample_is_difference_sample+
				"\n\t\t\tSample Degradation Priority: "+sample_degradation_priority+
				"\n\t\t\tTrack ID: "+track_ID+
				"\n\t\t\tDefault Sample Description Index: "+default_sample_description_index+
				"\n\t\t\tDefault Sample Duration: "+default_sample_duration+
				"\n\t\t\tDefault Sample Size: "+default_sample_size+"\n";
	}
	
}
