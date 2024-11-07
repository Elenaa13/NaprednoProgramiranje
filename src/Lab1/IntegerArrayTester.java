package Lab1;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;


class IntegerArray {

    private final int[] arr;

    public IntegerArray(int[] arr){
        this.arr=Arrays.copyOf(arr, arr.length);
    }

    public int length(){
        return arr.length;
    }

    public int getElementAt(int i){
        return arr[i];
    }

    public int sum(){
        int sum=0;
        for (int i=0; i<arr.length; i++){
            sum+=arr[i];
        }
        return sum;
    }

    public double average(){
        return (double)sum()/ arr.length;
    }

    public IntegerArray getSorted(){
        int [] arr2=new int[arr.length];
        for (int i=0; i<arr.length; i++){
            arr2[i]=arr[i];
        }
        for (int i=0;i<arr2.length;i++){
            for (int j=i+1;j<arr2.length;j++){
                if (arr2[i]>arr2[j]){
                    int tmp=arr2[i];
                    arr2[i]=arr2[j];
                    arr2[j]=tmp;
                }
            }
        }
        return new IntegerArray(arr2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerArray that = (IntegerArray) o;
        return Objects.deepEquals(arr, that.arr);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(arr);
    }

    public IntegerArray concat(IntegerArray ia){
        int size=arr.length+ia.length();
        int[] arr3=new int[size];
        for (int i=0; i<size;i++){
            if (i<this.arr.length){
                arr3[i]=this.arr[i];
            }
            else {
                arr3[i]+=ia.arr[i- ia.length()];
            }
        }
        return new IntegerArray(arr3);
    }
    @Override
    public String toString() {
        return Arrays.toString(arr).replace("[", "[").replace("]", "]");
    }

//    public IntegerArray add(IntegerArray ia) {
//        if (this.arr.length==ia.length()){
//            for (int i=0; i<this.arr.length; i++){
//                 arr[i]+=ia.arr[i];
//            }
//        }
//        return this;
//    }
//
//    public double mediana(){
//        double median;
//
//    }

}



class ArrayReader {

    public static IntegerArray readIntegerArray(InputStream input) {
        Scanner jin = new Scanner(input);
        int n = jin.nextInt();
        int a[] = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = jin.nextInt();
        }
        jin.close();
        return new IntegerArray(a);
    }

}

public class IntegerArrayTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        IntegerArray ia = null;
        switch (s) {
            case "testSimpleMethods":
                ia = new IntegerArray(generateRandomArray(scanner.nextInt()));
                testSimpleMethods(ia);
                break;
            case "testConcat":
                testConcat(scanner);
                break;
            case "testEquals":
                testEquals(scanner);
                break;
            case "testSorting":
                testSorting(scanner);
                break;
            case "testReading":
                testReading(new ByteArrayInputStream(scanner.nextLine().getBytes()));
                break;
            case "testImmutability":
                int a[] = generateRandomArray(scanner.nextInt());
                ia = new IntegerArray(a);
                testSimpleMethods(ia);
                testSimpleMethods(ia);
                IntegerArray sorted_ia = ia.getSorted();
                testSimpleMethods(ia);
                testSimpleMethods(sorted_ia);
                sorted_ia.getSorted();
                testSimpleMethods(sorted_ia);
                testSimpleMethods(ia);
                a[0] += 2;
                testSimpleMethods(ia);
                ia = ArrayReader.readIntegerArray(new ByteArrayInputStream(integerArrayToString(ia).getBytes()));
                testSimpleMethods(ia);
                break;
        }
        scanner.close();
    }

    static void testReading(InputStream in) {
        IntegerArray read = ArrayReader.readIntegerArray(in);
        System.out.println(read);
    }

    static void testSorting(Scanner scanner) {
        int[] a = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        System.out.println(ia.getSorted());
    }

    static void testEquals(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        int[] c = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        IntegerArray ib = new IntegerArray(b);
        IntegerArray ic = new IntegerArray(c);
        System.out.println(ia.equals(ib));
        System.out.println(ia.equals(ic));
        System.out.println(ib.equals(ic));
    }

    static void testConcat(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        IntegerArray array1 = new IntegerArray(a);
        IntegerArray array2 = new IntegerArray(b);
        IntegerArray concatenated = array1.concat(array2);
        System.out.println(concatenated);
    }

    static void testSimpleMethods(IntegerArray ia) {
        System.out.print(integerArrayToString(ia));
        System.out.println(ia);
        System.out.println(ia.sum());
        System.out.printf("%.2f\n", ia.average());
    }


    static String integerArrayToString(IntegerArray ia) {
        StringBuilder sb = new StringBuilder();
        sb.append(ia.length()).append('\n');
        for (int i = 0; i < ia.length(); ++i)
            sb.append(ia.getElementAt(i)).append(' ');
        sb.append('\n');
        return sb.toString();
    }

    static int[] readArray(Scanner scanner) {
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = scanner.nextInt();
        }
        return a;
    }


    static int[] generateRandomArray(int k) {
        Random rnd = new Random(k);
        int n = rnd.nextInt(8) + 2;
        int a[] = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = rnd.nextInt(20) - 5;
        }
        return a;
    }

}


