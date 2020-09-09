package main;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import boxes.Box;
import boxes.FullBox;

public class Main {
	//읽어들이는거 함수 따로 빼기
	//박스 리스트나 다른걸로 해서 객체 계속 가지고 있기
	//계층 구조 상황에 따라서 다름 -> 어느 박스에 있는지 확인할 플래그 변수 생성?
	public static void main(String[] args) throws Exception{
		File file=new File("BigBuckBunny.mp4");
		if(file.isFile()) {
			System.out.println("파일 존재\n");
		}
		
		ArrayList boxes=new ArrayList();
		ArrayList full_boxes=new ArrayList();
		int box_count=0;
		int full_box_count=0;
		
		
		InputStream fis=new FileInputStream("BigBuckBunny.mp4");
		
		byte[] size_byte=new byte[4];
		byte[] type_byte=new byte[4];
		long size;
		String type;
		
		Calendar cal=Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		
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
				
				boxes.add(ftyp);
				box_count++;
			}else if(type.equals("moov")){
				boxes.MovieBox moov=new boxes.MovieBox("moov");
				moov.size=size;
				System.out.println(moov);
				
				boxes.add(moov);
				box_count++;
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
				
				cal.set(1970, 0, 1, 0, 0, 0);
				cal.add(Calendar.SECOND, -2082844800);
				cal.add(Calendar.SECOND, (int)creation_time);
				mvhd.creation_time=cal.getTime().toString();
				
				cal.set(1970, 0, 1, 0, 0, 0);
				cal.add(Calendar.SECOND, -2082844800);
				cal.add(Calendar.SECOND, (int)modification_time);
				mvhd.modification_time=cal.getTime().toString();
				
				
				System.out.println(mvhd);
				
				full_boxes.add(mvhd);
				full_box_count++;
			}else if(type.equals("free")){
				boxes.FreeSpaceBox free=new boxes.FreeSpaceBox("free");
				free.size=size;
				fis.skip(free.size-8);
				System.out.println(free);
				
				boxes.add(free);
				box_count++;
			}else if(type.equals("skip")){
				boxes.FreeSpaceBox skip=new boxes.FreeSpaceBox("skip");
				skip.size=size;
				fis.skip(skip.size-8);
				System.out.println(skip);
				
				boxes.add(skip);
				box_count++;
			}else if(type.equals("trak")){
				boxes.TrackBox trak=new boxes.TrackBox("trak");
				trak.size=size;
				System.out.println(trak);
				
				boxes.add(trak);
				box_count++;
			}else if(type.equals("tkhd")){
				long creation_time;
				long modification_time;
				
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);

				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				
				boxes.TrackHeaderBox tkhd=new boxes.TrackHeaderBox("tkhd", v, f);
				
				tkhd.size=size;
				
				if(v==1) {
					byte[] creation_time_byte=new byte[8];
					fis.read(creation_time_byte);
					creation_time=Util.ByteArrayToLong(creation_time_byte);
					byte[] modification_time_byte=new byte[8];
					fis.read(modification_time_byte);
					modification_time=Util.ByteArrayToLong(modification_time_byte);
					byte[] track_ID=new byte[4];
					fis.read(track_ID);
					tkhd.track_ID=Util.ByteArrayToLong(track_ID);
					fis.skip(4);
					byte[] duration=new byte[8];
					fis.read(duration);
					tkhd.duration=Util.ByteArrayToLong(duration);
				}else {
					byte[] creation_time_byte=new byte[4];
					fis.read(creation_time_byte);
					creation_time=Util.ByteArrayToLong(creation_time_byte);
					byte[] modification_time_byte=new byte[4];
					fis.read(modification_time_byte);
					modification_time=Util.ByteArrayToLong(modification_time_byte);
					byte[] track_ID=new byte[4];
					fis.read(track_ID);
					tkhd.track_ID=Util.ByteArrayToLong(track_ID);
					fis.skip(4);
					byte[] duration=new byte[4];
					fis.read(duration);
					tkhd.duration=Util.ByteArrayToLong(duration);
				}
				fis.skip(8);
				
				byte[] layer=new byte[2];
				fis.read(layer);
				tkhd.layer=Util.ByteArrayToLong(layer);
				
				byte[] alternate_group=new byte[2];
				fis.read(alternate_group);
				tkhd.alternate_group=Util.ByteArrayToLong(alternate_group);
				
				byte[] volume_1=new byte[1];
				fis.read(volume_1);
				byte[] volume_2=new byte[1];
				fis.read(volume_2);
				tkhd.volume=(double)Util.ByteArrayToLong(volume_1)+((double)Util.ByteArrayToLong(volume_2))*0.1;
				
				fis.skip(38);
				
				byte[] track_width_1=new byte[2];
				fis.read(track_width_1);
				byte[] track_width_2=new byte[2];
				fis.read(track_width_2);
				tkhd.track_width=(double)Util.ByteArrayToLong(track_width_1)+((double)Util.ByteArrayToLong(track_width_2))*0.01;
				
				byte[] track_height_1=new byte[2];
				fis.read(track_height_1);
				byte[] track_height_2=new byte[2];
				fis.read(track_height_2);
				tkhd.track_height=(double)Util.ByteArrayToLong(track_height_1)+((double)Util.ByteArrayToLong(track_height_2))*0.01;

				cal.set(1970, 0, 1, 0, 0, 0);
				cal.add(Calendar.SECOND, -2082844800);
				cal.add(Calendar.SECOND, (int)creation_time);
				tkhd.creation_time=cal.getTime().toString();
				
				cal.set(1970, 0, 1, 0, 0, 0);
				cal.add(Calendar.SECOND, -2082844800);
				cal.add(Calendar.SECOND, (int)modification_time);
				tkhd.modification_time=cal.getTime().toString();
				
				
				System.out.println(tkhd);
				
				full_boxes.add(tkhd);
				box_count++;
			}else if(type.equals("mdia")){
				boxes.MediaBox mdia=new boxes.MediaBox("mdia");
				mdia.size=size;
				System.out.println(mdia);
				
				boxes.add(mdia);
				box_count++;
			}else if(type.equals("udta")){
				boxes.UserDataBox udta=new boxes.UserDataBox("udta");
				udta.size=size;
				System.out.println(udta);
				
				boxes.add(udta);
				box_count++;
			}else if(type.equals("mdhd")){
				long creation_time;
				long modification_time;
				
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				fis.skip(3);
				
				boxes.MediaHeaderBox mdhd=new boxes.MediaHeaderBox("mdhd", v, 0);
				
				mdhd.size=size;
				
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
					mdhd.timescale=Util.ByteArrayToLong(timescale);
					fis.read(duration);
					mdhd.duration=Util.ByteArrayToLong(duration);
				}else {
					byte[] creation_time_byte=new byte[4];
					fis.read(creation_time_byte);
					creation_time=Util.ByteArrayToLong(creation_time_byte);
					byte[] modification_time_byte=new byte[4];
					fis.read(modification_time_byte);
					modification_time=Util.ByteArrayToLong(modification_time_byte);
					byte[] timescale=new byte[4];
					fis.read(timescale);
					mdhd.timescale=Util.ByteArrayToLong(timescale);
					byte[] duration=new byte[4];
					fis.read(duration);
					mdhd.duration=Util.ByteArrayToLong(duration);
				}
				
				byte[] language=new byte[2];
				fis.read(language);
				mdhd.language=Util.ByteArrayToLong(language);
				
				byte[] quality=new byte[2];
				fis.read(quality);
				mdhd.quality=Util.ByteArrayToLong(quality);
				
				
				cal.set(1970, 0, 1, 0, 0, 0);
				cal.add(Calendar.SECOND, -2082844800);
				cal.add(Calendar.SECOND, (int)creation_time);
				mdhd.creation_time=cal.getTime().toString();
				
				cal.set(1970, 0, 1, 0, 0, 0);
				cal.add(Calendar.SECOND, -2082844800);
				cal.add(Calendar.SECOND, (int)modification_time);
				mdhd.modification_time=cal.getTime().toString();
				
				
				System.out.println(mdhd);
				
				full_boxes.add(mdhd);
				full_box_count++;
			}else if(type.equals("hdlr")){
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				fis.skip(3);
				
				boxes.HandlerReferenceBox hdlr=new boxes.HandlerReferenceBox("hdlr", v, 0);
				
				hdlr.size=size;
				
				byte[] component_type_byte=new byte[4];
				fis.read(component_type_byte);
				hdlr.component_type=new String(component_type_byte,StandardCharsets.US_ASCII);
				
				byte[] component_subtype_byte=new byte[4];
				fis.read(component_subtype_byte);
				hdlr.component_subtype=new String(component_subtype_byte,StandardCharsets.US_ASCII);
				
				fis.skip(12);
				
				byte[] component_name_byte=new byte[(int)size-32];
				fis.read(component_name_byte);
				hdlr.component_name=new String(component_name_byte,StandardCharsets.US_ASCII);
				
				System.out.println(hdlr);
				
				full_boxes.add(hdlr);
				full_box_count++;
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