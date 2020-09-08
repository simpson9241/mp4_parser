package boxes;

import java.util.Arrays;

public class FileTypeBox extends Box{
	public String major_brand; //32비트
	public long minor_version; //32비트
	public String compatible_brands; //박스 끝까지
	public FileTypeBox(String boxtype) { //ftyp
		super(boxtype);
	}
	
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Type: FileTypeBox\n"+
				"Major Brand: "+ major_brand+
				"\nMinor Version: "+minor_version+
				"\nCompatible Brands: "+compatible_brands+"\n";
	}
	
}
