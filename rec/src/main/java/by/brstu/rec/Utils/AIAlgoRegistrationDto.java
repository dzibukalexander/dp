package by.brstu.rec.Utils;

public class AIAlgoRegistrationDto {
    private String name;
    private String description;
    private String inputType;
    private String outputType;
    private String endpoint;
    private String kaggleURL;

    public AIAlgoRegistrationDto() {}

    public AIAlgoRegistrationDto(String name, String description, String inputType, String outputType, String endpoint, String kaggleURL) {
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.outputType = outputType;
        this.endpoint = endpoint;
        this.kaggleURL = kaggleURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getKaggleURL() {
        return kaggleURL;
    }

    public void setKaggleURL(String kaggleURL) {
        this.kaggleURL = kaggleURL;
    }
}
