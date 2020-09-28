package boxes;

import java.util.ArrayList;

public class DataInformationBox extends Box {

	public DataInformationBox(String boxtype) { //dinf
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		return tab.toString()+"dinf\n"+
				tab.toString()+"\tSize: "+size+"\n"+
				tab.toString()+"\tType: DataInformationBox\n";
	}

	public void SetStructDepth(int box_count, int full_box_count, ArrayList<Box> boxes, ArrayList<FullBox> full_boxes,long stream_position) {
		for(int i=full_box_count-1;i>=0;i--) {
			if(full_boxes.get(i).type.equals("meta")) {
				if(full_boxes.get(i).end_position>stream_position) {
					struct_depth=full_boxes.get(i).struct_depth+1;
					break;
				}
			}
		}
		
		for(int i=box_count-1;i>=0;i--) {
			if(boxes.get(i).type.equals("minf")) {
				if(boxes.get(i).end_position>stream_position) {
					struct_depth=4;
					break;
				}
			}
		}
	}
	
}
