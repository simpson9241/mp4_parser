package main;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

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
				long creation_time;
				long modification_time;
				
				
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				fis.skip(3);
				
				boxes.MovieHeaderBox mvhd=new boxes.MovieHeaderBox("mvhd", v, 0);
				
				mvhd.size=size;
				
				if(v==1) {
					byte[] creation_time_byte=new byte[8];
					fis.read(creation_time_byte);
					creation_time=Util.ByteArrayToLong(creation_time_byte);
					byte[] modification_time_byte=new byte[8];
					fis.read(modification_time_byte);
					modification_time=Util.ByteArrayToLong(modification_time_byte);
					byte[] duration=new byte[8];
					byte[] timescale=new byte[4];
					fis.read(timescale);
					mvhd.timescale=Util.ByteArrayToLong(timescale);
					fis.read(duration);
					mvhd.duration=Util.ByteArrayToLong(duration);
				}else {
					byte[] creation_time_byte=new byte[4];
					fis.read(creation_time_byte);
					creation_time=Util.ByteArrayToLong(creation_time_byte);
					byte[] modification_time_byte=new byte[4];
					fis.read(modification_time_byte);
					modification_time=Util.ByteArrayToLong(modification_time_byte);
					byte[] timescale=new byte[4];
					fis.read(timescale);
					mvhd.timescale=Util.ByteArrayToLong(timescale);
					byte[] duration=new byte[4];
					fis.read(duration);
					mvhd.duration=Util.ByteArrayToLong(duration);
				}
				
				
				byte[] rate_1=new byte[2];
				fis.read(rate_1);
				byte[] rate_2=new byte[2];
				fis.read(rate_2);
				mvhd.rate=(double)Util.ByteArrayToLong(rate_1)+((double)Util.ByteArrayToLong(rate_2))*0.01;
				
				byte[] volume_1=new byte[1];
				fis.read(volume_1);
				byte[] volume_2=new byte[1];
				fis.read(volume_2);
				mvhd.volume=(double)Util.ByteArrayToLong(volume_1)+((double)Util.ByteArrayToLong(volume_2))*0.1;
				
				fis.skip(70);
				
				byte[] next_track_id=new byte[4];
				fis.read(next_track_id);
				mvhd.next_track_ID=Util.ByteArrayToLong(next_track_id);
				
				Calendar cal=Calendar.getInstance();
				cal.set(1904, 0, 1,0,0,0);
				cal.add(Calendar.SECOND, (int)creation_time);
				mvhd.creation_time=cal.getTime().toString();
				
				cal.set(1904, 0, 1,0,0,0);
				cal.add(Calendar.SECOND, (int)modification_time);
				mvhd.modification_time=cal.getTime().toString();
				
				
				System.out.println(mvhd);
			}else if(type.equals("free")){
				boxes.FreeSpaceBox free=new boxes.FreeSpaceBox("free");
				free.size=size;
				fis.skip(free.size-8);
				System.out.println(free);
			}else if(type.equals("skip")){
				boxes.FreeSpaceBox skip=new boxes.FreeSpaceBox("skip");
				skip.size=size;
				fis.skip(skip.size-8);
				System.out.println(skip);
			}else {
				System.out.println(size);
				System.out.println(type);
				fis.skip(size-8);
			}
		}
		fis.close();
		System.out.println("\nfinished");

	}

}