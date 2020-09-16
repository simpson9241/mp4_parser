package boxes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import main.Util;

public class AVC1Box {
	public long sample_description_size;
	public String data_format;
	public long data_reference_index;
	public int version;
	public int revision_level;
	public String vendor;
	public long temporal_quality;
	public long spatial_quality;
	public int width;
	public int height;
	public double horizontal_resolution;
	public double vertical_resolution;
	public long data_size;
	public int frame_count;
	public String compressor_name;
	public int depth;
	public int color_table_id;
	
	public void SetAVC1Box(InputStream fis) throws Exception{
		//Reserved 6 bytes
		fis.skip(6);
		
		byte[] data_reference_index=new byte[2];
		fis.read(data_reference_index);
		this.data_reference_index=Util.ByteArrayToLong(data_reference_index);
		
		byte[] version=new byte[2];
		fis.read(version);
		this.version=(int)Util.ByteArrayToLong(version);
		
		byte[] revision_level=new byte[2];
		fis.read(revision_level);
		this.revision_level=(int)Util.ByteArrayToLong(revision_level);
		
		byte[] vendor=new byte[4];
		fis.read(vendor);
		this.vendor=new String(vendor,StandardCharsets.US_ASCII);
		
		byte[] temporal_quality=new byte[4];
		fis.read(temporal_quality);
		this.temporal_quality=Util.ByteArrayToLong(temporal_quality);
		
		byte[] spatial_quality=new byte[4];
		fis.read(spatial_quality);
		this.spatial_quality=Util.ByteArrayToLong(spatial_quality);
		
		byte[] width=new byte[2];
		fis.read(width);
		this.width=(int)Util.ByteArrayToLong(width);
		
		byte[] height=new byte[2];
		fis.read(height);
		this.height=(int)Util.ByteArrayToLong(height);
		
		byte[] horizontal_resolution_1=new byte[2];
		fis.read(horizontal_resolution_1);
		byte[] horizontal_resolution_2=new byte[2];
		fis.read(horizontal_resolution_2);
		this.horizontal_resolution=(double)Util.ByteArrayToLong(horizontal_resolution_1)+((double)Util.ByteArrayToLong(horizontal_resolution_2))*0.01;
		
		byte[] vertical_resolution_1=new byte[2];
		fis.read(vertical_resolution_1);
		byte[] vertical_resolution_2=new byte[2];
		fis.read(vertical_resolution_2);
		this.vertical_resolution=(double)Util.ByteArrayToLong(vertical_resolution_1)+((double)Util.ByteArrayToLong(vertical_resolution_2))*0.01;
		
		byte[] data_size=new byte[4];
		fis.read(data_size);
		this.data_size=Util.ByteArrayToLong(data_size);
		
		byte[] frame_count=new byte[2];
		fis.read(frame_count);
		this.frame_count=(int)Util.ByteArrayToLong(frame_count);
		
		byte[] compressor_name=new byte[32];
		fis.read(compressor_name);
		this.compressor_name=new String(compressor_name,StandardCharsets.US_ASCII);
		
		byte[] depth=new byte[2];
		fis.read(depth);
		this.depth=(int)Util.ByteArrayToLong(depth);
		
		byte[] color_table_id=new byte[2];
		fis.read(color_table_id);
		this.color_table_id=(int)Util.ByteArrayToLong(color_table_id);
	}
	
	@Override
	public String toString() {
		return "\t\t\t\t\t\tavc1\n"+
				"\t\t\t\t\t\t\tSample Description Size: "+sample_description_size+
				"\n\t\t\t\t\t\t\tType: AVC1"+
				"\n\t\t\t\t\t\t\tData Reference Index: "+data_reference_index+
				"\n\t\t\t\t\t\t\tVersion: "+version+
				"\n\t\t\t\t\t\t\tRevision Level: "+revision_level+
				"\n\t\t\t\t\t\t\tVendor: "+vendor+
				"\n\t\t\t\t\t\t\tTemporal Quality: "+temporal_quality+
				"\n\t\t\t\t\t\t\tSpatial Quality: "+spatial_quality+
				"\n\t\t\t\t\t\t\tWidth: "+width+
				"\n\t\t\t\t\t\t\tHeight: "+height+
				"\n\t\t\t\t\t\t\tHorizontal Resolution: "+horizontal_resolution+
				"\n\t\t\t\t\t\t\tVertical Resolution: "+vertical_resolution+
				"\n\t\t\t\t\t\t\tData Size: "+data_size+
				"\n\t\t\t\t\t\t\tFrame Count: "+frame_count+
				"\n\t\t\t\t\t\t\tCompressor Name: "+compressor_name+
				"\n\t\t\t\t\t\t\tDepth: "+depth+
				"\n\t\t\t\t\t\t\tColor Table ID: "+color_table_id+"\n";
	}
}
