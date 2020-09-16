package boxes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
	
	public String toString(String tab) {
		
		return tab+"hdlr\n"+
		tab+"\tSize: "+size+"\n"+
		tab+"\tVersion: "+version+"\n"+
		tab+"\tComponent Type: "+component_type+"\n"+
		tab+"\tComponent Subtype: "+component_subtype+"\n"+
		tab+"\tComponent Name: "+component_name+"\n";
	}
	
	

}
