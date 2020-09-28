package boxes;

public class UserDataBox extends Box{

	public UserDataBox(String boxtype) { //udta
		super(boxtype);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		return tab.toString()+"udta\n"+
				tab.toString()+"\tSize: "+size+"\n"+
				tab.toString()+"\tType: UserDataBox\n";
	}
}
