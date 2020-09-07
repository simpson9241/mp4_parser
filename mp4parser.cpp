#include <stdio.h>
// 파일 열기
// 헤더 읽기
// Atom 확인
// Atom 파싱

struct FileTypeBox{
  unsigned int size;
  unsigned int major_brand; //32비트
  unsigned int minor_version; //32비트
  unsigned int* compatible_brands; //32비트 * brands_cnt
  // compatible_brands=(unsigned int*)malloc(sizeof(unsigned int)*brands_cnt);
  // for(int i=0;i<brands_cnt;i++){
  //   compatible_brands[i]=(unsigned int*)malloc(sizeof(unsigend int));
  // }
}ftyp;

struct MediaDataBox{
  unsigned int size;
  unsigned int data : 8;
}mdat;

struct FreeSpaceBox{
  unsigned int size;
  unsigned int data : 8;
}fsb;

struct MovieBox{
  unsigned int size;
  //trak

}moov;

struct MovieHeaderBox{
  // version은 int
  unsigned int size;
  // version 1일 때: 64 64 32 64(비트)
  // version 0일 때: 32 32 32 32(비트)
  unsigned int creation_time;
  unsigned int modification_time;
  unsigned int timescale;
  unsigned int duration;

  unsigned int rate=0x00010000; //32비트
  unsigned int volume=0x0100; //16비트
  //const bit reserved=0; 16비트
  //const unsigned int[2] reserved=0; 32비트
  unsigned int matrix[9]={0x00010000,0,0,0,0x00010000,0,0,0,0x40000000}; // 32비트
  //bit[6] pre_defined=0; 192비트
  unsigned int next_track_ID; //32비트
}mvhd;

struct TrackBox{
  unsigned int size;
  //tkhd
  //mdia
  //edts
}trak;

struct TrackHeaderBox{
  unsigned int size;
  // version은 int
  // version 1일 때: 64 64 32 32 64(비트)
  // version 0일 때: 32 32 32 32 32(비트)
  unsigned int creation_time;
  unsigned int modification_time;
  unsigned int track_ID;
  // unsigned int reserved=0;
  unsigned int duration;

  unsigned int reserved=0; //64비트
  unsigned int layer=0; //16비트
  unsigned int alternate_group=0; //16비트
  unsigned int volume; //track이 오디오라면 0x0100 아니면 0 16비트
  // unsigned int reserved=0; 16비트
  unsigned int matrix[9]={0x00010000,0,0,0,0x00010000,0,0,0,0x40000000};
  unsigned int width; //32비트
  unsigned int height; //32비트

  //tref
}tkhd;

struct TrackReferenceBox{
  unsigned int size;

}tref;

struct TrackReferenceTypeBox{
  unsigned int size;
  unsigned int reference_type; //32비트
  // unsigned int track_IDs[]; //32비트
};

struct MediaBox{
  unsigned int size;
  //mdhd
  //hdlr
  //minf
}mdia;

struct MediaHeaderBox{
  unsigned int size;
  // version은 int
  // version 1일 때: 64 64 32 64
  // version 0일 때: 32 32 32 32
  unsigned int creation_time;
  unsigned int modification_time;
  unsigned int timescale;
  unsigned int duration;

  //bit pad=0; 1비트
  unsigned int language[3]; //15비트
  unsigned int pre_defined=0; // 16비트
}mdhd;

struct HandlerReferenceBox{ //mdia 또는 meta
  unsigned int size;
  unsigned int pre_defined=0; //32비트
  unsigned int handler_type; //32비트
  //const unsigned int reserved[3]=0; // 96비트
  char name[128]; //임의
}hdlr;

struct MediaInformationBox{
  unsigned int size;
  //vmhd
  //smhd
  //hmhd
  //nmhd

  //stbl
}minf;

struct VideoMediaHeaderBox{
  unsigned int size;
  unsigned int graphicsmode=0; //16비트
  unsigned int opcolor[3]={0,0,0}; //16비트
}vmhd;

struct SoundMediaHeaderBox{
  unsigned int size;
  unsigned int balance=0; //16비트
  unsigned int reserved=0; //16비트
}smhd;

struct HintMediaHeaderBox{
  unsigned int size;
  unsigned int maxPDUsize; //16비트
  unsigned int avgPDUsize; //16비트
  unsigned int maxbitrate; //32비트
  unsigned int avgbitrate; //32비트
  unsigned int reserved=0; //32비트
}hmhd;

struct NullMediaHeaderBox{
  unsigned int size;

}nmhd;

struct SampleTableBox{
  unsigned int size;
  //stsd
  //stts
  //ctts
  //stss
  //stsh
}stbl;

struct SampleDescriptionBox{
  unsigned int size;
  //공식 문서 28페이지 참조하기

}stsd;

struct SampleEntry{
  unsigned int size;
  const unsigned int reserved[6]=0; //8비트
  unsigned int data_reference_index; //16비트
};

struct HintSampleEntry{
  unsigned int size;
  unsigned int* data; //8비트
};

struct BitRateBox{
  unsigned int size;
  unsigned int bufferSizeDB; //32비트
  unsigned int maxBitrate; //32비트
  unsigned int abgBitrate; //32비트
}btrt;

struct MetaDataSampleEntry{
  unsigned int size;
};

struct XMLMetaDataSampleEntry{
  unsigned int size;
  string content_encoding; //optional
  string namespace;
  string schema_location; //optional
  // BitrateBox (); // optional
}metx;

struct TextMetaDataSampleEntry{
  unsigned int size;
  string content_encoding; //optional
  string mime_format;
  //BitrateBox (); //optional
}mett;

struct PixelAspectRatioBox{
  unsigned int size;
  unsigned int hSpacing; //32비트
  unsigned int vSpacing; //32비트
}pasp;

struct CleanApertureBox{
  unsigned int size;
  unsigned int cleanApertureWidthN; //32비트
  unsigned int cleanApertureWidthD; //32비트
  unsigned int cleanApertureHeightN; //32비트
  unsigned int cleanApertureHeightD; //32비트
  unsigned int horizoffN; //32비트
  unsigned int horizOffD; //32비트
  unsigned int vertOffN; //32비트
  unsigned int vertOffD; //32비트
}clap;

struct VisualSampleEntry{
  unsigned int size;
  // unsigned int pre_defined=0; //16비트
  const unsigned int reserved=0; //16비트
  //unsigned int pre_defined[3]=0; //96비트
  unsigned int width; //16비트
  unsigned int height; //16비트
  unsigned int horizresolution=0x00480000; //72 dpi 32비트
  unsigned int vertresolution=0x00480000; //72 dpi 32비트
  // const unsigned int reserved=0; // 32비트
  unsigned int frame_count=1; //16비트
  string compressorname[32];
  unsigned int depth=0x0018; //16비트
  int pre_defined=-1; //16비트
  CleanApertureBox clap; //optional
  PixelAspectRatioBox pasp; //optional
};

struct AudioSampleEntry{
  unsigned int size;
  // const unsigned int reserved[2]=0; //32비트
  unsigned int channelcount=2; //16비트
  unsigned int samplesize=16; //16비트
  unsigned int pre_defined=0; //16비트
  const unsigned int reserved=0; //16비트
  // unsigned int samplerate= {default samplerate of media}<<16; //32비트
};

struct DegradationPriorityBox{
  unsigned int size;
  unsigned int* priority; //16비트
  // sampe_count 값에 맞춰 배열 동적 할당
}stdp;

struct SampleScaleBox{
  unsigned int size;
  // bit reserved=0; //7비트
  // bit constraint_flag; //1비트
  unsigned int scale_method; //8비트
  int display_center_x; //16비트
  int display_center_y; //16비트
};

struct TimeToSampleBox{
  unsigned int size;
  unsigned int entry_count;//32비트
  unsigned int sample_count;//32비트 샘플 GOP 테이블의 개수
  unsigned int sample_delta; //32비트 샘플 GOP 테이블의 델타 값
  //공식문서 32페이지 참조
}stts;

struct CompositionTimetoSampleBox{
  unsigned int size;
  unsigned int entry_count; //32비트
  unsigned int sample_count; //32비트
  unsigned int sample_offset; //32비트
  //공식문서 33페이지 참조
}ctts;

struct SyncSampleBox{
  unsigned int size;
  unsigned int entry_count;// 32비트
  unsigned int sample_number; //32비트 샘플 개수
}stss;

struct ShadowSyncSampleBox{
  unsigned int size;
  unsigned int entry_count; //32비트 엔트리 개수
  unsigned int shadowed_sample_number; //32비트 alternative sync sample이 있는 샘플의 개수
  unsigned int sync_sample_number; //32비트 alternative sync sample의 개수
}stsh;

struct EditBox{
  unsigned int size;
  //elst
}edts;

struct EditListBox{
  unsigned int size;
  unsigned int entry_count; //32비트
  //version 1: 64 64
  //version 0: 32 32
  unsigned int segment_duration;
  int media_time;

  int media_rate_integer; //16비트
  int media_rate_fraction=0; //16비트

}eslt;

struct TrackDataLayoutStructures{
  unsigned int size;
}dinf;

struct DataEntryUrlBox{
  unsigned int size;
  string location;
}url;

struct DataEntryUrnBox{
  unsigned int size;
  string name;
  string location;
}urn;

struct DataReferenceBox{
  unsigned int size;
  unsigned int entry_count; //32비트
  DataEntryBox* data_entry; //entry_count만큼 malloc
}dref;

struct SampleSizeBox{
  unsigned int size;
  unsigned int sample_size; //32비트
  unsigned int sample_count; //32비트
}stsz;

struct CompactSampleSizeBox{
  unsigned int size;
  unsigned int reserved=0; //24비트
  unsigned int field_size; //8비트
  unsigned int sample_count; //32비트
  unsigned int entry_size; //24비트 샘플 크기 측정
}stz2;

struct SampleToChunkBox{
  unsigned int size;
  unsigned int entry_count; //32비트
  //entry_count만큼 밑 코드 반복
  unsigned int first_chunk; //32비트
  unsigned int samples_per_chunk; //32비트
  unsigned int sample_description_index; 32비트

}stsc;


//40페이지 완료

// 메인 함수
int main(){


  return 0;
}
