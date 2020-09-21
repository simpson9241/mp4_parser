package boxes;

public class MovieExtendsBox extends Box {

	public MovieExtendsBox(String boxtype) { //mvex
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "\tmvex\n"+
				"\t\tSize: "+size+
				"\n\t\tType: MovieExtendsBox\n";
	}
}
