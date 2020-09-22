package boxes;

import java.io.IOException;
import java.io.InputStream;

import main.Util;

public class TrackFragmentHeaderBox extends FullBox{
	public long track_ID;
	public long base_data_offset;
	public long sample_description_index;
	public long default_sample_duration;
	public long default_sample_size;
	public long default_sample_flags;
	public int sample_depends_on;
	public int sample_is_depended_on;
	public int sample_has_redundancy;
	public int sample_padding_value;
	public int sample_is_difference_sample;
	public int sample_degradation_priority;
	int flag_1=0;
	int flag_2=0;
	int flag_8=0;
	int flag_16=0;
	int flag_32=0;
	public TrackFragmentHeaderBox(String boxtype, long v, long f) {
		super(boxtype, v, f);
	}

	public void SetTFHDBox(InputStream fis) throws IOException {
		byte[] track_ID= new byte[4];
		fis.read(track_ID);
		this.track_ID= Util.ByteArrayToLong(track_ID);
		long flags=this.flags;
		
		
		while(flags>0){
			if(flags>=0x000020) {
				flag_32=1;
				flags-=0x000020;
			}else if(flags>=0x000010) {
				flag_16=1;
				flags-=0x000010;
			}else if(flags>=0x000008) {
				flag_8=1;
				flags-=0x000008;
			}else if(flags>=0x000002) {
				flag_2=1;
				flags-=0x000002;
			}else {
				flag_1=1;
				flags-=0x000001;
			}
		}
		
		if(flag_1==1) {
			byte[] base_data_offset= new byte[8];
			fis.read(base_data_offset);
			this.base_data_offset= Util.ByteArrayToLong(base_data_offset);
		}
		if(flag_2==1) {
			byte[] sample_description_index= new byte[4];
			fis.read(sample_description_index);
			this.sample_description_index= Util.ByteArrayToLong(sample_description_index);
		}
		if(flag_8==1) {
			byte[] default_sample_duration= new byte[4];
			fis.read(default_sample_duration);
			this.default_sample_duration= Util.ByteArrayToLong(default_sample_duration);
		}
		if(flag_16==1) {
			byte[] default_sample_size= new byte[4];
			fis.read(default_sample_size);
			this.default_sample_size= Util.ByteArrayToLong(default_sample_size);
		}
		if(flag_32==1) {
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
	}
	
	public void PrintTFHDBox() {
		System.out.println("\t\ttfhd"+
								"\n\t\t\tSize: "+size+
								"\n\t\t\tType: TrackFragmentHeaderBox"+
								"\n\t\t\tVersion: "+version+
								"\n\t\t\tFlags: "+flags+
								"\n\t\t\tTrack ID: "+track_ID);
		if(flag_1==1) {
			System.out.println("\t\t\tBase Data Offset: "+base_data_offset);
		}
		if(flag_2==1) {
			System.out.println("\t\t\tSample Description Index: "+sample_description_index);
		}
		if(flag_8==1) {
			System.out.println("\t\t\tDefault Sample Duration: "+default_sample_duration);
		}
		if(flag_16==1) {
			System.out.println("\t\t\tDefault Sample Size: "+default_sample_size);
		}
		if(flag_32==1) {
			System.out.println("\t\t\tSample Depends On: "+sample_depends_on+
					"\n\t\t\tSample Is Depended On: "+sample_is_depended_on+
					"\n\t\t\tSample Has Redundancy: "+sample_has_redundancy+
					"\n\t\t\tSample Padding Value: "+sample_padding_value+
					"\n\t\t\tSample Is Difference Sample: "+sample_is_difference_sample+
					"\n\t\t\tSample Degradation Priority: "+sample_degradation_priority);
		}
	}
}
