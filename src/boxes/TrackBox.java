package boxes;

public class TrackBox extends Box{

	public TrackBox(String boxtype) { //trak
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "\ttrak\n"+
				"\t\tSize: "+size+
				"\n\t\tType: TrackBox\n";
	}
}
