package boxes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import main.Util;

public class MP4ABox {
	public long start_position;
	public long end_position;
	public long sample_description_size;
	public String data_format;
	public long data_reference_index;
	public int channelcount;
	public int samplesize;
	public int pre_defined;
	public long samplerate;
	
	public void SetMP4ABox(InputStream fis) throws Exception{
		//Reserved 6 bytes
		fis.skip(6);
		
		byte[] data_reference_index=new byte[2];
		fis.read(data_reference_index);
		this.data_reference_index=Util.ByteArrayToLong(data_reference_index);
		
		//reserved 8 bytes
		fis.skip(8);
		
		byte[] channelcount=new byte[2];
		fis.read(channelcount);
		this.channelcount=(int)Util.ByteArrayToLong(channelcount);
		
		byte[] samplesize=new byte[2];
		fis.read(samplesize);
		this.samplesize=(int)Util.ByteArrayToLong(samplesize);
		
		
		byte[] pre_defined=new byte[2];
		fis.read(pre_defined);
		this.pre_defined=(int)Util.ByteArrayToLong(pre_defined);
		if(this.pre_defined==0xfffe) {
			this.pre_defined=-2;
		}else if(this.pre_defined==0x0010) {
			this.pre_defined=16;
		}
		
		//reserved 2 bytes
		fis.skip(2);
		
		byte[] samplerate=new byte[4];
		fis.read(samplerate);
		this.samplerate=(Util.ByteArrayToLong(samplerate)>>16);
		
		fis.skip(sample_description_size-20-16);
	}
	
	@Override
	public String toString() {
		return "\t\t\t\t\t\tmp4a\n"+
				"\t\t\t\t\t\t\tSample Description Size: "+sample_description_size+
				"\n\t\t\t\t\t\t\tType: MP4A"+
				"\n\t\t\t\t\t\t\tData Reference Index: "+data_reference_index+
				"\n\t\t\t\t\t\t\tChannel Count: "+channelcount+
				"\n\t\t\t\t\t\t\tSample Size: "+samplesize+
				"\n\t\t\t\t\t\t\tPredefined: "+pre_defined+
				"\n\t\t\t\t\t\t\tSample Rate: "+samplerate+"\n";
	}
	
}
