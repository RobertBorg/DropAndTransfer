// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: FileTransfer.proto

package com.rauban.dropandtransfer.protocol;

public final class FileTransfer {
  private FileTransfer() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface FileDropHeaderOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required uint64 size = 1;
    boolean hasSize();
    long getSize();
    
    // required bool isDir = 2;
    boolean hasIsDir();
    boolean getIsDir();
    
    // required string resourceName = 3;
    boolean hasResourceName();
    String getResourceName();
  }
  public static final class FileDropHeader extends
      com.google.protobuf.GeneratedMessage
      implements FileDropHeaderOrBuilder {
    // Use FileDropHeader.newBuilder() to construct.
    private FileDropHeader(Builder builder) {
      super(builder);
    }
    private FileDropHeader(boolean noInit) {}
    
    private static final FileDropHeader defaultInstance;
    public static FileDropHeader getDefaultInstance() {
      return defaultInstance;
    }
    
    public FileDropHeader getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.rauban.dropandtransfer.protocol.FileTransfer.internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.rauban.dropandtransfer.protocol.FileTransfer.internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required uint64 size = 1;
    public static final int SIZE_FIELD_NUMBER = 1;
    private long size_;
    public boolean hasSize() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getSize() {
      return size_;
    }
    
    // required bool isDir = 2;
    public static final int ISDIR_FIELD_NUMBER = 2;
    private boolean isDir_;
    public boolean hasIsDir() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public boolean getIsDir() {
      return isDir_;
    }
    
    // required string resourceName = 3;
    public static final int RESOURCENAME_FIELD_NUMBER = 3;
    private java.lang.Object resourceName_;
    public boolean hasResourceName() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getResourceName() {
      java.lang.Object ref = resourceName_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          resourceName_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getResourceNameBytes() {
      java.lang.Object ref = resourceName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        resourceName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      size_ = 0L;
      isDir_ = false;
      resourceName_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasSize()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasIsDir()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasResourceName()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeUInt64(1, size_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBool(2, isDir_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getResourceNameBytes());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(1, size_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(2, isDir_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getResourceNameBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeaderOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.rauban.dropandtransfer.protocol.FileTransfer.internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.rauban.dropandtransfer.protocol.FileTransfer.internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_fieldAccessorTable;
      }
      
      // Construct using com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        size_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        isDir_ = false;
        bitField0_ = (bitField0_ & ~0x00000002);
        resourceName_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader.getDescriptor();
      }
      
      public com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader getDefaultInstanceForType() {
        return com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader.getDefaultInstance();
      }
      
      public com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader build() {
        com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader buildPartial() {
        com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader result = new com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.size_ = size_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.isDir_ = isDir_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.resourceName_ = resourceName_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader) {
          return mergeFrom((com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader other) {
        if (other == com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader.getDefaultInstance()) return this;
        if (other.hasSize()) {
          setSize(other.getSize());
        }
        if (other.hasIsDir()) {
          setIsDir(other.getIsDir());
        }
        if (other.hasResourceName()) {
          setResourceName(other.getResourceName());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasSize()) {
          
          return false;
        }
        if (!hasIsDir()) {
          
          return false;
        }
        if (!hasResourceName()) {
          
          return false;
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              size_ = input.readUInt64();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              isDir_ = input.readBool();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              resourceName_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required uint64 size = 1;
      private long size_ ;
      public boolean hasSize() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getSize() {
        return size_;
      }
      public Builder setSize(long value) {
        bitField0_ |= 0x00000001;
        size_ = value;
        onChanged();
        return this;
      }
      public Builder clearSize() {
        bitField0_ = (bitField0_ & ~0x00000001);
        size_ = 0L;
        onChanged();
        return this;
      }
      
      // required bool isDir = 2;
      private boolean isDir_ ;
      public boolean hasIsDir() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public boolean getIsDir() {
        return isDir_;
      }
      public Builder setIsDir(boolean value) {
        bitField0_ |= 0x00000002;
        isDir_ = value;
        onChanged();
        return this;
      }
      public Builder clearIsDir() {
        bitField0_ = (bitField0_ & ~0x00000002);
        isDir_ = false;
        onChanged();
        return this;
      }
      
      // required string resourceName = 3;
      private java.lang.Object resourceName_ = "";
      public boolean hasResourceName() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getResourceName() {
        java.lang.Object ref = resourceName_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          resourceName_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setResourceName(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        resourceName_ = value;
        onChanged();
        return this;
      }
      public Builder clearResourceName() {
        bitField0_ = (bitField0_ & ~0x00000004);
        resourceName_ = getDefaultInstance().getResourceName();
        onChanged();
        return this;
      }
      void setResourceName(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        resourceName_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:com.rauban.dropandtransfer.protocol.FileDropHeader)
    }
    
    static {
      defaultInstance = new FileDropHeader(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:com.rauban.dropandtransfer.protocol.FileDropHeader)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022FileTransfer.proto\022#com.rauban.dropand" +
      "transfer.protocol\"C\n\016FileDropHeader\022\014\n\004s" +
      "ize\030\001 \002(\004\022\r\n\005isDir\030\002 \002(\010\022\024\n\014resourceName" +
      "\030\003 \002(\t"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_com_rauban_dropandtransfer_protocol_FileDropHeader_descriptor,
              new java.lang.String[] { "Size", "IsDir", "ResourceName", },
              com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader.class,
              com.rauban.dropandtransfer.protocol.FileTransfer.FileDropHeader.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
