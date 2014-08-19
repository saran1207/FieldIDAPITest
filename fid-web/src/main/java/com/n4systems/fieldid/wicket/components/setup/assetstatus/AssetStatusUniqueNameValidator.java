package com.n4systems.fieldid.wicket.components.setup.assetstatus;

import com.n4systems.fieldid.service.asset.AssetStatusService;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * This is the Unique Name Validator for Asset Statuses.  It just checks to make sure the name you're using for an
 * AssetStatus is unique and generates and error if it's not.
 *
 * Created by Jordan Heath on 06/08/14.
 */
public class AssetStatusUniqueNameValidator extends AbstractValidator<String> {

    @SpringBean
    private AssetStatusService assetStatusService;

    private Long id;

    /**
     * This is the constructor.  You must feed it an ID or a null value.
     *
     * @param id - A <b>Long</b> value representing the ID of the AssetStatus or null if it doesn't have an ID.
     */
    public AssetStatusUniqueNameValidator(Long id) {
        this.id = id;
    }

    /**
     * Validate the name of an AssetStatus to ensure that it is unique.
     *
     * @param stringIValidatable - An IValidatable object representing the String name of the AssetStatus.
     */
    @Override
    protected void onValidate(IValidatable<String> stringIValidatable) {
        String name = stringIValidatable.getValue();

        if(assetStatusService.exists(name, id)) {
            ValidationError error = new ValidationError();
            error.addMessageKey("error.assetstatusduplicate");
            stringIValidatable.error(error);
        }
    }
}
