package boxes;

public class VideoMediaHeaderBox extends FullBox{

	public int graphics_mode;
	public int opcolor_r;
	public int opcolor_g;
	public int opcolor_b;
	
	
	public VideoMediaHeaderBox(String boxtype, long v, long f) { //vmhd
		super(boxtype, v, f);
		// TODO Auto-generated constructor stub
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
