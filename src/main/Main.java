package main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import boxes.SampletoChunkBox;
public class Main {
	//stsd box 하위 구조 구현해야됨
	//skip 왜 했는지 구조 쓰기
	//프린트하는거 클래스 안에서 하게 빼내기
	// 버전에 따른 구현도 클래스 안에서 할 수 있게 빼내기
	public static void main(String[] args) throws Exception{
//		File file=new File("video_960_30.mp4");
//		if(file.isFile()) {
//			System.out.println("파일 존재\n");
//		}
		int depth=0; //계층 구조 확인용
		int which_first_flag=0; //moov 1 mdat 2
		ArrayList<boxes.Box> boxes=new ArrayList<>();
		ArrayList<boxes.FullBox> full_boxes=new ArrayList<>();
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
				ftyp.SetFTYPBox(fis);
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
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				//flag 3바이트
				fis.skip(3);
				boxes.MovieHeaderBox mvhd=new boxes.MovieHeaderBox("mvhd", v, 0);
				mvhd.size=size;
				mvhd.SetMVHDBox(fis, cal);
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
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				boxes.TrackHeaderBox tkhd=new boxes.TrackHeaderBox("tkhd", v, f);
				tkhd.size=size;
				tkhd.SetTKHDBox(fis, cal);
				System.out.println(tkhd);
				full_boxes.add(tkhd);
				full_box_count++;
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
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				//flags set to 0
				fis.skip(3);
				boxes.MediaHeaderBox mdhd=new boxes.MediaHeaderBox("mdhd", v, 0);
				mdhd.size=size;
				mdhd.SetMDHDBox(fis, cal);
				System.out.println(mdhd);
				full_boxes.add(mdhd);
				full_box_count++;
			}else if(type.equals("hdlr")){
				depth++;
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				//Flags
				fis.skip(3);
				boxes.HandlerReferenceBox hdlr=new boxes.HandlerReferenceBox("hdlr", v, 0);
				hdlr.size=size;
				hdlr.SetHDLRBox(fis);
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
				vmhd.SetVMHDBox(fis);
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
				
				dref.SetDREFBox(fis);
				
				full_boxes.add(dref);
				full_box_count++;
				
				for(int i=0;i<depth;i++) {
					tab.append("\t");
				}
				
				System.out.println(dref.toString(tab.toString()));
				
				if(dref.entry_count!=0) {
					dref.SetDREFBox_Table(fis);
					dref.DREFBox_Table_Print(tab.toString());
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
				//version 1 byte flags 3 bytes
				fis.skip(4);
				
				stsd.SetSTSDBox(fis);
				
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
				
				boxes.TimetoSampleBox stts=new boxes.TimetoSampleBox("stts",v,f);
				stts.size=size;
				
				stts.SetSTTSBox(fis);
				
				full_boxes.add(stts);
				full_box_count++;
								
				System.out.println(stts);
				
				if(stts.entry_count!=0) {
					stts.SetSTTSBox_Table(fis);
					stts.STTSBox_Table_Print();
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
				
				stss.SetSTSSBox(fis);
				
				full_boxes.add(stss);
				full_box_count++;
								
				System.out.println(stss);
				
				if(stss.entry_count!=0) {
					stss.SetSTSSBox_Table(fis);
					stss.STSSBox_Table_Print();
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
				
				stsc.SetSTSCBox(fis);
				
				full_boxes.add(stsc);
				full_box_count++;
								
				System.out.println(stsc);
				
				if(stsc.entry_count!=0) {
					stsc.SetSTSCBox_Table(fis);
					stsc.STSCBox_Table_Print();
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
				
				stsz.SetSTSZBox(fis);
								
				System.out.println(stsz);
				
				if(stsz.entry_count!=0) {
					stsz.SetSTSZBox_Table(fis);
					stsz.STSZBox_Table_Print();
				}

				full_boxes.add(stsz);
				full_box_count++;
				
			}else if(type.equals("smhd")){

				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				boxes.SoundMediaHeaderBox smhd=new boxes.SoundMediaHeaderBox("smhd", v, f);
				
				smhd.size=size;
				
				smhd.SetSMHDBox(fis);
				
				System.out.println(smhd);
				
				full_boxes.add(smhd);
				full_box_count++;
			}else if(type.equals("stco")){
				
				byte[] version=new byte[1];
				fis.read(version);
				long v=Util.ByteArrayToLong(version);
				
				byte[] flags=new byte[3];
				fis.read(flags);
				long f=Util.ByteArrayToLong(flags);
				
				boxes.ChunkOffsetBox stco=new boxes.ChunkOffsetBox("stco",v,f);
				stco.size=size;
				
				stco.SetSTCOBox(fis);
				
				System.out.println(stco);
				
				if(stco.entry_count!=0) {
					stco.SetSTCOBox_Table(fis);
					stco.STCOBox_Table_Print();
				}

				full_boxes.add(stco);
				full_box_count++;
			}else if(type.equals("mdat")){
				boxes.MediaDataBox mdat=new boxes.MediaDataBox("mdat");
				mdat.size=size;
				fis.skip(size);
				if(size==1) {
					byte[] largesize=new byte[8];
					fis.read(largesize);
					mdat.largesize=Util.ByteArrayToLong(largesize);
				}
				int sample_index=0;
				int stsc_index=0;
				int chunk_index=0;
				int sample_count=0;
				int stts_index=0;
				int stts_count=0;
				int sample_duration=0;
				long sample_offset=0;
				int video_sample_count=0;
				int stss_index=0;
				int audio_sample_count=0;
				int keyframe_index=0;
				
				if(size==1) {
					System.out.println("mdat\n"+
										"Size: "+mdat.largesize+"\n"
										+"Type: MediaDataBox\n");
				}else {
					System.out.println("mdat\n"+
							"Size: "+mdat.size+"\n"
							+"Type: MediaDataBox\n");
				}
				boxes.SampletoChunkBox stsc=null;
				boxes.SampleSizeBox stsz=null;
				boxes.ChunkOffsetBox stco = null;
				boxes.TimetoSampleBox stts=null;
				boxes.SyncSampleBox stss=null;
				for(int i=0;i<full_box_count;i++) {
					if(full_boxes.get(i).type.equals("vmhd")) { //video
						sample_index=0;
						stsc_index=0;
						chunk_index=0;
						sample_count=0;
						stts_index=0;
						stts_count=0;
						sample_duration=0;
						stss_index=0;
						sample_offset=0;
						System.out.println("\nVideo Sample");
						
						stco=mdat.FindSTCO(i, full_boxes, full_box_count);
						stss=mdat.FindSTSS(i, full_boxes, full_box_count);
						stsc=mdat.FindSTSC(i, full_boxes, full_box_count);
						stsz=mdat.FindSTSZ(i, full_boxes, full_box_count);
						stts=mdat.FindSTTS(i, full_boxes, full_box_count);
						if(stss!=null) {
							keyframe_index=(int)stss.sync_sample_table.get(stss_index).intValue();
						}else {
							keyframe_index=-1;
						}
						sample_count=(int) stsc.table.get(0).samples_per_chunk;
						sample_duration=(int) stts.time_to_sample_table.get(0).sample_duration;
						for(chunk_index=0;chunk_index<stco.entry_count;chunk_index++) {
							if((stsc_index+1)<stsc.entry_count) {
								if(stsc.table.get(stsc_index+1).first_chunk==(chunk_index+1)) {
									sample_count=(int) stsc.table.get(stsc_index+1).samples_per_chunk;
									stsc_index++;
								}
							}
							InputStream sample_stream=new FileInputStream("BigBuckBunny.mp4");
							sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
							sample_offset=stco.chunk_offset_table.get(chunk_index);
							
							
							for(int k=0;k<sample_count;k++) {
								if(stts.time_to_sample_table.get(stts_index).sample_count==stts_count) {
									sample_duration=(int)stts.time_to_sample_table.get(stts_index+1).sample_duration;
									stts_index++;
								}
								boxes.MediaData sample=new boxes.MediaData();
								sample.chunk_number=chunk_index+1;
								sample.duration=sample_duration;
								sample.offset=sample_offset;
								sample.sample_index=sample_index+1;
								sample.size=stsz.sample_size_table.get(sample_index).longValue();
								if(keyframe_index==-1) {
									sample.iskeyframe=1;
								}else if((sample.sample_index==keyframe_index)) {
									sample.iskeyframe=1;
									if(stss.entry_count!=(stss_index+1)) {
										keyframe_index=stss.sync_sample_table.get(++stss_index).intValue();	
									}
								}
								byte[] data=new byte[(int)sample.size];
								sample_stream.read(data);
								sample.data=Util.ByteArrayToHex(data);
								
								mdat.datas.add(sample);
								
								video_sample_count++;
								sample_index++;
								stts_count++;
								sample_stream.close();
							}
						}
						stsc=null;
						stsz=null;
						stco = null;
						stts=null;
						stss=null;
						
//						for(int k=0;k<sample_index;k++) {
//							System.out.println("Sample "+mdat.datas.get(k).sample_index);
//							System.out.println("Offset: "+mdat.datas.get(k).offset+
//									"\tSize: "+mdat.datas.get(k).size+
//									"\tDuration: "+mdat.datas.get(k).duration+
//									"\tData: "+mdat.datas.get(k).data);
//						}
//						
						for(int k=0;k<5;k++) {
							mdat.MDATBox_Datas_Print(k);
						}
					}else if(full_boxes.get(i).type.equals("smhd")) { //audio
						System.out.println("\nAudio Sample");
						sample_index=0;
						stsc_index=0;
						chunk_index=0;
						sample_count=0;
						stts_index=0;
						stts_count=0;
						sample_duration=0;
						sample_offset=0;
						stss_index=0;
						stco=mdat.FindSTCO(i, full_boxes, full_box_count);
						stsc=mdat.FindSTSC(i, full_boxes, full_box_count);
						stsz=mdat.FindSTSZ(i, full_boxes, full_box_count);
						stts=mdat.FindSTTS(i, full_boxes, full_box_count);
						
						sample_count=(int) stsc.table.get(0).samples_per_chunk;
						sample_duration=(int) stts.time_to_sample_table.get(0).sample_duration;
						for(chunk_index=0;chunk_index<stco.entry_count;chunk_index++) {
							if((stsc_index+1)<stsc.entry_count) {
								if(stsc.table.get(stsc_index+1).first_chunk==(chunk_index+1)) {
									sample_count=(int) stsc.table.get(stsc_index+1).samples_per_chunk;
									stsc_index++;
								}
							}
							InputStream sample_stream=new FileInputStream("BigBuckBunny.mp4");
							sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
							sample_offset=stco.chunk_offset_table.get(chunk_index);
							
							for(int k=0;k<sample_count;k++) {
								if(stts.time_to_sample_table.get(stts_index).sample_count==stts_count) {
									sample_duration=(int)stts.time_to_sample_table.get(stts_index+1).sample_duration;
									stts_index++;
								}
								boxes.MediaData sample=new boxes.MediaData();
								sample.chunk_number=chunk_index+1;
								sample.duration=sample_duration;
								sample.offset=sample_offset;
								sample.sample_index=sample_index+1;
								sample.size=stsz.sample_size_table.get(sample_index).longValue();
								
								byte[] data=new byte[(int)sample.size];
								sample_stream.read(data);
								sample.data=Util.ByteArrayToHex(data);
								
								mdat.datas.add(sample);
								sample_index++;
								stts_count++;
								audio_sample_count++;
							}

							sample_stream.close();
						}
						stsc=null;
						stsz=null;
						stco = null;
						stts=null;
						stss=null;

//						for(int k=0;k<sample_index;k++) {
//							System.out.println("Sample "+mdat.datas.get(k).sample_index);
//							System.out.println("Offset: "+mdat.datas.get(k).offset+
//									"\tSize: "+mdat.datas.get(k).size+
//									"\tDuration: "+mdat.datas.get(k).duration+
//									"\tData: "+mdat.datas.get(k).data);
//						}
//						
						for(int k=video_sample_count;k<video_sample_count+5;k++) {
							mdat.MDATBox_Datas_Print(k);
						}
					}
					

				}
				boxes.add(mdat);
				box_count++;
				
				depth=0;
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