package boxes;

public class MovieFragmentBox extends Box {

	public MovieFragmentBox(String boxtype) { //moof
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "moof\n"+
				"\tSize: "+size+
				"\n\tType: MovieFragmentBox\n";
	}
}
