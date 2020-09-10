package main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
public class Main {
	//읽어들이는거 함수 따로 빼기
	//박스 리스트나 다른걸로 해서 객체 계속 가지고 있기
	//계층 구조 상황에 따라서 다름 -> 어느 박스에 있는지 확인할 플래그 변수 생성?
	//변수의 값에 따라 for i는 변수의 값까지 반복해서 \t로 이루어진 스트링 생성
	//\t의 자리에 스트링 넣어서 출력
	//sample table 300개 넘어가면 그 이상으로 출력 x 안하게 조정
	public static void main(String[] args) throws Exception{
		File file=new File("BigBuckBunny.mp4");
//		if(file.isFile()) {
//			System.out.println("파일 존재\n");
//		}
		
		int depth=0;
		
		ArrayList boxes=new ArrayList();
		ArrayList full_boxes=new ArrayList();
		int box_count=0;
		int full_box_count=0;
		
		StringBuilder tab=new StringBuilder();
		
		
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
				
				depth=0;
			}else if(type.equals("moov")){
				boxes.MovieBox moov=new boxes.MovieBox("moov");
				moov.size=size;
				System.out.println(moov);
				
				boxes.add(moov);
				box_count++;
				
				depth=0;
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
				
				depth=1;
			}else if(type.equals("free")){
				
				
				boxes.FreeSpaceBox free=new boxes.FreeSpaceBox("free");
				free.size=size;
				fis.skip(free.size-8);
							
				for(int i=0;i<depth;i++) {
					tab.append("\t");
				}
				
				
				System.out.println(free.toString(tab.toString()));
				tab.delete(0, tab.length());
				
				boxes.add(free);
				box_count++;
				
			}else if(type.equals("skip")){
				boxes.FreeSpaceBox skip=new boxes.FreeSpaceBox("skip");
				skip.size=size;
				fis.skip(skip.size-8);
				
				for(int i=0;i<depth;i++) {
					tab.append("\t");
				}
				
				
				System.out.println(skip.toString(tab.toString()));
				tab.delete(0, tab.length());
				
				
				boxes.add(skip);
				box_count++;
			}else if(type.equals("trak")){
				boxes.TrackBox trak=new boxes.TrackBox("trak");
				trak.size=size;
				System.out.println(trak);
				
				boxes.add(trak);
				box_count++;
				
				depth=1;
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
				
				depth=2;
			}else if(type.equals("mdia")){
				boxes.MediaBox mdia=new boxes.MediaBox("mdia");
				mdia.size=size;
				System.out.println(mdia);
				
				boxes.add(mdia);
				box_count++;
				
				depth=2;
			}else if(type.equals("udta")){
				depth++;
				boxes.UserDataBox udta=new boxes.UserDataBox("udta");
				udta.size=size;
				for(int i=0;i<depth;i++) {
					tab.append("\t");
				}
				
				System.out.println(udta.toString(tab.toString()));

				tab.delete(0, tab.length());
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
				depth++;
				
				
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
				
				for(int i=0;i<depth;i++) {
					tab.append("\t");
				}
				
				
				System.out.println(hdlr.toString(tab.toString()));
				tab.delete(0, tab.length());
				
				full_boxes.add(hdlr);
				full_box_count++;
				
				depth--;
			}else if(type.equals("minf")){
				boxes.MediaInformationBox minf=new boxes.MediaInformationBox("minf");
				minf.size=size;
				System.out.println(minf);
				
				boxes.add(minf);
				box_count++;
				
				depth=3;
			}else if(type.equals("vmhd")){

				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				boxes.VideoMediaHeaderBox vmhd=new boxes.VideoMediaHeaderBox("vmhd", v, f);
				
				vmhd.size=size;
				
				byte[] graphics_mode=new byte[2];
				fis.read(graphics_mode);
				vmhd.graphics_mode=(int)Util.ByteArrayToLong(graphics_mode);
				
				byte[] opcolor_r=new byte[2];
				fis.read(opcolor_r);
				vmhd.opcolor_r=(int)Util.ByteArrayToLong(opcolor_r);
				
				byte[] opcolor_g=new byte[2];
				fis.read(opcolor_g);
				vmhd.opcolor_g=(int)Util.ByteArrayToLong(opcolor_g);
				
				byte[] opcolor_b=new byte[2];
				fis.read(opcolor_b);
				vmhd.opcolor_b=(int)Util.ByteArrayToLong(opcolor_b);
				
				System.out.println(vmhd);
				
				full_boxes.add(vmhd);
				full_box_count++;
			}else if(type.equals("dinf")){
				depth++;
				boxes.DataInformationBox dinf=new boxes.DataInformationBox("dinf");
				dinf.size=size;
				
				boxes.add(dinf);
				box_count++;
				
				for(int i=0;i<depth;i++) {
					tab.append("\t");
				}
				
				
				System.out.println(dinf.toString(tab.toString()));
				tab.delete(0, tab.length());
				
				
			}else if(type.equals("dref")){
				depth++;
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				boxes.DataReferenceBox dref=new boxes.DataReferenceBox("dref",v,f);
				dref.size=size;
				
				byte[] number_of_entries=new byte[4];
				fis.read(number_of_entries);
				dref.number_of_entries=Util.ByteArrayToLong(number_of_entries);
				
				full_boxes.add(dref);
				full_box_count++;
				
				for(int i=0;i<depth;i++) {
					tab.append("\t");
				}
				
				System.out.println(dref.toString(tab.toString()));
				
				if(dref.number_of_entries!=0) {
					for(int i=0;i<dref.number_of_entries;i++) {
						boxes.DataReference datareference=new boxes.DataReference();
						byte[] reference_size=new byte[4];
						fis.read(reference_size);
						datareference.size=Util.ByteArrayToLong(reference_size);
						
						byte[] reference_type=new byte[4];
						fis.read(reference_type);
						datareference.type=new String(reference_type,StandardCharsets.US_ASCII);
						
						byte[] reference_version=new byte[1];
						fis.read(reference_version);
						datareference.version=Util.ByteArrayToLong(reference_version);
						
						byte[] reference_flags=new byte[3];
						fis.read(reference_flags);
						datareference.flags=Util.ByteArrayToLong(reference_flags);
						
						byte[] reference_data=new byte[(int)datareference.size-12];
						fis.read(reference_data);
						datareference.data=new String(reference_data,StandardCharsets.US_ASCII);
						dref.data_references.add(datareference);
					}
				}
				
				if(dref.number_of_entries!=0) {
					System.out.println(tab+"Data References");
					for(int i=0;i<dref.number_of_entries;i++) {
						System.out.println(tab+"\tSize: "+dref.data_references.get(i).size+"\n"+
											tab+"\tType: "+dref.data_references.get(i).type+"\n"+
											tab+"\tVersion: "+dref.data_references.get(i).version+"\n"+
											tab+"\tFlags: "+dref.data_references.get(i).flags+"\n"+
											tab+"\tData: "+dref.data_references.get(i).data+"\n");
					}
				}
				

				tab.delete(0, tab.length());
				depth--;
				
			}else if(type.equals("stbl")){
				boxes.SampleTableBox stbl=new boxes.SampleTableBox("stbl");
				stbl.size=size;
				System.out.println(stbl);
				
				boxes.add(stbl);
				box_count++;
				
				depth=4;
			}else if(type.equals("stsd")){
				boxes.SampleDescriptionBox stsd=new boxes.SampleDescriptionBox("stsd",0,0);
				stsd.size=size;
				
				fis.skip(4);
				
				byte[] number_of_entries=new byte[4];
				fis.read(number_of_entries);
				stsd.number_of_entries=Util.ByteArrayToLong(number_of_entries);
				
				System.out.println(stsd);
				
				full_boxes.add(stsd);
				full_box_count++;
				
			}else if(type.equals("stts")){
				
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				boxes.SampletoTableBox stts=new boxes.SampletoTableBox("stts",v,f);
				stts.size=size;
				
				byte[] number_of_entries=new byte[4];
				fis.read(number_of_entries);
				stts.number_of_entries=Util.ByteArrayToLong(number_of_entries);
				
				full_boxes.add(stts);
				full_box_count++;
								
				System.out.println(stts);
				
				if(stts.number_of_entries!=0) {
					for(int i=0;i<stts.number_of_entries;i++) {
						boxes.TimetoSampleTable table=new boxes.TimetoSampleTable();

						byte[] sample_count=new byte[4];
						fis.read(sample_count);
						table.sample_count=Util.ByteArrayToLong(sample_count);
						
						byte[] sample_duration=new byte[4];
						fis.read(sample_duration);
						table.sample_duration=Util.ByteArrayToLong(sample_duration);
						
						stts.time_to_sample_table.add(table);
					}
				}
				
				if(stts.number_of_entries!=0) {
					System.out.println("\t\t\t\t\t\tTime-to-sample Table\n");
					for(int i=0;i<stts.number_of_entries;i++) {
						System.out.println("\t\t\t\t\t\tSample Count: "+stts.time_to_sample_table.get(i).sample_count+"\tSample Duration: "+stts.time_to_sample_table.get(i).sample_duration);
					}
				}

				
			}else if(type.equals("stss")){
				
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				boxes.SyncSampleBox stss=new boxes.SyncSampleBox("stss",v,f);
				stss.size=size;
				
				byte[] number_of_entries=new byte[4];
				fis.read(number_of_entries);
				stss.entry_count=Util.ByteArrayToLong(number_of_entries);
				
				full_boxes.add(stss);
				full_box_count++;
								
				System.out.println(stss);
				
				if(stss.entry_count!=0) {
					for(int i=0;i<stss.entry_count;i++) {
						
						byte[] sample=new byte[4];
						fis.read(sample);
						stss.sync_sample_table.add(Util.ByteArrayToLong(sample));
					}
				}
				
				if(stss.entry_count!=0) {
					System.out.println("\t\t\t\t\t\tSync Sample Table\n");
					for(int i=0;i<stss.entry_count;i++) {
						System.out.println("\t\t\t\t\t\tSample "+i+": "+stss.sync_sample_table.get(i).longValue());
					}
				}

				
			}else if(type.equals("stsc")){
				
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				boxes.SampletoChunkBox stsc=new boxes.SampletoChunkBox("stsc",v,f);
				stsc.size=size;
				
				byte[] entry_count=new byte[4];
				fis.read(entry_count);
				stsc.entry_count=Util.ByteArrayToLong(entry_count);
				
				full_boxes.add(stsc);
				full_box_count++;
								
				System.out.println(stsc);
				
				if(stsc.entry_count!=0) {
					for(int i=0;i<stsc.entry_count;i++) {
						boxes.SampletoChunkTable table=new boxes.SampletoChunkTable();

						byte[] first_chunk=new byte[4];
						fis.read(first_chunk);
						table.first_chunk=Util.ByteArrayToLong(first_chunk);
						
						byte[] samples_per_chunk=new byte[4];
						fis.read(samples_per_chunk);
						table.samples_per_chunk=Util.ByteArrayToLong(samples_per_chunk);
						
						byte[] sample_description_id=new byte[4];
						fis.read(sample_description_id);
						table.sample_description_id=Util.ByteArrayToLong(sample_description_id);
						
						stsc.table.add(table);
					}
				}
				
				if(stsc.entry_count!=0) {
					System.out.println("\t\t\t\t\t\tSample-to-Chunk Table\n");
					for(int i=0;i<stsc.entry_count;i++) {
						System.out.println("\t\t\t\t\t\tFirst Chunk: "+stsc.table.get(i).first_chunk+"\tSamples per Chunk: "+stsc.table.get(i).samples_per_chunk+"\tSample Description ID: "+stsc.table.get(i).sample_description_id);
					}
				}
				
			}else if(type.equals("stsz")){
				
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				boxes.SampleSizeBox stsz=new boxes.SampleSizeBox("stsz",v,f);
				stsz.size=size;
				
				byte[] sample_size=new byte[4];
				fis.read(sample_size);
				stsz.sample_size=Util.ByteArrayToLong(sample_size);
				
				byte[] entry_count=new byte[4];
				fis.read(entry_count);
				stsz.entry_count=Util.ByteArrayToLong(entry_count);
				
				full_boxes.add(stsz);
				full_box_count++;
								
				System.out.println(stsz);
				
				if(stsz.entry_count!=0) {
					for(int i=0;i<stsz.entry_count;i++) {
						
						byte[] sample=new byte[4];
						fis.read(sample);
						stsz.sample_size_table.add(Util.ByteArrayToLong(sample));
					}
				}
				
				if(stsz.entry_count!=0) {
					System.out.println("\t\t\t\t\t\tSample Size Table\n");
					for(int i=0;i<stsz.entry_count;i++) {
						System.out.println("\t\t\t\t\t\tSample "+i+": "+stsz.sample_size_table.get(i).longValue());
					}
				}
				
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