package boxes;

import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class TrackFragmentRandomAccessBox extends FullBox{
	public long track_ID;
	public int length_size_of_traf_num;
	public int length_size_of_trun_num;
	public int length_size_of_sample_num;
	public long entry_count;
	public ArrayList<TrackFragmentRandomAccess> table=new ArrayList<>();
	public TrackFragmentRandomAccessBox(String boxtype, long v, long f) {
		super(boxtype, v, f);
	}
	public void SetTFRABox(InputStream fis) throws Exception{
		byte[] track_ID=new byte[4];
		fis.read(track_ID);
		this.track_ID=Util.ByteArrayToLong(track_ID);
		//Reserved 26bits
		fis.skip(3);
		
		byte[] length=new byte[1];
		fis.read(length);
		this.length_size_of_traf_num=(int)((Util.ByteArrayToLong(length)>>4)&3);
		this.length_size_of_trun_num=(int)((Util.ByteArrayToLong(length)>>2)&3);
		this.length_size_of_sample_num=(int)((Util.ByteArrayToLong(length))&3);
	
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
		
		if(this.entry_count>0) {
			for(int i=0;i<this.entry_count;i++) {
				TrackFragmentRandomAccess entry=new TrackFragmentRandomAccess();
				if(version==1) {
					byte[] time=new byte[8];
					fis.read(time);
					entry.time=Util.ByteArrayToLong(time);
					byte[] moof_offset=new byte[8];
					fis.read(moof_offset);
					entry.moof_offset=Util.ByteArrayToLong(moof_offset);
				}else {
					byte[] time=new byte[4];
					fis.read(time);
					entry.time=Util.ByteArrayToLong(time);
					byte[] moof_offset=new byte[4];
					fis.read(moof_offset);
					entry.moof_offset=Util.ByteArrayToLong(moof_offset);
				}

				byte[] traf_number=new byte[this.length_size_of_traf_num+1];
				fis.read(traf_number);
				entry.traf_number=Util.ByteArrayToLong(traf_number);
				byte[] trun_number=new byte[this.length_size_of_trun_num+1];
				fis.read(trun_number);
				entry.trun_number=Util.ByteArrayToLong(trun_number);
				byte[] sample_number=new byte[this.length_size_of_sample_num+1];
				fis.read(sample_number);
				entry.sample_number=Util.ByteArrayToLong(sample_number);
				
				this.table.add(entry);
			}
		}
	}
	public void TFRABox_Table_Print() {
		if(this.entry_count>10) {
			System.out.println("\tTrack Fragment Random Access Table\n");
			for(int i=0;i<10;i++) {
				System.out.println("\t\tTime: "+this.table.get(i).time+
						"\tMoof Offset: "+this.table.get(i).moof_offset+
						"\tTraf Number: "+this.table.get(i).traf_number+
						"\tTrun Number: "+this.table.get(i).trun_number+
						"\tSample Number: "+this.table.get(i).sample_number+"\n");
			}
		}else {
			System.out.println("\tTrack Fragment Random Access Table\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println("\t\tTime: "+this.table.get(i).time+
						"\tMoof Offset: "+this.table.get(i).moof_offset+
						"\tTraf Number: "+this.table.get(i).traf_number+
						"\tTrun Number: "+this.table.get(i).trun_number+
						"\tSample Number: "+this.table.get(i).sample_number+"\n");
			}
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\ttfra\n"+
				"\t\tSize: "+size+"\n"+
				"\t\tType: TrackFragmentRandomAccessBox"+"\n"+
				"\t\tVersion: "+ version+"\n"+
				"\t\tFlags: "+ flags+"\n"+
				"\t\tTrack ID: "+ track_ID+
				"\t\tLength Size of Traf Num: "+length_size_of_traf_num+
				"\t\tLength Size of Trun Num: "+ length_size_of_trun_num+
				"\t\tLength Size of Sample Num: "+ length_size_of_sample_num+
				"\t\tNumber of Entry: "+ entry_count+"\n";
	}
}
