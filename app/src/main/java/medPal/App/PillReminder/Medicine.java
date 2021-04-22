package medPal.App.PillReminder;

import java.io.Serializable;

/**
 * Medicine Class.
 */
public class Medicine implements Serializable {

    public static final String MEDICINE_IMAGE_PLACEHOLDER = "http://hpyzl1.jupiter.nottingham.edu.my/pill_drugs-512.png";

    private int medicineId;
    private String medicineName;
    private String manufacturer;
    private int dosage;
    private String imagePath;
    private String purpose;
    private String medicineRemarks;


    /**
     * Constructor of medicine object.
     * @param medicineId Medicine ID.
     * @param medicineName Medicine name.
     * @param manufacturer Manufacturer of this medicine.
     * @param dosage Dosage of this medicine.
     * @param imagePath Image path of this medicine.
     * @param purpose Purpose of taking this medicine.
     * @param medicineRemarks Remarks for this medicine.
     */
    public Medicine(int medicineId, String medicineName, String manufacturer, int dosage, String imagePath, String purpose,
                    String medicineRemarks) {

        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.manufacturer = manufacturer;
        this.dosage = dosage;
        if(imagePath.length() > 0) {
            this.imagePath = "http://hpyzl1.jupiter.nottingham.edu.my/medpal-img/" + imagePath;
        }else {
            this.imagePath = "";
        }
        this.purpose = purpose;
        this.medicineRemarks = medicineRemarks;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getDosage() {
        return dosage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getMedicineRemarks() {
        return medicineRemarks;
    }

    @Override
    public String toString() {
        return "Medicine [medicineId=" + medicineId + ", medicineName=" + medicineName + ", dosage=" + dosage
                + ", imagePath=" + imagePath + ", purpose=" + purpose + ", medicineRemarks=" + medicineRemarks + "]";
    }
}
