package boxes;

public class SampleDescriptionBox extends FullBox{
	public long number_of_entries;
	public SampleDescriptionBox(String boxtype, long v, long f) { //stsd
		super(boxtype, v, f);
	}

	@Override
	public String toString() {
		return "\t\t\t\t\tstsd\n"+
				"\t\t\t\t\t\tSize: "+size+
				"\n\t\t\t\t\t\tType: SampleDescriptionBox\n";
	}
}
