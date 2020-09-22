package boxes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class TrackFragmentRunBox extends FullBox {
	public long sample_count;
	public long data_offset;
	public long first_sample_flags=0;
	int flag_1=0;
	int flag_2=0;
	int flag_3=0;
	int flag_4=0;
	int flag_5=0;
	int flag_6=0;
	public ArrayList<TrackFragmentRunTable> table=new ArrayList<>();
	public TrackFragmentRunBox(String boxtype, long v, long f) {// trun
		super(boxtype, v, f);
	}

	public void SetTRUNBox(InputStream fis) throws IOException {
		byte[] sample_count= new byte[4];
		fis.read(sample_count);
		this.sample_count= Util.ByteArrayToLong(sample_count);
		long flags=this.flags;
		
		
		while(flags>0){
			if(flags>=0x000800) {
				flag_6=1;
				flags-=0x000800;
			}else if(flags>=0x000400) {
				flag_5=1;
				flags-=0x000400;
			}else if(flags>=0x000200) {
				flag_4=1;
				flags-=0x000200;
			}else if(flags>=0x000100) {
				flag_3=1;
				flags-=0x000100;
			}else if(flags>=0x000004){
				flag_2=1;
				flags-=0x000004;
			}else {
				flag_1=1;
				flags-=0x000001;
			}
		}
		
		if(flag_1==1) {
			byte[] data_offset= new byte[4];
			fis.read(data_offset);
			this.data_offset= Util.ByteArrayToLong(data_offset);
		}
		if(flag_2==1) {
			byte[] first_sample_flags= new byte[4];
			fis.read(first_sample_flags);
			this.first_sample_flags= Util.ByteArrayToLong(first_sample_flags);
		}
		if(this.sample_count>0) {
			for(int i=0;i<this.sample_count;i++) {
				TrackFragmentRunTable entry=new TrackFragmentRunTable();
				if(flag_3==1) {
					byte[] sample_duration= new byte[4];
					fis.read(sample_duration);
					entry.sample_duration= Util.ByteArrayToLong(sample_duration);
				}
				if(flag_4==1) {
					byte[] sample_size= new byte[4];
					fis.read(sample_size);
					entry.sample_size= Util.ByteArrayToLong(sample_size);
				}
				if(flag_5==1) {
					
					byte[] sample_flags= new byte[4];
					fis.read(sample_flags);
					entry.sample_flags=(int) (Util.ByteArrayToLong(sample_flags));
					
					byte[] sample_flags_1= new byte[1];
					sample_flags_1[0]=sample_flags[0];
					entry.sample_depends_on=(int) (Util.ByteArrayToLong(sample_flags_1)&3);
					
					byte[] sample_flags_2= new byte[1];
					sample_flags_2[0]=sample_flags[1];
					entry.sample_is_depended_on=(int) (Util.ByteArrayToLong(sample_flags_2)>>6);
					entry.sample_has_redundancy=(int) ((Util.ByteArrayToLong(sample_flags_2)>>4)&3);
					entry.sample_padding_value=(int) ((Util.ByteArrayToLong(sample_flags_2)>>1)&7);
					entry.sample_is_difference_sample=(int) (Util.ByteArrayToLong(sample_flags_2)&1);
					byte[] sample_flags_3= new byte[2];
					sample_flags_3[0]=sample_flags[2];
					sample_flags_3[1]=sample_flags[3];
					entry.sample_degradation_priority=(int) Util.ByteArrayToLong(sample_flags_3);
				}
				if(flag_6==1) {
					byte[] sample_composition_time_offset= new byte[4];
					fis.read(sample_composition_time_offset);
					entry.sample_composition_time_offset= Util.ByteArrayToLong(sample_composition_time_offset);
				}
				this.table.add(entry);
			}
		}
		
	}
	
	public void PrintTRUNBox() {
		System.out.println("\t\ttrun"+
								"\n\t\t\tSize: "+size+
								"\n\t\t\tType: TrackFragmentRunBox"+
								"\n\t\t\tVersion: "+version+
								"\n\t\t\tFlags: "+flags+
								"\n\t\t\tSample Counnt: "+sample_count);
		if(flag_1==1) {
			System.out.println("\t\t\tData Offset: "+data_offset);
		}
		if(flag_2==1) {
			System.out.println("\t\t\tFirst Sample Flags: "+first_sample_flags);
		}
	}
	public void PrintTable() {
		if(this.sample_count>0) {
			if(this.sample_count>10) {
				System.out.println("\n\t\t\tSample Table");
				for(int i=0;i<10;i++) {
					System.out.println(
							"\n\t\t\tSample: "+i+
							"\n\t\t\tSample Duration: "+this.table.get(i).sample_duration+
							"\tSample Size: "+this.table.get(i).sample_size+
							"\tSample Flags: "+this.table.get(i).sample_flags+
							"\tSample Composition Time Offset: "+this.table.get(i).sample_composition_time_offset);
				}
			}else {

				System.out.println("\n\t\t\tSample Table");
				for(int i=0;i<this.sample_count;i++) {
					System.out.println(
							"\n\t\t\tSample: "+i+
							"\n\t\t\tSample Duration: "+this.table.get(i).sample_duration+
							"\tSample Size: "+this.table.get(i).sample_size+
							"\tSample Flags: "+this.table.get(i).sample_flags+
							"\tSample Composition Time Offset: "+this.table.get(i).sample_composition_time_offset);
				}
			}
			
		}
	}
}
