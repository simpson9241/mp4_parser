package boxes;

public class UserDataBox extends Box{

	public UserDataBox(String boxtype) { //udta
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	public String toString(String tab) {
		return tab+"udta\n"+
				tab+"\tSize: "+size+"\n"+
				tab+"\tType: UserDataBox\n";
	}
}
