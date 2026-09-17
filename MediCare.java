import java.util.*;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * PROJECT: MediCare – Hospital/Clinic Appointment, Prescription & Billing Management System
 * TECH STACK: Java
 * 
 * This is a complete, single-file Java project demonstrating:
 * - OOP Principles (Encapsulation, Inheritance, Abstraction, Polymorphism)
 * - Java Collections (ArrayList, HashMap)
 * - Multithreading (Appointment Reminder Thread)
 * - Exception Handling
 * - Singleton Design Pattern
 * - Input Validation (Regex)
 */
public class MediCare {

    // Main controller and entry point
    public static void main(String[] args) {
        HospitalController controller = new HospitalController();
        controller.start();
    }

    // ==========================================
    // 1. DATA MODELS (Demonstrating Encapsulation & Inheritance)
    // ==========================================

    /**
     * Abstract base class for all entities.
     * Demonstrates: Inheritance and Abstraction.
     */
    static abstract class BaseEntity {
        private int id;
        private String createdAt;

        public BaseEntity(int id) {
            this.id = id;
            this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getCreatedAt() { return createdAt; }
    }

    static class Patient extends BaseEntity {
        private String name, phone, email, address;
        private int age;

        public Patient(int id, String name, int age, String phone, String email, String address) {
            super(id);
            this.name = name;
            this.age = age;
            this.phone = phone;
            this.email = email;
            this.address = address;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        @Override
        public String toString() {
            return String.format("[ID: %d] Name: %-15s | Age: %-3d | Phone: %-12s", getId(), name, age, phone);
        }
    }

    static class Doctor extends BaseEntity {
        private String name, specialization, phone;

        public Doctor(int id, String name, String specialization, String phone) {
            super(id);
            this.name = name;
            this.specialization = specialization;
            this.phone = phone;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSpecialization() { return specialization; }
        public void setSpecialization(String spec) { this.specialization = spec; }

        @Override
        public String toString() {
            return String.format("[ID: %d] Dr. %-15s | Specialization: %-15s", getId(), name, specialization);
        }
    }

    static class Appointment extends BaseEntity {
        private int patientId, doctorId;
        private String date, time, reason, status; // Scheduled, Completed, Cancelled

        public Appointment(int id, int patientId, int doctorId, String date, String time, String reason) {
            super(id);
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.date = date;
            this.time = time;
            this.reason = reason;
            this.status = "Scheduled";
        }

        public int getPatientId() { return patientId; }
        public int getDoctorId() { return doctorId; }
        public String getDate() { return date; }
        public void setDate(String d) { this.date = d; }
        public String getTime() { return time; }
        public void setTime(String t) { this.time = t; }
        public String getStatus() { return status; }
        public void setStatus(String s) { this.status = s; }

        @Override
        public String toString() {
            return String.format("[ID: %d] Date: %s | Time: %s | Status: %s", getId(), date, time, status);
        }
    }

    static class Medicine {
        private String name, dosage, duration;

        public Medicine(String name, String dosage, String duration) {
            this.name = name;
            this.dosage = dosage;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return name + " (" + dosage + ") - " + duration;
        }
    }

    static class Prescription extends BaseEntity {
        private int appointmentId, patientId;
        private String diagnosis;
        private List<Medicine> medicines;

        public Prescription(int id, int appointmentId, int patientId, String diagnosis) {
            super(id);
            this.appointmentId = appointmentId;
            this.patientId = patientId;
            this.diagnosis = diagnosis;
            this.medicines = new ArrayList<>();
        }

        public void addMedicine(Medicine m) { medicines.add(m); }
        public List<Medicine> getMedicines() { return medicines; }
        public String getDiagnosis() { return diagnosis; }
    }

    static class Bill extends BaseEntity {
        private int appointmentId;
        private double consultationFee, medicineCharges, total;
        private String paymentStatus;

        public Bill(int id, int appointmentId, double consultationFee, double medicineCharges) {
            super(id);
            this.appointmentId = appointmentId;
            this.consultationFee = consultationFee;
            this.medicineCharges = medicineCharges;
            this.total = consultationFee + medicineCharges;
            this.paymentStatus = "Pending";
        }

        public void setPaymentStatus(String s) { this.paymentStatus = s; }

        @Override
        public String toString() {
            return String.format("[Bill ID: %d] Total: ₹%.2f | Status: %s", getId(), total, paymentStatus);
        }
    }

    // ==========================================
    // 2. DATA STORAGE (Demonstrating Singleton & Collections)
    // ==========================================

    static class MediCareDB {
        private static MediCareDB instance;
        private Map<Integer, Patient> patients = new HashMap<>();
        private Map<Integer, Doctor> doctors = new HashMap<>();
        private Map<Integer, Appointment> appointments = new HashMap<>();
        private List<Prescription> prescriptions = new ArrayList<>();
        private List<Bill> bills = new ArrayList<>();

        private int pId = 1, dId = 1, aId = 1, prId = 1, bId = 1;

        private MediCareDB() {}

        public static synchronized MediCareDB getInstance() {
            if (instance == null) instance = new MediCareDB();
            return instance;
        }

        public void addPatient(Patient p) { p.setId(pId++); patients.put(p.getId(), p); }
        public void addDoctor(Doctor d) { d.setId(dId++); doctors.put(d.getId(), d); }
        public void addAppointment(Appointment a) { a.setId(aId++); appointments.put(a.getId(), a); }
        public void addPrescription(Prescription pr) { pr.setId(prId++); prescriptions.add(pr); }
        public void addBill(Bill b) { b.setId(bId++); bills.add(b); }

        public Patient getPatient(int id) { return patients.get(id); }
        public Doctor getDoctor(int id) { return doctors.get(id); }
        public Appointment getAppointment(int id) { return appointments.get(id); }
        public Collection<Patient> allPatients() { return patients.values(); }
        public Collection<Doctor> allDoctors() { return doctors.values(); }
        public Collection<Appointment> allAppointments() { return appointments.values(); }
        public List<Bill> allBills() { return bills; }
        public List<Prescription> allPrescriptions() { return prescriptions; }
    }

    // ==========================================
    // 3. MULTITHREADING (Appointment Reminder)
    // ==========================================

    static class ReminderThread extends Thread {
        private boolean running = true;
        private MediCareDB db = MediCareDB.getInstance();

        public void stopReminder() { running = false; }

        @Override
        public void run() {
            System.out.println("[System] Background Reminder Thread Started...");
            while (running) {
                try {
                    String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    for (Appointment a : db.allAppointments()) {
                        if (a.getDate().equals(today) && a.getStatus().equals("Scheduled")) {
                            Patient p = db.getPatient(a.getPatientId());
                            System.out.println("\n>>> REMINDER: Appointment #" + a.getId() + " for " + p.getName() + " is today at " + a.getTime() + "!");
                        }
                    }
                    Thread.sleep(30000); // Check every 30 seconds
                } catch (InterruptedException e) {
                    System.out.println("[System] Reminder Thread Interrupted.");
                    break;
                }
            }
        }
    }

    // ==========================================
    // 4. BUSINESS CONTROLLER (The Logic)
    // ==========================================

    static class HospitalController {
        private MediCareDB db = MediCareDB.getInstance();
        private Scanner sc = new Scanner(System.in);
        private ReminderThread reminder = new ReminderThread();

        public void start() {
            reminder.start();
            System.out.println("**************************************************");
            System.out.println("*     Welcome to MediCare Hospital System      *");
            System.out.println("**************************************************");

            while (true) {
                try {
                    System.out.println("\nMAIN MENU:");
                    System.out.println("1. Patient Management");
                    System.out.println("2. Doctor Management");
                    System.out.println("3. Appointment Management");
                    System.out.println("4. Prescription Management");
                    System.out.println("5. Billing & Payments");
                    System.out.println("6. Exit");
                    System.out.print("Choose option: ");
                    
                    int choice = Integer.parseInt(sc.nextLine());

                    switch (choice) {
                        case 1: patientMenu(); break;
                        case 2: doctorMenu(); break;
                        case 3: appointmentMenu(); break;
                        case 4: prescriptionMenu(); break;
                        case 5: billingMenu(); break;
                        case 6: 
                            reminder.stopReminder();
                            System.out.println("Exiting MediCare. Goodbye!");
                            System.exit(0);
                        default: System.out.println("Invalid choice.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        // --- Patient CRUD ---
        private void patientMenu() {
            System.out.println("\n[PATIENT MANAGEMENT]");
            System.out.println("1. Add Patient  2. View All  3. Search  4. Delete");
            int c = Integer.parseInt(sc.nextLine());
            if (c == 1) {
                System.out.print("Name: "); String name = sc.nextLine();
                System.out.print("Age: "); int age = Integer.parseInt(sc.nextLine());
                System.out.print("Phone: "); String phone = sc.nextLine();
                System.out.print("Email: "); String email = sc.nextLine();
                System.out.print("Address: "); String addr = sc.nextLine();

                if (name.isEmpty() || !phone.matches("\\d{10}")) {
                    System.out.println("Validation Failed: Name required and Phone must be 10 digits.");
                    return;
                }
                db.addPatient(new Patient(0, name, age, phone, email, addr));
                System.out.println("Patient added successfully.");
            } else if (c == 2) {
                db.allPatients().forEach(System.out::println);
            } else if (c == 3) {
                System.out.print("Enter Patient Name to search: ");
                String q = sc.nextLine().toLowerCase();
                db.allPatients().stream().filter(p -> p.getName().toLowerCase().contains(q)).forEach(System.out::println);
            } else if (c == 4) {
                System.out.print("Enter ID to delete: ");
                int id = Integer.parseInt(sc.nextLine());
                db.patients.remove(id);
                System.out.println("Deleted if existed.");
            }
        }

        // --- Doctor CRUD ---
        private void doctorMenu() {
            System.out.println("\n[DOCTOR MANAGEMENT]");
            System.out.println("1. Add Doctor  2. View All");
            int c = Integer.parseInt(sc.nextLine());
            if (c == 1) {
                System.out.print("Name: "); String name = sc.nextLine();
                System.out.print("Specialization: "); String spec = sc.nextLine();
                System.out.print("Phone: "); String phone = sc.nextLine();
                db.addDoctor(new Doctor(0, name, spec, phone));
                System.out.println("Doctor added.");
            } else if (c == 2) {
                db.allDoctors().forEach(System.out::println);
            }
        }

        // --- Appointment Logic ---
        private void appointmentMenu() {
            System.out.println("\n[APPOINTMENT MANAGEMENT]");
            System.out.println("1. Book  2. View All  3. Cancel");
            int c = Integer.parseInt(sc.nextLine());
            if (c == 1) {
                System.out.print("Patient ID: "); int pId = Integer.parseInt(sc.nextLine());
                System.out.print("Doctor ID: "); int dId = Integer.parseInt(sc.nextLine());
                System.out.print("Date (YYYY-MM-DD): "); String date = sc.nextLine();
                System.out.print("Time (HH:MM): "); String time = sc.nextLine();
                
                if (db.getPatient(pId) == null || db.getDoctor(dId) == null) {
                    System.out.println("Invalid Patient or Doctor ID.");
                    return;
                }

                // Check Doctor Double-Booking
                for (Appointment a : db.allAppointments()) {
                    if (a.getDoctorId() == dId && a.getDate().equals(date) && a.getTime().equals(time) && !a.getStatus().equals("Cancelled")) {
                        System.out.println("CONFLICT: Doctor is already booked at this time!");
                        return;
                    }
                }

                db.addAppointment(new Appointment(0, pId, dId, date, time, "General Checkup"));
                System.out.println("Appointment Booked.");
            } else if (c == 2) {
                db.allAppointments().forEach(System.out::println);
            } else if (c == 3) {
                System.out.print("Enter Appointment ID to cancel: ");
                int id = Integer.parseInt(sc.nextLine());
                Appointment a = db.getAppointment(id);
                if (a != null) a.setStatus("Cancelled");
                System.out.println("Status updated to Cancelled.");
            }
        }

        // --- Prescription Logic ---
        private void prescriptionMenu() {
            System.out.println("\n[PRESCRIPTION MANAGEMENT]");
            System.out.println("1. Create Prescription  2. View All");
            int c = Integer.parseInt(sc.nextLine());
            if (c == 1) {
                System.out.print("Appointment ID: "); int aId = Integer.parseInt(sc.nextLine());
                Appointment a = db.getAppointment(aId);
                if (a == null) { System.out.println("Appointment not found."); return; }

                System.out.print("Diagnosis: "); String diag = sc.nextLine();
                Prescription pr = new Prescription(0, aId, a.getPatientId(), diag);
                
                System.out.print("How many medicines? ");
                int n = Integer.parseInt(sc.nextLine());
                for (int i = 0; i < n; i++) {
                    System.out.print("Medicine Name: "); String mName = sc.nextLine();
                    System.out.print("Dosage: "); String dose = sc.nextLine();
                    pr.addMedicine(new Medicine(mName, dose, "5 days"));
                }
                db.addPrescription(pr);
                a.setStatus("Completed");
                System.out.println("Prescription saved.");
            } else if (c == 2) {
                for (Prescription p : db.allPrescriptions()) {
                    Patient pat = db.getPatient(p.patientId);
                    System.out.println("Patient: " + pat.getName() + " | Diagnosis: " + p.getDiagnosis());
                    System.out.println("Medicines: " + p.getMedicines());
                }
            }
        }

        // --- Billing Logic ---
        private void billingMenu() {
            System.out.println("\n[BILLING MANAGEMENT]");
            System.out.println("1. Generate Bill  2. View Bills");
            int c = Integer.parseInt(sc.nextLine());
            if (c == 1) {
                System.out.print("Appointment ID: "); int aId = Integer.parseInt(sc.nextLine());
                System.out.print("Consultation Fee: "); double fee = Double.parseDouble(sc.nextLine());
                System.out.print("Medicine Charges: "); double med = Double.parseDouble(sc.nextLine());
                
                Bill b = new Bill(0, aId, fee, med);
                System.out.print("Mark as Paid? (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) b.setPaymentStatus("Paid");
                db.addBill(b);
                System.out.println("Bill Generated: " + b);
            } else if (c == 2) {
                db.allBills().forEach(System.out::println);
            }
        }
    }
}
