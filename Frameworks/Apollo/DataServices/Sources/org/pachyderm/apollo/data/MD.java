//
//  MDItem.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/23/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

/**
 *  String constants for the system defined metadata (MD) keys. 
 *  This is adopted from the Metadata framework in Mac OS X 10.4.
 */

public final class MD {
	
  /*------------------------------------------------------------------------------------------------*
   *  Common Metadata Attribute Keys
   *------------------------------------------------------------------------------------------------*/
  
	public static final String AttributeChangeDate = "AttributeChangeDate";
	public static final String Audiences = "Audiences";
	public static final String Authors = "Authors";
	public static final String City = "City";
	public static final String Comment = "Comment";
	public static final String ContactKeywords = "ContactKeywords";
	public static final String ContentCreationDate = "ContentCreationDate";
	public static final String ContentModificationDate = "ContentModificationDate";
	public static final String ContentType = "ContentType";
	public static final String Contributors = "Contributors";
	public static final String Copyright = "Copyright";
	public static final String Country = "Country";
	public static final String Coverage = "Coverage";
	public static final String Creator = "Creator";
	public static final String Description = "Description";
	public static final String DueDate = "DueDate";
	public static final String DurationSeconds = "DurationSeconds";
	public static final String EmailAddresses = "EmailAddresses";
	public static final String EncodingApplications = "EncodingApplications";
	public static final String FinderComment = "FinderComment";
	public static final String Fonts = "Fonts";
	public static final String Headline = "Headline";
	public static final String Identifier = "Identifier";
	public static final String InstantMessageAddresses = "InstantMessageAddresses";
	public static final String Instructions = "Instructions";
	public static final String Keywords = "Keywords";
	public static final String Kind = "Kind";
	public static final String Languages = "Languages";
	public static final String LastUsedDate = "LastUsedDate";
	public static final String NumberOfPages = "NumberOfPages";
	public static final String Organizations = "Organizations";
	public static final String PageHeight = "PageHeight";
	public static final String PageWidth = "PageWidth";
	public static final String PhoneNumbers = "PhoneNumbers";
	public static final String Projects = "Projects";
	public static final String Publishers = "Publishers";
	public static final String Recipients = "Recipients";
	public static final String Rights = "Rights";
	public static final String SecurityMethod = "SecurityMethod";
	public static final String StarRating = "StarRating";
	public static final String StateOrProvince = "StateOrProvince";
	public static final String TextContent = "TextContent";
	public static final String Title = "Title";
	public static final String Version = "Version";
	public static final String WhereFroms = "WhereFroms";
	
  /*------------------------------------------------------------------------------------------------*
   *  Image Metadata Attribute Keys
   *------------------------------------------------------------------------------------------------*/
	
	public static final String PixelHeight = "PixelHeight";
	public static final String PixelWidth = "PixelWidth";
	public static final String ColorSpace = "ColorSpace";
	public static final String BitsPerSample = "BitsPerSample";
	public static final String FlashOnOff = "FlashOnOff";
	public static final String FocalLength = "FocalLength";
	public static final String AcquisitionMake = "AcquisitionMake";
	public static final String AcquisitionModel = "AcquisitionModel";
	public static final String ISOSpeed = "ISOSpeed";
	public static final String Orientation = "Orientation";
	public static final String LayerNames = "LayerNames";
	public static final String WhiteBalance = "WhiteBalance";
	public static final String Aperture = "Aperture";
	public static final String ProfileName = "ProfileName";
	public static final String ResolutionWidthDPI = "ResolutionWidthDPI";
	public static final String ResolutionHeightDPI = "ResolutionHeightDPI";
	public static final String ExposureMode = "ExposureMode";
	public static final String ExposureTimeSeconds = "ExposureTimeSeconds";
	public static final String EXIFVersion = "EXIFVersion";
	public static final String Album = "Album";
	public static final String HasAlphaChannel = "HasAlphaChannel";
	public static final String RedEyeOnOff = "RedEyeOnOff";
	public static final String MeteringMode = "MeteringMode";
	public static final String MaxAperture = "MaxAperture";
	public static final String FNumber = "FNumber";
	public static final String ExposureProgram = "ExposureProgram";
	public static final String ExposureTimeString = "ExposureTimeString";
	
  /*------------------------------------------------------------------------------------------------*
   *  Video Metadata Attribute Keys
   *------------------------------------------------------------------------------------------------*/
	
	public static final String AudioBitRate = "AudioBitRate";
	public static final String Codecs = "Codecs";
	public static final String DeliveryType = "DeliveryType";
	public static final String MediaTypes = "MediaTypes";
	public static final String Streamable = "Streamable";
	public static final String TotalBitRate = "TotalBitRate";
	public static final String VideoBitRate = "VideoBitRate";
	
  /*------------------------------------------------------------------------------------------------*
   *  Audio Metadata Attribute Keys
   *------------------------------------------------------------------------------------------------*/
	
	public static final String AppleLoopDescriptors = "AppleLoopDescriptors";
	public static final String AppleLoopsKeyFilterType = "AppleLoopsKeyFilterType";
	public static final String AppleLoopsLoopMode = "AppleLoopsLoopMode";
	public static final String AppleLoopsRootKey = "AppleLoopsRootKey";
	public static final String AudioChannelCount = "AudioChannelCount";
	public static final String AudioEncodingApplication = "AudioEncodingApplication";
	public static final String AudioSampleRate = "AudioSampleRate";
	public static final String AudioTrackNumber = "AudioTrackNumber";
	public static final String Composer = "Composer";
	public static final String IsGeneralMIDISequence = "IsGeneralMIDISequence";
	public static final String KeySignature = "KeySignature";
	public static final String Lyricist = "Lyricist";
	public static final String MusicalGenre = "MusicalGenre";
	public static final String MusicalInstrumentCategory = "MusicalInstrumentCategory";
	public static final String MusicalInstrumentName = "MusicalInstrumentName";
	public static final String RecordingDate = "RecordingDate";
	public static final String RecordingYear = "RecordingYear";
	public static final String Tempo = "Tempo";
	public static final String TimeSignature = "TimeSignature";
	
  /*------------------------------------------------------------------------------------------------*
   *  File System Metadata Attribute Keys
   *------------------------------------------------------------------------------------------------*/
	
	public static final String DisplayName = "DisplayName";
	public static final String FSContentChangeDate = "FSContentChangeDate";
	public static final String FSCreationDate = "FSCreationDate";
	public static final String FSExists = "FSExists";
	public static final String FSInvisible = "FSInvisible";
	public static final String FSIsExtensionHidden = "FSIsExtensionHidden";
	public static final String FSIsReadable = "FSIsReadable";
	public static final String FSIsWriteable = "FSIsWriteable";
	public static final String FSLabel = "FSLabel";
	public static final String FSName = "FSName";
	public static final String FSNodeCount = "FSNodeCount";
	public static final String FSOwnerGroupID = "FSOwnerGroupID";
	public static final String FSOwnerUserID = "FSOwnerUserID";
	public static final String FSSize = "FSSize";

  public static final String NetFailure = "NetFail";
  public static final String Path = "Path";		// use URL instead
	public static final String URL = "URL";
	
  /*------------------------------------------------------------------------------------------------*
   *  APOLLO Metadata Attribute Keys
   *------------------------------------------------------------------------------------------------*/
	
  public static final String _APOLLOPreview128 = "APOLLO_Preview128";
	static final String _APOLLOMetadataReferences = "APOLLO_MetadataReferences";
	static final String _APOLLOPreview64 = "APOLLO_Preview64";
	static final String _APOLLOPreview32 = "APOLLO_Preview32";
	static final String _APOLLOPreviewOriginal = "APOLLO_PreviewOriginal";
	static final String _APOLLORelationshipPrefix = "APOLLO_Relationship-";

}
