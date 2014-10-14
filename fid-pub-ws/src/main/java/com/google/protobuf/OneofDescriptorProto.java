// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/kirillternovsky/Documents/web/fid-pub-ws/src/main/proto/descriptor.proto
package com.google.protobuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

/**
 * Describes a oneof.
 */
public final class OneofDescriptorProto extends Message {

  public static final String DEFAULT_NAME = "";

  @ProtoField(tag = 1, type = STRING)
  public final String name;

  public OneofDescriptorProto(String name) {
    this.name = name;
  }

  private OneofDescriptorProto(Builder builder) {
    this(builder.name);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof OneofDescriptorProto)) return false;
    return equals(name, ((OneofDescriptorProto) other).name);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = name != null ? name.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<OneofDescriptorProto> {

    public String name;

    public Builder() {
    }

    public Builder(OneofDescriptorProto message) {
      super(message);
      if (message == null) return;
      this.name = message.name;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    @Override
    public OneofDescriptorProto build() {
      return new OneofDescriptorProto(this);
    }
  }
}
