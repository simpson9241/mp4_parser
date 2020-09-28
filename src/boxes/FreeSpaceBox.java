package boxes;

public class FreeSpaceBox extends Box{

	public FreeSpaceBox(String boxtype) { //free skip
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		return tab.toString()+"free\n"+
				tab.toString()+"\tSize: "+size+"\n"+
				tab.toString()+"\tType: FreeSpaceBox\n";
	}
}
