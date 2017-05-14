package com.ebox.ebox;
public class variable {


    public static  String Selected_Message;
    public static  String Selected_Message_subject;
    public  static String entered_email;
    public  static String ID_Number;
    public static boolean isLogin;
    public static boolean isNoData;
    public static String Current_user_cn;
    public  static  String Current_user_name;
    public  static  String Current_user_Fullname;
    public  static  String Current_user_level;


    private static String  base_url = "http://kennethabenojar.com/econcern/";
   // private static String base_url = "http://192.168.43.122:8080/eConcern/";
    public static String url_login = base_url + "login.php";
    public static String url_category = base_url + "category.php";
    public  static  String url_subject = base_url+"subject.php";
    public  static  String url_insertRecord = base_url+ "Insert_Suggestion.php";
    public static String url_record = base_url+"suggestions.php";
    public  static  String url_send_vCode = base_url+"phpmailer.php";
    public static String url_verifyCode = base_url+"c.php";
    public static String url_change_password = base_url+"change_password.php";
    public static String url_selected = base_url+"selected_content.php";
    public static String uploadPhoto = base_url+"photo.php";
    public static String url_photo = base_url+"photos/";

}
