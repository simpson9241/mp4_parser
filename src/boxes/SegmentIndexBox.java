package boxes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class SegmentIndexBox extends FullBox{
	public long reference_ID;
	public long timescale;
	public long earliest_presentation_time;
	public long first_offset;
	public long reference_count;
	public ArrayList<SegmentIndexTable> table=new ArrayList<>();
	public SegmentIndexBox(String boxtype, long v, long f) { //sidx
		super(boxtype, v, f);
	}
	
	public void SetSIDXBox(InputStream fis) throws IOException {
		byte[] reference_id= new byte[4];
		fis.read(reference_id);
		this.reference_ID= Util.ByteArrayToLong(reference_id);

		byte[] timescale= new byte[4];
		fis.read(timescale);
		this.timescale= Util.ByteArrayToLong(timescale);
		
		if(this.version==0) {
			byte[] earliest_presentation_time= new byte[4];
			fis.read(earliest_presentation_time);
			this.earliest_presentation_time= Util.ByteArrayToLong(earliest_presentation_time);

			byte[] first_offset= new byte[4];
			fis.read(first_offset);
			this.first_offset= Util.ByteArrayToLong(first_offset);

		}else {
			byte[] earliest_presentation_time= new byte[8];
			fis.read(earliest_presentation_time);
			this.earliest_presentation_time= Util.ByteArrayToLong(earliest_presentation_time);

			byte[] first_offset= new byte[8];
			fis.read(first_offset);
			this.first_offset= Util.ByteArrayToLong(first_offset);
		}
		
		//Resereved 2 bytes
		fis.skip(2);
		byte[] reference_count= new byte[2];
		fis.read(reference_count);
		this.reference_count= Util.ByteArrayToLong(reference_count);
		
		if(this.reference_count>0) {
			for(int i=0;i<this.reference_count;i++) {
				SegmentIndexTable entry=new SegmentIndexTable();
				byte[] temp_1= new byte[4];
				fis.read(temp_1);				
				entry.reference_type= (int)(Util.ByteArrayToLong(temp_1)>>>31);
				//뒤에 31비트 앤드 연산
				entry.referenced_size= (Util.ByteArrayToLong(temp_1)&2147483647);
				
				byte[] duration= new byte[4];
				fis.read(duration);
				entry.subsegment_duration= Util.ByteArrayToLong(duration);
				
				byte[] temp_2= new byte[4];
				fis.read(temp_2);				
				entry.contains_RAP= (int)(Util.ByteArrayToLong(temp_2)>>>31);
				//뒤에 31비트 앤드 연산
				entry.RAP_delta_time= (Util.ByteArrayToLong(temp_2)&2147483647);
			
				this.table.add(entry);
			}
		}
	}

	@Override
	public String toString() {
		return "sidx\n"+
				"\tSize: "+size+
				"\n\tType: SegmentIndexBox"+
				"\n\tVersion: "+version+
				"\n\tFlags: "+flags+
				"\n\tReference ID: "+reference_ID+
				"\n\tTimescale: "+timescale+
				"\n\tEarliest Presentation Time: "+earliest_presentation_time+
				"\n\tFirst Offset: "+first_offset+
				"\n\tReference Count: "+reference_count+"\n";
	}
	
	public void PrintTable() {
			if(this.reference_count>10) {

				System.out.println("\tReference Table");
				for(int i=0;i<10;i++) {
					System.out.println(
							"\n\tReference: "+i+
							"\n\tReference Type: "+this.table.get(i).reference_type+
							"\tReferenced Size: "+this.table.get(i).referenced_size+
							"\tSubSegment Duration: "+this.table.get(i).subsegment_duration+
							"\tContains RAP: "+this.table.get(i).contains_RAP+
							"\tRAP Delta Time: "+this.table.get(i).RAP_delta_time);
				}
			}else {
				System.out.println("\n\tReference Table");
				for(int i=0;i<this.reference_count;i++) {
					System.out.println(
							"\n\tReference: "+i+
							"\n\tReference Type: "+this.table.get(i).reference_type+
							"\tReferenced Size: "+this.table.get(i).referenced_size+
							"\tSubSegment Duration: "+this.table.get(i).subsegment_duration+
							"\tContains RAP: "+this.table.get(i).contains_RAP+
							"\tRAP Delta Time: "+this.table.get(i).RAP_delta_time);
				}
			}
	}
	
}
