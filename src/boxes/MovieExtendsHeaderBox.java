package boxes;

import java.io.IOException;
import java.io.InputStream;

import main.Util;

public class MovieExtendsHeaderBox extends FullBox{
	public long fragment_duration;
	public MovieExtendsHeaderBox(String boxtype, long v, long f) { //mehd
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}
	
	public void SetMEHDBox(InputStream fis) throws IOException {
		if(this.version==1) {
			byte[] duration= new byte[8];
			fis.read(duration);
			this.fragment_duration= Util.ByteArrayToLong(duration);
		}else {
			byte[] duration= new byte[4];
			fis.read(duration);
			this.fragment_duration= Util.ByteArrayToLong(duration);
		}
	}
	
	@Override
	public String toString() {
		return "\t\tmehd\n"+
				"\t\t\tSize: "+size+
				"\n\t\t\tType: MovieExtendsHeaderBox"+
				"\n\t\t\tFragment Duration: "+fragment_duration+"\n";
	}
}
