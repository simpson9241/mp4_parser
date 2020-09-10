package boxes;

import java.util.ArrayList;

public class SyncSampleBox extends FullBox{
	public long entry_count;
	public ArrayList<Long> sync_sample_table=new ArrayList<Long>();
	public SyncSampleBox(String boxtype, long v, long f) {// stss 0 0
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
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
