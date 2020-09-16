package boxes;

import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class SyncSampleBox extends FullBox{
	public long entry_count;
	public ArrayList<Long> sync_sample_table=new ArrayList<Long>();
	public SyncSampleBox(String boxtype, long v, long f) {// stss 0 0
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public void SetSTSSBox(InputStream fis) throws Exception{
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	public void SetSTSSBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			
			byte[] sample=new byte[4];
			fis.read(sample);
			this.sync_sample_table.add(Util.ByteArrayToLong(sample));
		}
	}
	
	public void STSSBox_Table_Print() {
		if(this.entry_count>50) {
			System.out.println("\t\t\t\t\t\tSync Sample Table\n");
			for(int i=0;i<50;i++) {
				System.out.println("\t\t\t\t\t\tSample "+i+": "+this.sync_sample_table.get(i).longValue());
			}	
		}else {
			System.out.println("\t\t\t\t\t\tSync Sample Table\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println("\t\t\t\t\t\tSample "+i+": "+this.sync_sample_table.get(i).longValue());
			}
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\t\tstss\n"+
				"\t\t\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\t\t\tType: SyncSampleBox"+"\n"+
				"\t\t\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
}
