package main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import boxes.*;

public class Main {
	public static void main(String[] args) throws Exception {
		//trex box 데이터 오차 있음
		//mdat 깔때 아스키 코드도 같이 까게 만들어놓기
		String source="BigBuckBunny.mp4";
		long stream_position=0;
		int mdat_position_flag=0;//먼저오면 0 나중에 오면 1
		int ismdatafter=0;
		int fragment_flag=0; //moof가 있으면 fmp4 -> 1
		ArrayList<Box> boxes = new ArrayList<>();
		ArrayList<FullBox> full_boxes = new ArrayList<>();
		int box_count = 0;
		int full_box_count = 0;
		FileInputStream fis = new FileInputStream(source);
		byte[] size_byte = new byte[4];
		byte[] type_byte = new byte[4];
		long size;
		String type;
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		long mdat_size=0;
		long mdat_largesize=0;
		long v,f;
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
		while (fis.read(size_byte) != -1) {
			fis.read(type_byte);
			size = Util.ByteArrayToLong(size_byte);
			type = new String(type_byte, StandardCharsets.US_ASCII);
			if (type.equals("ftyp")) {
				FileTypeBox ftyp = new FileTypeBox("ftyp");
				ftyp.struct_depth=0;
				ftyp.start_position=stream_position;
				ftyp.end_position=stream_position+size;
				stream_position+=size;
				ftyp.size = size;
				ftyp.SetFTYPBox(fis);
				System.out.println(ftyp);
				boxes.add(ftyp);
				box_count++;
			} else if (type.equals("moov")) {
				MovieBox moov = new MovieBox("moov");
				moov.struct_depth=0;
				moov.size = size;
				System.out.println(moov);
				boxes.add(moov);
				box_count++;
				mdat_position_flag=1;
				moov.start_position=stream_position;
				moov.end_position=stream_position+size;
				stream_position+=8;
			}else if (type.equals("mvex")) {
				MovieExtendsBox mvex = new MovieExtendsBox("mvex");
				mvex.struct_depth=1;
				mvex.size = size;
				System.out.println(mvex);
				boxes.add(mvex);
				box_count++;
				mdat_position_flag=1;
				fragment_flag=1;
				mvex.start_position=stream_position;
				mvex.end_position=stream_position+size;
				stream_position+=8;
			}else if (type.equals("mehd")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
				
				MovieExtendsHeaderBox mehd = new MovieExtendsHeaderBox("mehd", v, f);
				mehd.struct_depth=2;
				mehd.size = size;
				mehd.SetMEHDBox(fis);
				System.out.println(mehd);
				full_boxes.add(mehd);
				full_box_count++;
				mehd.start_position=stream_position;
				mehd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("trex")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
				TrackExtendsBox trex = new TrackExtendsBox("trex", v, f);
				trex.struct_depth=2;
				trex.size = size;
				trex.SetTREXBox(fis);
				System.out.println(trex);
				full_boxes.add(trex);
				full_box_count++;
				trex.start_position=stream_position;
				trex.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("sidx")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
				
				SegmentIndexBox sidx = new SegmentIndexBox("sidx", v, f);
				sidx.struct_depth=0;
				sidx.size = size;
				sidx.SetSIDXBox(fis);
				System.out.println(sidx);
				if(sidx.reference_count>0) {
					sidx.PrintTable();
				}
				full_boxes.add(sidx);
				full_box_count++;
				sidx.start_position=stream_position;
				sidx.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("tfdt")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
				
				TrackFragmentBaseMediaDecodeTimeBox tfdt = new TrackFragmentBaseMediaDecodeTimeBox("tfdt", v, f);
				tfdt.struct_depth=2;
				tfdt.size = size;
				tfdt.SetTFDTBox(fis);
				System.out.println(tfdt);
				full_boxes.add(tfdt);
				full_box_count++;
				tfdt.start_position=stream_position;
				tfdt.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("moof")) {
				MovieFragmentBox moof = new MovieFragmentBox("moof");
				moof.struct_depth=0;
				moof.size = size;
				System.out.println(moof);
				boxes.add(moof);
				box_count++;
				mdat_position_flag=1;
				fragment_flag=1;
				moof.start_position=stream_position;
				moof.end_position=stream_position+size;
				stream_position+=8;
			}else if (type.equals("mfhd")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
				
				MovieFragmentHeaderBox mfhd = new MovieFragmentHeaderBox("mfhd", v, f);
				mfhd.struct_depth=1;
				mfhd.size = size;
				mfhd.SetMFHDBox(fis);
				System.out.println(mfhd);
				full_boxes.add(mfhd);
				full_box_count++;
				mfhd.start_position=stream_position;
				mfhd.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("traf")) {
				TrackFragmentBox traf = new TrackFragmentBox("traf");
				traf.struct_depth=1;
				traf.size = size;
				System.out.println(traf);
				boxes.add(traf);
				box_count++;
				traf.start_position=stream_position;
				traf.end_position=stream_position+size;
				stream_position+=8;
			}else if (type.equals("tfhd")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
				
				TrackFragmentHeaderBox tfhd = new TrackFragmentHeaderBox("mfhd", v, f);
				tfhd.struct_depth=3;
				tfhd.size = size;
				tfhd.SetTFHDBox(fis);
				tfhd.PrintTFHDBox();
				full_boxes.add(tfhd);
				full_box_count++;
				tfhd.start_position=stream_position;
				tfhd.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("trun")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
				
				TrackFragmentRunBox trun = new TrackFragmentRunBox("trun", v, f);
				trun.struct_depth=3;
				trun.size = size;
				trun.SetTRUNBox(fis);
				trun.PrintTRUNBox();
				trun.PrintTable();
				full_boxes.add(trun);
				full_box_count++;
				trun.start_position=stream_position;
				trun.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("mvhd")) {
				v = Util.getVersion(fis);
				// flag 3바이트
				fis.skip(3);
				MovieHeaderBox mvhd = new MovieHeaderBox("mvhd", v, 0);
				mvhd.struct_depth=1;
				mvhd.size = size;
				mvhd.SetMVHDBox(fis, cal);
				System.out.println(mvhd);
				full_boxes.add(mvhd);
				full_box_count++;
				mvhd.start_position=stream_position;
				mvhd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("free")) {
				FreeSpaceBox free = new FreeSpaceBox("free");
				free.SetStructDepth(box_count, full_box_count, boxes, full_boxes, stream_position);
				free.size = size;
				fis.skip(free.size - 8);
				System.out.println(free);
				boxes.add(free);
				box_count++;
				free.start_position=stream_position;
				free.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("skip")) {
				FreeSpaceBox skip = new FreeSpaceBox("skip");
				skip.SetStructDepth(box_count, full_box_count, boxes, full_boxes, stream_position);
				skip.size = size;
				fis.skip(skip.size - 8);
				System.out.println(skip.toString());
				boxes.add(skip);
				box_count++;
				skip.start_position=stream_position;
				skip.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("trak")) {
				TrackBox trak = new TrackBox("trak");
				trak.size = size;
				System.out.println(trak);

				boxes.add(trak);
				box_count++;

				trak.struct_depth= 1;
				trak.start_position=stream_position;
				trak.end_position=stream_position+size;
				stream_position+=8;
			}else if (type.equals("mfra")) {
				MovieFragmentRandomAccessBox mfra = new MovieFragmentRandomAccessBox("mfra");
				mfra.size = size;
				System.out.println(mfra);
				
				boxes.add(mfra);
				box_count++;

				mfra.struct_depth= 0;
				mfra.start_position=stream_position;
				mfra.end_position=stream_position+size;
				stream_position+=8;
			}else if (type.equals("tfra")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				TrackFragmentRandomAccessBox tfra = new TrackFragmentRandomAccessBox("tfra", v, f);
				tfra.size = size;

				tfra.SetTFRABox(fis);

				full_boxes.add(tfra);
				full_box_count++;
				
				System.out.println(tfra);

				if (tfra.entry_count != 0) {
					tfra.TFRABox_Table_Print();
				}
				tfra.start_position=stream_position;
				tfra.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("mfro")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				MovieFragmentRandomAccessOffsetBox mfro = new MovieFragmentRandomAccessOffsetBox("mfro", v, f);
				mfro.size = size;

				mfro.SetMFROBox(fis);

				full_boxes.add(mfro);
				full_box_count++;
				
				System.out.println(mfro);
				mfro.start_position=stream_position;
				mfro.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("edts")) {
				EditBox edts = new EditBox("edts");
				edts.size = size;
				System.out.println(edts);

				boxes.add(edts);
				box_count++;

				edts.struct_depth= 1;
				edts.start_position=stream_position;
				edts.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("elst")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				EditListBox elst = new EditListBox("elst", v, f);
				elst.size = size;

				elst.SetELSTBox(fis);

				full_boxes.add(elst);
				full_box_count++;
				
				System.out.println(elst);

				if (elst.entry_count != 0) {
					elst.SetELSTBox_Table(fis);
					elst.ELSTBox_Table_Print();
				}
				elst.start_position=stream_position;
				elst.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("tkhd")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
				TrackHeaderBox tkhd = new TrackHeaderBox("tkhd", v, f);
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
				MediaBox mdia = new MediaBox("mdia");
				mdia.size = size;
				System.out.println(mdia);
				boxes.add(mdia);
				box_count++;
				mdia.struct_depth = 2;
				mdia.start_position=stream_position;
				mdia.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("udta")) {
				
				UserDataBox udta = new UserDataBox("udta");
				udta.size = size;
				udta.SetStructDepth(box_count, boxes, stream_position);
				
				System.out.println(udta.toString());
				boxes.add(udta);
				box_count++;
				udta.start_position=stream_position;
				udta.end_position=stream_position+size;
				stream_position+=8;
				
			} else if (type.equals("mdhd")) {
				v = Util.getVersion(fis);
				// flags set to 0
				fis.skip(3);
				MediaHeaderBox mdhd = new MediaHeaderBox("mdhd", v, 0);
				mdhd.size = size;
				mdhd.SetMDHDBox(fis, cal);
				System.out.println(mdhd);
				full_boxes.add(mdhd);
				full_box_count++;
				mdhd.start_position=stream_position;
				mdhd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("hdlr")) {
				v = Util.getVersion(fis);
				// Flags
				fis.skip(3);
				HandlerReferenceBox hdlr = new HandlerReferenceBox("hdlr", v, 0);
				hdlr.SetStructDepth(box_count, full_box_count, boxes, full_boxes, stream_position);
				hdlr.size = size;
				hdlr.SetHDLRBox(fis);
				System.out.println(hdlr.toString());
				full_boxes.add(hdlr);
				full_box_count++;
				hdlr.start_position=stream_position;
				hdlr.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("minf")) {
				MediaInformationBox minf = new MediaInformationBox("minf");
				minf.size = size;
				System.out.println(minf);
				boxes.add(minf);
				box_count++;
				minf.start_position=stream_position;
				minf.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("vmhd")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);
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
				DataInformationBox dinf = new DataInformationBox("dinf");
				dinf.size = size;

				boxes.add(dinf);
				box_count++;
				
				dinf.SetStructDepth(box_count, full_box_count, boxes, full_boxes, stream_position);
				
				System.out.println(dinf.toString());
				dinf.start_position=stream_position;
				dinf.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("dref")) {

				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				DataReferenceBox dref = new DataReferenceBox("dref", v, f);
				dref.size = size;

				dref.SetDREFBox(fis);

				full_boxes.add(dref);
				full_box_count++;
				
				dref.SetStructDepth(box_count, boxes, stream_position);

				System.out.println(dref.toString());

				if (dref.entry_count != 0) {
					dref.SetDREFBox_Table(fis);
					dref.DREFBox_Table_Print();
				}
				dref.start_position=stream_position;
				dref.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("stbl")) {
				SampleTableBox stbl = new SampleTableBox("stbl");
				stbl.size = size;
				System.out.println(stbl);

				boxes.add(stbl);
				box_count++;
				stbl.start_position=stream_position;
				stbl.end_position=stream_position+size;
				stream_position+=8;
			} else if (type.equals("stsd")) {
				SampleDescriptionBox stsd = new SampleDescriptionBox("stsd", 0, 0);
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
				AVC1Box avc1=new AVC1Box("avc1",0,0);
				avc1.sample_description_size=size;
				avc1.data_format=type;
				avc1.SetAVC1Box(fis);
				System.out.println(avc1);
				full_boxes.add(avc1);
				full_box_count++;
				
			}else if(type.equals("avcC")){
				//ISO/IEC 14496-15 18페이지
				AVCCBox avcc=new AVCCBox();
				avcc.size=size;
				avcc.type=type;
				avcc.SetAVCCBox(fis);
				System.out.println(avcc);
				avcc.FMP4Skip(full_boxes, full_box_count, fis);
			}else if(type.equals("mp4a")){
				MP4ABox mp4a=new MP4ABox();
				mp4a.sample_description_size=size;
				mp4a.data_format=type;
				mp4a.SetMP4ABox(fis);
				System.out.println(mp4a);
				
			}else if (type.equals("stts")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				TimetoSampleBox stts = new TimetoSampleBox("stts", v, f);
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
			} else if (type.equals("ctts")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				CompositionOffsetBox ctts = new CompositionOffsetBox("ctts", v, f);
				ctts.size = size;

				ctts.SetCTTSBox(fis);

				full_boxes.add(ctts);
				full_box_count++;
				
				System.out.println(ctts);

				if (ctts.entry_count != 0) {
					ctts.SetCTTSBox_Table(fis);
					ctts.CTTSBox_Table_Print();
				}
				ctts.start_position=stream_position;
				ctts.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("sbgp")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				SampletoGroupBox sbgp = new SampletoGroupBox("sbgp", v, f);
				sbgp.size = size;

				sbgp.SetSBGPBox(fis);

				full_boxes.add(sbgp);
				full_box_count++;
				
				sbgp.SetStructDepth(box_count, boxes, stream_position);

				System.out.println(sbgp.toString());

				if (sbgp.entry_count != 0) {
					sbgp.SetSBGPBox_Table(fis);
					sbgp.SBGPBox_Table_Print();
				}

				sbgp.start_position=stream_position;
				sbgp.end_position=stream_position+size;
				stream_position+=size;
			}else if (type.equals("stss")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				SyncSampleBox stss = new SyncSampleBox("stss", v, f);
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
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				SampletoChunkBox stsc = new SampletoChunkBox("stsc", v, f);
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
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				SampleSizeBox stsz = new SampleSizeBox("stsz", v, f);
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
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				SoundMediaHeaderBox smhd = new SoundMediaHeaderBox("smhd", v, f);

				smhd.size = size;

				smhd.SetSMHDBox(fis);

				System.out.println(smhd);

				full_boxes.add(smhd);
				full_box_count++;
				
				smhd.start_position=stream_position;
				smhd.end_position=stream_position+size;
				stream_position+=size;
			} else if (type.equals("stco")) {
				v = Util.getVersion(fis);
				f = Util.getFlags(fis);

				ChunkOffsetBox stco = new ChunkOffsetBox("stco", v, f);
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
			} else if (type.equals("mdat")&&(fragment_flag==0)&&(mdat_position_flag==1)) { //일반 mp4에 mdat이 moov 뒤에 오는 경우
				MediaDataBox mdat = new MediaDataBox("mdat");
				mdat.size = size;
				fis.skip(size-8);
				if (size == 1) {
					byte[] largesize = new byte[8];
					fis.read(largesize);
					mdat.largesize = Util.ByteArrayToLong(largesize);
				}
				System.out.println(mdat);
				
				SampletoChunkBox stsc = null;
				SampleSizeBox stsz = null;
				ChunkOffsetBox stco = null;
				TimetoSampleBox stts = null;
				SyncSampleBox stss = null;
				for (int i = 0; i < full_box_count; i++) {
					if (full_boxes.get(i).type.equals("vmhd")) { // video
						if(sample_index==-1) {

							sample_index = 0;	
						}
						mdat.SetVariablesZero(stsc_index, chunk_index, video_sample_count, stts_index, stts_count, sample_duration, stss_index, sample_offset);
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
							InputStream sample_stream = new FileInputStream(source);
							sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
							sample_offset = stco.chunk_offset_table.get(chunk_index);

							for (int k = 0; k < sample_count; k++) {
								if ((stts.time_to_sample_table.get(stts_index).sample_count == stts_count)&&(stts.entry_count!=stts_index+1)) {
									sample_duration = (int) stts.time_to_sample_table
											.get(stts_index + 1).sample_duration;
									stts_index++;
								}
								MediaData sample = new MediaData();
								sample.SetVideoVariables(k, chunk_index, sample_duration, mdat, sample_index, sample_offset, stsz, video_sample_count, keyframe_index, stss, stss_index, sample_stream);

								mdat.datas.add(sample);

								video_sample_count++;
								sample_index++;
								stts_count++;
							}
							sample_stream.close();
						}
						mdat.SetBoxesNull(stsc, stsz, stco, stts, stss);
//						for(int k=0;k<sample_index;k++) {
//							mdat.MDATBox_Datas_Print(k);
//						}
						for (int k = 0; k < 5; k++) {
							mdat.MDATBox_Datas_Print(k);
						}
					} else if (full_boxes.get(i).type.equals("smhd")) { // audio
						System.out.println("\nAudio Sample");
						if(sample_index==-1) {

							sample_index = 0;
				
						}
						mdat.SetVariablesZero(stsc_index, chunk_index, audio_sample_count, stts_index, stts_count, sample_duration, stss_index, sample_offset);
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
							InputStream sample_stream = new FileInputStream(source);
							sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
							sample_offset = stco.chunk_offset_table.get(chunk_index);

							for (int k = 0; k < sample_count; k++) {
								if ((stts.time_to_sample_table.get(stts_index).sample_count == stts_count)&&(stts.entry_count!=stts_index+1)) {
									sample_duration = (int) stts.time_to_sample_table.get(stts_index + 1).sample_duration;
									stts_index++;
								}
								audio_sample_count++;
								MediaData sample = new MediaData();
								sample.SetAudioVariables(k, chunk_index, sample_duration, mdat, sample_index, sample_offset, stsz, audio_sample_count, keyframe_index, stss, stss_index, sample_stream);

								mdat.datas.add(sample);
								sample_index++;
								stts_count++;
							}

							sample_stream.close();
						}
						mdat.SetBoxesNull(stsc, stsz, stco, stts, stss);
//						for(int k=0;k<sample_index;k++) {
//							mdat.MDATBox_Datas_Print(k);
//						}
						for (int k = video_sample_count; k < video_sample_count + 5; k++) {
							mdat.MDATBox_Datas_Print(k);
						}
					}
				}
				boxes.add(mdat);
				box_count++;
			}else if (type.equals("mdat")&&(fragment_flag==1)&&(mdat_position_flag==1)) { //fmp4 이며 mdat이 제일 뒤에 오는 경우
				MediaDataBox mdat = new MediaDataBox("mdat");
				mdat.size =size;
				fis.skip(size-8);
				if (size == 1) {
					byte[] largesize = new byte[8];
					fis.read(largesize);
					mdat.largesize = Util.ByteArrayToLong(largesize);
				}

				if (size == 1) {
					System.out.println("mdat\n" + "Size: " + mdat.largesize + "\n" + "Type: MediaDataBox\n");
				} else {
					System.out.println("mdat\n" + "Size: " + mdat.size + "\n" + "Type: MediaDataBox\n");
				}
				
				TrackFragmentRunBox trun=null;
				for(int i=full_box_count-1;i>=0;i--) {
					if(full_boxes.get(i).type.equals("trun")) {
						trun=(TrackFragmentRunBox)full_boxes.get(i);
					}
				}
				
				for(int i=0;i<trun.sample_count;i++) {
					MediaData sample=new MediaData();
					sample.size=trun.table.get(i).sample_size;
					sample.duration=trun.table.get(i).sample_duration;
					sample.offset=trun.table.get(i).sample_composition_time_offset;
					mdat.datas.add(sample);
				}
				
				boxes.add(mdat);
				box_count++;
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
		
		if((ismdatafter==1)&&(fragment_flag==0)) {//mdat 나중에 까기
			MediaDataBox mdat = new MediaDataBox("mdat");
			mdat.size = mdat_size;
			if (mdat_size == 1) {
				mdat.largesize = mdat_largesize;
			}

			if (mdat_size == 1) {
				System.out.println("mdat\n" + "Size: " + mdat.largesize + "\n" + "Type: MediaDataBox\n");
			} else {
				System.out.println("mdat\n" + "Size: " + mdat.size + "\n" + "Type: MediaDataBox\n");
			}
			SampletoChunkBox stsc = null;
			SampleSizeBox stsz = null;
			ChunkOffsetBox stco = null;
			TimetoSampleBox stts = null;
			SyncSampleBox stss = null;
			for (int i = 0; i < full_box_count; i++) {
				if (full_boxes.get(i).type.equals("vmhd")) { // video
					if(sample_index==-1) {

						sample_index = 0;	
					}
					mdat.SetVariablesZero(stsc_index, chunk_index, video_sample_count, stts_index, stts_count, sample_duration, stss_index, sample_offset);
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
						InputStream sample_stream = new FileInputStream(source);
						sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
						sample_offset = stco.chunk_offset_table.get(chunk_index);

						for (int k = 0; k < sample_count; k++) {
							if ((stts.time_to_sample_table.get(stts_index).sample_count == stts_count)&&(stts.entry_count!=stts_index+1)) {
								sample_duration = (int) stts.time_to_sample_table.get(stts_index + 1).sample_duration;
								stts_index++;
							}
							MediaData sample = new MediaData();
							sample.SetVideoVariables(k, chunk_index, sample_duration, mdat, sample_index, sample_offset, stsz, video_sample_count, keyframe_index, stss, stss_index, sample_stream);

							mdat.datas.add(sample);

							video_sample_count++;
							sample_index++;
							stts_count++;
						}
						sample_stream.close();
					}
					mdat.SetBoxesNull(stsc, stsz, stco, stts, stss);
//					for(int k=0;k<sample_index;k++) {
//						mdat.MDATBox_Datas_Print(k);
//					}
					for (int k = 0; k < 5; k++) {
						mdat.MDATBox_Datas_Print(k);
					}
				} else if (full_boxes.get(i).type.equals("smhd")) { // audio
					System.out.println("\nAudio Sample");
					if(sample_index==-1) {

						sample_index = 0;
			
					}
					mdat.SetVariablesZero(stsc_index, chunk_index, audio_sample_count, stts_index, stts_count, sample_duration, stss_index, sample_offset);
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
						InputStream sample_stream = new FileInputStream(source);
						sample_stream.skip(stco.chunk_offset_table.get(chunk_index));
						sample_offset = stco.chunk_offset_table.get(chunk_index);
						for (int k = 0; k < sample_count; k++) {
							if(mdat.datas.size()>=video_sample_count+stsz.entry_count) {
//								mdat.MDATBox_Datas_Print(mdat.datas.size()-1);
								break;
							}
							if ((stts.time_to_sample_table.get(stts_index).sample_count == stts_count)&&(stts.entry_count!=stts_index+1)) {
								sample_duration = (int) stts.time_to_sample_table.get(stts_index + 1).sample_duration;
								stts_index++;
							}
							MediaData sample = new MediaData();
							audio_sample_count++;
							sample.SetAudioVariables(k, chunk_index, sample_duration, mdat, sample_index, sample_offset, stsz, audio_sample_count, keyframe_index, stss, stss_index, sample_stream);

							mdat.datas.add(sample);
							sample_index++;
							stts_count++;
						}

						sample_stream.close();
					}
					mdat.SetBoxesNull(stsc, stsz, stco, stts, stss);
//					for(int k=0;k<sample_index;k++) {
//						mdat.MDATBox_Datas_Print(k);
//					}
					for (int k = video_sample_count; k < video_sample_count + 5; k++) {
						mdat.MDATBox_Datas_Print(k);
					}
				}
			}
			boxes.add(mdat);
			box_count++;
		}
		System.out.println("\nFinished");
	}
}