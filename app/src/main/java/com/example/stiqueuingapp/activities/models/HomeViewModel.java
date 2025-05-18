package com.example.stiqueuingapp.activities.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeViewModel extends AndroidViewModel {

    // ADMISSION
    private MutableLiveData<String> admissionCounter1QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> admissionCounter1Counter = new MutableLiveData<>();
    private MutableLiveData<String> admissionCounter1CutOff = new MutableLiveData<>();

    private MutableLiveData<String> admissionCounter2QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> admissionCounter2Counter = new MutableLiveData<>();
    private MutableLiveData<String> admissionCounter2CutOff = new MutableLiveData<>();

    private MutableLiveData<String> admissionCounter3QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> admissionCounter3Counter = new MutableLiveData<>();
    private MutableLiveData<String> admissionCounter3CutOff = new MutableLiveData<>();

    // CASHIER
    private MutableLiveData<String> cashierCounter1QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> cashierCounter1Counter = new MutableLiveData<>();
    private MutableLiveData<String> cashierCounter1CutOff = new MutableLiveData<>();

    private MutableLiveData<String> cashierCounter2QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> cashierCounter2Counter = new MutableLiveData<>();
    private MutableLiveData<String> cashierCounter2CutOff = new MutableLiveData<>();

    private MutableLiveData<String> cashierCounter3QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> cashierCounter3Counter = new MutableLiveData<>();
    private MutableLiveData<String> cashierCounter3CutOff = new MutableLiveData<>();

    // REGISTRAR
    private MutableLiveData<String> registrarCounter1QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> registrarCounter1Counter = new MutableLiveData<>();
    private MutableLiveData<String> registrarCounter1CutOff = new MutableLiveData<>();

    private MutableLiveData<String> registrarCounter2QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> registrarCounter2Counter = new MutableLiveData<>();
    private MutableLiveData<String> registrarCounter2CutOff = new MutableLiveData<>();

    private MutableLiveData<String> registrarCounter3QueueNumber = new MutableLiveData<>();
    private MutableLiveData<String> registrarCounter3Counter = new MutableLiveData<>();
    private MutableLiveData<String> registrarCounter3CutOff = new MutableLiveData<>();

    // ADMISSION QUEUE NUMBERS
    public LiveData<String> getAdmissionCounter1QueueNumber() {
        return admissionCounter1QueueNumber;
    }

    public LiveData<String> getAdmissionCounter1Counter() {
        return admissionCounter1Counter;
    }

    public LiveData<String> getAdmissionCounter1CutOff() {
        return admissionCounter1CutOff;
    }

    public LiveData<String> getAdmissionCounter2QueueNumber() {
        return admissionCounter2QueueNumber;
    }

    public LiveData<String> getAdmissionCounter2Counter() {
        return admissionCounter2Counter;
    }

    public LiveData<String> getAdmissionCounter2CutOff() {
        return admissionCounter2CutOff;
    }

    public LiveData<String> getAdmissionCounter3QueueNumber() {
        return admissionCounter3QueueNumber;
    }

    public LiveData<String> getAdmissionCounter3Counter() {
        return admissionCounter3Counter;
    }

    public LiveData<String> getAdmissionCounter3CutOff() {
        return admissionCounter3CutOff;
    }

    // CASHIER QUEUE NUMBERS
    public LiveData<String> getCashierCounter1QueueNumber() {
        return cashierCounter1QueueNumber;
    }

    public LiveData<String> getCashierCounter1Counter() {
        return cashierCounter1Counter;
    }

    public LiveData<String> getCashierCounter1CutOff() {
        return cashierCounter1CutOff;
    }

    public LiveData<String> getCashierCounter2QueueNumber() {
        return cashierCounter2QueueNumber;
    }

    public LiveData<String> getCashierCounter2Counter() {
        return cashierCounter2Counter;
    }

    public LiveData<String> getCashierCounter2CutOff() {
        return cashierCounter2CutOff;
    }

    public LiveData<String> getCashierCounter3QueueNumber() {
        return cashierCounter3QueueNumber;
    }

    public LiveData<String> getCashierCounter3Counter() {
        return cashierCounter3Counter;
    }

    public LiveData<String> getCashierCounter3CutOff() {
        return cashierCounter3CutOff;
    }

    // REGISTRAR QUEUE NUMBERS
    public LiveData<String> getRegistrarCounter1QueueNumber() {
        return registrarCounter1QueueNumber;
    }

    public LiveData<String> getRegistrarCounter1Counter() {
        return registrarCounter1Counter;
    }

    public LiveData<String> getRegistrarCounter1CutOff() {
        return registrarCounter1CutOff;
    }

    public LiveData<String> getRegistrarCounter2QueueNumber() {
        return registrarCounter2QueueNumber;
    }

    public LiveData<String> getRegistrarCounter2Counter() {
        return registrarCounter2Counter;
    }

    public LiveData<String> getRegistrarCounter2CutOff() {
        return registrarCounter2CutOff;
    }

    public LiveData<String> getRegistrarCounter3QueueNumber() {
        return registrarCounter3QueueNumber;
    }

    public LiveData<String> getRegistrarCounter3Counter() {
        return registrarCounter3Counter;
    }

    public LiveData<String> getRegistrarCounter3CutOff() {
        return registrarCounter3CutOff;
    }

    public HomeViewModel(@NonNull Application application) {
        super(application);
        fetchFirebaseData();
    }

    private void fetchFirebaseData() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference admissionRef = db.collection("QUEUES").document("ADMISSION");
        DocumentReference cashierRef = db.collection("QUEUES").document("CASHIER");
        DocumentReference registrarRef = db.collection("QUEUES").document("REGISTRAR");

        admissionCounter1QueueNumber.setValue("A-000");
        admissionCounter2QueueNumber.setValue("A-000");
        admissionCounter3QueueNumber.setValue("A-000");

        admissionCounter2Counter.setValue("2");
        admissionCounter3Counter.setValue("3");

        cashierCounter1QueueNumber.setValue("C-000");
        cashierCounter2QueueNumber.setValue("C-000");
        cashierCounter3QueueNumber.setValue("C-000");

        cashierCounter2Counter.setValue("2");
        cashierCounter3Counter.setValue("3");

        registrarCounter1QueueNumber.setValue("R-000");
        registrarCounter2QueueNumber.setValue("R-000");
        registrarCounter3QueueNumber.setValue("R-000");

        registrarCounter2Counter.setValue("2");
        registrarCounter3Counter.setValue("3");

        admissionRef.addSnapshotListener((snapshot, error) -> {
            if (snapshot != null && snapshot.exists()) {
                Long currentCounter = snapshot.getLong("counter");
                long convertedCounter = (currentCounter != null) ? currentCounter : 1L;
                Boolean isOnBreak = snapshot.getBoolean("isOnBreak");
                Long currentServing = snapshot.getLong("currentServing");
                Long currentCutOff = snapshot.getLong("cutOffNumber");
                String formattedServing = String.format("%03d", (currentServing != null) ? currentServing : 1L);

                switch ((int) convertedCounter) {
                    case 1:
                        if (isOnBreak != null && isOnBreak) {
                            admissionCounter1QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            admissionCounter1QueueNumber.setValue("A-000");
                            admissionCounter1CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef1 = db.collection("TICKETS");
                        ticketsRef1.whereEqualTo("service", "admission")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        admissionCounter1QueueNumber.setValue("A-P-" + formattedServing);
                                        return;
                                    }
                                    admissionCounter1QueueNumber.setValue("A-" + formattedServing);
                                });
                        admissionCounter1CutOff.setValue(currentCutOff.toString());
                        admissionCounter1Counter.setValue("1");
                        break;
                    case 2:
                        if (isOnBreak != null && isOnBreak) {
                            admissionCounter2QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            admissionCounter2QueueNumber.setValue("A-000");
                            admissionCounter2CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef2 = db.collection("TICKETS");
                        ticketsRef2.whereEqualTo("service", "admission")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        admissionCounter2QueueNumber.setValue("A-P-" + formattedServing);
                                        return;
                                    }
                                    admissionCounter2QueueNumber.setValue("A-" + formattedServing);
                                });
                        admissionCounter2CutOff.setValue(currentCutOff.toString());
                        admissionCounter2Counter.setValue("2");
                        break;
                    case 3:
                        if (isOnBreak != null && isOnBreak) {
                            admissionCounter3QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            admissionCounter3QueueNumber.setValue("A-000");
                            admissionCounter3CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef3 = db.collection("TICKETS");
                        ticketsRef3.whereEqualTo("service", "admission")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        admissionCounter3QueueNumber.setValue("A-P-" + formattedServing);
                                        return;
                                    }
                                    admissionCounter3QueueNumber.setValue("A-" + formattedServing);
                                });
                        admissionCounter3CutOff.setValue(currentCutOff.toString());
                        admissionCounter3Counter.setValue("3");
                        break;
                    default:
                        break;
                }
            }
        });

        cashierRef.addSnapshotListener((snapshot, error) -> {
            if (snapshot != null && snapshot.exists()) {
                Long currentCounter = snapshot.getLong("counter");
                long convertedCounter = (currentCounter != null) ? currentCounter : 1L;
                Boolean isOnBreak = snapshot.getBoolean("isOnBreak");
                Long currentServing = snapshot.getLong("currentServing");
                Long currentCutOff = snapshot.getLong("cutOffNumber");
                String formattedServing = String.format("%03d", (currentServing != null) ? currentServing : 1L);

                switch ((int) convertedCounter) {
                    case 1:
                        if (isOnBreak != null && isOnBreak) {
                            cashierCounter1QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            cashierCounter1QueueNumber.setValue("C-000");
                            cashierCounter1CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef1 = db.collection("TICKETS");
                        ticketsRef1.whereEqualTo("service", "cashier")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        cashierCounter1QueueNumber.setValue("C-P-" + formattedServing);
                                        return;
                                    }
                                    cashierCounter1QueueNumber.setValue("C-" + formattedServing);
                                });
                        cashierCounter1CutOff.setValue(currentCutOff.toString());
                        cashierCounter1Counter.setValue("1");
                        break;
                    case 2:
                        if (isOnBreak != null && isOnBreak) {
                            cashierCounter2QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            cashierCounter2QueueNumber.setValue("C-000");
                            cashierCounter2CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef2 = db.collection("TICKETS");
                        ticketsRef2.whereEqualTo("service", "cashier")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        cashierCounter2QueueNumber.setValue("C-P-" + formattedServing);
                                        return;
                                    }
                                    cashierCounter2QueueNumber.setValue("C-" + formattedServing);
                                });
                        cashierCounter2CutOff.setValue(currentCutOff.toString());
                        cashierCounter1Counter.setValue("2");
                        break;
                    case 3:
                        if (isOnBreak != null && isOnBreak) {
                            cashierCounter3QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            cashierCounter3QueueNumber.setValue("C-000");
                            cashierCounter3CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef3 = db.collection("TICKETS");
                        ticketsRef3.whereEqualTo("service", "cashier")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        cashierCounter3QueueNumber.setValue("C-P-" + formattedServing);
                                        return;
                                    }
                                    cashierCounter3QueueNumber.setValue("C-" + formattedServing);
                                });
                        cashierCounter3CutOff.setValue(currentCutOff.toString());
                        cashierCounter1Counter.setValue("3");
                        break;
                    default:
                        break;
                }
            }
        });

        registrarRef.addSnapshotListener((snapshot, error) -> {
            if (snapshot != null && snapshot.exists()) {
                Long currentCounter = snapshot.getLong("counter");
                long convertedCounter = (currentCounter != null) ? currentCounter : 1L;
                Boolean isOnBreak = snapshot.getBoolean("isOnBreak");
                Long currentServing = snapshot.getLong("currentServing");
                Long currentCutOff = snapshot.getLong("cutOffNumber");
                String formattedServing = String.format("%03d", (currentServing != null) ? currentServing : 1L);

                switch ((int) convertedCounter) {
                    case 1:
                        if (isOnBreak != null && isOnBreak) {
                            registrarCounter1QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            registrarCounter1QueueNumber.setValue("R-000");
                            registrarCounter1CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef1 = db.collection("TICKETS");
                        ticketsRef1.whereEqualTo("service", "registrar")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        registrarCounter1QueueNumber.setValue("R-P-" + formattedServing);
                                        return;
                                    }
                                    registrarCounter1QueueNumber.setValue("R-" + formattedServing);
                                });
                        registrarCounter1CutOff.setValue(currentCutOff.toString());
                        registrarCounter1Counter.setValue("1");
                        break;
                    case 2:
                        if (isOnBreak != null && isOnBreak) {
                            registrarCounter2QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            registrarCounter2QueueNumber.setValue("R-000");
                            registrarCounter2CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef2 = db.collection("TICKETS");
                        ticketsRef2.whereEqualTo("service", "registrar")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        registrarCounter2QueueNumber.setValue("R-P-" + formattedServing);
                                        return;
                                    }
                                    registrarCounter2QueueNumber.setValue("R-" + formattedServing);
                                });
                        registrarCounter2CutOff.setValue(currentCutOff.toString());
                        registrarCounter2Counter.setValue("2");
                        break;
                    case 3:
                        if (isOnBreak != null && isOnBreak) {
                            registrarCounter3QueueNumber.setValue("PAUSED");
                            return;
                        }
                        if (currentServing != null && currentServing == 0) {
                            registrarCounter1QueueNumber.setValue("R-000");
                            registrarCounter1CutOff.setValue("000");
                        }
                        CollectionReference ticketsRef3 = db.collection("TICKETS");
                        ticketsRef3.whereEqualTo("service", "registrar")
                                .whereEqualTo("number", currentServing)
                                .limit(1)
                                .addSnapshotListener((ticketsSnapshot, ticketsError) -> {
                                    boolean isPWD = false;
                                    for (DocumentSnapshot doc : ticketsSnapshot.getDocuments()) {
                                        isPWD = doc.getBoolean("isPWD");
                                    }
                                    if (isPWD) {
                                        registrarCounter3QueueNumber.setValue("R-P-" + formattedServing);
                                        return;
                                    }
                                    registrarCounter3QueueNumber.setValue("R-" + formattedServing);
                                });
                        registrarCounter3CutOff.setValue(currentCutOff.toString());
                        registrarCounter3Counter.setValue("3");
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
