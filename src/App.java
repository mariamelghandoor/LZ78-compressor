import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class App {

    // method to return the number of bits needed to code an integer number
    public static int bitsNeeded(int number) {
        if (number == 0) {
            return 1; // Special case for 0
        }
        return (int)(Math.floor(Math.log(number) / Math.log(2))) + 1;
    }
    public static String toBinaryString(int number, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(number)).replace(' ', '0');
    }

    public static void compress_LZ78(String inputFileName, String outputFileName) {
        StringBuilder code = new StringBuilder();
        String input_file_path = "resources/" + inputFileName;
        try (BufferedReader br = new BufferedReader(new FileReader(input_file_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                code.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }

        ArrayList<ArrayList<String>> compressed_data = new ArrayList<>();
        StringBuilder binaryString = new StringBuilder();

        compressed_data.add(new ArrayList<>(Arrays.asList("0", null)));
        ArrayList<String> lookupArrayList = new ArrayList<>();
        lookupArrayList.add(null);
        int index;
        String saved, looking_for = "";
        int maxIndex = 0;
        for (int i = 0; i < code.length(); i++) {
            looking_for += code.charAt(i);
            index = 0;
            for (int j = 0; j < compressed_data.size(); j++) {
                if (lookupArrayList.get(j)!= null && lookupArrayList.get(j).equals(looking_for)) {
                    index = j;
                    if (i < code.length() - 1) {
                        looking_for += code.charAt(++i);
                    } else {
                        looking_for = (looking_for.length() == 1) ? null : looking_for;
                        break;
                    }
                }
            }
            // System.out.println("Index: " + index + "\nLooking for: " + looking_for);
            lookupArrayList.add(looking_for);
            saved = (looking_for == null) ? null : String.valueOf(looking_for.charAt(looking_for.length() - 1));
            compressed_data.add(new ArrayList<>(Arrays.asList(String.valueOf(index), saved)));
            maxIndex = Math.max(maxIndex, index);
            binaryString.append(toBinaryString(index, 8));
            binaryString.append(toBinaryString(saved != null ? saved.charAt(0) : 0, 8));

            looking_for = "";
        }
        System.out.println("Binary string: " + binaryString + "\nComprressed array " + compressed_data);

        int bitsRequired = bitsNeeded(code.length());
        System.out.println("Bits needed to represent file size ("+ ((code.length()-1) * 8) +") bits: " + bitsRequired *code.length() );
        int compressedSize = (bitsNeeded(maxIndex) + 8) * compressed_data.size(); 
        System.out.println("Compressed size: " + compressedSize + " bites");

        try (FileWriter fw = new FileWriter("resources/" + outputFileName)) {
            fw.write(binaryString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Binary string saved successfully.");
    }

    public static void decompress_LZ78(String binaryFileName, String txtFileName) {
        String binaryString = "";
        try (BufferedReader br = new BufferedReader(new FileReader("resources/" + binaryFileName))) {
            binaryString = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ArrayList<ArrayList<String>> compressed_data = new ArrayList<>();
        compressed_data.add(new ArrayList<>(Arrays.asList("0", null)));

        for (int i = 0; i < binaryString.length(); i += 16) {
            int index = Integer.parseInt(binaryString.substring(i, i + 8), 2);
            int asciiValue = Integer.parseInt(binaryString.substring(i + 8, i + 16), 2);

            String character = (asciiValue != 0) ? String.valueOf((char) asciiValue) : null;
            compressed_data.add(new ArrayList<>(Arrays.asList(String.valueOf(index), character)));
        }

        StringBuilder decompressed_data = new StringBuilder();
        ArrayList<String> lookupArrayList = new ArrayList<>();
        lookupArrayList.add(null);
        for (int i = 1; i < compressed_data.size(); i++) {
            if (compressed_data.get(i).get(0).equals("0")){
                decompressed_data.append(compressed_data.get(i).get(1));
                lookupArrayList.add(compressed_data.get(i).get(1));
            }else {
                decompressed_data.append(lookupArrayList.get(Integer.parseInt(compressed_data.get(i).get(0))));
                if (compressed_data.get(i).get(1) != null)
                    decompressed_data.append(compressed_data.get(i).get(1));
                    String data = lookupArrayList.get(Integer.parseInt(compressed_data.get(i).get(0))) + compressed_data.get(i).get(1);
                    lookupArrayList.add(data);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/" + txtFileName))) {
            bw.write(decompressed_data.toString());
            System.out.println("Decompressed data saved to " + txtFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an option: compress, decompress, or exit");
            String choice = scanner.nextLine().trim().toLowerCase();
            
            switch (choice) {
                case "compress":
                    System.out.print("Enter input file name: ");
                    String inputFile = scanner.nextLine();
                    System.out.print("Enter output binary file name: ");
                    String outputFile = scanner.nextLine();
                    compress_LZ78(inputFile, outputFile);
                    break;
                
                case "decompress":
                    System.out.print("Enter binary file name: ");
                    String binFile = scanner.nextLine();
                    System.out.print("Enter output text file name: ");
                    String txtFile = scanner.nextLine();
                    decompress_LZ78(binFile, txtFile);
                    break;
                
                case "exit":
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                
                default:
                    System.out.println("Invalid choice. Please enter 'compress', 'decompress', or 'exit'.");
            }
        }
    }
}