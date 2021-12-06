package eeet2582.realestatemgt.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import eeet2582.realestatemgt.bucket.BucketName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {
    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void upload(String path, String fileName,
                     Optional<Map<String,String>> optionalMetaData,
                     InputStream inputStream){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(stringStringMap ->
        {
            if (!stringStringMap.isEmpty()){
                stringStringMap.forEach(objectMetadata::addUserMetadata);
            }
        });
        try{
            s3.putObject(path,fileName,inputStream,objectMetadata);
        }
        catch(AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }

    public void delete(String fileName){
        s3.deleteObject(new DeleteObjectRequest(BucketName.HOUSE_IMAGE.getBucketName()+"/","socal_pics/"+fileName));
    }
}
