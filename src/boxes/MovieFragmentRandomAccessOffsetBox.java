package boxes;

import java.io.IOException;
import java.io.InputStream;

import main.Util;

public class MovieFragmentRandomAccessOffsetBox extends FullBox {
	public long local_size;
	public MovieFragmentRandomAccessOffsetBox(String boxtype, long v, long f) {
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}

	public void SetMFROBox(InputStream fis) throws IOException {
		byte[] size=new byte[4];
		fis.read(size);
		this.local_size=Util.ByteArrayToLong(size);
	}
	@Override
	public String toString() {
		return "\tmfro\n"+
				"\t\tSize: "+size+
				"\n\t\tType: MovieFragmentRandomAccessOffsetBox"+
				"\n\t\tMfra Size: "+ local_size+"\n";
	}
}
