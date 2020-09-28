package boxes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import main.Util;

public class DataReferenceBox extends FullBox{
	public long entry_count;
	public ArrayList<DataReference> data_references=new ArrayList<DataReference>();
	public DataReferenceBox(String boxtype, long v, long f) { //dref
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public void SetDREFBox(InputStream fis) throws Exception{
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	public void SetDREFBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			DataReference datareference=new DataReference();
			byte[] reference_size=new byte[4];
			fis.read(reference_size);
			datareference.size=Util.ByteArrayToLong(reference_size);
			
			byte[] reference_type=new byte[4];
			fis.read(reference_type);
			datareference.type=new String(reference_type,StandardCharsets.US_ASCII);
			
			byte[] reference_version=new byte[1];
			fis.read(reference_version);
			datareference.version=Util.ByteArrayToLong(reference_version);
			
			byte[] reference_flags=new byte[3];
			fis.read(reference_flags);
			datareference.flags=Util.ByteArrayToLong(reference_flags);
			
			byte[] reference_data=new byte[(int)datareference.size-12];
			fis.read(reference_data);
			datareference.data=new String(reference_data,StandardCharsets.US_ASCII);
			this.data_references.add(datareference);
		}
	}
	
	public void DREFBox_Table_Print() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		if(this.entry_count>50) {
			System.out.println(tab.toString()+"\tData References\n");
			for(int i=0;i<50;i++) {
				System.out.println(tab.toString()+"\t\tSize: "+this.data_references.get(i).size+"\n"+
									tab.toString()+"\t\tType: "+this.data_references.get(i).type+"\n"+
									tab.toString()+"\t\tVersion: "+this.data_references.get(i).version+"\n"+
									tab.toString()+"\t\tFlags: "+this.data_references.get(i).flags+"\n"+
									tab.toString()+"\t\tData: "+this.data_references.get(i).data+"\n\n");
			}
		}else {
			System.out.println(tab.toString()+"\tData References\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println(tab.toString()+"\t\tSize: "+this.data_references.get(i).size+"\n"+
									tab.toString()+"\t\tType: "+this.data_references.get(i).type+"\n"+
									tab.toString()+"\t\tVersion: "+this.data_references.get(i).version+"\n"+
									tab.toString()+"\t\tFlags: "+this.data_references.get(i).flags+"\n"+
									tab.toString()+"\t\tData: "+this.data_references.get(i).data+"\n\n");
			}
		}
	}
	
	public String toString() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		return tab.toString()+"dref\n"+
				tab.toString()+"\tSize: "+size+"\n"+
				tab.toString()+"\tType: DataReferenceBox"+"\n"+
				tab.toString()+"\tVersion: "+ version+"\n"+
				tab.toString()+"\tFlags: "+ flags+"\n"+
				tab.toString()+"\tNumber of Entries: "+ entry_count+"\n";
	}
	
	public void SetStructDepth(int box_count, ArrayList<Box> boxes,long stream_position) {
		for(int i=box_count-1;i>=0;i--) {
			if(boxes.get(i).type.equals("dinf")) {
				if(boxes.get(i).end_position>stream_position) {
					struct_depth=boxes.get(i).struct_depth+1;
					break;
				}
			}
		}
	}

}
