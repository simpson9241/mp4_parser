package boxes;

public class TrackFragmentBox extends Box{

	public TrackFragmentBox(String boxtype) { //traf
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "\ttraf\n"+
				"\t\tSize: "+size+
				"\n\t\tType: TrackFragmentBox\n";
	}
}
