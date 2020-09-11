package boxes;

import java.util.ArrayList;

public class ChunkOffsetBox extends FullBox{
	public long entry_count;
	public ArrayList<Long> chunk_offset_table=new ArrayList<Long>();
	public ChunkOffsetBox(String boxtype, long v, long f) { //stco
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\t\tstco\n"+
				"\t\t\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\t\t\tType: ChunkOffsetBox"+"\n"+
				"\t\t\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
}
