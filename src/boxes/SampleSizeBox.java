package boxes;

import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class SampleSizeBox extends FullBox {

	public long entry_count;
	public long sample_size;
	public ArrayList<Long> sample_size_table=new ArrayList<Long>();
	
	public SampleSizeBox(String boxtype, long v, long f) {//stsz
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public void SetSTSZBox(InputStream fis) throws Exception{
		byte[] sample_size=new byte[4];
		fis.read(sample_size);
		this.sample_size=Util.ByteArrayToLong(sample_size);
		
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	public void SetSTSZBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			
			byte[] sample=new byte[4];
			fis.read(sample);
			this.sample_size_table.add(Util.ByteArrayToLong(sample));
		}
	}
	
	public void STSZBox_Table_Print() {
		if(this.entry_count>50) {
			System.out.println("\t\t\t\t\t\tSample Size Table\n");
			for(int i=0;i<50;i++) {
				System.out.println("\t\t\t\t\t\tSample "+i+": "+this.sample_size_table.get(i).longValue());
			}
		}else {
			System.out.println("\t\t\t\t\t\tSample Size Table\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println("\t\t\t\t\t\tSample "+i+": "+this.sample_size_table.get(i).longValue());
			}
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\t\tstsz\n"+
				"\t\t\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\t\t\tType: SampleSizeBox"+"\n"+
				"\t\t\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\t\t\tSample Size: "+ sample_size+"\n"+
				"\t\t\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
}
