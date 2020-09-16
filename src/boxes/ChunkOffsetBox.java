package boxes;

import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class ChunkOffsetBox extends FullBox{
	public long entry_count;
	public ArrayList<Long> chunk_offset_table=new ArrayList<Long>();
	public ChunkOffsetBox(String boxtype, long v, long f) { //stco
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public void SetSTCOBox(InputStream fis) throws Exception{
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}

	public void SetSTCOBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			byte[] offset=new byte[4];
			fis.read(offset);
			this.chunk_offset_table.add(Util.ByteArrayToLong(offset));
		}
	}
	
	public void STCOBox_Table_Print() {
		if(this.entry_count>50) {
			System.out.println("\t\t\t\t\t\tChunk Offset Table\n");
			for(int i=0;i<50;i++) {
				System.out.println("\t\t\t\t\t\tSample "+i+": "+this.chunk_offset_table.get(i).longValue());
			}
		}else {
			System.out.println("\t\t\t\t\t\tChunk Offset Table\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println("\t\t\t\t\t\tSample "+i+": "+this.chunk_offset_table.get(i).longValue());
			}
		}
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
