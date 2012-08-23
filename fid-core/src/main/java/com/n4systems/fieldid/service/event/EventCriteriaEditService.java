package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.services.signature.SignatureService;
import org.apache.log4j.Logger;

import java.util.List;

public class EventCriteriaEditService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(EventCriteriaEditService.class);

    public void storeCriteriaChanges(AbstractEvent event) {
        event.getResults();

        List<AbstractEvent.SectionResults> sectionResults = event.getSectionResults();
        for (AbstractEvent.SectionResults sectionResult : sectionResults) {
            for (CriteriaResult result : sectionResult.results) {
                CriteriaResult existingResult = findExistingResultFor(result);
                syncResultData(existingResult, result);
            }
        }

    }

    private void syncResultData(CriteriaResult realResult, CriteriaResult result) {
        if (realResult instanceof OneClickCriteriaResult) {
            ((OneClickCriteriaResult)realResult).setState(((OneClickCriteriaResult)result).getState());
        } else if (realResult instanceof TextFieldCriteriaResult) {
            ((TextFieldCriteriaResult)realResult).setValue(((TextFieldCriteriaResult)result).getValue());
        } else if (realResult instanceof SelectCriteriaResult) {
            ((SelectCriteriaResult)realResult).setValue(((SelectCriteriaResult)result).getValue());
        } else if (realResult instanceof ComboBoxCriteriaResult) {
            ((ComboBoxCriteriaResult)realResult).setValue(((ComboBoxCriteriaResult)result).getValue());
        } else if (realResult instanceof UnitOfMeasureCriteriaResult) {
            ((UnitOfMeasureCriteriaResult)realResult).setPrimaryValue(((UnitOfMeasureCriteriaResult)result).getPrimaryValue());
            ((UnitOfMeasureCriteriaResult)realResult).setSecondaryValue(((UnitOfMeasureCriteriaResult)result).getSecondaryValue());
        } else if (realResult instanceof SignatureCriteriaResult) {
            ((SignatureCriteriaResult)realResult).setSigned(((SignatureCriteriaResult)result).isSigned());
            ((SignatureCriteriaResult)realResult).setImage(((SignatureCriteriaResult)result).getImage());
            ((SignatureCriteriaResult)realResult).setTemporaryFileId(((SignatureCriteriaResult)result).getTemporaryFileId());
            if (((SignatureCriteriaResult) realResult).isSigned()) {
                try {
                    new SignatureService().storeSignatureFileFor((SignatureCriteriaResult) realResult);
                } catch (Exception e) {
                    logger.error("Error saving signature", e);
                }
            }
        } else if (realResult instanceof DateFieldCriteriaResult) {
            ((DateFieldCriteriaResult) realResult).setValue(((DateFieldCriteriaResult)result).getValue());
        } else if (realResult instanceof NumberFieldCriteriaResult) {
            ((NumberFieldCriteriaResult) realResult).setValue(((NumberFieldCriteriaResult)result).getValue());
        } else if (realResult instanceof ScoreCriteriaResult) {
            ((ScoreCriteriaResult) realResult).setScore(((ScoreCriteriaResult)result).getScore());
        }
    }

    private CriteriaResult findExistingResultFor(CriteriaResult result) {
        return persistenceService.find(CriteriaResult.class, result.getId());
    }

}
