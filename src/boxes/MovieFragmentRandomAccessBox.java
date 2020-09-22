package boxes;

public class MovieFragmentRandomAccessBox extends Box{

	public MovieFragmentRandomAccessBox(String boxtype) {
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "mfra\n"+
				"\tSize: "+size+
				"\n\tType: MovieFragmentRandomAccessBox\n";
	}
}
