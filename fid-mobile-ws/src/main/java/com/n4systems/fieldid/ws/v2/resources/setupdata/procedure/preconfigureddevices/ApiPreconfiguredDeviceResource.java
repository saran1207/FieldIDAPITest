package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.preconfigureddevices;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.procedure.PreconfiguredDevice;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("preconfiguredDevices")
public class ApiPreconfiguredDeviceResource extends SetupDataResourceReadOnly<ApiPreconfiguredDevice, PreconfiguredDevice> {

    public ApiPreconfiguredDeviceResource() {
        super(PreconfiguredDevice.class, true);
    }

    @Override
    protected ApiPreconfiguredDevice convertEntityToApiModel(PreconfiguredDevice device) {
        ApiPreconfiguredDevice apiDevice = new ApiPreconfiguredDevice();
		apiDevice.setSid(device.getId());
		apiDevice.setActive(true);
		apiDevice.setModified(device.getModified());
		apiDevice.setDevice(device.getDevice());
		apiDevice.setMethod(device.getMethod());
        if(device.getIsolationPointSourceType() != null) {
			apiDevice.setIsolationPointSourceType(device.getIsolationPointSourceType().name());
        }
        return apiDevice;
    }
}