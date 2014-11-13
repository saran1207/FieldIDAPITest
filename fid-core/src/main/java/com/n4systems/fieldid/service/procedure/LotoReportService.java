package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.LotoPrintout;
import com.n4systems.model.LotoPrintoutType;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by rrana on 2014-10-31.
 */
public class LotoReportService extends FieldIdPersistenceService {

    private @Autowired
    S3Service s3Service;

    public void updateSelectedLongForm(LotoPrintout printout) {
        resetSelectedLongForm();
        persistenceService.update(printout);
    }

    public void resetSelectedLongForm() {
        LotoPrintout old = getSelectedLongForm();
        if(old != null) {
            old.setSelected(false);
            persistenceService.update(old);
        }
    }

    public void updateSelectedShortForm(LotoPrintout printout) {
        resetSelectedShortForm();
        persistenceService.update(printout);
    }
    public void resetSelectedShortForm() {
        LotoPrintout old = getSelectedShortForm();
        if(old != null) {
            old.setSelected(false);
            persistenceService.update(old);
        }
    }

    public LotoPrintout getSelectedLongForm() {
        QueryBuilder<LotoPrintout> query = createUserSecurityBuilder(LotoPrintout.class);
        query.addSimpleWhere("printoutType", LotoPrintoutType.LONG);
        query.addSimpleWhere("selected", true);
        return persistenceService.find(query);
    }

    public LotoPrintout getSelectedLongForm(Long tenantId) {
        QueryBuilder<LotoPrintout> queryBuilder = new QueryBuilder<>(LotoPrintout.class, new OpenSecurityFilter());
        queryBuilder.addSimpleWhere("tenant.id", tenantId);
        queryBuilder.addSimpleWhere("printoutType", LotoPrintoutType.LONG);
        queryBuilder.addSimpleWhere("selected", true);
        return persistenceService.find(queryBuilder);
    }

    public LotoPrintout getSelectedShortForm() {
        QueryBuilder<LotoPrintout> query = createUserSecurityBuilder(LotoPrintout.class);
        query.addSimpleWhere("printoutType", LotoPrintoutType.SHORT);
        query.addSimpleWhere("selected", true);
        return persistenceService.find(query);
    }

    public LotoPrintout getSelectedShortForm(Long tenantId) {
        QueryBuilder<LotoPrintout> queryBuilder = new QueryBuilder<>(LotoPrintout.class, new OpenSecurityFilter());
        queryBuilder.addSimpleWhere("tenant.id", tenantId);
        queryBuilder.addSimpleWhere("printoutType", LotoPrintoutType.SHORT);
        queryBuilder.addSimpleWhere("selected", true);
        return persistenceService.find(queryBuilder);
    }

    public List<LotoPrintout> getLongLotoPrintouts() {
        QueryBuilder<LotoPrintout> query = createUserSecurityBuilder(LotoPrintout.class);
        query.addSimpleWhere("printoutType", LotoPrintoutType.LONG);
        List<LotoPrintout> list = persistenceService.findAll(query);
        list.add(getDefaultLongForm());

        return list;
    }

    private LotoPrintout getDefaultLongForm() {
        LotoPrintout longFormPrintout = new LotoPrintout();
        longFormPrintout.setPrintoutName("Long Form - Default");
        longFormPrintout.setId(null);
        longFormPrintout.setPrintoutType(LotoPrintoutType.LONG);

        return longFormPrintout;
    }

    public List<LotoPrintout> getShortLotoPrintouts() {
        QueryBuilder<LotoPrintout> query = createUserSecurityBuilder(LotoPrintout.class);
        query.addSimpleWhere("printoutType", LotoPrintoutType.SHORT);
        List<LotoPrintout> list = persistenceService.findAll(query);
        list.add(getDefaultShortForm());

        return list;
    }

    private LotoPrintout getDefaultShortForm() {
        LotoPrintout shortFormPrintout = new LotoPrintout();
        shortFormPrintout.setPrintoutName("Short Form - Default");
        shortFormPrintout.setId(null);
        shortFormPrintout.setPrintoutType(LotoPrintoutType.SHORT);

        return shortFormPrintout;
    }

    public byte[] getLongJapser() throws IOException {
        LotoPrintout printout = getSelectedLongForm();
        if(printout == null) {
            printout = new LotoPrintout();
            printout.setPrintoutType(LotoPrintoutType.LONG);
            return s3Service.downloadDefaultLotoPrintout(printout);
        } else {
            return s3Service.downloadCustomLotoPrintout(printout);
        }
    }

    //TODO Make these two into one method... the only differentiating factor is whether or not we statically set as LONG or SHORT
    public Map<String, InputStream> getLongJasperMap() throws IOException {
        LotoPrintout printout = getSelectedLongForm();
        if(printout == null) {
            //return the default map...
            printout = new LotoPrintout();
            printout.setPrintoutType(LotoPrintoutType.LONG);
            return s3Service.downloadDefaultLotoJasperMap(printout);
        } else {
            //return the custom map...
            return s3Service.downloadCustomLotoJasperMap(printout);
        }
    }

    public Map<String, InputStream> getShortJasperMap() throws IOException {
        LotoPrintout printout = getSelectedLongForm();
        if(printout == null) {
            //return the default map...
            printout = new LotoPrintout();
            printout.setPrintoutType(LotoPrintoutType.SHORT);
            return s3Service.downloadDefaultLotoJasperMap(printout);
        } else {
            //return the custom map...
            return s3Service.downloadCustomLotoJasperMap(printout);
        }
    }

    public byte[] getShortJasper() throws IOException {
        LotoPrintout printout = getSelectedShortForm();
        if(printout == null) {
            printout = new LotoPrintout();
            printout.setPrintoutType(LotoPrintoutType.SHORT);
            return s3Service.downloadDefaultLotoPrintout(printout);

        } else {
            return s3Service.downloadCustomLotoPrintout(printout);
        }
    }

    @Transactional
    public void saveLotoReport(File zipFile, LotoPrintout printout) {
        try {
            unZipIt(zipFile, printout);
            persistenceService.saveOrUpdate(printout);
        } catch (IOException e) {
            e.printStackTrace();
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

    public void resetIfSelected(LotoPrintout printout) {
        LotoPrintout selected;

        if(printout.getPrintoutType().equals(LotoPrintoutType.LONG)) {
            selected = getSelectedLongForm(printout.getTenant().getId());
        } else {
            selected = getSelectedShortForm(printout.getTenant().getId());
        }

        if(selected != null && selected.getId().equals(printout.getId())) {
            selected.setSelected(false);
            persistenceService.update(selected);
        }
    }

    @Transactional
    public void deleteLotoPrintout(LotoPrintout lotoPrintout) {
        //if selected for print, then revert it to the default one
        resetIfSelected(lotoPrintout);

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
