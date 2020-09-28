package boxes;

import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class SampletoGroupBox extends FullBox{
	public long entry_count;
	public long group_type;
	ArrayList<SampletoGroupTable> sample_group_table=new ArrayList<>();
	public SampletoGroupBox(String boxtype, long v, long f) {
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}

	public void SetSBGPBox(InputStream fis) throws Exception{

		byte[] group_type=new byte[4];
		fis.read(group_type);
		this.group_type=Util.ByteArrayToLong(group_type);
		
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	public void SetSBGPBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			SampletoGroupTable sample_group_table=new SampletoGroupTable();
			
			byte[] count=new byte[4];
			fis.read(count);
			sample_group_table.sample_count=Util.ByteArrayToLong(count);
			
			byte[] index=new byte[4];
			fis.read(index);
			sample_group_table.group_description_index=Util.ByteArrayToLong(index);
			
			this.sample_group_table.add(sample_group_table);
		}
	}
	
	public void SBGPBox_Table_Print() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		if(this.entry_count>50) {
			System.out.println(tab.toString()+"\tComposition Offset Table\n");
			for(int i=0;i<50;i++) {
				System.out.println(tab.toString()+"\tSample Count: "+this.sample_group_table.get(i).sample_count+
						"\tGroup Description Index: "+this.sample_group_table.get(i).group_description_index+"\n");
			}
		}else {
			System.out.println(tab.toString()+"\tComposition Offset Table\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println(tab.toString()+"\tSample Count: "+this.sample_group_table.get(i).sample_count+
						"\tGroup Description Index: "+this.sample_group_table.get(i).group_description_index+"\n");
			}
		}
	}
	
	public String toString() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		
		return tab.toString()+"sbgp\n"+
				tab.toString()+"\tSize: "+size+"\n"+
				tab.toString()+"\tType: SampletoGroupBox"+"\n"+
				tab.toString()+"\tVersion: "+ version+"\n"+
				tab.toString()+"\tFlags: "+ flags+"\n"+
				tab.toString()+"\tNumber of Entries: "+ entry_count+"\n";
	}
	
	public void SetStructDepth(int box_count, ArrayList<Box> boxes,long stream_position) {
		for(int i=box_count-1;i>=0;i--) {
			if(boxes.get(i).type.equals("stbl")) {
				if(boxes.get(i).end_position>stream_position) {
					struct_depth=5;
					break;
				}
			}else if(boxes.get(i).type.equals("traf")) {
				if(boxes.get(i).end_position>stream_position) {
					struct_depth=2;
					break;
				}
			}
		}
	}
	
}
