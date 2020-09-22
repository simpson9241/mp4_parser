package boxes;

import java.io.IOException;
import java.io.InputStream;

import main.Util;

public class TrackFragmentBaseMediaDecodeTimeBox extends FullBox{
	public long baseMediaDecodeTime;
	public TrackFragmentBaseMediaDecodeTimeBox(String boxtype, long v, long f) {
		super(boxtype, v, f);
	}
	public void SetTFDTBox(InputStream fis) throws IOException {
		
		if(version==1) {
			byte[] decodetime= new byte[8];
			fis.read(decodetime);
			this.baseMediaDecodeTime= Util.ByteArrayToLong(decodetime);
		}else {
			byte[] decodetime= new byte[4];
			fis.read(decodetime);
			this.baseMediaDecodeTime= Util.ByteArrayToLong(decodetime);
		}
	}
	
	@Override
	public String toString() {
		return "\t\ttfdt\n"+
				"\t\t\tSize: "+size+
				"\n\t\t\tType: TrackFragmentBaseMediaDecodeTimeBox"+
				"\n\t\t\tVersion: "+version+
				"\n\t\t\tFlags: "+flags+
				"\n\t\t\tBase Media Decode Time: "+baseMediaDecodeTime+"\n";
	}
}
