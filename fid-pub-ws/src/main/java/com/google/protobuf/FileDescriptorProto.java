// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/kirillternovsky/Documents/web/fid-pub-ws/src/main/proto/descriptor.proto
package com.google.protobuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REPEATED;

/**
 * Describes a complete .proto file.
 */
public final class FileDescriptorProto extends Message {

  public static final String DEFAULT_NAME = "";
  public static final String DEFAULT_PACKAGE = "";
  public static final List<String> DEFAULT_DEPENDENCY = Collections.emptyList();
  public static final List<Integer> DEFAULT_PUBLIC_DEPENDENCY = Collections.emptyList();
  public static final List<Integer> DEFAULT_WEAK_DEPENDENCY = Collections.emptyList();
  public static final List<DescriptorProto> DEFAULT_MESSAGE_TYPE = Collections.emptyList();
  public static final List<EnumDescriptorProto> DEFAULT_ENUM_TYPE = Collections.emptyList();
  public static final List<ServiceDescriptorProto> DEFAULT_SERVICE = Collections.emptyList();
  public static final List<FieldDescriptorProto> DEFAULT_EXTENSION = Collections.emptyList();

  @ProtoField(tag = 1, type = STRING)
  public final String name;

  /**
   * file name, relative to root of source tree
   */
  @ProtoField(tag = 2, type = STRING)
  public final String _package;

  /**
   * e.g. "foo", "foo.bar", etc.
   * Names of files imported by this file.
   */
  @ProtoField(tag = 3, type = STRING, label = REPEATED)
  public final List<String> dependency;

  /**
   * Indexes of the public imported files in the dependency list above.
   */
  @ProtoField(tag = 10, type = INT32, label = REPEATED)
  public final List<Integer> public_dependency;

  /**
   * Indexes of the weak imported files in the dependency list.
   * For Google-internal migration only. Do not use.
   */
  @ProtoField(tag = 11, type = INT32, label = REPEATED)
  public final List<Integer> weak_dependency;

  /**
   * All top-level definitions in this file.
   */
  @ProtoField(tag = 4, label = REPEATED)
  public final List<DescriptorProto> message_type;

  @ProtoField(tag = 5, label = REPEATED)
  public final List<EnumDescriptorProto> enum_type;

  @ProtoField(tag = 6, label = REPEATED)
  public final List<ServiceDescriptorProto> service;

  @ProtoField(tag = 7, label = REPEATED)
  public final List<FieldDescriptorProto> extension;

  @ProtoField(tag = 8)
  public final FileOptions options;

  /**
   * This field contains optional information about the original source code.
   * You may safely remove this entire field whithout harming runtime
   * functionality of the descriptors -- the information is needed only by
   * development tools.
   */
  @ProtoField(tag = 9)
  public final SourceCodeInfo source_code_info;

  public FileDescriptorProto(String name, String _package, List<String> dependency, List<Integer> public_dependency, List<Integer> weak_dependency, List<DescriptorProto> message_type, List<EnumDescriptorProto> enum_type, List<ServiceDescriptorProto> service, List<FieldDescriptorProto> extension, FileOptions options, SourceCodeInfo source_code_info) {
    this.name = name;
    this._package = _package;
    this.dependency = immutableCopyOf(dependency);
    this.public_dependency = immutableCopyOf(public_dependency);
    this.weak_dependency = immutableCopyOf(weak_dependency);
    this.message_type = immutableCopyOf(message_type);
    this.enum_type = immutableCopyOf(enum_type);
    this.service = immutableCopyOf(service);
    this.extension = immutableCopyOf(extension);
    this.options = options;
    this.source_code_info = source_code_info;
  }

  private FileDescriptorProto(Builder builder) {
    this(builder.name, builder._package, builder.dependency, builder.public_dependency, builder.weak_dependency, builder.message_type, builder.enum_type, builder.service, builder.extension, builder.options, builder.source_code_info);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof FileDescriptorProto)) return false;
    FileDescriptorProto o = (FileDescriptorProto) other;
    return equals(name, o.name)
        && equals(_package, o._package)
        && equals(dependency, o.dependency)
        && equals(public_dependency, o.public_dependency)
        && equals(weak_dependency, o.weak_dependency)
        && equals(message_type, o.message_type)
        && equals(enum_type, o.enum_type)
        && equals(service, o.service)
        && equals(extension, o.extension)
        && equals(options, o.options)
        && equals(source_code_info, o.source_code_info);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = name != null ? name.hashCode() : 0;
      result = result * 37 + (_package != null ? _package.hashCode() : 0);
      result = result * 37 + (dependency != null ? dependency.hashCode() : 1);
      result = result * 37 + (public_dependency != null ? public_dependency.hashCode() : 1);
      result = result * 37 + (weak_dependency != null ? weak_dependency.hashCode() : 1);
      result = result * 37 + (message_type != null ? message_type.hashCode() : 1);
      result = result * 37 + (enum_type != null ? enum_type.hashCode() : 1);
      result = result * 37 + (service != null ? service.hashCode() : 1);
      result = result * 37 + (extension != null ? extension.hashCode() : 1);
      result = result * 37 + (options != null ? options.hashCode() : 0);
      result = result * 37 + (source_code_info != null ? source_code_info.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<FileDescriptorProto> {

    public String name;
    public String _package;
    public List<String> dependency;
    public List<Integer> public_dependency;
    public List<Integer> weak_dependency;
    public List<DescriptorProto> message_type;
    public List<EnumDescriptorProto> enum_type;
    public List<ServiceDescriptorProto> service;
    public List<FieldDescriptorProto> extension;
    public FileOptions options;
    public SourceCodeInfo source_code_info;

    public Builder() {
    }

    public Builder(FileDescriptorProto message) {
      super(message);
      if (message == null) return;
      this.name = message.name;
      this._package = message._package;
      this.dependency = copyOf(message.dependency);
      this.public_dependency = copyOf(message.public_dependency);
      this.weak_dependency = copyOf(message.weak_dependency);
      this.message_type = copyOf(message.message_type);
      this.enum_type = copyOf(message.enum_type);
      this.service = copyOf(message.service);
      this.extension = copyOf(message.extension);
      this.options = message.options;
      this.source_code_info = message.source_code_info;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * file name, relative to root of source tree
     */
    public Builder _package(String _package) {
      this._package = _package;
      return this;
    }

    /**
     * e.g. "foo", "foo.bar", etc.
     * Names of files imported by this file.
     */
    public Builder dependency(List<String> dependency) {
      this.dependency = checkForNulls(dependency);
      return this;
    }

    /**
     * Indexes of the public imported files in the dependency list above.
     */
    public Builder public_dependency(List<Integer> public_dependency) {
      this.public_dependency = checkForNulls(public_dependency);
      return this;
    }

    /**
     * Indexes of the weak imported files in the dependency list.
     * For Google-internal migration only. Do not use.
     */
    public Builder weak_dependency(List<Integer> weak_dependency) {
      this.weak_dependency = checkForNulls(weak_dependency);
      return this;
    }

    /**
     * All top-level definitions in this file.
     */
    public Builder message_type(List<DescriptorProto> message_type) {
      this.message_type = checkForNulls(message_type);
      return this;
    }

    public Builder enum_type(List<EnumDescriptorProto> enum_type) {
      this.enum_type = checkForNulls(enum_type);
      return this;
    }

    public Builder service(List<ServiceDescriptorProto> service) {
      this.service = checkForNulls(service);
      return this;
    }

    public Builder extension(List<FieldDescriptorProto> extension) {
      this.extension = checkForNulls(extension);
      return this;
    }

    public Builder options(FileOptions options) {
      this.options = options;
      return this;
    }

    /**
     * This field contains optional information about the original source code.
     * You may safely remove this entire field whithout harming runtime
     * functionality of the descriptors -- the information is needed only by
     * development tools.
     */
    public Builder source_code_info(SourceCodeInfo source_code_info) {
      this.source_code_info = source_code_info;
      return this;
    }

    @Override
    public FileDescriptorProto build() {
      return new FileDescriptorProto(this);
    }
  }
}
