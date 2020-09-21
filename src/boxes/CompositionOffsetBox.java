package boxes;

import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class CompositionOffsetBox extends FullBox{

	public long entry_count;
	ArrayList<CompositionOffset> composition_offset_table=new ArrayList<>();
	public CompositionOffsetBox(String boxtype, long v, long f) {
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}

	public void SetCTTSBox(InputStream fis) throws Exception{
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	public void SetCTTSBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			CompositionOffset composition_offset_list=new CompositionOffset();
			
			byte[] count=new byte[4];
			fis.read(count);
			composition_offset_list.sample_count=Util.ByteArrayToLong(count);
			
			byte[] offset=new byte[4];
			fis.read(offset);
			composition_offset_list.composition_offset=Util.ByteArrayToLong(offset);
			
			this.composition_offset_table.add(composition_offset_list);
		}
	}
	
	public void CTTSBox_Table_Print() {
		if(this.entry_count>50) {
			System.out.println("\t\t\t\t\t\tComposition Offset Table\n");
			for(int i=0;i<50;i++) {
				System.out.println("\t\t\t\t\t\tSample Count: "+this.composition_offset_table.get(i).sample_count+
						"\tComposition Offset: "+this.composition_offset_table.get(i).composition_offset+"\n");
			}
		}else {
			System.out.println("\t\t\t\t\tComposition Offset Table\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println("\t\t\t\t\tSample Count: "+this.composition_offset_table.get(i).sample_count+
						"\tComposition Offset: "+this.composition_offset_table.get(i).composition_offset+"\n");
			}
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\t\tctts\n"+
				"\t\t\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\t\t\tType: EditListBox"+"\n"+
				"\t\t\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
	
}
