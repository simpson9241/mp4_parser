package boxes;

import java.io.InputStream;

import main.Util;

public class SampleDescriptionBox extends FullBox{
	public long entry_count;
	public SampleDescriptionBox(String boxtype, long v, long f) { //stsd
		super(boxtype, v, f);
	}

	public void SetSTSDBox(InputStream fis) throws Exception{
		byte[] entry_count=new byte[4];
		fis.read(entry_count);
		this.entry_count=Util.ByteArrayToLong(entry_count);
	}
	
	@Override
	public String toString() {
		return "\t\t\t\t\tstsd\n"+
				"\t\t\t\t\t\tSize: "+size+
				"\n\t\t\t\t\t\tType: SampleDescriptionBox\n";
	}
}
