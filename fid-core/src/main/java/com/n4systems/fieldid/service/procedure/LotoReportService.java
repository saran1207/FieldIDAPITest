package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.LotoPrintout;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by rrana on 2014-10-31.
 */
public class LotoReportService extends FieldIdPersistenceService {

    private @Autowired
    S3Service s3Service;

    @Transactional
    public void saveLotoReport(File zipFile, LotoPrintout printout) {
        try {
            unZipIt(zipFile, printout);
            persistenceService.saveOrUpdate(printout);
        } catch (IOException e) {
        }
    }

    public List<LotoPrintout> getLotoReportsByTenantId(Long id) {
        QueryBuilder<LotoPrintout> queryBuilder = new QueryBuilder<>(LotoPrintout.class, new OpenSecurityFilter());
        queryBuilder.addSimpleWhere("tenant.id", id);
        return persistenceService.findAll(queryBuilder);
    }

    public Tenant getTenantById(Long id) {
        QueryBuilder<Tenant> queryBuilder = new QueryBuilder<>(Tenant.class, new OpenSecurityFilter());
        queryBuilder.addSimpleWhere("id", id);
        return persistenceService.find(queryBuilder);
    }

    public boolean exists(LotoPrintout printout) {
        QueryBuilder<LotoPrintout> queryBuilder = new QueryBuilder<>(LotoPrintout.class, new OpenSecurityFilter());
        queryBuilder.addSimpleWhere("tenant.id", printout.getTenant().getId());
        queryBuilder.addSimpleWhere("printoutName", printout.getPrintoutName());
        if (persistenceService.count(queryBuilder) > 0)
            return true;
        else
            return false;
    }

    @Transactional
    public void deleteLotoPrintout(LotoPrintout lotoPrintout) {
        //delete from S3
        s3Service.deleteLotoPrintout(lotoPrintout);

        //delete from the file system
        File file = PathHandler.getAbsoluteLotoPath(lotoPrintout);
        deleteFromFileSystem(file);

        //delete from the db
        QueryBuilder<LotoPrintout> queryBuilder = new QueryBuilder<>(LotoPrintout.class, new OpenSecurityFilter());
        queryBuilder.addSimpleWhere("id", lotoPrintout.getId());
        LotoPrintout printout = persistenceService.find(queryBuilder);
        //persistenceService.remove(lotoPrintout);
        persistenceService.delete(printout);
    }

    private void deleteFromFileSystem(File file) {
        if(file.isDirectory()){
            //directory is empty, then delete it
            if(file.list().length==0){
                file.delete();
            }else{
                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    deleteFromFileSystem(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                }
            }
        }else{
            //if file, then delete it
            file.delete();
        }
    }

    public byte[] getZipFile(LotoPrintout lotoPrintout) throws IOException {
        return s3Service.downloadZippedLotoPrintout(lotoPrintout);
    }

    public void unZipIt(File zipFile, LotoPrintout lotoPrintout) throws IOException {
        byte[] buffer = new byte[1024];

        //create output directory is not exists
        File folder = PathHandler.getAbsoluteLotoPath(lotoPrintout);
        if(!folder.exists()){
            folder.mkdirs();
        }

        //save the zip file to the local file system
        FileInputStream fin = new FileInputStream(zipFile);
        FileOutputStream fosZip = new FileOutputStream(new File(folder + File.separator + lotoPrintout.getPrintoutName() + ".zip"));
        int length;
        while ((length = fin.read(buffer)) > 0) {
            fosZip.write(buffer, 0, length);
        }
        fosZip.close();


        //save the zip file to S3
        s3Service.saveLotoPrintout(zipFile, PathHandler.getZipS3Path(lotoPrintout));

        //get the zip file content
        ZipInputStream zis =
                new ZipInputStream(new FileInputStream(zipFile.getAbsolutePath()));
        //get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while(ze!=null){

            String fileName = ze.getName();
            File newFile = new File(folder + File.separator + fileName);

            System.out.println("file unzip : "+ newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            //write the file to the file system
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            //save the file to S3
            s3Service.saveLotoPrintout(newFile, PathHandler.getS3Path(lotoPrintout, newFile.getName()));

            fos.close();
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        System.out.println("Done");
    }

}
