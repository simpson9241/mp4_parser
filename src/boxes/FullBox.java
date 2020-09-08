package boxes;

public class FullBox extends Box{
	long version;
	long flags;
	public FullBox(String boxtype,long v, long f) {
		super(boxtype); //32비트
		this.version=v; //8비트
		this.flags=f; //24비트
	}

}
