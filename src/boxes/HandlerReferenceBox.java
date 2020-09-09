package boxes;

public class HandlerReferenceBox extends FullBox{
	
	public String component_type;
	public String component_subtype;
	public String component_name;
	
	public HandlerReferenceBox(String boxtype, long v, long f) { //hdlr
		super(boxtype, v, f);
	}

	@Override
	public String toString() {
		
		return "\t\t\thdlr\n"+
		"\t\t\t\tSize: "+size+
		"\n\t\t\t\tVersion: "+version+
		"\n\t\t\t\tComponent Type: "+component_type+
		"\n\t\t\t\tComponent Subtype: "+component_subtype+
		"\n\t\t\t\tComponent Name: "+component_name+"\n";
	}
	
	

}
