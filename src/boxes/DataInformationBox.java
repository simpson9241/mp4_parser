package boxes;

public class DataInformationBox extends Box {

	public DataInformationBox(String boxtype) { //dinf
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(String tab) {
		return tab+"dinf\n"+
				tab+"\tSize: "+size+"\n"+
				tab+"\tType: DataInformationBox\n";
	}

}
