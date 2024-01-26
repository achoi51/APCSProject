import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class User {
    private String username;
    private String password;
    private double balance;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.balance = 0.0;
    }

    public String getUsername() { return username; }
    public boolean authenticate(String password) { return this.password.equals(password); }
    public double getBalance() { return balance; }

    public void deposit(double amount){
        if(amount > 0) {
            balance += amount;
            System.out.println("New bal: $" + balance);
        }else {
            System.out.println("Enter a positive value");
        }
    }

    public void withdraw(double amount) {
        if(amount > 0 && amount <= balance){
            balance -= amount;
            System.out.println("New bal: $" + balance);
        }else{
            System.out.println("Invalid amount or insufficient funds");
        }
    }
}

public class BankingSystem {
    private static Map<String, User> users = new HashMap<>();
    private static ArrayList<User> leaderboard = new ArrayList<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    createUser();
                    break;
                case 2:
                    deleteUser();
                    break;
                case 3:
                    loginUser();
                    break;
                case 4:
                    payOthers();
                    break;
                case 5:
                    work();
                    break;
                case 6:
                    gamble();
                    break;
                case 7:
                    showLeaderboard();
                    break;
                case 8:
                    listUsersAboveBalance();
                    break;
                case 9:
                    logout();
                    break;
                case 10:
                    System.out.println("Exiting :(");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Banking Project thing for apcs");
        System.out.println("1. Create a new user");
        System.out.println("2. Delete the currently logged in user");
        System.out.println("3. Login");
        System.out.println("4. Pay others");
        System.out.println("5. Work (get random money)");
        System.out.println("6. Gamble");
        System.out.println("7. Show leaderboard");
        System.out.println("8. List users with a certain amount of money");
        System.out.println("9. Logout");
        System.out.println("10. Exit");
        System.out.print("Enter number: ");
    }

    private static void createUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        users.put(username, newUser);
        leaderboard.add(newUser);

        System.out.println("User created successfully!");
    }

    private static void deleteUser() {
        if (currentUser == null) {
            System.out.println("Please log in first");
            return;
        }

        users.remove(currentUser.getUsername());
        leaderboard.remove(currentUser);
        currentUser = null;

        System.out.println("User deleted + Logging out");
    }

    private static void loginUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (!users.containsKey(username)) {
            System.out.println("Username not found. Please create an account");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);

        if (user.authenticate(password)) {
            currentUser = user;
            System.out.println("Login successful. Welcome, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("Invalid password");
        }
    }

    private static void payOthers() {
        if (currentUser == null) {
            System.out.println("Please log in first");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter recipient's username: ");
        String recipientUsername = scanner.nextLine();

        if (!users.containsKey(recipientUsername)) {
            System.out.println("Recipient not found");
            return;
        }

        User recipient = users.get(recipientUsername);

        System.out.print("Enter amount to pay: $");
        double amount = scanner.nextDouble();

        if (amount > 0 && amount <= currentUser.getBalance()) {
            currentUser.withdraw(amount);
            recipient.deposit(amount);
            System.out.println("Payment successful! New balance: $" + currentUser.getBalance());
        } else {
            System.out.println("Invalid payment amount/insufficient funds");
        }
    }

    private static void work() {
        if (currentUser == null) {
            System.out.println("Please log in first");
            return;
        }

        double randomAmount = Math.floor(Math.random() * 1000 + 1);
        currentUser.deposit(randomAmount);

        System.out.println("You earned $" + randomAmount + " from work. New balance: $" + currentUser.getBalance());
    }

    private static void gamble() {
        if (currentUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("How much do you want to gamble?");
        double amount = scanner.nextDouble();
        if(amount<=0){
            System.out.println("invalid");
        }


        System.out.print("Choose Odd (1) or Even (2): ");
        int userChoice = scanner.nextInt();

        if (userChoice != 1 && userChoice != 2) {
            System.out.println("Invalid choice. Please choose 1 (Odd) or 2 (Even).");
            return;
        }

        int diceRoll = (int) (Math.random() * 6) + 1;
        System.out.println("You rolled a " + diceRoll + "!");

        boolean isOdd = (diceRoll % 2 != 0);
        boolean userWins = (userChoice == 1 && isOdd) || (userChoice == 2 && !isOdd);

        if (userWins) {
            double winnings = 2 * diceRoll;
            currentUser.deposit(winnings);
            System.out.println("You won $" + winnings + ". New balance: $" + currentUser.getBalance());
        } else {
            double loss = diceRoll;
            currentUser.withdraw(loss);
            System.out.println("You lost $" + loss + ". New balance: $" + currentUser.getBalance());
        }
    }

    private static void showLeaderboard() {
        if (leaderboard.isEmpty()) {
            System.out.println("No users on the leaderboard");
        } else {
            System.out.println("Leaderboard:");
            for (User user : leaderboard) {
                System.out.println(user.getUsername() + ": $" + user.getBalance());
            }
        }
    }

    private static void listUsersAboveBalance() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter minimum balance: $");
        double minBalance = scanner.nextDouble();

        System.out.println("Users with balance over $" + minBalance + ":");
        for (User user : users.values()) {
            if (user.getBalance() > minBalance) {
                System.out.println(user.getUsername() + ": $" + user.getBalance());
            }
        }
    }

    private static void logout(){
        currentUser = null;
        System.out.println("Logged out");
    }
}
