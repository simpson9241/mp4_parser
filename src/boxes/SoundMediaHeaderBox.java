package boxes;

import java.io.InputStream;

import main.Util;

public class SoundMediaHeaderBox extends FullBox{
	public int balance;
	public int reserved;
	public SoundMediaHeaderBox(String boxtype, long v, long f) { //smhd
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public void SetSMHDBox(InputStream fis) throws Exception {
		byte[] balance=new byte[2];
		fis.read(balance);
		this.balance=(int)Util.ByteArrayToLong(balance);
		
		byte[] reserved=new byte[2];
		fis.read(reserved);
		this.reserved=(int)Util.ByteArrayToLong(reserved);
		
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
