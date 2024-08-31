import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



public class Hotel_reservation {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Gforgoat2****0";

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try{
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            

            while(true){
                System.out.println();
                System.out.println("HOTEL RESERVATION SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get room number");
                System.out.println("4. Update reservations");
                System.out.println("5. Delete reservations");
                System.out.println("0. Exit");
                int choice = sc.nextInt();

                switch(choice){
                    case 1: 
                    reserveRoom(conn, statement, sc);
                    break;

                    case 2:
                    viewReservations(conn, statement);
                    break;

                    case 3: 
                    getRoomNumber(conn, statement, sc);
                    break;
                    
                    case 4:
                    updateReservation(conn,statement,sc);
                    break;

                    case 5: 
                    deleteReservation(conn, statement, sc);
                    break;
                    
                    case 0:
                    exit();
                    sc.close();
                    return;

                    default:
                    System.out.println("Invalid choice. -- Please Try Again");
                    
                }
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public static void reserveRoom(Connection conn, Statement statement, Scanner sc){
        System.out.println("Enter guest name:  ");
        String guestName = sc.next();
        sc.nextLine();
        System.out.println("Enter room number:  ");
        int roomNumber = sc.nextInt();
        System.out.println("Enter guest's Contact number:  ");
        String contactNumber = sc.next();

        String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) "+
        "VALUES ('" + guestName + "'," + roomNumber + ",'" + contactNumber + "')";

        try{
            int affectedRows = statement.executeUpdate(sql);
            if(affectedRows > 0){
                System.out.println("Reservation successful");
            }else{
                System.out.println("Reservation failed");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void viewReservations(Connection conn, Statement statement){        
        String sql = "SELECT * FROM reservations";

        try{
            ResultSet result = statement.executeQuery(sql); 
            System.out.println("Current Reservations");
            System.out.println("-------------------------------------------------------------------------------------");
            while(result.next()){
                int reservationId = result.getInt("reservation_id");
                String guestName = result.getString("guest_name");
                int roomNumber = result.getInt("room_number");
                String contactNumber = result.getString("contact_number");
                String reservationDate = result.getString("reservation_date");
                                                                                                                         
                System.out.println("Reservation ID     Guest Name     Room Number     Contact Number     Date of Reservation");
                System.out.println(reservationId+"                    "+guestName+"           "+roomNumber+"           "+contactNumber+"         "+reservationDate);
                System.out.println();
            }
            System.out.println("-------------------------------------------------------------------------------------");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void getRoomNumber(Connection conn, Statement statement, Scanner sc){
        System.out.println("Enter Reservation ID:  ");
        int reservationId = sc.nextInt();
        String sql = "SELECT room_number, guest_name FROM reservations " + "WHERE reservation_id = '" + reservationId + "'";
        try{
            ResultSet result = statement.executeQuery(sql);

            if(result.next()){
                int roomNumber = result.getInt("room_number");
                String guestName = result.getString("guest_name");
                System.out.println("Room Number for reservation ID "+reservationId+" :  guest : "+guestName+" is: "+roomNumber);
            }else{
                System.out.println("Reservation not found for given ID and Name");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void updateReservation(Connection conn, Statement statement, Scanner sc){

        try{
            System.out.println("Enter the reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine(); // consume the nextline

            if(!reservationExists(conn, statement, reservationId)){
                System.out.println("Reservation not found");
                return;
            }

            System.out.println("Enter the new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.println("Enter the new room number: ");
            int newRoomNumber = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter the new contact number: ");
            String newContactNumber = sc.nextLine();
        
            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "'," + "room_number = " + newRoomNumber + "," + "contact_number = '" + newContactNumber + "'" + "WHERE reservation_id = " + reservationId;

            try{
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation updated successful");
                }else{
                    System.out.println("Reservation updation failed");
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void deleteReservation(Connection conn, Statement statement, Scanner sc){

        try{
            System.out.println("Enter Reservation ID to be deleted: ");
            int reservationId = sc.nextInt();

            if(!reservationExists(conn, statement, reservationId)){
                System.out.println("Reservation not found for given ID");
                return;
            }  
            String sql = "DELETE FROM reservations WHERE reservation_id = "+reservationId;

            int affectedRows = statement.executeUpdate(sql);
            if(affectedRows>0){
                System.out.println("Reservation deletion successful");
            }else{
                System.out.println("Reservation deletion failed");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExists(Connection conn, Statement statement, int reservationId){
        String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = "+reservationId;
        try{
            ResultSet result = statement.executeQuery(sql);
            return result.next(); // returns if data is available
        }catch(SQLException e){
            e.printStackTrace();
            return false; //handles database errors as needed
        }
    }

    public static void exit() throws InterruptedException{
        System.out.print("Exiting System");
        int i = 5;
        while(i != 0){
            System.out.print(".");
            Thread.sleep(300);
            i--;
        }
        System.out.println();
        System.out.println();
        System.out.println("Thank You for using our System");
        System.out.println();
        System.out.println();
    }
}