// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/kirillternovsky/Documents/web/fid-pub-ws/src/main/proto/descriptor.proto
package com.google.protobuf;

import com.squareup.wire.ExtendableMessage;
import com.squareup.wire.Extension;
import com.squareup.wire.ProtoEnum;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REPEATED;

/**
 * ===================================================================
 * Options
 * Each of the definitions above may have "options" attached.  These are
 * just annotations which may cause code to be generated slightly differently
 * or may contain hints for code that manipulates protocol messages.
 *
 * Clients may define custom options as extensions of the *Options messages.
 * These extensions may not yet be known at parsing time, so the parser cannot
 * store the values in them.  Instead it stores them in a field in the *Options
 * message called uninterpreted_option. This field must have the same name
 * across all *Options messages. We then use this field to populate the
 * extensions when we build a descriptor, at which point all protos have been
 * parsed and so all extensions are known.
 *
 * Extension numbers for custom options may be chosen as follows:
 * * For options which will only be used within a single application or
 *   organization, or for experimental options, use field numbers 50000
 *   through 99999.  It is up to you to ensure that you do not use the
 *   same number for multiple options.
 * * For options which will be published and used publicly by multiple
 *   independent entities, e-mail protobuf-global-extension-registry@google.com
 *   to reserve extension numbers. Simply provide your project name (e.g.
 *   Object-C plugin) and your porject website (if available) -- there's no need
 *   to explain how you intend to use them. Usually you only need one extension
 *   number. You can declare multiple options with only one extension number by
 *   putting them in a sub-message. See the Custom Options section of the docs
 *   for examples:
 *   http://code.google.com/apis/protocolbuffers/docs/proto.html#options
 *   If this turns out to be popular, a web service will be set up
 *   to automatically assign option numbers.
 */
public final class FileOptions extends ExtendableMessage<FileOptions> {

  public static final String DEFAULT_JAVA_PACKAGE = "";
  public static final String DEFAULT_JAVA_OUTER_CLASSNAME = "";
  public static final Boolean DEFAULT_JAVA_MULTIPLE_FILES = false;
  public static final Boolean DEFAULT_JAVA_GENERATE_EQUALS_AND_HASH = false;
  public static final Boolean DEFAULT_JAVA_STRING_CHECK_UTF8 = false;
  public static final OptimizeMode DEFAULT_OPTIMIZE_FOR = OptimizeMode.SPEED;
  public static final String DEFAULT_GO_PACKAGE = "";
  public static final Boolean DEFAULT_CC_GENERIC_SERVICES = false;
  public static final Boolean DEFAULT_JAVA_GENERIC_SERVICES = false;
  public static final Boolean DEFAULT_PY_GENERIC_SERVICES = false;
  public static final Boolean DEFAULT_DEPRECATED = false;
  public static final List<UninterpretedOption> DEFAULT_UNINTERPRETED_OPTION = Collections.emptyList();

  /**
   * Sets the Java package where classes generated from this .proto will be
   * placed.  By default, the proto package is used, but this is often
   * inappropriate because proto packages do not normally start with backwards
   * domain names.
   */
  @ProtoField(tag = 1, type = STRING)
  public final String java_package;

  /**
   * If set, all the classes from the .proto file are wrapped in a single
   * outer class with the given name.  This applies to both Proto1
   * (equivalent to the old "--one_java_file" option) and Proto2 (where
   * a .proto always translates to a single class, but you may want to
   * explicitly choose the class name).
   */
  @ProtoField(tag = 8, type = STRING)
  public final String java_outer_classname;

  /**
   * If set true, then the Java code generator will generate a separate .java
   * file for each top-level message, enum, and service defined in the .proto
   * file.  Thus, these types will *not* be nested inside the outer class
   * named by java_outer_classname.  However, the outer class will still be
   * generated to contain the file's getDescriptor() method as well as any
   * top-level extensions defined in the file.
   */
  @ProtoField(tag = 10, type = BOOL)
  public final Boolean java_multiple_files;

  /**
   * If set true, then the Java code generator will generate equals() and
   * hashCode() methods for all messages defined in the .proto file.
   * - In the full runtime, this is purely a speed optimization, as the
   * AbstractMessage base class includes reflection-based implementations of
   * these methods.
   * - In the lite runtime, setting this option changes the semantics of
   * equals() and hashCode() to more closely match those of the full runtime;
   * the generated methods compute their results based on field values rather
   * than object identity. (Implementations should not assume that hashcodes
   * will be consistent across runtimes or versions of the protocol compiler.)
   */
  @ProtoField(tag = 20, type = BOOL)
  public final Boolean java_generate_equals_and_hash;

  /**
   * If set true, then the Java2 code generator will generate code that
   * throws an exception whenever an attempt is made to assign a non-UTF-8
   * byte sequence to a string field.
   * Message reflection will do the same.
   * However, an extension field still accepts non-UTF-8 byte sequences.
   * This option has no effect on when used with the lite runtime.
   */
  @ProtoField(tag = 27, type = BOOL)
  public final Boolean java_string_check_utf8;

  @ProtoField(tag = 9, type = ENUM)
  public final OptimizeMode optimize_for;

  /**
   * Sets the Go package where structs generated from this .proto will be
   * placed.  There is no default.
   */
  @ProtoField(tag = 11, type = STRING)
  public final String go_package;

  /**
   * Should generic services be generated in each language?  "Generic" services
   * are not specific to any particular RPC system.  They are generated by the
   * main code generators in each language (without additional plugins).
   * Generic services were the only kind of service generation supported by
   * early versions of proto2.
   *
   * Generic services are now considered deprecated in favor of using plugins
   * that generate code specific to your particular RPC system.  Therefore,
   * these default to false.  Old code which depends on generic services should
   * explicitly set them to true.
   */
  @ProtoField(tag = 16, type = BOOL)
  public final Boolean cc_generic_services;

  @ProtoField(tag = 17, type = BOOL)
  public final Boolean java_generic_services;

  @ProtoField(tag = 18, type = BOOL)
  public final Boolean py_generic_services;

  /**
   * Is this file deprecated?
   * Depending on the target platform, this can emit Deprecated annotations
   * for everything in the file, or it will be completely ignored; in the very
   * least, this is a formalization for deprecating files.
   */
  @ProtoField(tag = 23, type = BOOL)
  public final Boolean deprecated;

  /**
   * The parser stores options it doesn't recognize here. See above.
   */
  @ProtoField(tag = 999, label = REPEATED)
  public final List<UninterpretedOption> uninterpreted_option;

  public FileOptions(String java_package, String java_outer_classname, Boolean java_multiple_files, Boolean java_generate_equals_and_hash, Boolean java_string_check_utf8, OptimizeMode optimize_for, String go_package, Boolean cc_generic_services, Boolean java_generic_services, Boolean py_generic_services, Boolean deprecated, List<UninterpretedOption> uninterpreted_option) {
    this.java_package = java_package;
    this.java_outer_classname = java_outer_classname;
    this.java_multiple_files = java_multiple_files;
    this.java_generate_equals_and_hash = java_generate_equals_and_hash;
    this.java_string_check_utf8 = java_string_check_utf8;
    this.optimize_for = optimize_for;
    this.go_package = go_package;
    this.cc_generic_services = cc_generic_services;
    this.java_generic_services = java_generic_services;
    this.py_generic_services = py_generic_services;
    this.deprecated = deprecated;
    this.uninterpreted_option = immutableCopyOf(uninterpreted_option);
  }

  private FileOptions(Builder builder) {
    this(builder.java_package, builder.java_outer_classname, builder.java_multiple_files, builder.java_generate_equals_and_hash, builder.java_string_check_utf8, builder.optimize_for, builder.go_package, builder.cc_generic_services, builder.java_generic_services, builder.py_generic_services, builder.deprecated, builder.uninterpreted_option);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof FileOptions)) return false;
    FileOptions o = (FileOptions) other;
    if (!extensionsEqual(o)) return false;
    return equals(java_package, o.java_package)
        && equals(java_outer_classname, o.java_outer_classname)
        && equals(java_multiple_files, o.java_multiple_files)
        && equals(java_generate_equals_and_hash, o.java_generate_equals_and_hash)
        && equals(java_string_check_utf8, o.java_string_check_utf8)
        && equals(optimize_for, o.optimize_for)
        && equals(go_package, o.go_package)
        && equals(cc_generic_services, o.cc_generic_services)
        && equals(java_generic_services, o.java_generic_services)
        && equals(py_generic_services, o.py_generic_services)
        && equals(deprecated, o.deprecated)
        && equals(uninterpreted_option, o.uninterpreted_option);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = extensionsHashCode();
      result = result * 37 + (java_package != null ? java_package.hashCode() : 0);
      result = result * 37 + (java_outer_classname != null ? java_outer_classname.hashCode() : 0);
      result = result * 37 + (java_multiple_files != null ? java_multiple_files.hashCode() : 0);
      result = result * 37 + (java_generate_equals_and_hash != null ? java_generate_equals_and_hash.hashCode() : 0);
      result = result * 37 + (java_string_check_utf8 != null ? java_string_check_utf8.hashCode() : 0);
      result = result * 37 + (optimize_for != null ? optimize_for.hashCode() : 0);
      result = result * 37 + (go_package != null ? go_package.hashCode() : 0);
      result = result * 37 + (cc_generic_services != null ? cc_generic_services.hashCode() : 0);
      result = result * 37 + (java_generic_services != null ? java_generic_services.hashCode() : 0);
      result = result * 37 + (py_generic_services != null ? py_generic_services.hashCode() : 0);
      result = result * 37 + (deprecated != null ? deprecated.hashCode() : 0);
      result = result * 37 + (uninterpreted_option != null ? uninterpreted_option.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends ExtendableBuilder<FileOptions> {

    public String java_package;
    public String java_outer_classname;
    public Boolean java_multiple_files;
    public Boolean java_generate_equals_and_hash;
    public Boolean java_string_check_utf8;
    public OptimizeMode optimize_for;
    public String go_package;
    public Boolean cc_generic_services;
    public Boolean java_generic_services;
    public Boolean py_generic_services;
    public Boolean deprecated;
    public List<UninterpretedOption> uninterpreted_option;

    public Builder() {
    }

    public Builder(FileOptions message) {
      super(message);
      if (message == null) return;
      this.java_package = message.java_package;
      this.java_outer_classname = message.java_outer_classname;
      this.java_multiple_files = message.java_multiple_files;
      this.java_generate_equals_and_hash = message.java_generate_equals_and_hash;
      this.java_string_check_utf8 = message.java_string_check_utf8;
      this.optimize_for = message.optimize_for;
      this.go_package = message.go_package;
      this.cc_generic_services = message.cc_generic_services;
      this.java_generic_services = message.java_generic_services;
      this.py_generic_services = message.py_generic_services;
      this.deprecated = message.deprecated;
      this.uninterpreted_option = copyOf(message.uninterpreted_option);
    }

    /**
     * Sets the Java package where classes generated from this .proto will be
     * placed.  By default, the proto package is used, but this is often
     * inappropriate because proto packages do not normally start with backwards
     * domain names.
     */
    public Builder java_package(String java_package) {
      this.java_package = java_package;
      return this;
    }

    /**
     * If set, all the classes from the .proto file are wrapped in a single
     * outer class with the given name.  This applies to both Proto1
     * (equivalent to the old "--one_java_file" option) and Proto2 (where
     * a .proto always translates to a single class, but you may want to
     * explicitly choose the class name).
     */
    public Builder java_outer_classname(String java_outer_classname) {
      this.java_outer_classname = java_outer_classname;
      return this;
    }

    /**
     * If set true, then the Java code generator will generate a separate .java
     * file for each top-level message, enum, and service defined in the .proto
     * file.  Thus, these types will *not* be nested inside the outer class
     * named by java_outer_classname.  However, the outer class will still be
     * generated to contain the file's getDescriptor() method as well as any
     * top-level extensions defined in the file.
     */
    public Builder java_multiple_files(Boolean java_multiple_files) {
      this.java_multiple_files = java_multiple_files;
      return this;
    }

    /**
     * If set true, then the Java code generator will generate equals() and
     * hashCode() methods for all messages defined in the .proto file.
     * - In the full runtime, this is purely a speed optimization, as the
     * AbstractMessage base class includes reflection-based implementations of
     * these methods.
     * - In the lite runtime, setting this option changes the semantics of
     * equals() and hashCode() to more closely match those of the full runtime;
     * the generated methods compute their results based on field values rather
     * than object identity. (Implementations should not assume that hashcodes
     * will be consistent across runtimes or versions of the protocol compiler.)
     */
    public Builder java_generate_equals_and_hash(Boolean java_generate_equals_and_hash) {
      this.java_generate_equals_and_hash = java_generate_equals_and_hash;
      return this;
    }

    /**
     * If set true, then the Java2 code generator will generate code that
     * throws an exception whenever an attempt is made to assign a non-UTF-8
     * byte sequence to a string field.
     * Message reflection will do the same.
     * However, an extension field still accepts non-UTF-8 byte sequences.
     * This option has no effect on when used with the lite runtime.
     */
    public Builder java_string_check_utf8(Boolean java_string_check_utf8) {
      this.java_string_check_utf8 = java_string_check_utf8;
      return this;
    }

    public Builder optimize_for(OptimizeMode optimize_for) {
      if (optimize_for == OptimizeMode.__UNDEFINED__) throw new IllegalArgumentException();
      this.optimize_for = optimize_for;
      return this;
    }

    /**
     * Sets the Go package where structs generated from this .proto will be
     * placed.  There is no default.
     */
    public Builder go_package(String go_package) {
      this.go_package = go_package;
      return this;
    }

    /**
     * Should generic services be generated in each language?  "Generic" services
     * are not specific to any particular RPC system.  They are generated by the
     * main code generators in each language (without additional plugins).
     * Generic services were the only kind of service generation supported by
     * early versions of proto2.
     *
     * Generic services are now considered deprecated in favor of using plugins
     * that generate code specific to your particular RPC system.  Therefore,
     * these default to false.  Old code which depends on generic services should
     * explicitly set them to true.
     */
    public Builder cc_generic_services(Boolean cc_generic_services) {
      this.cc_generic_services = cc_generic_services;
      return this;
    }

    public Builder java_generic_services(Boolean java_generic_services) {
      this.java_generic_services = java_generic_services;
      return this;
    }

    public Builder py_generic_services(Boolean py_generic_services) {
      this.py_generic_services = py_generic_services;
      return this;
    }

    /**
     * Is this file deprecated?
     * Depending on the target platform, this can emit Deprecated annotations
     * for everything in the file, or it will be completely ignored; in the very
     * least, this is a formalization for deprecating files.
     */
    public Builder deprecated(Boolean deprecated) {
      this.deprecated = deprecated;
      return this;
    }

    /**
     * The parser stores options it doesn't recognize here. See above.
     */
    public Builder uninterpreted_option(List<UninterpretedOption> uninterpreted_option) {
      this.uninterpreted_option = checkForNulls(uninterpreted_option);
      return this;
    }

    @Override
    public <E> Builder setExtension(Extension<FileOptions, E> extension, E value) {
      super.setExtension(extension, value);
      return this;
    }

    @Override
    public FileOptions build() {
      return new FileOptions(this);
    }
  }

  public enum OptimizeMode
      implements ProtoEnum {
    SPEED(1),
    /**
     * Generate complete code for parsing, serialization,
     * etc.
     */
    CODE_SIZE(2),
    /**
     * Use ReflectionOps to implement these methods.
     */
    LITE_RUNTIME(3),

    /**
     * Wire-generated value, do not access from application code.
     */
    __UNDEFINED__(UNDEFINED_VALUE);

    private final int value;

    private OptimizeMode(int value) {
      this.value = value;
    }

    @Override
    public int getValue() {
      return value;
    }
  }
}
