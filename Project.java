import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.sql.*;
import java.lang.Math;
import java.text.DecimalFormat;


public class Project{

private static DecimalFormat df = new DecimalFormat("0.00");
public static void main(String[] args){
   Connection conn = null;
     try{
         conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/cs315_fall2020?serverTimezone=UTC&"
         +"user=root&password=root");
         Statement stmt = conn.createStatement();
         
         Scanner input = new Scanner(System.in);
         //customerTable
         int customerID; String FName; String LName; String phoneNumber; String billingAddress = ""; String sql; 
         //productTable
         int productID; String productType; int productQuantity; double productUnitPrice;
         //shoppingCartTable
         int cartID = (int)(Math.random() * (100000 - 1))- 1;	int cartQuantity = 0; double totalCost = 0.00;
         //paymentTable
         int paymentID = (int)(Math.random() * (100000 - 1)) - 1; String creditCardNo;	String shippingAddress;
         
         
         System.out.println("WELCOME TO 'LuxuraDiamond.co' WHERE YOU CAN FIND AMAZING PURSES & BACKPACKS");
         System.out.println();
         
        System.out.println("Are you an existing customer? (Y/N)");
         String existing = input.nextLine();
         //customerID(PK), firstName, lastName, phoneNumber, billingAddress
         if(existing.equals("N")){
            System.out.println("To register, please type your customerID(numbers only): ");
               customerID = Integer.parseInt(input.nextLine());
            System.out.println("Please remember your customerID: " + customerID);
            System.out.println("Please type your First Name: ");
               FName = input.nextLine();
            System.out.println("Please type your Last Name: ");
               LName = input.nextLine();
            System.out.println("Please type your Phone Number(10 numbers only): ");
               phoneNumber = input.nextLine();
            System.out.println("Please type your Billing Address: ");
               billingAddress = input.nextLine();
               
         //Inserting to Database
         sql = "Insert into customerTable Values("+customerID+",'"+FName+"','"+LName+"',"+phoneNumber+",'"+billingAddress+"')";
         //System.out.println(sql);
         stmt.executeUpdate(sql);
            
            
         }
         //checking if the customer exist on the database
         else{
         System.out.println("Please type your customerID: ");
         customerID = Integer.parseInt(input.nextLine());
         int y = customerID;
         sql = "Select * FROM customerTable where customerID = " + customerID;
         ResultSet rs = stmt.executeQuery(sql); 
            while(rs.next())
            {
            FName = rs.getString("firstName");
            LName = rs.getString("lastName");
            System.out.println("Welcome back " +FName + " " + LName);
            break;
            }
            rs.close();
            
        
         
        }
        //PRODUCT VIEW
        System.out.println("OUR PRODUCTS FROM PRODUCT ID: 1 - 5 are PURSES & 6 - 10 are BACKPACKS:  ");
        
        sql = "Select * FROM productTable";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next())
        {
          productID = rs.getInt("productID");
          productType = rs.getString("productType");
          productQuantity = rs.getInt("productQuantity");
          productUnitPrice = rs.getDouble("productUnitPrice");
          
       
         System.out.println("-------------------------------------------------------------------");
         System.out.println("productID: "+productID);
         System.out.println("Description: "+productType);
         System.out.println("unitPrice: "+"$" +productUnitPrice);
         System.out.println("Quantity: "+productQuantity);
         System.out.println("-------------------------------------------------------------------");
        }
        rs.close();
        //ADDING TO CART
        System.out.println("Would you like to add an item on your cart? (Y/N)");
        String addingCart = input.nextLine();
        String check = addingCart;
        double total = 0;
        while(true){
           cartID+=4;
           if(addingCart.equals("Y"))
           {
             System.out.println("Which productID would you like to add on your shopping cart? (1 - 10)");
             int prod = Integer.parseInt(input.nextLine());
             System.out.println("How many productID number: " + prod + " would you like to add? (1 - 5)");
             int prod1 = Integer.parseInt(input.nextLine());
             
       
               sql = "Select * FROM productTable WHERE productID="+prod;
               rs = stmt.executeQuery(sql);
               while(rs.next())
               {
                  total = rs.getDouble("productUnitPrice");
               }
              
                int x = cartQuantity + prod1; //totalQuantity
                total = total * prod1; //price * quantity
                sql = "Insert into shoppingCartTable VALUES("+cartID+","+x+","+total+","+ prod+")";
                stmt.executeUpdate(sql);
   
               
               System.out.println("Would like to add more purse or backpack? (Y/N)");
               addingCart = input.nextLine();            
             }
             else
                  break;
                 
        }
        rs.close();
         
         if(check.equals("Y"))
         {
            //VIEW OF SHOPPING WITH TOTAL COST
            sql = "SELECT * FROM SHOPPINGCARTTOTAL";
            rs = stmt.executeQuery(sql);
            System.out.println("Your Shopping Cart: ");
            double totalEverything = 0;
            while(rs.next())
            {
               String a = rs.getString("productType");
               double b = rs.getDouble("totalCost");
               int c = rs.getInt("cartQuantity");
               
               
               
               System.out.println("Description: " + a);
               System.out.println("Quantity: " + c);
               System.out.println("Price * Quantity : $" + b);
               totalEverything+=b;
               
            }
            System.out.println("Your total is : $" + df.format(totalEverything));
            rs.close();
            
            System.out.println();
            System.out.println();
         
            //PAYMENT
            double x = totalEverything * 0.12;
            totalEverything = totalEverything + x;
            
            
            System.out.println("In ORDER TO PAY \nPlease enter your credit card number(16 numbers):");
            

            creditCardNo = input.nextLine();
            System.out.println("Please enter your Shipping Address: ");
            shippingAddress = input.nextLine();
            sql = "INSERT INTO paymentTable VALUES("+paymentID+",'"+creditCardNo+"', '"+shippingAddress+"' ,"+customerID+")";
            stmt.executeUpdate(sql);
            
            System.out.println("You have succesfully completed your order\n");
            
            sql = "SELECT * FROM customerTable where customerID="+customerID;
            rs = stmt.executeQuery(sql);
            while(rs.next())
            {
               String a = rs.getString("firstName");
               String b = rs.getString("lastName");
               System.out.println("Thank you very much, " +a+" " + b);
               break;             
            }
               System.out.println("The order will be shipped at : " + shippingAddress);
               System.out.println("and will be billed at :" + billingAddress);
            
            System.out.println("With the total of $" + df.format(totalEverything) +" with tax");
            System.out.println("SHOP WITH US AGAIN!");
            rs.close();
         }
         else
         {
            System.out.println("Thank you very much for checking us out!");
         }
         
        
         
            sql = "INSERT INTO transactionsTable SELECT * FROM shoppingCartTable";
            stmt.executeUpdate(sql);
            sql = "Delete FROM shoppingCartTable";
            stmt.executeUpdate(sql);
         
         
         
         
         
         System.out.println("\n");
         System.out.println("FOR ADMINSTRATORS TO CHECK ALL TRANSACTIONS & PAYMENT INFORMATION TYPE THE PASSWORD: (Y)");
         String in = input.nextLine();
         if(in.equals("Y")){
            System.out.println("All TRANSACTIONS");
            sql = "SELECT * FROM transactionsTable";
            rs = stmt.executeQuery(sql);
            while(rs.next())
               {
                  int a = rs.getInt("tCartID");
                  int b = rs.getInt("tCartQuantity");
                  double c = rs.getInt("tTotalCost");
                  int d = rs.getInt("tProductID");
                  
                  
                 System.out.println("CART ID: " + a);
                 System.out.println("Cart Quantity" + b);
                 System.out.println("Total Order Cost: $" + c);
                 System.out.println("PRODUCT ID: " + d);
                 System.out.println();
                 
               }
               rs.close();
            
            System.out.println("All PAYMENT INFORMATION");   
            sql = "SELECT * FROM paymentOrder";
            rs = stmt.executeQuery(sql);
            while(rs.next())
               {
                  String a = rs.getString("firstName");
                  String b = rs.getString("lastName");
                  String c = rs.getString("shippingAddress");
                  String d = rs.getString("creditCardNo");
   
   
                                
                  
                 System.out.println("Name: " + a + " " + b);
                 System.out.println("Shipping Address: " + c);
                 System.out.println("Credit Card Number: " + d);
                 System.out.println();
                 
               }
               rs.close();
         
         }
         
         
         
     }catch(SQLException ex) {
     System.out.println("SQLException " + ex.getMessage());
     System.out.println("SQLState " + ex.getSQLState());
     System.out.println("VendorError " + ex.getErrorCode());
     }
   }
   
}

