package boxes;

public class DataInformationBox extends Box {

	public DataInformationBox(String boxtype) { //dinf
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		return tab.toString()+"dinf\n"+
				tab.toString()+"\tSize: "+size+"\n"+
				tab.toString()+"\tType: DataInformationBox\n";
	}

}
