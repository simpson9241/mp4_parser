package boxes;

import java.util.ArrayList;

public class DataReferenceBox extends FullBox{
	public long entry_count;
	public ArrayList<DataReference> data_references=new ArrayList<DataReference>();
	public DataReferenceBox(String boxtype, long v, long f) { //dref
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(String tab) {
		// TODO Auto-generated method stub
		return tab+"dref\n"+
				tab+"\tSize: "+size+"\n"+
				tab+"\tType: DataReferenceBox"+"\n"+
				tab+"\tVersion: "+ version+"\n"+
				tab+"\tFlags: "+ flags+"\n"+
				tab+"\tNumber of Entries: "+ entry_count+"\n";
	}

}
