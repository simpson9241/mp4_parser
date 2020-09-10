package boxes;

public class HandlerReferenceBox extends FullBox{
	
	public String component_type;
	public String component_subtype;
	public String component_name;
	
	public HandlerReferenceBox(String boxtype, long v, long f) { //hdlr
		super(boxtype, v, f);
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
