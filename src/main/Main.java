package main;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws Exception{
		File file=new File("BigBuckBunny.mp4");
		if(file.isFile()) {
			System.out.println("파일 존재\n");
		}
		
		InputStream fis=new FileInputStream("BigBuckBunny.mp4");
		
		byte[] size_byte=new byte[4];
		byte[] type_byte=new byte[4];
		long size;
		String type;
		while(fis.read(size_byte)!=-1) {
			fis.read(type_byte);
			size=Util.ByteArrayToLong(size_byte);
			type=new String(type_byte,StandardCharsets.US_ASCII);
			
//			System.out.println(Arrays.toString(buffer));
			System.out.println(size);
			System.out.println(type);
			if(type.equals("ftyp")) {
				boxes.FileTypeBox ftyp=new boxes.FileTypeBox("ftyp");
				ftyp.size=size;
				byte[] major_brand=new byte[4];
				fis.read(major_brand);
				ftyp.major_brand=new String(major_brand,StandardCharsets.US_ASCII);
				byte[] minor_version=new byte[4];
				fis.read(minor_version);
				ftyp.minor_version=Util.ByteArrayToLong(minor_version);
				byte[] brands=new byte[(int)ftyp.size-16];
				fis.read(brands);
				ftyp.compatible_brands=new String(brands,StandardCharsets.US_ASCII);
				System.out.println(ftyp);
			}else if(type.equals("mvhd")){
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				fis.skip(3);
				
				boxes.MovieHeaderBox mvhd=new boxes.MovieHeaderBox("mvhd", v, 0);
			
			}else {
				fis.skip(size);
			}
		}
		fis.close();
		System.out.println("finished");

	}

}