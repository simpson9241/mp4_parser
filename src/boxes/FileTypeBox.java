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
		return "ftyp\n"+
				"\tSize: "+size+
				"\n\tType: FileTypeBox\n"+
				"\tMajor Brand: "+ major_brand+
				"\n\tMinor Version: "+minor_version+
				"\n\tCompatible Brands: "+compatible_brands+"\n";
	}
	
}
