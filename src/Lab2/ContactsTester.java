package Lab2;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

enum Operator{
    VIP,
    ONE,
    TMOBILE
}
abstract class Contact {
    //    private final String date;
    private final int year;
    private final int month;
    private final int days;

    public Contact(String date) {
        String[] parts = date.split("-");
        year = Integer.parseInt(parts[0]);
        month = Integer.parseInt(parts[1]);
        days = Integer.parseInt(parts[2]);
    }

    public boolean isNewerThan(Contact contact){
        // if (contact==null)return false;
        return getDays() > contact.getDays();
    }
    public long getDays(){
        return year* 365L + month*30L + days;
    }
    public abstract String getType();
}

class EmailContact extends Contact{
    private final String email;

    public EmailContact(String date,String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    @Override
    public String toString() {
        return "\"" + email + "\"";
    }
    @Override
    public String getType() {
        return "Email";
    }
}
class PhoneContact extends Contact{

    private final String phoneNumber;
    private final Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phoneNumber = phone;

        char op = phone.charAt(2);
        if (op == '0' || op == '1' || op == '2') {
            operator = Operator.TMOBILE;
        } else if (op == '5' || op == '6') {
            operator = Operator.ONE;
        } else {
            operator = Operator.VIP;
        }
    }

    public String getPhone() {
        return phoneNumber;
    }

    public Operator getOperator() {
        return operator;
    }
    @Override
    public String toString() {
        return "\"" + phoneNumber + "\"";
    }
    @Override
    public String getType() {
        return "Phone";
    }
}
class Student {
    private Contact[] contacts;
    //private int capacity;
    private int numContacts;

    private final String firstName;
    private final String lastName;
    private final String city;
    private final int age;
    private final long index;


    public Student( String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new Contact[0];
        this.numContacts = 0;

    }

    public void addEmailContact(String date, String email){
        contacts = Arrays.copyOf(contacts,numContacts+1);
        contacts[numContacts] = new EmailContact(date,email);
        numContacts++;
    }

    public void addPhoneContact(String date, String phone){
        contacts = Arrays.copyOf(contacts,numContacts+1);
        contacts[numContacts] = new PhoneContact(date,phone);
        numContacts++;
    }

    public Contact[] getEmailContacts() {
        Contact[] array;
        int i = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Email")) {
                i++;
            }
        }

        array = new Contact[i];
        i = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Email")) {
                array[i] = c;
                i++;
            }
        }

        return array;
    }

    public Contact[] getPhoneContacts() {
        Contact[] array;
        int i = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Phone")) {
                i++;
            }
        }

        array = new Contact[i];
        i = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Phone")) {
                array[i] = c;
                i++;
            }
        }

        return array;
    }


    public String getCity() {
        return city;
    }

    public String getFullName(){
        return firstName + " " +lastName;
    }

    public long getIndex() {
        return index;
    }

    public int getNumContacts() {
        return numContacts;
    }

    public Contact getLatestContact(){
        Contact latest = contacts[0];
        for(Contact c : contacts){
            if(c.isNewerThan(latest))latest = c;
        }
        return latest;
    }

    @Override
    public String toString() {
        return "{\"ime\":\"" +
                firstName +
                "\", \"prezime\":\"" +
                lastName +
                "\", \"vozrast\":" +
                age +
                ", \"grad\":\"" +
                city +
                "\", \"indeks\":" +
                index +
                ", \"telefonskiKontakti\":" +
                Arrays.toString(getPhoneContacts()) +
                ", \"emailKontakti\":" +
                Arrays.toString(getEmailContacts()) +
                "}";
    }
}
class Faculty {
    protected String name;
    protected Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = Arrays.copyOf(students, students.length);
    }

    public int countStudentsFromCity(String cityName){
        int counter = 0;
        for(Student s : students){
            if(s.getCity().equals(cityName))counter++;
        }
        return counter;
    }

    public Student getStudent(long index){
        for(Student s : students){
            if(s.getIndex()==index)return s;
        }
        return null;
    }

    public double getAverageNumberOfContacts()
    {
        double av = 0;
        for(Student s : students){
            av += s.getNumContacts();
        }
        return av/students.length;//sumljivo
    }

    public Student getStudentWithMostContacts(){
        Student max = students[0];
        for(Student s : students){
            if(s.getNumContacts()>max.getNumContacts())max = s;
            else if(s.getNumContacts() == max.getNumContacts() && s.getIndex()>max.getIndex())max = s;
        }
        return max;
    }

    @Override
    public String toString() {
        return "{\"fakultet\":\"" +
                name +
                "\", \"studenti\":" +
                Arrays.toString(students) +
                "}";
    }
}
public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
