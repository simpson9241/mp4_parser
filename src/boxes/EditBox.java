package boxes;

public class EditBox extends Box{

	public EditBox(String boxtype) { //edts
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public String toString() {
		return "\t\tedts\n"+
				"\t\t\tSize: "+size+
				"\n\t\t\tType: EditBox\n";
	}
}
