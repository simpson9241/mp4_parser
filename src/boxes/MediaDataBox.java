package boxes;

import java.util.ArrayList;

public class MediaDataBox extends Box{

	public ArrayList<MediaData> datas=new ArrayList<MediaData>();
	public MediaDataBox(String boxtype) {
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	
	public MediaDataBox(String boxtype,long size) {
		super(boxtype,size);
	}

}
