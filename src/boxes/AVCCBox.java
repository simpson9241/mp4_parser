package boxes;

import java.io.InputStream;
import java.util.ArrayList;

import main.Util;

public class AVCCBox {
	//ISO/IEC 14496-15 18페이지
	public long size;
	public String type;
	public int configurationVersion=1;
	public int AVCProfileIndication;
	public int profile_compatibility;
	public int AVCLevelIndication;
	public int lengthSizeMinusOne;
	public int numOfSequenceParameterSets;
	public int numOfPictureParameterSets;
	public ArrayList<SequenceParameterSet> sequenceParameterSets=new ArrayList<>();
	public ArrayList<PictureParameterSet> pictureParameterSets=new ArrayList<>();

	
	public void SetAVCCBox(InputStream fis) throws Exception{
		byte[] configurationVersion = new byte[1];
		fis.read(configurationVersion);
		this.configurationVersion = (int)Util.ByteArrayToLong(configurationVersion);
		
		byte[] AVCProfileIndication = new byte[1];
		fis.read(AVCProfileIndication);
		this.AVCProfileIndication = (int)Util.ByteArrayToLong(AVCProfileIndication);

		byte[] profile_compatibility = new byte[1];
		fis.read(profile_compatibility);
		this.profile_compatibility = (int)Util.ByteArrayToLong(profile_compatibility);
		
		byte[] AVCLevelIndication = new byte[1];
		fis.read(AVCLevelIndication);
		this.AVCLevelIndication = (int)Util.ByteArrayToLong(AVCLevelIndication);
		
		byte[] lengthSizeMinusOne= new byte[1];
		fis.read(lengthSizeMinusOne);
		this.lengthSizeMinusOne = (int)(Util.ByteArrayToLong(lengthSizeMinusOne)&0x00000011);
		
		byte[] numOfSequenceParameterSets= new byte[1];
		fis.read(numOfSequenceParameterSets);
		this.numOfSequenceParameterSets = (int)(Util.ByteArrayToLong(numOfSequenceParameterSets)&0x00011111);
	
		for(int i=0;i<this.numOfSequenceParameterSets;i++) {
			SequenceParameterSet sequenceparameterset=new SequenceParameterSet();
			byte[] sequenceParameterSetLength= new byte[2];
			fis.read(sequenceParameterSetLength);
			sequenceparameterset.sequenceParameterSetLength=(int)Util.ByteArrayToLong(sequenceParameterSetLength);
			this.sequenceParameterSets.add(sequenceparameterset);
			fis.skip(this.sequenceParameterSets.get(i).sequenceParameterSetLength);
		}
		byte[] numOfPictureParameterSets= new byte[1];
		fis.read(numOfPictureParameterSets);
		this.numOfPictureParameterSets = (int)Util.ByteArrayToLong(numOfPictureParameterSets);
	
		for(int i=0;i<this.numOfPictureParameterSets;i++) {
			PictureParameterSet pictureparameterset=new PictureParameterSet();
			byte[] pictureParameterSetLength= new byte[2];
			fis.read(pictureParameterSetLength);
			pictureparameterset.pictureParameterSetLength=(int)Util.ByteArrayToLong(pictureParameterSetLength);
			this.pictureParameterSets.add(pictureparameterset);
			fis.skip(this.pictureParameterSets.get(i).pictureParameterSetLength);
		}
	}
	
	@Override
	public String toString() {
		return "\t\t\t\t\t\t\tavcC\n"+
				"\t\t\t\t\t\t\t\tSize: "+size+
				"\n\t\t\t\t\t\t\t\tType: AVCConfigurationBox"+
				"\n\t\t\t\t\t\t\t\tConfiguration Version: "+configurationVersion+
				"\n\t\t\t\t\t\t\t\tAVC Profile Indication: "+AVCProfileIndication+
				"\n\t\t\t\t\t\t\t\tProfile Compatibility: "+profile_compatibility+
				"\n\t\t\t\t\t\t\t\tAVC Level Indication: "+AVCLevelIndication+
				"\n\t\t\t\t\t\t\t\tLength Size Minus One: "+lengthSizeMinusOne+
				"\n\t\t\t\t\t\t\t\tNum of Sequence Parameter Sets: "+numOfSequenceParameterSets+
				"\n\t\t\t\t\t\t\t\tNum of Picture Parameter Sets: "+numOfPictureParameterSets+"\n";
	}
}
