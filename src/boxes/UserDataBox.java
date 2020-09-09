package boxes;

public class UserDataBox extends Box{

	public UserDataBox(String boxtype) { //udta
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "\tudta\n"+
				"\t\tSize: "+size+
				"\n\t\tType: UserDataBox\n";
	}
}
