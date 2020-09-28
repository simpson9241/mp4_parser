package boxes;

import java.util.ArrayList;

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
	public void SetStructDepth(int box_count, ArrayList<Box> boxes,long stream_position) {
		for(int i=box_count-1;i>=0;i--) {
			if(boxes.get(i).type.equals("trak")) {
				if(boxes.get(i).end_position>stream_position) {
					struct_depth=boxes.get(i).struct_depth+1;
					break;
				}else{
					struct_depth=1;
					break;
				}
			}
		}
	}
}
