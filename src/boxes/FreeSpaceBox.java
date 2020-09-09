package boxes;

public class FreeSpaceBox extends Box{

	public FreeSpaceBox(String boxtype) { //free skip
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "free\n"+
				"\tSize: "+size+
				"\n\tType: FreeSpaceBox\n";
	}
}
