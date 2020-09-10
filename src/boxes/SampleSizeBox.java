package boxes;

import java.util.ArrayList;

public class SampleSizeBox extends FullBox {

	public long entry_count;
	public long sample_size;
	public ArrayList<Long> sample_size_table=new ArrayList<Long>();
	
	public SampleSizeBox(String boxtype, long v, long f) {//stsz
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\t\tstsc\n"+
				"\t\t\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\t\t\tType: SampleSizeBox"+"\n"+
				"\t\t\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\t\t\tSample Size: "+ sample_size+"\n"+
				"\t\t\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
}
