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
				String temp=new String(brands,StandardCharsets.US_ASCII);
				int count=0;
				StringBuffer sb=new StringBuffer();
				sb.append(temp);
				for(int i=4;i<temp.length();i=i+4) {
					sb.insert(i+count, " ");
					count++;
				}
				
				
				ftyp.compatible_brands=sb.toString();
				
				System.out.println(ftyp);
			}else if(type.equals("moov")){
				boxes.MovieBox moov=new boxes.MovieBox("moov");
				moov.size=size;
				
				System.out.println(moov);
			}else if(type.equals("mvhd")){
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				fis.skip(3);
				
				boxes.MovieHeaderBox mvhd=new boxes.MovieHeaderBox("mvhd", v, 0);
				
				mvhd.size=size;
				
				if(v==1) {
					byte[] creation_time=new byte[8];
					fis.read(creation_time);
					mvhd.creation_time=Util.ByteArrayToLong(creation_time);
					byte[] modification_time=new byte[8];
					fis.read(modification_time);
					mvhd.modification_time=Util.ByteArrayToLong(modification_time);
					byte[] duration=new byte[8];
					fis.read(duration);
					mvhd.duration=Util.ByteArrayToLong(duration);
				}else {
					byte[] creation_time=new byte[4];
					fis.read(creation_time);
					mvhd.creation_time=Util.ByteArrayToLong(creation_time);
					byte[] modification_time=new byte[4];
					fis.read(modification_time);
					mvhd.modification_time=Util.ByteArrayToLong(modification_time);
					byte[] duration=new byte[4];
					fis.read(duration);
					mvhd.duration=Util.ByteArrayToLong(duration);
				}
				
				byte[] timescale=new byte[4];
				fis.read(timescale);
				mvhd.timescale=Util.ByteArrayToLong(timescale);
				
				
				byte[] rate=new byte[4];
				fis.read(rate);
				mvhd.rate=Util.ByteArrayToLong(rate);
				
				byte[] volume=new byte[2];
				fis.read(volume);
				mvhd.creation_time=Util.ByteArrayToLong(volume);
				
				fis.skip(70);
				
				byte[] next_track_id=new byte[4];
				fis.read(next_track_id);
				mvhd.next_track_ID=Util.ByteArrayToLong(next_track_id);
				
				System.out.println(mvhd);
			}else {
				System.out.println(size);
				System.out.println(type);
				fis.skip(size-8);
			}
		}
		fis.close();
		System.out.println("finished");

	}

}