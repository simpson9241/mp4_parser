package boxes;

public class MediaInformationBox extends Box{

	public MediaInformationBox(String boxtype) {// minf
		super(boxtype);
	}

	@Override
	public String toString() {
		return "\t\t\tminf\n"+
				"\t\t\t\tSize: "+size+
				"\n\t\t\t\tType: MediaInformationBox\n";
	}
}
