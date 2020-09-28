package boxes;

import java.util.ArrayList;

public class FreeSpaceBox extends Box{

	public FreeSpaceBox(String boxtype) { //free skip
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		return tab.toString()+"free\n"+
				tab.toString()+"\tSize: "+size+"\n"+
				tab.toString()+"\tType: FreeSpaceBox\n";
	}
	
	public void SetStructDepth(int box_count, int full_box_count, ArrayList<Box> boxes, ArrayList<FullBox> full_boxes,long stream_position) {
		if(box_count>0) {
			if(full_box_count>0) {
				if(boxes.get(box_count-1).end_position>full_boxes.get(full_box_count-1).end_position) {
					if(full_boxes.get(full_box_count-1).end_position>stream_position) {
						struct_depth=full_boxes.get(full_box_count-1).struct_depth+1;
					}else {
						struct_depth=full_boxes.get(full_box_count-1).struct_depth;
					}
				}else {
					if(boxes.get(box_count-1).end_position>stream_position) {
						struct_depth=boxes.get(box_count-1).struct_depth+1;
					}else {
						struct_depth=boxes.get(box_count-1).struct_depth;
					}
				}
			}else {
				if(boxes.get(box_count-1).end_position>stream_position) {
					struct_depth=boxes.get(box_count-1).struct_depth+1;
				}else {
					struct_depth=boxes.get(box_count-1).struct_depth;
				}
			}
		}else {
			struct_depth=0;
		}
	}
}
