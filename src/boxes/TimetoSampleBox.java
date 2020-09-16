package boxes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import main.Util;

public class TimetoSampleBox extends FullBox{
	public long entry_count;
	public ArrayList<TimetoSampleTable> time_to_sample_table=new ArrayList<TimetoSampleTable>();
	
	public TimetoSampleBox(String boxtype, long v, long f) { //stts 0 0
		super(boxtype, v, f);
	}
	
	public void SetSTTSBox(InputStream fis) throws Exception{
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	public void SetSTTSBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			TimetoSampleTable table=new TimetoSampleTable();
			
			byte[] sample_count=new byte[4];
			fis.read(sample_count);
			table.sample_count=Util.ByteArrayToLong(sample_count);
			
			byte[] sample_duration=new byte[4];
			fis.read(sample_duration);
			table.sample_duration=Util.ByteArrayToLong(sample_duration);
			
			this.time_to_sample_table.add(table);
		}
	}
	
	public void STTSBox_Table_Print() {
		if(this.entry_count>50) {
			System.out.println("\t\t\t\t\t\tTime-to-sample Table\n");
			for(int i=0;i<50;i++) {
				System.out.println("\t\t\t\t\t\tSample Count: "+this.time_to_sample_table.get(i).sample_count+"\tSample Duration: "+this.time_to_sample_table.get(i).sample_duration);
			}
		}else {
			System.out.println("\t\t\t\t\t\tTime-to-sample Table\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println("\t\t\t\t\t\tSample Count: "+this.time_to_sample_table.get(i).sample_count+"\tSample Duration: "+this.time_to_sample_table.get(i).sample_duration);
			}
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\t\tstts\n"+
				"\t\t\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\t\t\tType: TimetoSampleBox"+"\n"+
				"\t\t\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
}
