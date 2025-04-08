package by.brstu.rec.Utils;

import java.util.Map;

public class PredictionResult {
    private String result;
    private Double confidence;
    private Map<String, Object> additionalData;
    private Byte[] photoData;

    public PredictionResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }

    public Byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(Byte[] photoData) {
        this.photoData = photoData;
    }
}
