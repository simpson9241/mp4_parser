package boxes;

public class FreeSpaceBox extends Box{

	public FreeSpaceBox(String boxtype) { //free skip
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(String tab) {
		return tab+"free\n"+
				tab+"\tSize: "+size+"\n"+
				tab+"\tType: FreeSpaceBox\n";
	}
}
