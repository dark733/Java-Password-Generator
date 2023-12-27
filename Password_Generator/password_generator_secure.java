import java.io.*;
import java.util.*;
import java.util.Base64;

public class password_generator_secure {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        preExistingChecker pep = new preExistingChecker();
        pep.preExistingPass();
        ask_password();
        try{
            System.out.println("Enter The Email/Id");
            String email = scanner.nextLine();
            System.out.print("Enter the length of the password: ");
            int length = scanner.nextInt();
            if(length<8 && length>0){
                throw new tooShortLengthException("The Password length is too short and is not secure");
            }
            else if(length<=0){
                throw new invalidLengthException("The Password length cannot be less than or equal to zero");
            }
            else{
                System.out.println("Length verified ok.");
            }
            String password = generatePassword(length);
            System.out.println("Generated Password: " + password);
            String base64EncodedPassword = convertToBase64(password);
            System.out.println("Base64 Encoded Password: " + base64EncodedPassword);
            storePassword(password,email);
            scanner.close();
        }
        
        catch(Exception e){
            System.out.println("Something went wrong... try again" + e);

        }

    }
    //ask user for password generator verification/authntication password
    public static void ask_password(){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the key to access the Password generator program");
        String user_pass_inp = s.nextLine();
        try{
            if(user_pass_inp.equals("root")){
                System.out.println("Access Granted!");
                System.out.println("<<------------>>");
            }
            else{
                System.out.println("Access Denied");
                boolean flag=false;
                throw new accessDeniedPasswordException("Exception occurred",flag);
            }

        }
        catch(accessDeniedPasswordException e){
            System.out.println("Custom Exception caught" + e.getMessage());
        }

    }

    // Function to generate a random password
    public static String generatePassword(int length) {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String combinedChars = uppercase + lowercase + numbers;
        Random random = new Random();
        char[] password = new char[length];

        // Ensure at least one uppercase, one lowercase, and one digit character
        password[0] = uppercase.charAt(random.nextInt(uppercase.length()));
        password[1] = lowercase.charAt(random.nextInt(lowercase.length()));
        password[2] = numbers.charAt(random.nextInt(numbers.length()));

        // Fill the remaining characters randomly
        for (int i = 3; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }

        // Shuffle the characters to make the sequence random
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(length);
            char temp = password[randomIndex];
            password[randomIndex] = password[i];
            password[i] = temp;
        }

        return new String(password);
    }

    // Function to convert a string to Base64
    public static String convertToBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    // Function to store the generated password in a file
    public static void storePassword(String password,String email) {
        try {
            File file = new File("passwords_gen.txt");
            FileWriter writer = new FileWriter(file, true);

            if (!file.exists()) {
                file.createNewFile();
            }

            Scanner scanner = new Scanner(file);
            int count = 0;
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                count++;
            }

            writer.write((count + 1) + ". Email/ID: " + email + ", Password: " + password + "\n");

            writer.close();
            scanner.close();
            System.out.println("Password stored in 'passwords_gen.txt' file.");
        } catch (IOException e) {
            System.out.println("An error occurred while storing the password: " + e.getMessage());
        }
    }
    

}
class preExistingChecker{
    public static void preExistingPass(){
        Scanner s = new Scanner(System.in);
        System.out.println("Do you have pre-Existing password and key? Y/y=Yes N/n=No");
        try{
            String str=s.nextLine();
            if(str.equals("Y") || str.equals("y")){
                System.out.println("Y=Ok");
                yesP();

            }
            else if(str.equals("N")||str.equals("n")){
                System.out.println("N=No");
                noP();

            }
            else{
                throw new invalidInputException("Invalid Input Exception");

            }
            

        }
        catch(invalidInputException e){
            System.out.println("Exception Caught" + e);
        }
        catch(Exception e){
            System.out.println("Other Exception Caught/Something went wrong "+e);
        }
        finally{
                System.out.println("Proceeding to Generator..");
        }

    }
    public static void yesP(){
        Scanner s = new Scanner(System.in);
        System.out.print("Enter your email: ");
        String email = s.nextLine();

        System.out.print("Enter your password: ");
        String password = s.nextLine();

        // Write the email and password to a file
        saveCredentialsToFile(email, password);

        System.out.println("Credentials saved successfully!");
        s.close();
        System.exit(0);

    }
    public static void noP(){
        Scanner s = new Scanner(System.in);
        System.out.println("Ok now moving to generator");
        // s.close();


    }
    public static void saveCredentialsToFile(String email, String password) {
        Scanner s = new Scanner(System.in);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("credentials.txt"));
            writer.write("Email: " + email);
            writer.newLine();
            writer.write("Password: " + password);
            writer.close();
        } 
        catch (IOException e) {
            System.out.println("An error occurred while saving credentials to file.");
            e.printStackTrace();
        }
        catch(Exception e){
            System.out.println("Exception Caught" + e);
        }
    }
}
class accessDeniedPasswordException extends Exception{
    public accessDeniedPasswordException(String msg,boolean flag){
        super(msg);
        if(flag == false){
            System.exit(0);
        }
    }

}
class tooShortLengthException extends Exception{
    public tooShortLengthException(String msg){
        super(msg);
    }
}
class invalidLengthException extends Exception{
    public invalidLengthException(String msg){
        super(msg);
    }
}
class invalidInputException extends Exception{
    public invalidInputException(String msg){
        super(msg);


    }
    
}
