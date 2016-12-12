package com.n4systems.fieldid.service.amazon;

@Deprecated // DELETE THIS
public class S3ImageAttachmentHandler { //extends S3AttachmentHandler {

//    private @Autowired ImageService imageService;
//
//
//    private List<Class<? extends Flavour>> flavours = Lists.newArrayList(MediumFlavour.class, ThumbnailFlavour.class );
//
//
//    public S3ImageAttachmentHandler() {
//    }
//
//    @Override
//    protected List<Class<? extends Flavour>> getFlavourTypes() {
//        return flavours;
//    }
//
//    @Override
//    protected Flavour getAttachmentFlavour(Attachment attachment, Class<? extends Flavour> flavour) {
//        Preconditions.checkNotNull(flavour,"must specify a valid non-null flavour class");
//        if (flavour.equals(MediumFlavour.class)) {
//            return new MediumFlavour(attachment);
//        } else if (flavour.equals(ThumbnailFlavour.class)) {
//            return new ThumbnailFlavour(attachment);
//        }
//        throw new IllegalArgumentException("invalid flavour type : " + flavour.getSimpleName());
//    }
//
//
//    // --------------------------------------------------------------------------------------------------
//
//
//    public class MediumFlavour extends Flavour {
//
//        public static final String SUFFIX = "medium";
//
//        public MediumFlavour(Attachment delegate) {
//            super(delegate);
//        }
//
//        @Override
//        public String getSuffix() {
//            return SUFFIX;
//        }
//
//        @Override
//        protected byte[] createBytes(byte[] bytes) {
//            return imageService.generateMedium(bytes);
//        }
//    }
//
//    public class ThumbnailFlavour extends Flavour {
//
//        public static final String SUFFIX = "thumbnail";
//
//        public ThumbnailFlavour(Attachment delegate) {
//            super(delegate);
//        }
//
//        @Override
//        protected byte[] createBytes(byte[] bytes) {
//            return imageService.generateThumbnail(bytes);
//        }
//
//        @Override
//        public String getSuffix() {
//            return SUFFIX;
//        }
//
//    }

}
