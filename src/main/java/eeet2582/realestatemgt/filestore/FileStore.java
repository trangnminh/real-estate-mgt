package eeet2582.realestatemgt.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import eeet2582.realestatemgt.bucket.BucketName;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
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
                       @NotNull List<Optional<Map<String,String>>> optionalMetaDataList,
                       InputStream inputStream){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaDataList.forEach(optionalMetaData ->{
            optionalMetaData.ifPresent(stringStringMap ->
            {
                if (!stringStringMap.isEmpty()){
                    stringStringMap.forEach(objectMetadata::addUserMetadata);
                    objectMetadata.setContentLength(Long.parseLong(stringStringMap.get("Content-Length")));
                }
            });
        });

        try{
            s3.putObject(path,fileName,inputStream,objectMetadata);
        }
        catch(AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }

    public String delete(String path){
        try{
            for (S3ObjectSummary file : s3.listObjects(BucketName.HOUSE_IMAGE.getBucketName(), "dataset/"+path+"/").getObjectSummaries()){
                s3.deleteObject(new DeleteObjectRequest(BucketName.HOUSE_IMAGE.getBucketName(), file.getKey()));
            }
            return "successfully deleted";
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Error: "+e.getErrorMessage());
        }
    }
}
