package boxes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import main.Util;

public class VideoMediaHeaderBox extends FullBox{

	public int graphics_mode;
	public int opcolor_r;
	public int opcolor_g;
	public int opcolor_b;
	
	
	public VideoMediaHeaderBox(String boxtype, long v, long f) { //vmhd
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
	}

	public void SetVMHDBox(InputStream fis) throws Exception {
		byte[] graphics_mode=new byte[2];
		fis.read(graphics_mode);
		this.graphics_mode=(int)Util.ByteArrayToLong(graphics_mode);
		
		byte[] opcolor_r=new byte[2];
		fis.read(opcolor_r);
		this.opcolor_r=(int)Util.ByteArrayToLong(opcolor_r);
		
		byte[] opcolor_g=new byte[2];
		fis.read(opcolor_g);
		this.opcolor_g=(int)Util.ByteArrayToLong(opcolor_g);
		
		byte[] opcolor_b=new byte[2];
		fis.read(opcolor_b);
		this.opcolor_b=(int)Util.ByteArrayToLong(opcolor_b);
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\tvmhd\n"+
				"\t\t\t\t\tSize: "+size+
				"\n\t\t\t\t\tType: VideoMediaHeaderBox"+
				"\n\t\t\t\t\tVersion: "+ version+
				"\n\t\t\t\t\tFlags: "+ flags+
				"\n\t\t\t\t\tGraphics Mode: "+ graphics_mode+
				"\n\t\t\t\t\tOpcolor_R: "+opcolor_r+
				"\n\t\t\t\t\tOpcolor G: "+opcolor_g+
				"\n\t\t\t\t\tOpcolor B: "+opcolor_b+"\n";
	}
}
