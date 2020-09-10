package boxes;

import java.util.ArrayList;

public class SampletoTableBox extends FullBox{
	public long number_of_entries;
	public ArrayList<TimetoSampleTable> time_to_sample_table=new ArrayList<TimetoSampleTable>();
	
	public SampletoTableBox(String boxtype, long v, long f) { //stts 0 0
		super(boxtype, v, f);
	}
	
	public String toString(String tab) {
		// TODO Auto-generated method stub
		return tab+"\t\t\t\t\tstts\n"+
				tab+"\t\t\t\t\t\tSize: "+size+"\n"+
				tab+"\t\t\t\t\t\tType: SampletoTableBox"+"\n"+
				tab+"\t\t\t\t\t\tVersion: "+ version+"\n"+
				tab+"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				tab+"\t\t\t\t\t\tNumber of Entries: "+ number_of_entries+"\n";
	}
}
