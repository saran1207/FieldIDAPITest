package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModel;

import java.util.List;

public class ApiProcedureDefinitionImage extends ApiReadWriteModel {

    private byte[] data;
    private String fileName;
    private List<ApiImageAnnotation> annotations;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ApiImageAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<ApiImageAnnotation> annotations) {
        this.annotations = annotations;
    }
}
