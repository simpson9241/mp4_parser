package boxes;

public class SoundMediaHeaderBox extends FullBox{
	public int balance;
	public int reserved;
	public SoundMediaHeaderBox(String boxtype, long v, long f) { //smhd
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\tsmhd\n"+
				"\t\t\t\t\tSize: "+size+
				"\n\t\t\t\t\tType: SoundMediaHeaderBox"+
				"\n\t\t\t\t\tVersion: "+ version+
				"\n\t\t\t\t\tFlags: "+ flags+
				"\n\t\t\t\t\tBalance: "+ balance+
				"\n\t\t\t\t\tReserved: "+reserved+"\n";
	}
}
