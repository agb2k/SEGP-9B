package medPal.App;

public class Medicine {
    private int medicineId;
    private String medicineName;
    private String manufacturer;
    private int dosage;
    private String imagePath;
    private String purpose;
    private String medicineRemarks;

    public Medicine(int medicineId, String medicineName, String manufacturer, int dosage, String imagePath, String purpose,
                    String medicineRemarks) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.manufacturer = manufacturer;
        this.dosage = dosage;
        this.imagePath = "https://bulacke.xyz/medpal-img/" + imagePath;
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
