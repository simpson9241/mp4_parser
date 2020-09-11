package boxes;

import java.util.ArrayList;

public class SampletoTableBox extends FullBox{
	public long entry_count;
	public ArrayList<TimetoSampleTable> time_to_sample_table=new ArrayList<TimetoSampleTable>();
	
	public SampletoTableBox(String boxtype, long v, long f) { //stts 0 0
		super(boxtype, v, f);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\t\tstts\n"+
				"\t\t\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\t\t\tType: SampletoTableBox"+"\n"+
				"\t\t\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
}
