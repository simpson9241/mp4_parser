package boxes;

import java.io.IOException;
import java.io.InputStream;

import main.Util;

public class MovieFragmentHeaderBox extends FullBox{
	public long sequence_number;
	public MovieFragmentHeaderBox(String boxtype, long v, long f) { //mfhd
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}

	public void SetMFHDBox(InputStream fis) throws IOException {
		byte[] sequence_number= new byte[4];
		fis.read(sequence_number);
		this.sequence_number= Util.ByteArrayToLong(sequence_number);
	}
	
	@Override
	public String toString() {
		return "\tmfhd\n"+
				"\t\tSize: "+size+
				"\n\t\tType: MovieFragmentHeaderBox"+
				"\n\t\tVersion: "+version+
				"\n\t\tFlags: "+flags+
				"\n\t\tSequence Number: "+sequence_number+"\n";
	}
}
