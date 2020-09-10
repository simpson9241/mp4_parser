package boxes;

public class SampleTableBox extends Box {

	public SampleTableBox(String boxtype) {// stbl
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "\t\t\t\tstbl\n"+
				"\t\t\t\t\tSize: "+size+
				"\n\t\t\t\t\tType: SampleTableBox\n";
	}
}
