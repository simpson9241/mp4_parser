package boxes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HandlerReferenceBox extends FullBox{
	
	public String component_type;
	public String component_subtype;
	public String component_name;
	
	public HandlerReferenceBox(String boxtype, long v, long f) { //hdlr
		super(boxtype, v, f);
	}
	
	public void SetHDLRBox(InputStream fis) throws Exception {
		byte[] component_type_byte=new byte[4];
		fis.read(component_type_byte);
		this.component_type=new String(component_type_byte,StandardCharsets.US_ASCII);
		
		byte[] component_subtype_byte=new byte[4];
		fis.read(component_subtype_byte);
		this.component_subtype=new String(component_subtype_byte,StandardCharsets.US_ASCII);
		//Component manufacturer 4 bytes Component flags 4 bytes Component flags mask 4 bytes
		fis.skip(12);
		
		byte[] component_name_byte=new byte[(int)size-32];
		fis.read(component_name_byte);
		this.component_name=new String(component_name_byte,StandardCharsets.US_ASCII);
		
	}
	
	public String toString() {
		StringBuilder tab=new StringBuilder();
		for(int i=0;i<struct_depth;i++) {
			tab.append("\t");
		}
		return tab.toString()+"hdlr\n"+
		tab.toString()+"\tSize: "+size+"\n"+
		tab.toString()+"\tVersion: "+version+"\n"+
		tab.toString()+"\tComponent Type: "+component_type+"\n"+
		tab.toString()+"\tComponent Subtype: "+component_subtype+"\n"+
		tab.toString()+"\tComponent Name: "+component_name+"\n";
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
			if(boxes.get(i).type.equals("mdia")) {
				if(boxes.get(i).end_position>stream_position) {
					struct_depth=3;
					break;
				}
			}else if(boxes.get(i).type.equals("minf")){
				if(boxes.get(i).end_position>stream_position) {
					struct_depth=4;
					break;
				}
			}
		}
	}

}
