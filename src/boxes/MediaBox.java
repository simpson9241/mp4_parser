package boxes;

public class MediaBox extends Box {

	public MediaBox(String boxtype) { //mdia
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "\t\tmdia\n"+
				"\t\t\tSize: "+size+
				"\n\t\t\tType: MediaBox\n";
	}
}
