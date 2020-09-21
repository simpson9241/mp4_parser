package boxes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import main.Util;

public class EditListBox extends FullBox{

	public long entry_count;
	ArrayList<EditList> edit_list_table=new ArrayList<>();
	public EditListBox(String boxtype, long v, long f) {
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public void SetELSTBox(InputStream fis) throws Exception{
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	public void SetELSTBox_Table(InputStream fis) throws Exception {
		for(int i=0;i<this.entry_count;i++) {
			EditList edit_list=new EditList();
			
			byte[] duration=new byte[4];
			fis.read(duration);
			edit_list.track_duration=Util.ByteArrayToLong(duration);
			
			byte[] time=new byte[4];
			fis.read(time);
			edit_list.media_time=Util.ByteArrayToLong(time);
			
			byte[] rate=new byte[4];
			fis.read(rate);
			edit_list.media_rate=Util.ByteArrayToLong(rate);
			
			this.edit_list_table.add(edit_list);
		}
	}
	
	public void ELSTBox_Table_Print() {
		if(this.entry_count>50) {
			System.out.println("\t\t\t\tEdit List\n");
			for(int i=0;i<50;i++) {
				System.out.println("\t\t\t\tTrack Duration: "+this.edit_list_table.get(i).track_duration+
						"\tMedia Time: "+this.edit_list_table.get(i).media_time+
						"\tMedia Rate: "+this.edit_list_table.get(i).media_rate+"\n");
			}
		}else {
			System.out.println("\t\t\t\tEdit List\n");
			for(int i=0;i<this.entry_count;i++) {
				System.out.println("\t\t\t\tTrack Duration: "+this.edit_list_table.get(i).track_duration+
						"\tMedia Time: "+this.edit_list_table.get(i).media_time+
						"\tMedia Rate: "+this.edit_list_table.get(i).media_rate+"\n");
			}
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\telst\n"+
				"\t\t\t\tSize: "+size+"\n"+
				"\t\t\t\tType: EditListBox"+"\n"+
				"\t\t\t\tVersion: "+ version+"\n"+
				"\t\t\t\tFlags: "+ flags+"\n"+
				"\t\t\t\tNumber of Entries: "+ entry_count+"\n";
	}
}
