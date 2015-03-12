package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.task.AsyncService.AsyncTask;
import com.n4systems.fieldid.service.task.DownloadLinkService;
import com.n4systems.model.LotoPrintout;
import com.n4systems.model.LotoPrintoutType;
import com.n4systems.model.Tenant;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.reporting.LotoPrintoutReportMapProducer;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by rrana on 2014-10-31.
 */
public class LotoReportService extends FieldIdPersistenceService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private SvgGenerationService svgGenerationService;

    @Autowired
    private DownloadLinkService downloadLinkService;

    private static final Logger log = Logger.getLogger(LotoReportService.class);

    /**
     * This method kicks off the asynchronous generation of a LOTO Printout (Long Form or Short Form).
     *
     * It also takes care of generating an associated DownloadLink entity, which causes a new entry to appear in the
     * user's "Downloads" page.
     *
     * @param procedureDefinition - A ProcedureDefinition entity, representing the LOTO Procedure you want a printout of.
     * @param type - A LotoPrintoutType enum indicating the type of printout you'd like to generate.
     * @return An initialized DownloadLink entity, pointing to the Printout in any stage of its generation.
     */
    public DownloadLink generateLotoPrintout(ProcedureDefinition procedureDefinition,
                                             LotoPrintoutType type) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        DownloadLink downloadLink = downloadLinkService.createDownloadLink(procedureDefinition.getProcedureCode() + " "
                + (type.equals(LotoPrintoutType.LONG) ? "Long":"Short") + " " + formatter.format(new Date()), ContentType.PDF);

        //Lambda Powah!!
        AsyncTask<?> task = asyncService.createTask(() -> {
            createPrintout(procedureDefinition, type, downloadLink);
            //We don't actually have anything to here, but this null return satisfies the requirements of what this
            //lambda replaces.
            return null;
        });

        asyncService.run(task);

        return downloadLink;
    }

    /**
     * This method generates LOTO Printouts via an Asynchronous task.  This allows the report to be generated in the
     * back end while not halting the front end of the client.  Just like all other printouts, these LOTO Printouts will
     * be found in the user's Downloads page.
     *
     * @param procedureDefinition - An initialized ProcedureDefinition representing the one we want a printout of.
     * @param type - A LotoPrintoutType enum representing the type of printout we want.
     * @param downloadLink - An initialized DownloadLink entity representing the DB entry for this downloadable file.
     */
    private void createPrintout(ProcedureDefinition procedureDefinition,
                                LotoPrintoutType type,
                                DownloadLink downloadLink) {

        //First, we want to set the DownloadLink as "In Progress"
        downloadLink.setState(DownloadState.INPROGRESS);

        downloadLinkService.update(downloadLink);

        try {
            Map<String, Object> reportMap = new HashMap<>();
            //Step 1: Load the map with the input streams which build the subreport...
            reportMap.putAll(getJasperMap(type));

            //...then we need to verify the contents of the map and report on missing subreport components.
            if(reportMap.get("isolationPointSubreport") == null) {
                log.warn((type.equals(LotoPrintoutType.LONG) ? "Long":"Short") + " Form Report for ProcedureDefinition with ID " + procedureDefinition.getId() +
                        " is missing the Isolation Points Subreport... did you expect this?");
            }

            if(type.equals(LotoPrintoutType.SHORT) && reportMap.get("imageSubreport") == null) {
                log.warn("Short Form Report for ProcedureDefinition with ID " + procedureDefinition.getId() +
                         " is missing the Images Subreport... did you expect this?");
            }

            //Step 2: break apart the Procedure Definition and make a map that we'll process when creating the report.
            reportMap.putAll(new LotoPrintoutReportMapProducer(procedureDefinition.getProcedureCode() + "Printout",
                             procedureDefinition,
                             new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault()),
                             s3Service,
                             svgGenerationService).produceMap());

            //Step 3: Use the "main" input stream to build the Jasper Report.
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject((InputStream) reportMap.get("main"));

            //Step 4: Fill the report...
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportMap, new JREmptyDataSource());

            //Okay... so the DownloadLink should automagically create its own file.  Excuse my lack of trust that it will
            //actually do what it says on the tin.
            FileOutputStream output = new FileOutputStream(downloadLink.getFile());

            //Step 5: Export the report to the downloadFile.
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();

            //Now for the moment of truth... can we close the OutputStream??
            output.close();

            //If we got this far, it closed successfully.  Lets flip the DownloadLink to COMPLETED and call it a day.
            downloadLink.setState(DownloadState.COMPLETED);
            downloadLinkService.update(downloadLink);

            //Aaaand we're done.  We return from here so that we can set the DownloadState to failed, regardless of
            //the exception we encounter. That effectively makes it so that the end of the method for the happy path is
            //here and the end of the method for the unhappy path is the bottom of the method.
            return;
        } catch (FileNotFoundException fne) {
            //I want to log this one specially on its own, because I don't entirely trust this file will get generated.
            //This should be separate, because if the file is not created, this points to an underlying issue like a
            //full hard drive.
            log.fatal("The File pointed to by a DownloadLink didn't actually exist!!",
                      fne);
        } catch (JRException | IOException e) {
            //If either of these happen, it was in relation to the actual generation of the report. Again, we should log
            //this, but both of these exceptions basically have the same flavour of Awful and mean more or less the same
            //thing: there was a problem that caused the report not to print.  Maybe we couldn't read the Jasper files,
            //maybe we couldn't load the report... heck, maybe the Jasper files referenced invalid fields/parameters.
            log.error("Failure in generating a LOTO Printout of Type " + type.getLabel() +
                      " for Procedure Code " + procedureDefinition.getProcedureCode() +
                      " with ID " + procedureDefinition.getId(),
                      e);
        }

        downloadLink.setState(DownloadState.FAILED);
        downloadLinkService.update(downloadLink);
    }

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

    /**
     * This method retrieves a Map of InputStreams keyed by Strings.  This map represents the main report and all
     * subreport sections that is to be used for a LOTO Printout.
     *
     * @param type - A LotoPrintouType enum indicating the type of Printout you want a map of InputStreams for.
     * @return A Map of InputStreams keyed by Strings representing the main report and all subreport components.
     * @throws IOException
     */
    public Map<String, InputStream> getJasperMap(LotoPrintoutType type) throws IOException {
        LotoPrintout printout = type.equals(LotoPrintoutType.LONG) ? getSelectedLongForm() : getSelectedShortForm();
        if(printout == null) {
            //If we didn't get anything back, they're using the default report... so we grab that.
            printout = new LotoPrintout();
            printout.setPrintoutType(type);
            return s3Service.downloadDefaultLotoJasperMap(printout);
        } else {
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
