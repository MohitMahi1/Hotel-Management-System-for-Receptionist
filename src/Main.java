// Hotel Reservation System

import java.sql.*;
import java.util.Date;
import java.util.Scanner;


public class Main {

    //Database url
    private static final String url = "jdbc:mysql://localhost:3306/hoteldb";

    //Database credentials
    private static final String username = "root";
    private static final String password = "@@mohit@@20218982";
    //main method
    public static void main(String[] args) throws Exception {



        // Load the Driver class by forName() method.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // Establish the connection to the SQL by the help of Connection Interface.
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database."+ connection);

            // Now we create our main menu
            while (true){
                System.out.println();
                Scanner sc = new Scanner(System.in);
                System.out.println("HOTEL RESERVATION SYSTEM");
                System.out.println();
                System.out.println("1.  Reserve a Room for Geust");
                System.out.println("2.  View All the Reservation");
                System.out.println("3.  Get Room Number");
                System.out.println("4.  Update Reservation for new Geust (or) Same Geust");
                System.out.println("5.  Delete The Reservation");
//                System.out.println("6.  Resolving complaints");
                System.out.println("0. Exit");
                System.out.println();
                System.out.println("Enter the choice you want: ");
                int choice = sc.nextInt();

                switch (choice){
                    case 1:
                        reserveRoom(connection,sc);
                        break;
                    case 2:
                        viewReserveRoom(connection);
                        break;
                    case 3:
                        getRoomNum(connection,sc);
                        break;
                    case 4:
                        updateReservation(connection,sc);
                        break;
                    case 5:
                        deleteReservation(connection,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Please Enter a Valid number");
                }

            }


        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        catch (InterruptedException e){
//            throw new RuntimeException(e);
//        }

    }

     private static void reserveRoom(Connection connection, Scanner sc){
         try {
             System.out.println("ENTER THE DETAILS OF CUSTOMER");
             System.out.println();
             System.out.println("Enter the name of the Guest: ");
             String guestName = sc.next();
             System.out.println("Alert the Room Number: ");
             int roomNum = sc.nextInt();
             System.out.println("Enter the contact number of Guest: ");
             int number = sc.nextInt();

             System.out.println("Enter the Checkout Timing of Guest: ");//FORMAT:- (YYYY-MM-DD HH:MM:SS)
             String dateTime = sc.next();

             String sqlQuerry = "INSERT INTO reservation(guest_name, room_num, contact_num, check_out_time)" +
                     "VALUES('" + guestName +"', "+roomNum+", "+number +", '"+dateTime+"')";

             try(Statement statement = connection.createStatement()){
                 int affectRow = statement.executeUpdate(sqlQuerry);

                 if(affectRow >0){
                     System.out.println("Reservation Succesfull");
                 }
                 else {
                     System.out.println("Reservation Failed");
                 }
             }
         }
         catch (SQLException e){
             e.printStackTrace();;
         }
     }

    private static void viewReserveRoom(Connection connection) throws SQLException {

            String sqlQuery = "SELECT * FROM reservation;";
            try{
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sqlQuery);

                System.out.println("ALL RESERVATIONS ARE......");
                System.out.println("--------------------------------------------------------------------------------------");

                while(result.next()){
                    int reservationId = result.getInt("reservation_id");
                    String guestName = result.getString("guest_name");
                    int roomNum = result.getInt("room_num");
                    int contact = result.getInt("contact_num");
                    String reservationDate = result.getTimestamp("reservation_date").toString();
                    String checkOutTime = result.getTime("check_out_time").toString();


                    System.out.println("The Reservation ID:" +reservationId);
                    System.out.println("The name of the Guest: " + guestName);
                    System.out.println("Alert the" + guestName +"Room Number is: "+roomNum);
                    System.out.println("The contact number of Guest is: " + contact);
                    System.out.println("The Reservation Date and Time of Guest: " + reservationDate);
                    System.out.println("The Checkout Date and Time of Guest: " +checkOutTime);

                }
                System.out.println("--------------------------------------------------------------------------------------");

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    private static void getRoomNum(Connection connection, Scanner sc) throws SQLException {
        System.out.println("Enter the Reservation ID : ");
        int reservid = sc.nextInt();
        System.out.println("Enter the name of the guest : ");
        String nameGuest = sc.next();

        String sqlQuery = "SELECT room_num FROM" +
                "WHERE reservation_id =" + reservid +
                "AND guest_name ='"+ nameGuest +"'";

        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);

            if (result.next()){
                int roomNum = result.getInt("room_num");

                System.out.println("The Room number for Guest Name "+nameGuest+ "And Reservation id" + reservid + "is : " +roomNum);
            }
            else {
                System.out.println("The Room number is not Found for Guest Name "+nameGuest+ "And Reservation id" + reservid);
                System.out.println("Please Enter Valid Details... OR The name " + nameGuest + "of Guest  is not there");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void updateReservation(Connection connection, Scanner sc) throws SQLException {

//        System.out.println("The Guest is Already in Hotel OR New to Hotel");
//        System.out.println("1. Already Have a room, Update the Checkout Time");
//        System.out.println("2. New To Hotel ");
//        if()

        // If the old Guest is update here checkout time
        try{
            System.out.println("Enter Reservation Id to Update: ");
            int reservationId = sc.nextInt();
            if(!reservationExist(connection, reservationId)){
                System.out.println("Reservation is not Found for Given ID");
                return;
            }

            else {
                System.out.println("Enter the name of the new Guest: ");
                String newGuestName = sc.next();
                System.out.println("Alert Room Number: ");
                int newRoomNum = sc.nextInt();
                System.out.println("Enter the contact number of Guest: ");
                int newNumber = sc.nextInt();

                System.out.println("Enter the Checkout Timing of Guest: ");//FORMAT:- (YYYY-MM-DD HH:MM:SS)
                String NewDateTime = sc.next();

                String sqlQuery = "UPDATE reservation SET guest_name = '"+ newGuestName+"'," +
                        "room_num = "+ newRoomNum+"," +
                        "contact_num =" + newNumber+ ", "+
                        "check_out_time= '" + NewDateTime +"', WHERE reservation_id = "+reservationId;

                try{
                    Statement statement = connection.createStatement();
                    int roesAffected = statement.executeUpdate(sqlQuery);

                    if(roesAffected>0){
                        System.out.println("Reservation Update Successful....");
                    }
                    else {
                        System.out.println("Please Give Valid detail...");
                    }
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }


        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    private static void deleteReservation(Connection connection, Scanner sc) throws SQLException{
        System.out.println("Enter the Reservation Id For Delete the reservation: ");
        int reservationId = sc.nextInt();
        if(!reservationExist(connection,reservationId)){
            System.out.println("The Given Reservation ID is not Found");
            System.out.println(" Please Enter a valid Detail....");
        }
        else {
            String sqlQuery = "DELETE FROM reservation WHERE reservation_id ="+reservationId;
            try {
                Statement statement = connection.createStatement();
                int affectedRows = statement.executeUpdate(sqlQuery);

                if(affectedRows>0){
                    System.out.println("Delete the Reservation Successfully....");
                }
                else {
                    System.out.println("Deletion Reservation Failed");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private static boolean reservationExist(Connection connection, int reservationId) {

        String sqlQuery = "SELECT reservation_id FROM WHERE reservation_id=" +reservationId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            return resultSet.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void exit(){
        System.out.println("Exiting the System");
        System.out.println("THANK YOU FOR USING THR HOTEL RESERVATION SYSTEM....");
    }


}