package main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import org.apache.commons.io.input.*;

import boxes.TrackBox;

public class Main {
	// stsd box 하위 구조 구현해야됨
	public static void main(String[] args) throws Exception {
//		File file=new File("video_960_30.mp4");
//		if(file.isFile()) {
//			System.out.println("파일 존재\n");
//		}
		long stream_position=0;
		int mdat_position_flag=0;//먼저오면 0 나중에 오면 1
		int ismdatafter=0;
		int fragment_flag=0; //moof가 있으면 fmp4 -> 1
		int depth = 0; // 계층 구조 확인용
		int which_first_flag = 0; // moov 1 mdat 2
		ArrayList<boxes.Box> boxes = new ArrayList<>();
		ArrayList<boxes.FullBox> full_boxes = new ArrayList<>();
		int box_count = 0;
		int full_box_count = 0;
		StringBuilder tab = new StringBuilder();
		InputStream fis = new FileInputStream("output.mp4");
		byte[] size_byte = new byte[4];
		byte[] type_byte = new byte[4];
		byte[] mdat_storage;
		long size;
		String type;
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		long mdat_size=0;
		long mdat_largesize=0;
		while (fis.read(size_byte) != -1) {
			fis.read(type_byte);
			size = Util.ByteArrayToLong(size_byte);
			type = new String(type_byte, StandardCharsets.US_ASCII);
			if (type.equals("ftyp")) {
				boxes.FileTypeBox ftyp = new boxes.FileTypeBox("ftyp");
				ftyp.struct_depth=0;
				ftyp.start_position=stream_position;
				ftyp.end_position=stream_position+size;
				stream_position+=size;
				ftyp.size = size;
				ftyp.SetFTYPBox(fis);
				System.out.println(ftyp);
				boxes.add(ftyp);
				box_count++;
				depth = 0;
			} else if (type.equals("moov")) {
				boxes.MovieBox moov = new boxes.MovieBox("moov");
				moov.struct_depth=0;
				moov.size = size;
				System.out.println(moov);
				boxes.add(moov);
				box_count++;
				depth = 0;
				mdat_position_flag=1;
				moov.start_position=stream_position;
				moov.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("mvhd")) {
				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);
				// flag 3바이트
				fis.skip(3);
				boxes.MovieHeaderBox mvhd = new boxes.MovieHeaderBox("mvhd", v, 0);
				mvhd.struct_depth=1;
				mvhd.size = size;
				mvhd.SetMVHDBox(fis, cal);
				System.out.println(mvhd);
				full_boxes.add(mvhd);
				full_box_count++;
				depth = 1;
				mvhd.start_position=stream_position;
				mvhd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("free")) {
				boxes.FreeSpaceBox free = new boxes.FreeSpaceBox("free");
				if(box_count>0) {
					if(full_box_count>0) {
						if(boxes.get(box_count-1).end_position>full_boxes.get(full_box_count-1).end_position) {
							if(full_boxes.get(full_box_count-1).end_position>stream_position) {
								free.struct_depth=full_boxes.get(full_box_count-1).struct_depth+1;
							}else {
								free.struct_depth=full_boxes.get(full_box_count-1).struct_depth;
							}
						}else {
							if(boxes.get(box_count-1).end_position>stream_position) {
								free.struct_depth=boxes.get(box_count-1).struct_depth+1;
							}else {
								free.struct_depth=boxes.get(box_count-1).struct_depth;
							}
						}
					}else {
						if(boxes.get(box_count-1).end_position>stream_position) {
							free.struct_depth=boxes.get(box_count-1).struct_depth+1;
						}else {
							free.struct_depth=boxes.get(box_count-1).struct_depth;
						}
					}
				}else {
					free.struct_depth=0;
				}
				
				
				free.size = size;
				fis.skip(free.size - 8);
				for (int i = 0; i < free.struct_depth; i++) {
					tab.append("\t");
				}
				System.out.println(free.toString(tab.toString()));
				tab.delete(0, tab.length());
				boxes.add(free);
				box_count++;
				free.start_position=stream_position;
				free.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("skip")) {
				boxes.FreeSpaceBox skip = new boxes.FreeSpaceBox("skip");
				
				if(box_count>0) {
					if(full_box_count>0) {
						if(boxes.get(box_count-1).end_position>full_boxes.get(full_box_count-1).end_position) {
							if(full_boxes.get(full_box_count-1).end_position>stream_position) {
								skip.struct_depth=full_boxes.get(full_box_count-1).struct_depth+1;
							}else {
								skip.struct_depth=full_boxes.get(full_box_count-1).struct_depth;
							}
						}else {
							if(boxes.get(box_count-1).end_position>stream_position) {
								skip.struct_depth=boxes.get(box_count-1).struct_depth+1;
							}else {
								skip.struct_depth=boxes.get(box_count-1).struct_depth;
							}
						}
					}else {
						if(boxes.get(box_count-1).end_position>stream_position) {
							skip.struct_depth=boxes.get(box_count-1).struct_depth+1;
						}else {
							skip.struct_depth=boxes.get(box_count-1).struct_depth;
						}
					}
				}else {
					skip.struct_depth=0;
				}
				
				skip.size = size;
				fis.skip(skip.size - 8);
				for (int i = 0; i < skip.struct_depth; i++) {
					tab.append("\t");
				}
				System.out.println(skip.toString(tab.toString()));
				tab.delete(0, tab.length());
				boxes.add(skip);
				box_count++;
				skip.start_position=stream_position;
				skip.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("trak")) {
				boxes.TrackBox trak = new boxes.TrackBox("trak");
				trak.size = size;
				System.out.println(trak);

				boxes.add(trak);
				box_count++;

				trak.struct_depth= 1;
				trak.start_position=stream_position;
				trak.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("tkhd")) {
				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);
				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);
				boxes.TrackHeaderBox tkhd = new boxes.TrackHeaderBox("tkhd", v, f);
				tkhd.size = size;
				tkhd.SetTKHDBox(fis, cal);
				System.out.println(tkhd);
				full_boxes.add(tkhd);
				full_box_count++;
				tkhd.struct_depth = 2;
				tkhd.start_position=stream_position;
				tkhd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("mdia")) {
				boxes.MediaBox mdia = new boxes.MediaBox("mdia");
				mdia.size = size;
				System.out.println(mdia);
				boxes.add(mdia);
				box_count++;
				mdia.struct_depth = 2;
				mdia.start_position=stream_position;
				mdia.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("udta")) {
				
				boxes.UserDataBox udta = new boxes.UserDataBox("udta");
				udta.size = size;
				
				for(int i=box_count-1;i>=0;i--) {
					if(boxes.get(i).type.equals("trak")) {
						if(boxes.get(i).end_position>stream_position) {
							udta.struct_depth=boxes.get(i).struct_depth+1;
							break;
						}else{
							udta.struct_depth=1;
							break;
						}
					}
				}
				for (int i = 0; i < udta.struct_depth; i++) {
					tab.append("\t");
				}
				System.out.println(udta.toString(tab.toString()));
				tab.delete(0, tab.length());
				boxes.add(udta);
				box_count++;
				udta.start_position=stream_position;
				udta.end_position=stream_position+size;
				stream_position+=8;
				
			} else if (type.equals("mdhd")) {
				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);
				// flags set to 0
				fis.skip(3);
				boxes.MediaHeaderBox mdhd = new boxes.MediaHeaderBox("mdhd", v, 0);
				mdhd.size = size;
				mdhd.SetMDHDBox(fis, cal);
				System.out.println(mdhd);
				full_boxes.add(mdhd);
				full_box_count++;
				mdhd.start_position=stream_position;
				mdhd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("hdlr")) {
				depth++;
				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);
				// Flags
				fis.skip(3);
				boxes.HandlerReferenceBox hdlr = new boxes.HandlerReferenceBox("hdlr", v, 0);
				
				for(int i=full_box_count-1;i>=0;i--) {
					if(full_boxes.get(i).type.equals("meta")) {
						if(full_boxes.get(i).end_position>stream_position) {
							hdlr.struct_depth=full_boxes.get(i).struct_depth+1;
							break;
						}
					}
				}
				
				for(int i=box_count-1;i>=0;i--) {
					if(boxes.get(i).type.equals("mdia")) {
						if(boxes.get(i).end_position>stream_position) {
							hdlr.struct_depth=3;
							break;
						}
					}else if(boxes.get(i).type.equals("minf")){
						if(boxes.get(i).end_position>stream_position) {
							hdlr.struct_depth=4;
							break;
						}
					}
				}
				
				hdlr.size = size;
				hdlr.SetHDLRBox(fis);
				for (int i = 0; i < hdlr.struct_depth; i++) {
					tab.append("\t");
				}
				System.out.println(hdlr.toString(tab.toString()));
				tab.delete(0, tab.length());
				full_boxes.add(hdlr);
				full_box_count++;
				depth--;
				hdlr.start_position=stream_position;
				hdlr.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("minf")) {
				boxes.MediaInformationBox minf = new boxes.MediaInformationBox("minf");
				minf.size = size;
				System.out.println(minf);
				boxes.add(minf);
				box_count++;
				depth = 3;
				minf.start_position=stream_position;
				minf.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("vmhd")) {
				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);
				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);
				boxes.VideoMediaHeaderBox vmhd = new boxes.VideoMediaHeaderBox("vmhd", v, f);
				vmhd.size = size;
				vmhd.SetVMHDBox(fis);
				System.out.println(vmhd);
				full_boxes.add(vmhd);
				full_box_count++;
				vmhd.start_position=stream_position;
				vmhd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("dinf")) {
				depth++;
				boxes.DataInformationBox dinf = new boxes.DataInformationBox("dinf");
				dinf.size = size;

				boxes.add(dinf);
				box_count++;
				
				for(int i=full_box_count-1;i>=0;i--) {
					if(full_boxes.get(i).type.equals("meta")) {
						if(full_boxes.get(i).end_position>stream_position) {
							dinf.struct_depth=full_boxes.get(i).struct_depth+1;
							break;
						}
					}
				}
				
				for(int i=box_count-1;i>=0;i--) {
					if(boxes.get(i).type.equals("minf")) {
						if(boxes.get(i).end_position>stream_position) {
							dinf.struct_depth=4;
							break;
						}
					}
				}
				
				
				
				for (int i = 0; i < dinf.struct_depth; i++) {
					tab.append("\t");
				}
				System.out.println(dinf.toString(tab.toString()));
				tab.delete(0, tab.length());
				dinf.start_position=stream_position;
				dinf.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("dref")) {
				
				
				
				
				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);

				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);

				boxes.DataReferenceBox dref = new boxes.DataReferenceBox("dref", v, f);
				dref.size = size;

				dref.SetDREFBox(fis);

				full_boxes.add(dref);
				full_box_count++;
				
				for(int i=box_count-1;i>=0;i--) {
					if(boxes.get(i).type.equals("dinf")) {
						if(boxes.get(i).end_position>stream_position) {
							dref.struct_depth=boxes.get(i).struct_depth+1;
							break;
						}
					}
				}
				
				for (int i = 0; i < dref.struct_depth; i++) {
					tab.append("\t");
				}

				System.out.println(dref.toString(tab.toString()));

				if (dref.entry_count != 0) {
					dref.SetDREFBox_Table(fis);
					dref.DREFBox_Table_Print(tab.toString());
				}
				tab.delete(0, tab.length());

				dref.start_position=stream_position;
				dref.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("stbl")) {
				boxes.SampleTableBox stbl = new boxes.SampleTableBox("stbl");
				stbl.size = size;
				System.out.println(stbl);

				boxes.add(stbl);
				box_count++;

				depth = 4;
				
				stbl.start_position=stream_position;
				stbl.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("stsd")) {
				boxes.SampleDescriptionBox stsd = new boxes.SampleDescriptionBox("stsd", 0, 0);
				stsd.size = size;
				// version 1 byte flags 3 bytes
				fis.skip(4);

				stsd.SetSTSDBox(fis);

				System.out.println(stsd);

				full_boxes.add(stsd);
				full_box_count++;

				stsd.start_position=stream_position;
				stsd.end_position=stream_position+size;
				stream_position+=size;
			} else if(type.equals("avc1")){
				boxes.AVC1Box avc1=new boxes.AVC1Box();
				avc1.sample_description_size=size;
				avc1.data_format=type;
				avc1.SetAVC1Box(fis);
				System.out.println(avc1);
			}else if(type.equals("avcC")){
				//ISO/IEC 14496-15 18페이지
				boxes.AVCCBox avcc=new boxes.AVCCBox();
				avcc.size=size;
				avcc.type=type;
				avcc.SetAVCCBox(fis);
				System.out.println(avcc);
			}else if(type.equals("mp4a")){
				boxes.MP4ABox mp4a=new boxes.MP4ABox();
				mp4a.sample_description_size=size;
				mp4a.data_format=type;
				mp4a.SetMP4ABox(fis);
				System.out.println(mp4a);
			}else if (type.equals("stts")) {

				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);

				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);

				boxes.TimetoSampleBox stts = new boxes.TimetoSampleBox("stts", v, f);
				stts.size = size;

				stts.SetSTTSBox(fis);

				full_boxes.add(stts);
				full_box_count++;

				System.out.println(stts);

				if (stts.entry_count != 0) {
					stts.SetSTTSBox_Table(fis);
					stts.STTSBox_Table_Print();
				}
				stts.start_position=stream_position;
				stts.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("stss")) {

				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);

				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);

				boxes.SyncSampleBox stss = new boxes.SyncSampleBox("stss", v, f);
				stss.size = size;

				stss.SetSTSSBox(fis);

				full_boxes.add(stss);
				full_box_count++;

				System.out.println(stss);

				if (stss.entry_count != 0) {
					stss.SetSTSSBox_Table(fis);
					stss.STSSBox_Table_Print();
				}
				stss.start_position=stream_position;
				stss.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("stsc")) {

				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);

				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);

				boxes.SampletoChunkBox stsc = new boxes.SampletoChunkBox("stsc", v, f);
				stsc.size = size;

				stsc.SetSTSCBox(fis);

				full_boxes.add(stsc);
				full_box_count++;

				System.out.println(stsc);

				if (stsc.entry_count != 0) {
					stsc.SetSTSCBox_Table(fis);
					stsc.STSCBox_Table_Print();
				}
				
				stsc.start_position=stream_position;
				stsc.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("stsz")) {

				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);

				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);

				boxes.SampleSizeBox stsz = new boxes.SampleSizeBox("stsz", v, f);
				stsz.size = size;

				stsz.SetSTSZBox(fis);

				System.out.println(stsz);

				if (stsz.entry_count != 0) {
					stsz.SetSTSZBox_Table(fis);
					stsz.STSZBox_Table_Print();
				}

				full_boxes.add(stsz);
				full_box_count++;

				stsz.start_position=stream_position;
				stsz.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("smhd")) {

				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);

				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);

				boxes.SoundMediaHeaderBox smhd = new boxes.SoundMediaHeaderBox("smhd", v, f);

				smhd.size = size;

				smhd.SetSMHDBox(fis);

				System.out.println(smhd);

				full_boxes.add(smhd);
				full_box_count++;
				
				smhd.start_position=stream_position;
				smhd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("stco")) {

				byte[] version = new byte[1];
				fis.read(version);
				long v = Util.ByteArrayToLong(version);

				byte[] flags = new byte[3];
				fis.read(flags);
				long f = Util.ByteArrayToLong(flags);

				boxes.ChunkOffsetBox stco = new boxes.ChunkOffsetBox("stco", v, f);
				stco.size = size;

				stco.SetSTCOBox(fis);

				System.out.println(stco);

				if (stco.entry_count != 0) {
					stco.SetSTCOBox_Table(fis);
					stco.STCOBox_Table_Print();
				}

				full_boxes.add(stco);
				full_box_count++;
				
				stco.start_position=stream_position;
				stco.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("mdat")&&(fragment_flag==0)&&(mdat_position_flag==1)) {
				boxes.MediaDataBox mdat = new boxes.MediaDataBox("mdat");
				mdat.size = size;
				fis.skip(size-8);
				if (size == 1) {
					byte[] largesize = new byte[8];
					fis.read(largesize);
					mdat.largesize = Util.ByteArrayToLong(largesize);
				}
				int sample_index = -1;
				int stsc_index = 0;
				int chunk_index = 0;
				int sample_count = 0;
				int stts_index = 0;
				int stts_count = 0;
				int sample_duration = 0;
				long sample_offset = 0;
				int video_sample_count = 0;
				int stss_index = 0;
				int audio_sample_count = 0;
				int keyframe_index = 0;

				if (size == 1) {
					System.out.println("mdat\n" + "Size: " + mdat.largesize + "\n" + "Type: MediaDataBox\n");
				} else {
					System.out.println("mdat\n" + "Size: " + mdat.size + "\n" + "Type: MediaDataBox\n");
				}
				boxes.SampletoChunkBox stsc = null;
				boxes.SampleSizeBox stsz = null;
				boxes.ChunkOffsetBox stco = null;
				boxes.TimetoSampleBox stts = null;
				boxes.SyncSampleBox stss = null;
				for (int i = 0; i < full_box_count; i++) {
					if (full_boxes.get(i).type.equals("vmhd")) { // video
						if(sample_index==-1) {

							sample_index = 0;	
						}
						stsc_index = 0;
						chunk_index = 0;
						sample_count = 0;
						stts_index = 0;
						stts_count = 0;
						sample_duration = 0;
						stss_index = 0;
						sample_offset = 0;
						System.out.println("\nVideo Sample");

						stco = mdat.FindSTCO(i, full_boxes, full_box_count);
						stss = mdat.FindSTSS(i, full_boxes, full_box_count);
						stsc = mdat.FindSTSC(i, full_boxes, full_box_count);
						stsz = mdat.FindSTSZ(i, full_boxes, full_box_count);
						stts = mdat.FindSTTS(i, full_boxes, full_box_count);
						if (stss != null) {
							keyframe_index = (int) stss.sync_sample_table.get(stss_index).intValue();
						} else {
							keyframe_index = -1;
						}
						sample_count = (int) stsc.table.get(0).samples_per_chunk;
						sample_duration = (int) stts.time_to_sample_table.get(0).sample_duration;
						for (chunk_index = 0; chunk_index < stco.entry_count; chunk_index++) {
							if ((stsc_index + 1) < stsc.entry_count) {
								if (stsc.table.get(stsc_index + 1).first_chunk == (chunk_index + 1)) {
									sample_count = (int) stsc.table.get(stsc_index + 1).samples_per_chunk;
									stsc_index++;
								}
							}
							InputStream sample_stream = new FileInputStream("output.mp4");
							sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
							sample_offset = stco.chunk_offset_table.get(chunk_index);

							for (int k = 0; k < sample_count; k++) {
								if (stts.time_to_sample_table.get(stts_index).sample_count == stts_count) {
									sample_duration = (int) stts.time_to_sample_table
											.get(stts_index + 1).sample_duration;
									stts_index++;
								}
								boxes.MediaData sample = new boxes.MediaData();
								sample.chunk_number = chunk_index + 1;
								sample.duration = sample_duration;
								if(k>0) {
									sample.offset = mdat.datas.get(sample_index-1).offset+mdat.datas.get(sample_index-1).size;
								}else {
									sample.offset = sample_offset;
								}
								sample.sample_index = sample_index + 1;
								sample.size = stsz.sample_size_table.get(video_sample_count).longValue();
								if (keyframe_index == -1) {
									sample.iskeyframe = 1;
								} else if ((sample.sample_index == keyframe_index)) {
									sample.iskeyframe = 1;
									if (stss.entry_count != (stss_index + 1)) {
										keyframe_index = stss.sync_sample_table.get(++stss_index).intValue();
									}
								}
								byte[] data = new byte[(int) sample.size];
								sample_stream.read(data);
								sample.data = Util.ByteArrayToHex(data);

								mdat.datas.add(sample);

								video_sample_count++;
								sample_index++;
								stts_count++;
							}
							sample_stream.close();
						}
						stsc = null;
						stsz = null;
						stco = null;
						stts = null;
						stss = null;

//						for(int k=0;k<sample_index;k++) {
//							mdat.MDATBox_Datas_Print(k);
//						}
//						
						for (int k = 0; k < 5; k++) {
							mdat.MDATBox_Datas_Print(k);
						}
					} else if (full_boxes.get(i).type.equals("smhd")) { // audio
						System.out.println("\nAudio Sample");
						if(sample_index==-1) {

							sample_index = 0;
				
						}
						stsc_index = 0;
						chunk_index = 0;
						sample_count = 0;
						stts_index = 0;
						stts_count = 0;
						sample_duration = 0;
						sample_offset = 0;
						stss_index = 0;
						stco = mdat.FindSTCO(i, full_boxes, full_box_count);
						stsc = mdat.FindSTSC(i, full_boxes, full_box_count);
						stsz = mdat.FindSTSZ(i, full_boxes, full_box_count);
						stts = mdat.FindSTTS(i, full_boxes, full_box_count);

						sample_count = (int) stsc.table.get(0).samples_per_chunk;
						sample_duration = (int) stts.time_to_sample_table.get(0).sample_duration;
						for (chunk_index = 0; chunk_index < stco.entry_count; chunk_index++) {
							if ((stsc_index + 1) < stsc.entry_count) {
								if (stsc.table.get(stsc_index + 1).first_chunk == (chunk_index + 1)) {
									sample_count = (int) stsc.table.get(stsc_index + 1).samples_per_chunk;
									stsc_index++;
								}
							}
							InputStream sample_stream = new FileInputStream("output.mp4");
							sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
							sample_offset = stco.chunk_offset_table.get(chunk_index);

							for (int k = 0; k < sample_count; k++) {
								if (stts.time_to_sample_table.get(stts_index).sample_count == stts_count) {
									sample_duration = (int) stts.time_to_sample_table
											.get(stts_index + 1).sample_duration;
									stts_index++;
								}
								boxes.MediaData sample = new boxes.MediaData();
								sample.chunk_number = chunk_index + 1;
								sample.duration = sample_duration;
								if(k>0) {
									sample.offset = mdat.datas.get(sample_index-1).offset+mdat.datas.get(sample_index-1).size;
								}else {
									sample.offset = sample_offset;
								}
								sample.sample_index = sample_index + 1;
								sample.size = stsz.sample_size_table.get(audio_sample_count).longValue();

								byte[] data = new byte[(int) sample.size];
								sample_stream.read(data);
								sample.data = Util.ByteArrayToHex(data);

								mdat.datas.add(sample);
								sample_index++;
								stts_count++;
								audio_sample_count++;
							}

							sample_stream.close();
						}
						stsc = null;
						stsz = null;
						stco = null;
						stts = null;
						stss = null;

//						for(int k=0;k<sample_index;k++) {
//							mdat.MDATBox_Datas_Print(k);
//						}
//						
						for (int k = video_sample_count; k < video_sample_count + 5; k++) {
							mdat.MDATBox_Datas_Print(k);
						}
					}
				}
				boxes.add(mdat);
				box_count++;
				depth = 0;
			}else if (type.equals("mdat")&&(fragment_flag==1)&&(mdat_position_flag==1)) {
				boxes.MediaDataBox mdat = new boxes.MediaDataBox("mdat");
				mdat.size =size;
				fis.skip(size-8);
				if (size == 1) {
					byte[] largesize = new byte[8];
					fis.read(largesize);
					mdat.largesize = Util.ByteArrayToLong(largesize);
				}
				int sample_index = -1;
				int stsc_index = 0;
				int chunk_index = 0;
				int sample_count = 0;
				int stts_index = 0;
				int stts_count = 0;
				int sample_duration = 0;
				long sample_offset = 0;
				int video_sample_count = 0;
				int stss_index = 0;
				int audio_sample_count = 0;
				int keyframe_index = 0;

				if (size == 1) {
					System.out.println("mdat\n" + "Size: " + mdat.largesize + "\n" + "Type: MediaDataBox\n");
				} else {
					System.out.println("mdat\n" + "Size: " + mdat.size + "\n" + "Type: MediaDataBox\n");
				}
				boxes.SampletoChunkBox stsc = null;
				boxes.SampleSizeBox stsz = null;
				boxes.ChunkOffsetBox stco = null;
				boxes.TimetoSampleBox stts = null;
				boxes.SyncSampleBox stss = null;
				for (int i = 0; i < full_box_count; i++) {
					if (full_boxes.get(i).type.equals("vmhd")) { // video
						if(sample_index==-1) {

							sample_index = 0;	
						}
						stsc_index = 0;
						chunk_index = 0;
						sample_count = 0;
						stts_index = 0;
						stts_count = 0;
						sample_duration = 0;
						stss_index = 0;
						sample_offset = 0;
						System.out.println("\nVideo Sample");

						stco = mdat.FindSTCO(i, full_boxes, full_box_count);
						stss = mdat.FindSTSS(i, full_boxes, full_box_count);
						stsc = mdat.FindSTSC(i, full_boxes, full_box_count);
						stsz = mdat.FindSTSZ(i, full_boxes, full_box_count);
						stts = mdat.FindSTTS(i, full_boxes, full_box_count);
						if (stss != null) {
							keyframe_index = (int) stss.sync_sample_table.get(stss_index).intValue();
						} else {
							keyframe_index = -1;
						}
						sample_count = (int) stsc.table.get(0).samples_per_chunk;
						sample_duration = (int) stts.time_to_sample_table.get(0).sample_duration;
						for (chunk_index = 0; chunk_index < stco.entry_count; chunk_index++) {
							if ((stsc_index + 1) < stsc.entry_count) {
								if (stsc.table.get(stsc_index + 1).first_chunk == (chunk_index + 1)) {
									sample_count = (int) stsc.table.get(stsc_index + 1).samples_per_chunk;
									stsc_index++;
								}
							}
							InputStream sample_stream = new FileInputStream("output.mp4");
							sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
							sample_offset = stco.chunk_offset_table.get(chunk_index);

							for (int k = 0; k < sample_count; k++) {
								if (stts.time_to_sample_table.get(stts_index).sample_count == stts_count) {
									sample_duration = (int) stts.time_to_sample_table
											.get(stts_index + 1).sample_duration;
									stts_index++;
								}
								boxes.MediaData sample = new boxes.MediaData();
								sample.chunk_number = chunk_index + 1;
								sample.duration = sample_duration;
								if(k>0) {
									sample.offset = mdat.datas.get(sample_index-1).offset+mdat.datas.get(sample_index-1).size;
								}else {
									sample.offset = sample_offset;
								}
								sample.sample_index = sample_index + 1;
								sample.size = stsz.sample_size_table.get(video_sample_count).longValue();
								if (keyframe_index == -1) {
									sample.iskeyframe = 1;
								} else if ((sample.sample_index == keyframe_index)) {
									sample.iskeyframe = 1;
									if (stss.entry_count != (stss_index + 1)) {
										keyframe_index = stss.sync_sample_table.get(++stss_index).intValue();
									}
								}
								byte[] data = new byte[(int) sample.size];
								sample_stream.read(data);
								sample.data = Util.ByteArrayToHex(data);

								mdat.datas.add(sample);

								video_sample_count++;
								sample_index++;
								stts_count++;
							}
							sample_stream.close();
						}
						stsc = null;
						stsz = null;
						stco = null;
						stts = null;
						stss = null;

//						for(int k=0;k<sample_index;k++) {
//							mdat.MDATBox_Datas_Print(k);
//						}
//						
						for (int k = 0; k < 5; k++) {
							mdat.MDATBox_Datas_Print(k);
						}
					} else if (full_boxes.get(i).type.equals("smhd")) { // audio
						System.out.println("\nAudio Sample");
						if(sample_index==-1) {

							sample_index = 0;
				
						}
						stsc_index = 0;
						chunk_index = 0;
						sample_count = 0;
						stts_index = 0;
						stts_count = 0;
						sample_duration = 0;
						sample_offset = 0;
						stss_index = 0;
						stco = mdat.FindSTCO(i, full_boxes, full_box_count);
						stsc = mdat.FindSTSC(i, full_boxes, full_box_count);
						stsz = mdat.FindSTSZ(i, full_boxes, full_box_count);
						stts = mdat.FindSTTS(i, full_boxes, full_box_count);

						sample_count = (int) stsc.table.get(0).samples_per_chunk;
						sample_duration = (int) stts.time_to_sample_table.get(0).sample_duration;
						for (chunk_index = 0; chunk_index < stco.entry_count; chunk_index++) {
							if ((stsc_index + 1) < stsc.entry_count) {
								if (stsc.table.get(stsc_index + 1).first_chunk == (chunk_index + 1)) {
									sample_count = (int) stsc.table.get(stsc_index + 1).samples_per_chunk;
									stsc_index++;
								}
							}
							InputStream sample_stream = new FileInputStream("output.mp4");
							sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
							sample_offset = stco.chunk_offset_table.get(chunk_index);

							for (int k = 0; k < sample_count; k++) {
								if (stts.time_to_sample_table.get(stts_index).sample_count == stts_count) {
									sample_duration = (int) stts.time_to_sample_table
											.get(stts_index + 1).sample_duration;
									stts_index++;
								}
								boxes.MediaData sample = new boxes.MediaData();
								sample.chunk_number = chunk_index + 1;
								sample.duration = sample_duration;
								if(k>0) {
									sample.offset = mdat.datas.get(sample_index-1).offset+mdat.datas.get(sample_index-1).size;
								}else {
									sample.offset = sample_offset;
								}
								sample.sample_index = sample_index + 1;
								sample.size = stsz.sample_size_table.get(audio_sample_count).longValue();

								byte[] data = new byte[(int) sample.size];
								sample_stream.read(data);
								sample.data = Util.ByteArrayToHex(data);

								mdat.datas.add(sample);
								sample_index++;
								stts_count++;
								audio_sample_count++;
							}

							sample_stream.close();
						}
						stsc = null;
						stsz = null;
						stco = null;
						stts = null;
						stss = null;

//						for(int k=0;k<sample_index;k++) {
//							mdat.MDATBox_Datas_Print(k);
//						}
//						
						for (int k = video_sample_count; k < video_sample_count + 5; k++) {
							mdat.MDATBox_Datas_Print(k);
						}
					}
				}
				boxes.add(mdat);
				box_count++;
				depth = 0;
			} else if(type.equals("mdat")&&(mdat_position_flag)==0) {
				ismdatafter=1;
				mdat_size=size;
				if (mdat_size == 1) {
					byte[] largesize = new byte[8];
					fis.read(largesize);
					mdat_largesize = Util.ByteArrayToLong(largesize);
				}
				fis.skip(size-8);
			}else {
				System.out.println(size);
				System.out.println(type);
				fis.skip(size - 8);
				stream_position+=size;
			}
		}
		
		if(ismdatafter==1) {//mdat 나중에 까기
			boxes.MediaDataBox mdat = new boxes.MediaDataBox("mdat");
			mdat.size = mdat_size;
			if (mdat_size == 1) {
				byte[] largesize = new byte[8];
				fis.read(largesize);
				mdat.largesize = Util.ByteArrayToLong(largesize);
			}
			int sample_index = -1;
			int stsc_index = 0;
			int chunk_index = 0;
			int sample_count = 0;
			int stts_index = 0;
			int stts_count = 0;
			int sample_duration = 0;
			long sample_offset = 0;
			int video_sample_count = 0;
			int stss_index = 0;
			int audio_sample_count = 0;
			int keyframe_index = 0;

			if (mdat_size == 1) {
				System.out.println("mdat\n" + "Size: " + mdat.largesize + "\n" + "Type: MediaDataBox\n");
			} else {
				System.out.println("mdat\n" + "Size: " + mdat.size + "\n" + "Type: MediaDataBox\n");
			}
			boxes.SampletoChunkBox stsc = null;
			boxes.SampleSizeBox stsz = null;
			boxes.ChunkOffsetBox stco = null;
			boxes.TimetoSampleBox stts = null;
			boxes.SyncSampleBox stss = null;
			for (int i = 0; i < full_box_count; i++) {
				if (full_boxes.get(i).type.equals("vmhd")) { // video
					if(sample_index==-1) {

						sample_index = 0;	
					}
					stsc_index = 0;
					chunk_index = 0;
					sample_count = 0;
					stts_index = 0;
					stts_count = 0;
					sample_duration = 0;
					stss_index = 0;
					sample_offset = 0;
					System.out.println("\nVideo Sample");

					stco = mdat.FindSTCO(i, full_boxes, full_box_count);
					stss = mdat.FindSTSS(i, full_boxes, full_box_count);
					stsc = mdat.FindSTSC(i, full_boxes, full_box_count);
					stsz = mdat.FindSTSZ(i, full_boxes, full_box_count);
					stts = mdat.FindSTTS(i, full_boxes, full_box_count);
					if (stss != null) {
						keyframe_index = (int) stss.sync_sample_table.get(stss_index).intValue();
					} else {
						keyframe_index = -1;
					}
					sample_count = (int) stsc.table.get(0).samples_per_chunk;
					sample_duration = (int) stts.time_to_sample_table.get(0).sample_duration;
					for (chunk_index = 0; chunk_index < stco.entry_count; chunk_index++) {
						if ((stsc_index + 1) < stsc.entry_count) {
							if (stsc.table.get(stsc_index + 1).first_chunk == (chunk_index + 1)) {
								sample_count = (int) stsc.table.get(stsc_index + 1).samples_per_chunk;
								stsc_index++;
							}
						}
						InputStream sample_stream = new FileInputStream("output.mp4");
						sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
						sample_offset = stco.chunk_offset_table.get(chunk_index);

						for (int k = 0; k < sample_count; k++) {
							if (stts.time_to_sample_table.get(stts_index).sample_count == stts_count) {
								sample_duration = (int) stts.time_to_sample_table
										.get(stts_index + 1).sample_duration;
								stts_index++;
							}
							boxes.MediaData sample = new boxes.MediaData();
							sample.chunk_number = chunk_index + 1;
							sample.duration = sample_duration;
							if(k>0) {
								sample.offset = mdat.datas.get(sample_index-1).offset+mdat.datas.get(sample_index-1).size;
							}else {
								sample.offset = sample_offset;
							}
							sample.sample_index = sample_index + 1;
							sample.size = stsz.sample_size_table.get(video_sample_count).longValue();
							if (keyframe_index == -1) {
								sample.iskeyframe = 1;
							} else if ((sample.sample_index == keyframe_index)) {
								sample.iskeyframe = 1;
								if (stss.entry_count != (stss_index + 1)) {
									keyframe_index = stss.sync_sample_table.get(++stss_index).intValue();
								}
							}
							byte[] data = new byte[(int) sample.size];
							sample_stream.read(data);
							sample.data = Util.ByteArrayToHex(data);

							mdat.datas.add(sample);

							video_sample_count++;
							sample_index++;
							stts_count++;
						}
						sample_stream.close();
					}
					stsc = null;
					stsz = null;
					stco = null;
					stts = null;
					stss = null;

//					for(int k=0;k<sample_index;k++) {
//						mdat.MDATBox_Datas_Print(k);
//					}
//					
					for (int k = 0; k < 5; k++) {
						mdat.MDATBox_Datas_Print(k);
					}
				} else if (full_boxes.get(i).type.equals("smhd")) { // audio
					System.out.println("\nAudio Sample");
					if(sample_index==-1) {

						sample_index = 0;
			
					}
					stsc_index = 0;
					chunk_index = 0;
					sample_count = 0;
					stts_index = 0;
					stts_count = 0;
					sample_duration = 0;
					sample_offset = 0;
					stss_index = 0;
					stco = mdat.FindSTCO(i, full_boxes, full_box_count);
					stsc = mdat.FindSTSC(i, full_boxes, full_box_count);
					stsz = mdat.FindSTSZ(i, full_boxes, full_box_count);
					stts = mdat.FindSTTS(i, full_boxes, full_box_count);

					sample_count = (int) stsc.table.get(0).samples_per_chunk;
					sample_duration = (int) stts.time_to_sample_table.get(0).sample_duration;
					for (chunk_index = 0; chunk_index < stco.entry_count; chunk_index++) {
						if ((stsc_index + 1) < stsc.entry_count) {
							if (stsc.table.get(stsc_index + 1).first_chunk == (chunk_index + 1)) {
								sample_count = (int) stsc.table.get(stsc_index + 1).samples_per_chunk;
								stsc_index++;
							}
						}
						InputStream sample_stream = new FileInputStream("output.mp4");
						sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
						sample_offset = stco.chunk_offset_table.get(chunk_index);

						for (int k = 0; k < sample_count; k++) {
							if (stts.time_to_sample_table.get(stts_index).sample_count == stts_count) {
								sample_duration = (int) stts.time_to_sample_table
										.get(stts_index + 1).sample_duration;
								stts_index++;
							}
							boxes.MediaData sample = new boxes.MediaData();
							sample.chunk_number = chunk_index + 1;
							sample.duration = sample_duration;
							if(k>0) {
								sample.offset = mdat.datas.get(sample_index-1).offset+mdat.datas.get(sample_index-1).size;
							}else {
								sample.offset = sample_offset;
							}
							sample.sample_index = sample_index + 1;
							sample.size = stsz.sample_size_table.get(audio_sample_count).longValue();

							byte[] data = new byte[(int) sample.size];
							sample_stream.read(data);
							sample.data = Util.ByteArrayToHex(data);

							mdat.datas.add(sample);
							sample_index++;
							stts_count++;
							audio_sample_count++;
						}

						sample_stream.close();
					}
					stsc = null;
					stsz = null;
					stco = null;
					stts = null;
					stss = null;

//					for(int k=0;k<sample_index;k++) {
//						mdat.MDATBox_Datas_Print(k);
//					}
//					
					for (int k = video_sample_count; k < video_sample_count + 5; k++) {
						mdat.MDATBox_Datas_Print(k);
					}
				}
			}
			boxes.add(mdat);
			box_count++;
			depth = 0;
		}
		
		
		System.out.println("\nFinished");
	}
}