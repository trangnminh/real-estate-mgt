package eeet2582.realestatemgt.bucket;

public enum BucketName {

    HOUSE_IMAGE("realestatemgt");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
