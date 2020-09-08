package boxes;

public class MovieBox extends Box {

	public MovieBox(String boxtype) {//moov
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "moov\n"+
				"\tSize: "+size+
				"\n\tType: MovieBox\n";
	}
	
}
