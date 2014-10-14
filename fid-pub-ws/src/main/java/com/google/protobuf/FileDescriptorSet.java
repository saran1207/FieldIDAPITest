// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/kirillternovsky/Documents/web/fid-pub-ws/src/main/proto/descriptor.proto
package com.google.protobuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

/**
 * The protocol compiler can output a FileDescriptorSet containing the .proto
 * files it parses.
 */
public final class FileDescriptorSet extends Message {

  public static final List<FileDescriptorProto> DEFAULT_FILE = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED)
  public final List<FileDescriptorProto> file;

  public FileDescriptorSet(List<FileDescriptorProto> file) {
    this.file = immutableCopyOf(file);
  }

  private FileDescriptorSet(Builder builder) {
    this(builder.file);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof FileDescriptorSet)) return false;
    return equals(file, ((FileDescriptorSet) other).file);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = file != null ? file.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<FileDescriptorSet> {

    public List<FileDescriptorProto> file;

    public Builder() {
    }

    public Builder(FileDescriptorSet message) {
      super(message);
      if (message == null) return;
      this.file = copyOf(message.file);
    }

    public Builder file(List<FileDescriptorProto> file) {
      this.file = checkForNulls(file);
      return this;
    }

    @Override
    public FileDescriptorSet build() {
      return new FileDescriptorSet(this);
    }
  }
}
