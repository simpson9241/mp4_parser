package boxes;

import java.io.IOException;
import java.io.InputStream;

import main.Util;

public class MediaData {
	public long duration;
	public long offset;
	public long chunk_number;
	public long size;
	public long sample_index;
	public String data;
	public int iskeyframe=0;
	
	public void SetVideoVariables(int k,int chunk_index, int sample_duration, MediaDataBox mdat,int sample_index,long sample_offset,SampleSizeBox stsz,int video_sample_count,int keyframe_index,SyncSampleBox stss,int stss_index,InputStream sample_stream) throws IOException {
		this.chunk_number = chunk_index + 1;
		this.duration = sample_duration;
		if(k>0) {
			this.offset = mdat.datas.get(sample_index-1).offset+mdat.datas.get(sample_index-1).size;
		}else {
			this.offset = sample_offset;
		}
		this.sample_index = sample_index + 1;
		this.size = stsz.sample_size_table.get(video_sample_count).longValue();
		if (keyframe_index == -1) {
			this.iskeyframe = 1;
		} else if ((this.sample_index == keyframe_index)) {
			this.iskeyframe = 1;
			if (stss.entry_count != (stss_index + 1)) {
				keyframe_index = stss.sync_sample_table.get(++stss_index).intValue();
			}
		}
		byte[] data = new byte[(int) this.size];
		sample_stream.read(data);
		this.data = Util.ByteArrayToHex(data);
	}
	
	public void SetAudioVariables(int k,int chunk_index, int sample_duration, MediaDataBox mdat,int sample_index,long sample_offset,SampleSizeBox stsz,int audio_sample_count,int keyframe_index,SyncSampleBox stss,int stss_index,InputStream sample_stream) throws IOException {
		this.chunk_number = chunk_index + 1;
		this.duration = sample_duration;
		if(k>0) {
			this.offset = mdat.datas.get(sample_index-1).offset+mdat.datas.get(sample_index-1).size;
		}else {
			this.offset = sample_offset;
		}
		this.sample_index = sample_index + 1;
		this.size = stsz.sample_size_table.get(audio_sample_count-1).longValue();

		byte[] data = new byte[(int) this.size];
		sample_stream.read(data);
		this.data = Util.ByteArrayToHex(data);
	}
}
