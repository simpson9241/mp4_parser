package boxes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import main.Util;

public class FileTypeBox extends Box{
	public String major_brand; //32비트
	public long minor_version; //32비트
	public String compatible_brands; //박스 끝까지
	public FileTypeBox(String boxtype) { //ftyp
		super(boxtype);
	}
	public void SetFTYPBox(InputStream fis) throws Exception {
		byte[] major_brand=new byte[4];
		fis.read(major_brand);
		byte[] minor_version=new byte[4];
		fis.read(minor_version);
		byte[] brands=new byte[(int)this.size-16];
		fis.read(brands);
		String temp=new String(brands,StandardCharsets.US_ASCII);
		int count=0;
		StringBuffer sb=new StringBuffer();
		sb.append(temp);
		for(int i=4;i<temp.length();i=i+4) {
			sb.insert(i+count, " ");
			count++;
		}
		this.major_brand=new String(major_brand,StandardCharsets.US_ASCII);
		this.minor_version=Util.ByteArrayToLong(minor_version);
		this.compatible_brands=sb.toString();
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
