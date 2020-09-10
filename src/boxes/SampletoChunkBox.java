package boxes;

import java.util.ArrayList;

public class SampletoChunkBox extends FullBox{
	public long entry_count;
	public ArrayList<SampletoChunkTable> table=new ArrayList<SampletoChunkTable>();
	public SampletoChunkBox(String boxtype, long v, long f) {// stsc 0 0
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\t\tstsc\n"+
				"\t\t\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\t\t\tType: SampletoChunkBox"+"\n"+
				"\t\t\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
}
