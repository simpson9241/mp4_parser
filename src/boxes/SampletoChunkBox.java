package boxes;

import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class SampletoChunkBox extends FullBox{
	public long entry_count;
	public ArrayList<SampletoChunkTable> table=new ArrayList<SampletoChunkTable>();
	public SampletoChunkBox(String boxtype, long v, long f) {// stsc 0 0
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public void SetSTSCBox(InputStream fis) throws Exception{
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	public void SetSTSCBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			SampletoChunkTable table=new SampletoChunkTable();

			byte[] first_chunk=new byte[4];
			fis.read(first_chunk);
			table.first_chunk=Util.ByteArrayToLong(first_chunk);
			
			byte[] samples_per_chunk=new byte[4];
			fis.read(samples_per_chunk);
			table.samples_per_chunk=Util.ByteArrayToLong(samples_per_chunk);
			
			byte[] sample_description_id=new byte[4];
			fis.read(sample_description_id);
			table.sample_description_id=Util.ByteArrayToLong(sample_description_id);
			
			this.table.add(table);
		}
	}
	
	public void STSCBox_Table_Print() {
		if(this.entry_count>50) {
			System.out.println("\t\t\t\t\t\tSample-to-Chunk Table\n");
			for(int i=0;i<50;i++) {
				System.out.println("\t\t\t\t\t\tFirst Chunk: "+this.table.get(i).first_chunk+"\tSamples per Chunk: "+this.table.get(i).samples_per_chunk+"\tSample Description ID: "+this.table.get(i).sample_description_id);
			}
		}else {
			System.out.println("\t\t\t\t\t\tSample-to-Chunk Table\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println("\t\t\t\t\t\tFirst Chunk: "+this.table.get(i).first_chunk+"\tSamples per Chunk: "+this.table.get(i).samples_per_chunk+"\tSample Description ID: "+this.table.get(i).sample_description_id);
			}
		}
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
