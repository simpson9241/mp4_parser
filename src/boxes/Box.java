package boxes;

public class Box {
	public int struct_depth;
	public long start_position;
	public long end_position;
	public long size; //32비트
	public String type; //32비트
	public long largesize; //64비트
	public long[] usertype=new long[16]; 
	
	public Box(String boxtype) {
		this.type=boxtype;
		
	}
	
	public Box(String boxtype, long extended_type) {
		this.type=boxtype;
		if(type.compareTo("uuid")==0) {
//			this.usertype=extended_type;
		}
	}
	
}
