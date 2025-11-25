package org.example;

public class CNCMachineFactory implements EntityFactory<CNCMachine> {
    private final String[] MAKERS = {
            "Haas Automation", "Mazak", "Okuma", "Doosan", "Hurco", "Hermle",
            "Siemens", "Mikron", "TRUMPF Group", "GROB", "Studer", "Hurco",
            "Motor Sich"
    };
    private final String[] COUNTRIES_OF_ORIGIN = {
            "Japan", "Germany", "USA", "South Korea", "Switzerland", "China",
            "UK", "Ukraine"
    };
    private static final String[] MODELS = {
            "VF-2", "VF-3", "DMU 50", "DMU 75", "Integrex i-200",
            "Okuma Genos M560", "FANUC Robodrill α-D21LiB",
            "Doosan DNM 5700", "Brother Speedio S700X1", "Makino PS95"
    };
    private final String[] COMMAND_LANGUAGES = {
            "G-code", "Heidenhain", "Mazatrol", "OSP", "Fanuc",
            "Conversational + G-code", "ISO G-code"
    };
    private final String[] OPERATING_SYSTEMS = {
            "Haas CNC Control", "CELOS", "SmoothX", "OSP Suite", "Fanuc 31i",
            "WinMax", "TNC 640", "Fanuc iHMI", "Sinumerik 840D sl"
    };
    private static final String[] DELIMITERS = {",", "#", ";", "|", "/"};
    private int randomValueFromRange(int min, int max, int step) {
        int count = (max - min) / step;
        int idx = (int)(Math.random() * (count + 1));
        return min + idx * step;
    }
    private double randomValueFromRange(double min, double max, double step) {
        double count = Math.floor((max - min) / step);
        double idx = Math.floor(Math.random() * (count + 1));
        return min + idx * step;
    }
    private String randomStringWithOptionalMultipleValues(String[] predefinedValues) {
        if (Math.random() < 0.7) {
            return predefinedValues[(int) (Math.random() * predefinedValues.length)];
        }
        int valuesNumber = randomValueFromRange(2, 6, 1);
        StringBuilder result = new StringBuilder();
        String delimiter = " " + DELIMITERS[(int) (Math.random() * DELIMITERS.length)] + " ";
        for (int i = 0; i < valuesNumber; i++) {
            if (i > 0) {
                result.append(delimiter);
            }
            result.append(predefinedValues[(int) (Math.random() * predefinedValues.length)]);
        }
        return result.toString();
    }
    @Override
    public CNCMachine create() {
        CNCMachine machine = new CNCMachine();
        machine.setMaker(randomStringWithOptionalMultipleValues(MAKERS));
        machine.setCountryOfOrigin(randomStringWithOptionalMultipleValues(COUNTRIES_OF_ORIGIN));
        machine.setModel(randomStringWithOptionalMultipleValues(MODELS));
        machine.setLength(randomValueFromRange(2000., 4000., 100.));
        machine.setWidth(randomValueFromRange(2000., 4000., 100.));
        machine.setHeight(randomValueFromRange(2000., 4000., 100.));
        machine.setWeight(randomValueFromRange(1000., 2000., 20.));
        machine.setMaxPowerConsumption(randomValueFromRange(12000, 22000, 1000));
        machine.setTableWorkArea(randomValueFromRange(500., 1000., 20.));
        machine.setWorkingVoltage(randomValueFromRange(380, 400, 20));
        machine.setCommandLanguage(randomStringWithOptionalMultipleValues(COMMAND_LANGUAGES));
        machine.setOperatingSystem(randomStringWithOptionalMultipleValues(OPERATING_SYSTEMS));
        return machine;
    }
}
