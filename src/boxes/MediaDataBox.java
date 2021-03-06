package boxes;

import java.util.ArrayList;

public class MediaDataBox extends Box{

	public ArrayList<MediaData> datas=new ArrayList<MediaData>();
	public MediaDataBox(String boxtype) {
		super(boxtype);
		// TODO Auto-generated constructor stub
	}
	public ChunkOffsetBox FindSTCO(int i,ArrayList<FullBox> full_boxes,int full_box_count) {
		for(int j=i;j<full_box_count;j++) {
			if(full_boxes.get(j).type.equals("stco")){
				return (ChunkOffsetBox)full_boxes.get(j);
			}
		}
		return null;
	}
	
	public SyncSampleBox FindSTSS(int i,ArrayList<FullBox> full_boxes,int full_box_count) {
		for(int j=i;j<full_box_count;j++) {
			if(full_boxes.get(j).type.equals("stss")){
				return (SyncSampleBox)full_boxes.get(j);
			}
		}
		return null;
	}
	
	public SampletoChunkBox FindSTSC(int i,ArrayList<FullBox> full_boxes,int full_box_count) {
		for(int j=i;j<full_box_count;j++) {
			if(full_boxes.get(j).type.equals("stsc")){
				return (SampletoChunkBox)full_boxes.get(j);
			}
		}
		return null;
	}
	
	public SampleSizeBox FindSTSZ(int i,ArrayList<FullBox> full_boxes,int full_box_count) {
		for(int j=i;j<full_box_count;j++) {
			if(full_boxes.get(j).type.equals("stsz")){
				return (SampleSizeBox)full_boxes.get(j);
			}
		}
		return null;
	}
	
	public TimetoSampleBox FindSTTS(int i,ArrayList<FullBox> full_boxes,int full_box_count) {
		for(int j=i;j<full_box_count;j++) {
			if(full_boxes.get(j).type.equals("stts")){
				return (TimetoSampleBox)full_boxes.get(j);
			}
		}
		return null;
	}
	
	public MediaDataBox(String boxtype,long size) {
		super(boxtype,size);
	}
	
	public void MDATBox_Datas_Print(int k) {
		if(this.datas.get(k).iskeyframe==1) {
			System.out.println("Sample "+this.datas.get(k).sample_index+" I Frame");
			System.out.println("Offset: "+this.datas.get(k).offset+
					"\tSize: "+this.datas.get(k).size+
					"\tDuration: "+this.datas.get(k).duration+
					"\tData: "+this.datas.get(k).data);
		}else {
			System.out.println("Sample "+this.datas.get(k).sample_index);
			System.out.println("Offset: "+this.datas.get(k).offset+
					"\tSize: "+this.datas.get(k).size+
					"\tDuration: "+this.datas.get(k).duration+
					"\tData: "+this.datas.get(k).data);
		}
	}
	@Override
	public String toString() {
		if(this.size==1) {
			return "mdat\n" + "Size: " + this.largesize + "\n" + "Type: MediaDataBox\n";
		}else {
			return "mdat\n" + "Size: " + this.size + "\n" + "Type: MediaDataBox\n";
		}
	}
	
	public void SetBoxesNull(SampletoChunkBox stsc,SampleSizeBox stsz, ChunkOffsetBox stco, TimetoSampleBox stts, SyncSampleBox stss) {
		stsc=null;
		stsz=null;
		stco=null;
		stts=null;
		stss=null;
	}
	public void SetVariablesZero(int stsc_index,int chunk_index, int sample_count, int stts_index,int stts_count, int sample_duration, int stss_index, long sample_offset) {
		stsc_index=0;
		chunk_index=0;
		sample_count=0;
		stts_index=0;
		stts_count=0;
		sample_duration=0;
		stss_index=0;
		sample_offset=0;
	}

}
