package com.n4systems.api.conversion.asset;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ViewToModelConverter;
import com.n4systems.api.conversion.event.LocationSpecification;
import com.n4systems.api.model.AssetView;
import com.n4systems.api.validation.validators.LocationValidator;
import com.n4systems.model.*;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import com.n4systems.model.infooption.InfoOptionConversionException;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.model.location.PredefinedLocationTreeLoader;
import com.n4systems.model.orders.NonIntegrationOrderManager;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.persistence.Transaction;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.*;

public class AssetToModelConverter implements ViewToModelConverter<Asset, AssetView> {
	private final AssetByMobileGuidLoader externalIdLoader;
	private final OrgByNameLoader orgLoader;
	private final NonIntegrationOrderManager nonIntegrationOrderManager;
	private final AssetStatusByNameLoader assetStatusLoader;
	private final InfoOptionMapConverter optionConverter;
    private final UserByFullNameLoader userLoader;
    private AssetType type;
	private User identifiedBy;
    private final PredefinedLocationTreeLoader predefinedLocationTreeLoader;

    public AssetToModelConverter(AssetByMobileGuidLoader externalIdLoader, OrgByNameLoader orgLoader, NonIntegrationOrderManager nonIntegrationOrderManager,
                                 AssetStatusByNameLoader assetStatusLoader, InfoOptionMapConverter optionConverter,
                                 PredefinedLocationTreeLoader predefinedLocationTreeLoader, UserByFullNameLoader userLoader) {
		this.externalIdLoader = externalIdLoader;
		this.orgLoader = orgLoader;
		this.nonIntegrationOrderManager = nonIntegrationOrderManager;
		this.assetStatusLoader = assetStatusLoader;
		this.optionConverter = optionConverter;
        this.predefinedLocationTreeLoader = predefinedLocationTreeLoader;
        this.userLoader = userLoader;
	}
	
	@Override
	public Asset toModel(AssetView view, Transaction transaction) throws ConversionException {

		boolean edit = view.getGlobalId() != null;
		Asset model = (edit) ? loadModel(view.getGlobalId(), transaction) : new Asset();

		PrimaryOrg primaryOrg = identifiedBy.getOwner().getPrimaryOrg();

		if (view.getIdentified() != null) {
			// this problem should never occur, the date validator will ensure it is actually a date instance
			if (!(view.getIdentified() instanceof Date)) {
				throw new ConversionException("AssetView field identified should have been instance of java.lang.Date but was " + view.getIdentified().getClass().getName());
			}
			model.setIdentified((Date)view.getIdentified());
		} else {
			model.setIdentified(new Date());
		}
		
		model.setOwner(resolveOwner(view, transaction));
		model.setTenant(model.getOwner().getTenant());
		model.setIdentifiedBy(identifiedBy);
		model.setType(type);		
		model.setIdentifier(view.getIdentifier());
		model.setRfidNumber(view.getRfidNumber());
		model.setCustomerRefNumber(view.getCustomerRefNumber());
		model.setAdvancedLocation(resolveLocation(primaryOrg, view, transaction));
		model.setPurchaseOrder(view.getPurchaseOrder());
		model.setComments(view.getComments());
		model.setAssetStatus(resolveAssetStatus(view.getStatus(), transaction));
		model.setPublished(primaryOrg.isAutoPublish());
        if(view.getAssignedUser() != null) {
            model.setAssignedUser(resolveAssignedUser(view, transaction));
        }
        boolean integrationEnabled = primaryOrg.hasExtendedFeature(ExtendedFeature.Integration);
        boolean orderDetailsEnabled = primaryOrg.hasExtendedFeature(ExtendedFeature.OrderDetails);

        if(orderDetailsEnabled && !integrationEnabled) {
            model.setNonIntergrationOrderNumber(view.getShopOrder());
        } else if (!integrationEnabled) {
			model.setShopOrder(createShopOrder(view.getShopOrder(), model.getOwner().getTenant(), transaction));
		}
		
		try {
			// this could throw an exception if a select box info option could not be resolved.  It
			// will not throw on missing but required info fields.  We let the validators take care of that part.
			if (model.isNew()) {
				model.setInfoOptions(new TreeSet<InfoOptionBean>());
				model.getInfoOptions().addAll(optionConverter.convertAssetAttributes(view.getAttributes(), type));
			}
			else {
				/* Replace infoOptions in the model from those in the view */
				List<InfoOptionBean> updatedOptions = optionConverter.convertAssetAttributes(view.getAttributes(), type);
				List<InfoOptionBean> cleansedOptions = cleanseNonStaticInfoOptionIds(updatedOptions);
				//model.getInfoOptions().clear();
				//model.getInfoOptions().addAll(cleansedOptions);
				//model.setInfoOptions(new HashSet<InfoOptionBean>(cleansedOptions));
				Map<String, InfoOptionBean> cleansedOptionsByName = new HashMap();
				cleansedOptions.forEach((option) -> cleansedOptionsByName.put(option.getInfoField().getName(), option));
				Map<String, InfoOptionBean> existingOptionsByName = new HashMap();
				model.getInfoOptions().forEach((option) -> existingOptionsByName.put(option.getInfoField().getName(), option));
				for(InfoFieldBean infoField: model.getType().getInfoFields()) {
					boolean optionExistsInNew = cleansedOptionsByName.containsKey(infoField.getName());
					boolean optionExistsInOld = existingOptionsByName.containsKey(infoField.getName());
					if (optionExistsInOld && !optionExistsInNew) {
						System.out.println("AssetToModelConverter - option has to be removed ");
						model.getInfoOptions().remove(existingOptionsByName.get(infoField.getName()));
					}
					else
					if (!optionExistsInOld && optionExistsInNew) {
						System.out.println("AssetToModelConverter - option has to be added");
						InfoOptionBean option = cleansedOptionsByName.get(infoField.getName());
						Long uniqueId = option.getUniqueID();
						if (uniqueId != null && !transaction.getEntityManager().contains(option))
							/* Attach detached entity */
							option = transaction.getEntityManager().find(InfoOptionBean.class, option.getUniqueID());
						else
							option = cleansedOptionsByName.get(infoField.getName());
						model.getInfoOptions().add(option);
					}
					else
					if (optionExistsInNew && optionExistsInOld) {
						System.out.println("AssetToModelConverter - option exists in both");
						if (!cleansedOptionsByName.get(infoField.getName()).getName().equals(existingOptionsByName.get(infoField.getName()).getName())) {
							System.out.println("AssetToModelConverter - option has to be modified");
							model.getInfoOptions().remove(existingOptionsByName.get(infoField.getName()));
							InfoOptionBean option = cleansedOptionsByName.get(infoField.getName());
							if (option.getUniqueID() != null && !transaction.getEntityManager().contains(option)) {
								System.out.println("... update to static option");
								option = transaction.getEntityManager().find(InfoOptionBean.class, option.getUniqueID());
							}
							model.getInfoOptions().add(option);
						}
					}
				}
			}
		} catch (InfoOptionConversionException e) {
			throw new ConversionException(e);
		}

		model.setMobileGUID(view.getGlobalId());
		model.setGlobalId(view.getGlobalId());

		return model;
	}

    protected Location resolveLocation(PrimaryOrg primaryOrg, AssetView view, Transaction transaction) {
        if (primaryOrg.hasExtendedFeature(ExtendedFeature.AdvancedLocation)) {
            PredefinedLocationTree predefinedLocationTree = predefinedLocationTreeLoader.load(transaction);
            Location location = new LocationValidator().getLocation(new LocationSpecification(view.getLocation()), predefinedLocationTree);
            return location;
        } else {
            return Location.onlyFreeformLocation(view.getLocation());
        }
    }

    private LineItem createShopOrder(String orderNumber, Tenant tenant, Transaction transaction) {
		/*
		 * TODO: refactor to use the existing transaction once the legacy asset manager gets off ejb  and the asset saver in the importer is using the same transaction as the converter to do the work.
		 * 
		 * The problem here is that the AssetSaveService is running inside its own transaction so if we use the 
		 * existing transaction here, the non integration order will not have been committed by time the asset
		 * save service tries to look it up by id.
		 */
		return (orderNumber != null) ? nonIntegrationOrderManager.createAndSave(orderNumber, tenant) : null;
	}
	
	private AssetStatus resolveAssetStatus(String assetStatus, Transaction transaction) {
		return (assetStatus != null) ? assetStatusLoader.setName(assetStatus).load(transaction) : null;
	}

	private BaseOrg resolveOwner(AssetView view, Transaction transaction) {
		orgLoader.setOrganizationName(view.getOrganization());
		orgLoader.setCustomerName(view.getCustomer());
		orgLoader.setDivision(view.getDivision());
		
		return orgLoader.load(transaction);
	}

    private User resolveAssignedUser(AssetView view,Transaction transaction) {
        return userLoader.setFullName(view.getAssignedUser()).load(transaction).get(0);
    }

	public void setType(AssetType type) {
		this.type = type;
	}

	public AssetType getType() {
		return type;
	}
	
	public void setIdentifiedBy(User identifiedBy) {
		this.identifiedBy = identifiedBy;
	}

	private Asset loadModel(String externalId, Transaction transaction) throws ConversionException {
		Asset model = externalIdLoader.setMobileGuid(externalId).addPostFetchFields("infoOptions").load(transaction);

		if (model == null) {
			throw new ConversionException("No model found for external id [" + externalId + "]");
		}
		if (!model.getType().getId().equals(getType().getId())) {
			throw new ConversionException("Loaded model for external id [" + externalId +
					"] has asset type " + model.getType().getName() + " which does not match asset type " + getType().getName());
		}
		return model;
	}

	private List<InfoOptionBean> cleanseNonStaticInfoOptionIds(List<InfoOptionBean> enteredInfoOptions) {
		for (InfoOptionBean enteredInfoOption : enteredInfoOptions) {
			if (!enteredInfoOption.isStaticData()) {
				enteredInfoOption.setUniqueID(null);
			}
		}
		return enteredInfoOptions;
	}
}
